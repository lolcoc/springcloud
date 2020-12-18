package com.springcloud.demo.util;

import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * hash相关操作
 *
 * 提示: 简单的，可以将redis中hash的数据结构看作是 Map<String, Map<HK, HV>>
 * 提示: redis中String的数据结构可参考resources/data-structure/Hash(散列)的数据结构(示例一).png
 *      redis中String的数据结构可参考resources/data-structure/Hash(散列)的数据结构(示例二).png
 *
 * @author JustryDeng
 * @date 2020/3/8 23:39:26
 */
public class RedisStringUtil {

        private static RedisTemplate redisTemplate = RedisUtil.getInstance();

        /**
         * 设置key-value
         * 注: 若已存在相同的key, 那么原来的key-value会被丢弃。
         * @param key
         *            key
         * @param value
         *            key对应的value
         */
        public static void set(String key, Object value) {
            redisTemplate.opsForValue().set(key, value);
        }

        /**
         * 处理redis中key对应的value值, 将第offset位的值, 设置为1或0。
         *
         * 说明: 在redis中，存储的字符串都是以二级制的进行存在的; 如存储的key-value里，值为abc,实际上，
         *       在redis里面存储的是011000010110001001100011,前8为对应a,中间8为对应b,后面8位对应c。
         *       示例：这里如果setBit(key, 6, true)的话，就是将索引位置6的那个数，设置值为1，值就变成
         *            了011000110110001001100011
         *       追注:offset即index,从0开始。
         *
         * 注: 参数value为true, 则设置为1；参数value为false, 则设置为0。
         *
         * 注: 若redis中不存在对应的key,那么会自动创建新的。
         * 注: offset可以超过value在二进制下的索引长度。
         *
         * @param key
         *            定位value的key
         * @param offset
         *            要改变的bit的索引
         * @param value
         *            改为1或0, true - 改为1, false - 改为0
         *
         * @return set是否成功
         * @date 2020/3/8 16:30:37
         */
        public static boolean setBit(String key, long offset, boolean value) {
            Boolean result = redisTemplate.opsForValue().setBit(key, offset, value);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 设置key-value
         *
         * 注: 若已存在相同的key, 那么原来的key-value会被丢弃
         *
         * @param key
         *            key
         * @param value
         *            key对应的value
         * @param timeout
         *            过时时长
         * @param unit
         *            timeout的单位
         * @date 2020/3/8 15:40:59
         */
        public static void setEx(String key, Object value, long timeout, TimeUnit unit) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }

        /**
         * 若不存在key时, 向redis中添加key-value, 返回成功/失败。
         * 若存在，则不作任何操作, 返回false。
         *
         * @param key
         *            key
         * @param value
         *            key对应的value
         *
         * @return set是否成功
         * @date 2020/3/8 16:51:36
         */
        public static boolean setIfAbsent(String key, String value) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 若不存在key时, 向redis中添加一个(具有超时时长的)key-value, 返回成功/失败。
         * 若存在，则不作任何操作, 返回false。
         *
         * @param key
         *            key
         * @param value
         *            key对应的value
         * @param timeout
         *            超时时长
         * @param unit
         *            timeout的单位
         *
         * @return set是否成功
         * @date 2020/3/8 16:51:36
         */
        public static boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 从(redis中key对应的)value的offset位置起(包含该位置),用replaceValue替换对应长度的值。
         *
         * 举例说明:
         *       1.假设redis中存在key-value ("ds", "0123456789"); 调
         *         用setRange("ds", "abcdefghijk", 3)后， redis中该value值就变为了[012abcdefghijk]
         *
         *       2.假设redis中存在key-value ("jd", "0123456789");调
         * 		   用setRange("jd", "xyz", 3)后， redis中该value值就变为了[012xyz6789]
         *
         *       3.假设redis中存在key-value ("ey", "0123456789");调
         * 		   用setRange("ey", "qwer", 15)后， redis中该value值就变为了[0123456789     qwer]
         *       注:case3比较特殊，offset超过了原value的长度了, 中间就会有一些空格来填充，但是如果在程序
         *          中直接输出的话，中间那部分空格可能会出现乱码。
         *
         * @param key
         *            定位key-value的key
         * @param replaceValue
         *            要替换的值
         * @param offset
         *            起始位置
         * @date 2020/3/8 17:04:31
         */
        public static void setRange(String key, String replaceValue, long offset) {
            redisTemplate.opsForValue().set(key, replaceValue, offset);
        }

