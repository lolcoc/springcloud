package com.springcloud.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
@SuppressWarnings("unused")
public class RedisUtil implements ApplicationContextAware {


    private static RedisTemplate redisTemplate;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RedisUtil.redisTemplate = (RedisTemplate)applicationContext.getBean("redisTemplate");
    }

    public static RedisTemplate getInstance(){
        return redisTemplate;
    }

        /**
         * key相关操作
         * 根据key, 删除redis中的对应key-value
         *  注: 若删除失败, 则返回false。
         *      若redis中，不存在该key, 那么返回的也是false。
         *      所以，不能因为返回了false,就认为redis中一定还存
         *      在该key对应的key-value。
         * @param key
         *            要删除的key
         * @return  删除是否成功
         */
        public static boolean delete(String key) {
            // 返回值只可能为true/false, 不可能为null
            Boolean result = redisTemplate.delete(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 根据keys, 批量删除key-value
         *
         * 注: 若redis中，不存在对应的key, 那么计数不会加1, 即:
         *     redis中存在的key-value里，有名为a1、a2的key，
         *     删除时，传的集合是a1、a2、a3，那么返回结果为2。
         *
         * @param keys
         *            要删除的key集合
         * @return  删除了的key-value个数
         */
        public static long delete(Collection<String> keys) {
            Long count = redisTemplate.delete(keys);
            if (count == null) {
                throw new RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 将key对应的value值进行序列化，并返回序列化后的value值。
         * 注: 若不存在对应的key, 则返回null。
         * 注: dump时，并不会删除redis中的对应key-value。
         * 注: dump功能与restore相反。
         * @param key
         *            要序列化的value的key
         * @return  序列化后的value值
         */
        public static byte[] dump(String key) {
            byte[] result = redisTemplate.dump(key);
            return result;
        }

        /**
         * 将给定的value值，反序列化到redis中, 形成新的key-value。
         * @param key
         *            value对应的key
         * @param value
         *            要反序列的value值。
         *            注: 这个值可以由{@link this#dump(String)}获得
         * @param timeToLive
         *            反序列化后的key-value的存活时长
         * @param unit
         *            timeToLive的单位
         * @throws RedisSystemException
         *             如果redis中已存在同样的key时，抛出此异常
         */
        public static void restore(String key, byte[] value, long timeToLive, TimeUnit unit) {
            restore(key, value, timeToLive, unit, false);
        }

        /**
         * 将给定的value值，反序列化到redis中, 形成新的key-value。
         *
         * @param key
         *            value对应的key
         * @param value
         *            要反序列的value值。
         *            注: 这个值可以由{@link this#dump(String)}获得
         * @param timeout
         *            反序列化后的key-value的存活时长
         * @param unit
         *            timeout的单位
         * @param replace
         *            若redis中已经存在了相同的key, 是否替代原来的key-value
         *
         * @throws RedisSystemException
         *             如果redis中已存在同样的key, 且replace为false时，抛出此异常
         */
        public static void restore(String key, byte[] value, long timeout, TimeUnit unit, boolean replace) {
            redisTemplate.restore(key, value, timeout, unit, replace);
        }

        /**
         * redis中是否存在,指定key的key-value
         *
         * @param key
         *            指定的key
         * @return  是否存在对应的key-value
         */
        public static boolean hasKey(String key) {
            Boolean result = redisTemplate.hasKey(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 给指定的key对应的key-value设置: 多久过时
         *
         * 注:过时后，redis会自动删除对应的key-value。
         * 注:若key不存在，那么也会返回false。
         *
         * @param key
         *            指定的key
         * @param timeout
         *            过时时间
         * @param unit
         *            timeout的单位
         * @return  操作是否成功
         */
        public static boolean expire(String key, long timeout, TimeUnit unit) {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 给指定的key对应的key-value设置: 什么时候过时
         *
         * 注:过时后，redis会自动删除对应的key-value。
         * 注:若key不存在，那么也会返回false。
         *
         * @param key
         *            指定的key
         * @param date
         *            啥时候过时
         *
         * @return  操作是否成功
         */
        public static boolean expireAt(String key, Date date) {
            Boolean result = redisTemplate.expireAt(key, date);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 找到所有匹配pattern的key,并返回该key的结合.
         *
         * 提示:若redis中键值对较多，此方法耗时相对较长，慎用！慎用！慎用！
         *
         * @param pattern
         *            匹配模板。
         *            注: 常用的通配符有:
         *                 ?    有且只有一个;
         *                 *     >=0哥;
         *
         * @return  匹配pattern的key的集合。 可能为null。
         * @date 2020/3/8 12:38:38
         */
        public static Set<String> keys(String pattern) {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys;
        }

        /**
         * 将当前数据库中的key对应的key-value,移动到对应位置的数据库中。
         *
         * 注:单机版的redis,默认将存储分为16个db, index为0 到 15。
         * 注:同一个db下，key唯一； 但是在不同db中，key可以相同。
         * 注:若目标db下，已存在相同的key, 那么move会失败，返回false。
         *
         * @param key
         *            定位要移动的key-value的key
         * @param dbIndex
         *            要移动到哪个db
         * @return 移动是否成功。
         *         注: 若目标db下，已存在相同的key, 那么move会失败，返回false。
         * @date 2020/3/8 13:01:00
         */
        public static boolean move(String key, int dbIndex) {
            Boolean result = redisTemplate.move(key, dbIndex);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 移除key对应的key-value的过期时间, 使该key-value一直存在
         *
         * 注: 若key对应的key-value，本身就是一直存在(无过期时间的)， 那么persist方法会返回false;
         *    若没有key对应的key-value存在，本那么persist方法会返回false;
         *
         * @param key
         *            定位key-value的key
         * @return 操作是否成功
         * @date 2020/3/8 13:10:02
         */
        public static boolean persist(String key) {
            Boolean result = redisTemplate.persist(key);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取key对应的key-value的过期时间
         *
         * 注: 若key-value永不过期， 那么返回的为-1。
         * 注: 若不存在key对应的key-value， 那么返回的为-2
         * 注:若存在零碎时间不足1 SECONDS,则(大体上)四舍五入到SECONDS级别。
         *
         * @param key
         *            定位key-value的key
         * @return  过期时间(单位s)
         * @date 2020/3/8 13:17:35
         */
        public static long getExpire(String key) {
            Long result = getExpire(key, TimeUnit.SECONDS);
            return result;
        }

        /**
         * 获取key对应的key-value的过期时间
         *
         * 注: 若key-value永不过期， 那么返回的为-1。
         * 注: 若不存在key对应的key-value， 那么返回的为-2
         * 注:若存在零碎时间不足1 unit,则(大体上)四舍五入到unit别。
         *
         * @param key
         *            定位key-value的key
         * @return  过期时间(单位unit)
         * @date 2020/3/8 13:17:35
         */
        public static long getExpire(String key, TimeUnit unit) {
            Long result = redisTemplate.getExpire(key, unit);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 从redis的所有key中，随机获取一个key
         *
         * 注: 若redis中不存在任何key-value, 那么这里返回null
         *
         * @return  随机获取到的一个key
         * @date 2020/3/8 14:11:43
         */
        public static Object randomKey() {
            return redisTemplate.randomKey();
        }

        /**
         * 重命名对应的oldKey为新的newKey
         *
         * 注: 若oldKey不存在， 则会抛出异常.
         * 注: 若redis中已存在与newKey一样的key,
         *     那么原key-value会被丢弃，
         *     只留下新的key，以及原来的value
         *     示例说明: 假设redis中已有 (keyAlpha, valueAlpha) 和 (keyBeta, valueBeat),
         *              在使用rename(keyAlpha, keyBeta)替换后, redis中只会剩下(keyBeta, valueAlpha)
         *
         * @param oldKey
         *            旧的key
         * @param newKey
         *            新的key
         * @throws RedisSystemException
         *             若oldKey不存在时， 抛出此异常
         * @date 2020/3/8 14:14:17
         */
        public static void rename(String oldKey, String newKey) {
            redisTemplate.rename(oldKey, newKey);
        }

        /**
         * 当redis中不存在newKey时, 重命名对应的oldKey为新的newKey。
         * 否者不进行重命名操作。
         *
         * 注: 若oldKey不存在， 则会抛出异常.
         *
         * @param oldKey
         *            旧的key
         * @param newKey
         *            新的key
         * @throws RedisSystemException
         *             若oldKey不存在时， 抛出此异常
         * @date 2020/3/8 14:14:17
         */
        public static boolean renameIfAbsent(String oldKey, String newKey) {
            Boolean result = redisTemplate.renameIfAbsent(oldKey, newKey);
            if (result == null) {
                throw new RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取key对应的value的数据类型
         *
         * 注: 若redis中不存在该key对应的key-value， 那么这里返回NONE。
         *
         * @param key
         *            用于定位的key
         * @return  key对应的value的数据类型
         * @date 2020/3/8 14:40:16
         */
        public static DataType type(String key) {
            DataType result = redisTemplate.type(key);
            return result;
        }

    /**
     * 当使用Pipeline 或 Transaction操作redis时, (不论redis中实际操作是否成功, 这里)结果(都)会返回null。
     * 此时，如果试着将null转换为基本类型的数据时，会抛出此异常。
     *
     * 即: 此工具类中的某些方法, 希望不要使用Pipeline或Transaction操作redis。
     *
     * 注: Pipeline 或 Transaction默认是不启用的， 可详见源码:
     *     @see LettuceConnection#isPipelined()
     *     @see LettuceConnection#isQueueing()
     *     @see JedisConnection#isPipelined()
     *     @see JedisConnection#isQueueing()
     */
    public static class RedisOpsResultIsNullException extends NullPointerException {

        public RedisOpsResultIsNullException() {
            super();
        }

        public RedisOpsResultIsNullException(String message) {
            super(message);
        }
    }

    /**
     * 提供一些基础功能支持
     *
     * @author JustryDeng
     * @date 2020/3/16 0:48:14
     */

        /** 默认拼接符 */
        public static final String DEFAULT_SYMBOL = ":";

        /**
         * 拼接args
         *
         * @see RedisUtil#joinBySymbol(String, String...)
         */
        public static String join(String... args) {
            return RedisUtil.joinBySymbol(DEFAULT_SYMBOL, args);
        }

        /**
         * 使用symbol拼接args
         *
         * @param symbol
         *            分隔符， 如: 【:】
         * @param args
         *            要拼接的元素数组, 如: 【a b c】
         *
         * @return  拼接后的字符串, 如  【a:b:c】
         * @date 2019/9/8 16:11
         */
        public static String joinBySymbol(String symbol, String... args) {
            if (symbol == null || symbol.trim().length() == 0) {
                throw new RuntimeException(" symbol must not be empty!");
            }
            if (args == null || args.length == 0) {
                throw new RuntimeException(" args must not be empty!");
            }
            StringBuilder sb = new StringBuilder(16);
            for (String arg : args) {
                sb.append(arg).append(symbol);
            }
            sb.replace(sb.length() - symbol.length(), sb.length(), "");
            return sb.toString();
        }

}