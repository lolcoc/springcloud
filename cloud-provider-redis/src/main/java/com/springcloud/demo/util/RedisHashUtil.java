package com.springcloud.demo.util;

import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisHashUtil {

    private static RedisTemplate redisTemplate = RedisUtil.getInstance();
        /**
         * 向key对应的hash中，增加一个键值对entryKey-entryValue
         *
         * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
         *     而使用新的entryKey-entryValue。
         *
         *
         * @param key
         *            定位hash的key
         * @param entryKey
         *            要向hash中增加的键值对里的 键
         * @param entryValue
         *            要向hash中增加的键值对里的 值
         * @date 2020/3/8 23:49:52
         */
        public static void hPut(String key, String entryKey, String entryValue) {
            redisTemplate.opsForHash().put(key, entryKey, entryValue);
        }

        /**
         * 向key对应的hash中，增加maps(即: 批量增加entry集)
         *
         * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
         *     而使用新的entryKey-entryValue
         *
         * @param key
         *            定位hash的key
         * @param maps
         *            要向hash中增加的键值对集
         * @date 2020/3/8 23:49:52
         */
        public static void hPutAll(String key, Map<String, String> maps) {
            redisTemplate.opsForHash().putAll(key, maps);
        }

        /**
         * 当key对应的hash中,不存在entryKey时，才(向key对应的hash中，)增加entryKey-entryValue
         * 否者，不进行任何操作
         *
         * @param key
         *            定位hash的key
         * @param entryKey
         *            要向hash中增加的键值对里的 键
         * @param entryValue
         *            要向hash中增加的键值对里的 值
         *
         * @return 操作是否成功。
         * @date 2020/3/8 23:49:52
         */
        public static boolean hPutIfAbsent(String key, String entryKey, String entryValue) {
            Boolean result = redisTemplate.opsForHash().putIfAbsent(key, entryKey, entryValue);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取到key对应的hash里面的对应字段的值
         *
         * 注: 若redis中不存在对应的key, 则返回null。
         *     若key对应的hash中不存在对应的entryKey, 也会返回null。
         *
         * @param key
         *            定位hash的key
         * @param entryKey
         *            定位hash里面的entryValue的entryKey
         *
         * @return  key对应的hash里的entryKey对应的entryValue值
         * @date 2020/3/9 9:09:30
         */
        public static Object hGet(String key, String entryKey) {
            Object entryValue = redisTemplate.opsForHash().get(key, entryKey);
            return entryValue;
        }

        /**
         * 获取到key对应的hash(即: 获取到key对应的Map<HK, HV>)
         *
         * 注: 若redis中不存在对应的key, 则返回一个没有任何entry的空的Map(，而不是返回null)。
         *
         * @param key
         *            定位hash的key
         *
         * @return  key对应的hash。
         * @date 2020/3/9 9:09:30
         */
        public static Map<Object, Object> hGetAll(String key) {
            Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
            return result;
        }

        /**
         * 批量获取(key对应的)hash中的entryKey的entryValue
         *
         * 注: 若hash中对应的entryKey不存在，那么返回的对应的entryValue值为null
         * 注: redis中key不存在，那么返回的List中，每个元素都为null。
         *     追注: 这个List本身不为null, size也不为0， 只是每个list中的每个元素为null而已。
         *
         * @param key
         *            定位hash的key
         * @param entryKeys
         *            需要获取的hash中的字段集
         * @return  hash中对应entryKeys的对应entryValue集
         * @date 2020/3/9 9:25:38
         */
        public static List<Object> hMultiGet(String key, Collection<Object> entryKeys) {
            List<Object> entryValues = redisTemplate.opsForHash().multiGet(key, entryKeys);
            return entryValues;
        }

        /**
         * (批量)删除(key对应的)hash中的对应entryKey-entryValue
         *
         * 注: 1、若redis中不存在对应的key, 则返回0;
         *     2、若要删除的entryKey，在key对应的hash中不存在，在count不会+1, 如:
         *                 RedisUtil.HashOps.hPut("ds", "name", "邓沙利文");
         *                 RedisUtil.HashOps.hPut("ds", "birthday", "1994-02-05");
         *                 RedisUtil.HashOps.hPut("ds", "hobby", "女");
         *                 则调用RedisUtil.HashOps.hDelete("ds", "name", "birthday", "hobby", "non-exist-entryKey")
         *                 的返回结果为3
         * 注: 若(key对应的)hash中的所有entry都被删除了，那么该key也会被删除
         *
         * @param key
         *            定位hash的key
         * @param entryKeys
         *            定位要删除的entryKey-entryValue的entryKey
         *
         * @return 删除了对应hash中多少个entry
         * @date 2020/3/9 9:37:47
         */
        public static long hDelete(String key, Object... entryKeys) {
            Long count = redisTemplate.opsForHash().delete(key, entryKeys);
            if (count == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 查看(key对应的)hash中，是否存在entryKey对应的entry
         *
         * 注: 若redis中不存在key,则返回false。
         * 注: 若key对应的hash中不存在对应的entryKey, 也会返回false。
         *
         * @param key
         *            定位hash的key
         * @param entryKey
         *            定位hash中entry的entryKey
         *
         * @return  hash中是否存在entryKey对应的entry.
         * @date 2020/3/9 9:51:55
         */
        public static boolean hExists(String key, String entryKey) {
            Boolean exist = redisTemplate.opsForHash().hasKey(key, entryKey);
            return exist;
        }

        /**
         * 增/减(hash中的某个entryValue值) 整数
         *
         * 注: 负数则为减。
         * 注: 若key不存在，那么会自动创建对应的hash,并创建对应的entryKey、entryValue,entryValue的初始值为increment。
         * 注: 若entryKey不存在，那么会自动创建对应的entryValue,entryValue的初始值为increment。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         *     抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key
         *            用于定位hash的key
         * @param entryKey
         *            用于定位entryValue的entryKey
         * @param increment
         *            增加多少
         * @return  增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         * @date 2020/3/9 10:09:28
         */
        public static long hIncrBy(String key, Object entryKey, long increment) {
            Long result = redisTemplate.opsForHash().increment(key, entryKey, increment);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 增/减(hash中的某个entryValue值) 浮点数
         *
         * 注: 负数则为减。
         * 注: 若key不存在，那么会自动创建对应的hash,并创建对应的entryKey、entryValue,entryValue的初始值为increment。
         * 注: 若entryKey不存在，那么会自动创建对应的entryValue,entryValue的初始值为increment。
         * 注: 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
         *     抛出org.springframework.data.redis.RedisSystemException
         * 注: 因为是浮点数， 所以可能会出现精度问题。
         *     追注: 本人简单测试了几组数据，暂未出现精度问题。
         *
         * @param key
         *            用于定位hash的key
         * @param entryKey
         *            用于定位entryValue的entryKey
         * @param increment
         *            增加多少
         * @return  增加后的总值。
         * @throws RedisSystemException key对应的value值不支持增/减操作时
         * @date 2020/3/9 10:09:28
         */
        public static double hIncrByFloat(String key, Object entryKey, double increment) {
            Double result = redisTemplate.opsForHash().increment(key, entryKey, increment);
            if (result == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return result;
        }

        /**
         * 获取(key对应的)hash中的所有entryKey
         *
         * 注: 若key不存在，则返回的是一个空的Set(，而不是返回null)
         *
         * @param key
         *            定位hash的key
         *
         * @return  hash中的所有entryKey
         * @date 2020/3/9 10:30:13
         */
        public static Set<Object> hKeys(String key) {
            Set<Object> entryKeys = redisTemplate.opsForHash().keys(key);
            return entryKeys;
        }

        /**
         * 获取(key对应的)hash中的所有entryValue
         *
         * 注: 若key不存在，则返回的是一个空的List(，而不是返回null)
         *
         * @param key
         *            定位hash的key
         *
         * @return  hash中的所有entryValue
         * @date 2020/3/9 10:30:13
         */
        public static List<Object> hValues(String key) {
            List<Object> entryValues = redisTemplate.opsForHash().values(key);
            return entryValues;
        }

        /**
         * 获取(key对应的)hash中的所有entry的数量
         *
         * 注: 若redis中不存在对应的key, 则返回值为0
         *
         * @param key
         *            定位hash的key
         *
         * @return  (key对应的)hash中,entry的个数
         * @date 2020/3/9 10:41:01
         */
        public static long hSize(String key) {
            Long count = redisTemplate.opsForHash().size(key);
            if (count == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return count;
        }

        /**
         * 根据options匹配到(key对应的)hash中的对应的entryKey, 并返回对应的entry集
         *
         *
         * 注: ScanOptions实例的创建方式举例:
         *     1、ScanOptions.NONE
         *     2、ScanOptions.scanOptions().match("n??e").build()
         *
         * @param key
         *            定位hash的key
         * @param options
         *            匹配entryKey的条件
         *            注: ScanOptions.NONE表示全部匹配。
         *            注: ScanOptions.scanOptions().match(pattern).build()表示按照pattern匹配,
         *                其中pattern中可以使用通配符 * ? 等,
         *                * 表示>=0个字符
         *                ？ 表示有且只有一个字符
         *                此处的匹配规则与{@link RedisUtil.KeyOps#keys(String)}处的一样。
         *
         * @return  匹配到的(key对应的)hash中的entry
         * @date 2020/3/9 10:49:27
         */
        public static Cursor<Map.Entry<Object, Object>> hScan(String key, ScanOptions options) {
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options);
            return cursor;
        }
}
