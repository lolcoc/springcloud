package com.springcloud.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁(单机版).
 *
 * 使用方式(示例):
 * 			boolean flag = false;
 * 			String lockName = "sichuan:mianyang:fucheng:ds";
 * 			String lockValue = UUID.randomUUID().toString();
 * 			try {
 * 		        //	非阻塞获取(锁的最大存活时间采用默认值)
 * 				flag = RedisUtil.LockOps.getLock(lockName, lockValue);
 * 				//	非阻塞获取e.g.
 * 				flag = RedisUtil.LockOps.getLock(lockName, lockValue, 3, TimeUnit.SECONDS);
 * 			    // 阻塞获取(锁的最大存活时间采用默认值)
 * 		        flag = RedisUtil.LockOps.getLockUntilTimeout(lockName, lockValue, 2000);
 * 		        // 阻塞获取e.g.
 * 		        flag = RedisUtil.LockOps.getLockUntilTimeout(lockName, lockValue, 2, TimeUnit.SECONDS, 2000);
 * 				if (!flag) {
 * 				    throw new RuntimeException(" obtain redis-lock[" + lockName + "] fail");
 * 				}
 * 		     	// your logic
 * 			    //	...
 *          } finally {
 * 				if (flag) {
 * 					RedisUtil.LockOps.releaseLock(lockName, lockValue);
 *              }
 *          }
 *
 * |--------------------------------------------------------------------------------------------------------------------|
 * |单机版分布式锁、集群版分布式锁，特别说明:                                                                                 |
 * |   - 此锁是针对单机Redis的分布式锁;                                                                                    |
 * |   - 对于Redis集群而言, 此锁可能存在失效的情况。考虑如下情况:                                                              |
 * |         首先，当客户端A通过key-value(假设为key名为key123)在Master上获取到一个锁。                                        |
 * |         然后，Master试着把这个数据同步到Slave的时候突然挂了(此时Slave上没有该分布式锁的key123)。                            |
 * |         接着，Slave变成了Master。                                                                                    |
 * |         不巧的是，客户端B此时也一以相同的key去获取分布式锁；                                                              |
 * |                 因为现在的Master上没有key123代表的分布式锁，                                                            |
 * |                 所以客户端B此时再通过key123去获取分布式锁时，                                                            |
 * |                 就能获取成功。                                                                                       |
 * |         那么此时，客户端A和客户端B同时获取到了同一把分布式锁，分布式锁失效。                                                 |
 * |   - 在Redis集群模式下，如果需要严格的分布式锁的话，可使用Redlock算法来实现。Redlock算法原理简述:                              |
 * |     - 获取分布式锁：                                                                                                 |
 * |           1. 客户端获取服务器当前的的时间t0。                                                                           |
 * |           2. 使用相同的key和value依次向5个实例获取锁。                                                                  |
 * |              注:为了避免在某个redis节点耗时太久而影响到对后面的Redis节点的锁的获取;                                         |
 * |                 客户端在获取每一个Redis节点的锁的时候,自身需要设置一个较小的等待获取锁超时的时间,                             |
 * |                 一旦都在某个节点获取分布式锁的时间超过了超时时间，那么就认为在这个节点获取分布式锁失败，                        |
 * |                 （不把时间浪费在这一个节点上），继续获取下一个节点的分布式锁。                                              |
 * |           3. 客户端通过当前时间(t1)减去t0，计算(从所有redis节点)获取锁所消耗的总时间t2(注：t2=t1-t0)。                      |
 * |              只有t2小于锁本身的锁定时长(注:若锁的锁定时长是1小时， 假设下午一点开始上锁，那么锁会在下午两点                     |
 * |              的时候失效， 而你却在两点后才获取到锁，这个时候已经没意义了)，并且，客户端在至少在多半Redis                        |
 * |              节点上获取到锁, 我们才认为分布式锁获取成功。                                                                |
 * |           5. 如果锁已经获取，那么  锁的实际有效时长 = 锁的总有效时长 - 获取分布式锁所消耗的时长; 锁的实际有效时长 应保证 > 0。    |
 * |              注: 也就是说， 如果获取锁失败，那么                                                                        |
 * |                  A. 可能是   获取到的锁的个数，不满足大多数原则。                                                         |
 * |                  B. 也可能是 锁的实际有效时长不大于0。                                                                  |
 * |      - 释放分布式锁： 在每个redis节点上试着删除锁(, 不论有没有在该节点上获取到锁)。                                          |
 * |   - 集群下的分布式锁，可直接使用现有类库<a href="https://github.com/redisson/redisson"/>                                |
 * |                                                                                                                    |
 * |   注: 如果Redis集群项目能够容忍master宕机导致单机版分布式锁失效的情况的话，那么是直接使用单机版分布式锁在Redis集群的项目中的；     |
 * |       如果Redis集群项目不能容忍单机版分布式锁失效的情况的话，那么请使用基于RedLock算法的集群版分布式锁；                        |
 * |--------------------------------------------------------------------------------------------------------------------|
 */
public class RedisLockUtil {

    private static RedisTemplate redisTemplate = RedisUtil.getInstance();

    /** lua脚本, 保证 释放锁脚本 的原子性(以避免, 并发场景下, 释放了别人的锁) */
    private static final String RELEASE_LOCK_LUA;

    /** 分布式锁默认(最大)存活时长 */
    public static final long DEFAULT_LOCK_TIMEOUT = 3;

    /** DEFAULT_LOCK_TIMEOUT的单位 */
    public static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;