        /**
         * 获取到key对应的value的长度。
         *
         * 注: 长度等于{@link String#length}。
         * 注: 若redis中不存在对应的key-value, 则返回值为0.
         *
         * @param key
         *            定位value的key
         * @return  value的长度
         * @date 2020/3/8 17:14:30
         */
        public static long size(String key) {
            Long result = redisTemplate.opsForValue().size(key);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 批量设置 key-value
         *
         * 注: 若存在相同的key, 则原来的key-value会被丢弃。
         *
         * @param maps
         *            key-value 集
         * @date 2020/3/8 17:21:19
         */
        public static void multiSet(Map<String, String> maps) {
            redisTemplate.opsForValue().multiSet(maps);
        }

        /**
         * 当redis中,不存在任何一个keys时, 才批量设置 key-value, 并返回成功/失败.
         * 否者，不进行任何操作, 并返回false。
         *
         * 即: 假设调用此方法时传入的参数map是这样的: {k1=v1, k2=v2, k3=v3}
         *     那么redis中, k1、k2、k3都不存在时,才会批量设置key-value;
         *     否则不会设置任何key-value。
         *
         * 注: 若存在相同的key, 则原来的key-value会被丢弃。
         *
         * 注:
         *
         * @param maps
         *            key-value 集
         *
         * @return 操作是否成功
         * @date 2020/3/8 17:21:19
         */
        public static boolean multiSetIfAbsent(Map<String, String> maps) {
            Boolean result = redisTemplate.opsForValue().multiSetIfAbsent(maps);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 增/减 整数
         *
         * 注: 负数则为减。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         *     抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key
         *            用于定位value的key
         * @param increment
         *            增加多少
         * @return  增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         * @date 2020/3/8 17:45:51
         */
        public static long incrBy(String key, long increment) {
            Long result = redisTemplate.opsForValue().increment(key, increment);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 增/减 浮点数
         *
         * 注: 慎用浮点数，会有精度问题。
         *     如: 先 RedisUtil.StringOps.set("ds", "123");
         *         然后再RedisUtil.StringOps.incrByFloat("ds", 100.6);
         *         就会看到精度问题。
         * 注: 负数则为减。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         *     抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key
         *            用于定位value的key
         * @param increment
         *            增加多少
         * @return  增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         * @date 2020/3/8 17:45:51
         */
        public static double incrByFloat(String key, double increment) {
            Double result = redisTemplate.opsForValue().increment(key, increment);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 追加值到末尾
         *
         * 注: 当redis中原本不存在key时,那么（从效果上来看）此方法就等价于{@link this#set(String, String)}
         *
         * @param key
         *            定位value的key
         * @param value
         *            要追加的value值
         * @return 追加后， 整个value的长度
         * @date 2020/3/8 17:59:21
         */
        public static int append(String key, String value) {
            Integer result = redisTemplate.opsForValue().append(key, value);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 根据key，获取到对应的value值
         *
         * @param key
         *            key-value对应的key
         * @return  该key对应的值。
         *          注: 若key不存在， 则返回null。
         *
         * @date 2020/3/8 16:27:41
         */
        public static Object get(String key) {
            return redisTemplate.opsForValue().get(key);
        }

        /**
         * 对(key对应的)value进行截取, 截取范围为[start, end]
         *
         * 注: 若[start, end]的范围不在value的范围中，那么返回的是空字符串 ""
         * 注: 若value只有一部分在[start, end]的范围中，那么返回的是value对应部分的内容(即:不足的地方，并不会以空来填充)
         *
         * @param key
         *            定位value的key
         * @param start
         *            起始位置 (从0开始)
         * @param end
         *            结尾位置 (从0开始)
         * @return  截取后的字符串
         * @date 2020/3/8 18:08:45
         */
        public static String getRange(String key, long start, long end) {
            String result = redisTemplate.opsForValue().get(key, start, end);
            return result;
        }

        /**
         * 给指定key设置新的value, 并返回旧的value
         *
         * 注: 若redis中不存在key, 那么此操作仍然可以成功， 不过返回的旧值是null
         *
         * @param key
         *            定位value的key
         * @param newValue
         *            要为该key设置的新的value值
         * @return  旧的value值
         * @date 2020/3/8 18:14:24
         */
        public static Object getAndSet(String key, String newValue) {
            return redisTemplate.opsForValue().getAndSet(key, newValue);
        }

        /**
         * 获取(key对应的)value在二进制下，offset位置的bit值。
         *
         * 注: 当offset的值在(二进制下的value的)索引范围外时, 返回的也是false。
         *
         * 示例:
         *      RedisUtil.StringOps.set("akey", "a");
         *      字符串a, 转换为二进制为01100001
         *      那么getBit("akey", 6)获取到的结果为false。
         *
         * @param key
         *            定位value的key
         * @param offset
         *            定位bit的索引
         * @return  offset位置对应的bit的值(true - 1, false - 0)
         * @date 2020/3/8 18:21:10
         */
        public static boolean getBit(String key, long offset) {
            Boolean result = redisTemplate.opsForValue().getBit(key, offset);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 批量获取value值
         *
         * 注: 若redis中，对应的key不存在，那么该key对应的返回的value值为null
         *
         * @param keys
         *            key集
         * @return  value值集合
         * @date 2020/3/8 18:26:33
         */
        public static List<String> multiGet(Collection<String> keys) {
            List<String> result = redisTemplate.opsForValue().multiGet(keys);
            return result;
        }
}