    static {
        // 不论lua中0是否代表失败; 对于java的Boolean而言, 返回0, 则会被解析为false
        RELEASE_LOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] "
                + "then "
                + "    return redis.call('del',KEYS[1]) "
                + "else "
                + "    return 0 "
                + "end ";
    }

    /**
     * 获取(分布式)锁.
     *
     * 注: 获取结果是即时返回的、是非阻塞的。
     *
     * @see RedisLockUtil#getLock(String, String, long, TimeUnit)
     */
    public static Object getLock(final String key, final String value) {
        return getLock(key, value, DEFAULT_LOCK_TIMEOUT, DEFAULT_TIMEOUT_UNIT);
    }

    /**
     * 获取(分布式)锁。
     * 若成功, 则直接返回;
     * 若失败, 则进行重试, 直到成功 或 超时为止。
     *
     * 注: 获取结果是阻塞的， 要么成功, 要么超时, 才返回。
     *
     * @param retryTimeoutLimit
     *            重试的超时时长(ms)
     * 其它参数可详见:
     *    @see RedisLockUtil#getLock(String, String, long, TimeUnit)
     *
     * @return 是否成功
     */
    public static boolean getLockUntilTimeout(final String key, final String value,
                                              final long retryTimeoutLimit) {
        return getLockUntilTimeout(key, value, DEFAULT_LOCK_TIMEOUT, DEFAULT_TIMEOUT_UNIT, retryTimeoutLimit);
    }

    /**
     * 获取(分布式)锁。
     * 若成功, 则直接返回;
     * 若失败, 则进行重试, 直到成功 或 超时为止。
     *
     * 注: 获取结果是阻塞的， 要么成功, 要么超时, 才返回。
     *
     * @param retryTimeoutLimit
     *            重试的超时时长(ms)
     * 其它参数可详见:
     *    @see RedisLockUtil#getLock(String, String, long, TimeUnit, boolean)
     *
     * @return 是否成功
     */
    public static boolean getLockUntilTimeout(final String key, final String value,
                                              final long timeout, final TimeUnit unit,
                                              final long retryTimeoutLimit) {
        long startTime = Instant.now().toEpochMilli();
        long now = startTime;
        do {
            try {
                Object alreadyGotLock = getLock(key, value, timeout, unit, false);
                if (alreadyGotLock == null) {
                    return true;
                }
            } catch (Exception e) {
                e.getMessage();
            }
            now = Instant.now().toEpochMilli();
        } while (now < startTime + retryTimeoutLimit);
        return false;
    }

    /**
     * 获取(分布式)锁
     *
     * 注: 获取结果是即时返回的、是非阻塞的。
     *
     * @see RedisLockUtil#getLock(String, String, long, TimeUnit, boolean)
     */
    public static Object getLock(final String key, final String value,
                                 final long timeout, final TimeUnit unit) {
        return getLock(key, value, timeout, unit, true);
    }

    /**
     * 获取(分布式)锁
     *
     * 注: 获取结果是即时返回的、是非阻塞的。
     *
     * @param key
     *            锁名
     * @param value
     *            锁名对应的value
     *            注: value一般采用全局唯一的值， 如: requestId、uuid等。
     *               这样， 释放锁的时候, 可以再次验证value值,
     *               保证自己上的锁只能被自己释放, 而不会被别人释放。
     *               当然, 如果锁超时时, 会被redis自动删除释放。
     * @param timeout
     *            锁的(最大)存活时长
     *            注: 一般的， 获取锁与释放锁 都是成对使用的, 在锁在达到(最大)存活时长之前，都会被主动释放。
     *                但是在某些情况下(如:程序获取锁后,释放锁前,崩了),锁得不到释放, 这时就需要等锁过
     *                了(最大)存活时长后，被redis自动删除清理了。这样就能保证redis中不会留下死数据。
     * @param unit
     *            timeout的单位
     * @param recordLog
     *            是否记录日志
     *
     * @return 是否成功
     */
    public static Object getLock(final String key, final String value,
                                 final long timeout, final TimeUnit unit,
                                 boolean recordLog) {
        if (recordLog) {
        }
        Object result = redisTemplate.execute((RedisConnection connection) ->
                connection.set(key.getBytes(StandardCharsets.UTF_8),
                        value.getBytes(StandardCharsets.UTF_8),
                        Expiration.seconds(unit.toSeconds(timeout)),
                        RedisStringCommands.SetOption.SET_IF_ABSENT)
        );
        if (result == null) {
            throw new RedisUtil.RedisOpsResultIsNullException();
        }
        return result;
    }

    /**
     * 释放(分布式)锁
     *
     * 注: 此方式能(通过value的唯一性)保证: 自己加的锁, 只能被自己释放。
     * 注: 锁超时时, 也会被redis自动删除释放。
     *
     * @param key
     *            锁名
     * @param value
     *            锁名对应的value
     *
     * @return 释放锁是否成功
     * @date 2020/3/15 17:00:45
     */
    public static Object releaseLock(final String key, final String value) {
        Object result = redisTemplate.execute((RedisConnection connection) ->
                connection.eval(RELEASE_LOCK_LUA.getBytes(),
                        ReturnType.BOOLEAN ,1,
                        key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8))
        );
        if (result == null) {
            throw new RedisUtil.RedisOpsResultIsNullException();
        }
        return result;
    }

    /**
     * 释放锁, 不校验该key对应的value值
     *
     * 注: 此方式释放锁，可能导致: 自己加的锁, 结果被别人释放了。
     *     所以不建议使用此方式释放锁。
     *
     * @param key
     *            锁名
     * @date 2020/3/15 18:56:59
     */
    @Deprecated
    public static void releaseLock(final String key) {
        RedisUtil.delete(key);
    }
}
