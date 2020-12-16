package com.springcloud.demo.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * list相关操作
 *
 * 提示: 列表中的元素，可以重复。
 *
 * 提示: list是有序的。
 *
 * 提示: redis中的list中的索引，可分为两类,这两类都可以用来定位list中元素:
 *      类别一: 从left到right, 是从0开始依次增大:   0,  1,  2,  3...
 *      类别二: 从right到left, 是从-1开始依次减小: -1, -2, -3, -4...
 *
 * 提示: redis中String的数据结构可参考resources/data-structure/List(列表)的数据结构(示例一).png
 *      redis中String的数据结构可参考resources/data-structure/List(列表)的数据结构(示例二).png
 */
public class RedisListUtil {

    private static RedisTemplate redisTemplate = RedisUtil.getInstance();
        /**
         * 从左端推入元素进列表
         *
         * 注: 若redis中不存在对应的key, 那么会自动创建
         *
         * @param key
         *            定位list的key
         * @param item
         *            要推入list的元素
         *
         * @return 推入后，(key对应的)list的size
         * @date 2020/3/9 11:56:05
         */
        public static long lLeftPush(String key, String item) {
            Long size = redisTemplate.opsForList().leftPush(key, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 从左端批量推入元素进列表
         *
         * 注: 若redis中不存在对应的key, 那么会自动创建
         * 注: 这一批item中，先push左侧的, 后push右侧的
         *
         * @param key
         *            定位list的key
         * @param items
         *            要批量推入list的元素集
         *
         * @return 推入后，(key对应的)list的size
         * @date 2020/3/9 11:56:05
         */
        public static long lLeftPushAll(String key, String... items) {
            Long size = redisTemplate.opsForList().leftPushAll(key, items);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 从左端批量推入元素进列表
         *
         * 注: 若redis中不存在对应的key, 那么会自动创建
         * 注: 这一批item中，那个item先从Collection取出来，就先push哪个
         *
         * @param key
         *            定位list的key
         * @param items
         *            要批量推入list的元素集
         *
         * @return 推入后，(key对应的)list的size
         * @date 2020/3/9 11:56:05
         */
        public static long lLeftPushAll(String key, Collection<String> items) {
            Long size = redisTemplate.opsForList().leftPushAll(key, items);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 如果redis中存在key, 则从左端批量推入元素进列表;
         * 否则，不进行任何操作
         *
         * @param key
         *            定位list的key
         * @param item
         *            要推入list的项
         *
         * @return  推入后，(key对应的)list的size
         * @date 2020/3/9 13:40:08
         */
        public static long lLeftPushIfPresent(String key, String item) {
            Long size = redisTemplate.opsForList().leftPushIfPresent(key, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 若key对应的list中存在pivot项, 那么将item放入第一个pivot项前(即:放在第一个pivot项左边);
         * 若key对应的list中不存在pivot项, 那么不做任何操作， 直接返回-1。
         *
         * 注: 若redis中不存在对应的key, 那么会自动创建
         *
         * @param key
         *            定位list的key
         * @param item
         *            要推入list的元素
         *
         * @return 推入后，(key对应的)list的size
         * @date 2020/3/9 11:56:05
         */
        public static long lLeftPush(String key, String pivot, String item) {
            Long size = redisTemplate.opsForList().leftPush(key, pivot, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link RedisListUtil#lLeftPush(String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPush(String key, String item) {
            Long size = redisTemplate.opsForList().rightPush(key, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link RedisListUtil#lLeftPushAll(String, String...)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushAll(String key, String... items) {
            Long size = redisTemplate.opsForList().rightPushAll(key, items);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link RedisListUtil#lLeftPushAll(String, Collection<String>)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushAll(String key, Collection<String> items) {
            Long size = redisTemplate.opsForList().rightPushAll(key, items);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link RedisListUtil#lLeftPushIfPresent(String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPushIfPresent(String key, String item) {
            Long size = redisTemplate.opsForList().rightPushIfPresent(key, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 与{@link RedisListUtil#lLeftPush(String, String, String)}类比即可， 不过是从list右侧推入元素
         */
        public static long lRightPush(String key, String pivot, String item) {
            Long size = redisTemplate.opsForList().rightPush(key, pivot, item);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 【非阻塞队列】 从左侧移出(key对应的)list中的第一个元素, 并将该元素返回
         *
         * 注: 此方法是非阻塞的， 即: 若(key对应的)list中的所有元素都被pop移出了，此时，再进行pop的话，会立即返回null
         * 注: 此方法是非阻塞的， 即: 若redis中不存在对应的key,那么会立即返回null
         * 注: 若将(key对应的)list中的所有元素都pop完了，那么该key会被删除
         *
         * @param key
         *            定位list的key
         * @return  移出的那个元素
         * @date 2020/3/9 14:33:56
         */
        public static Object lLeftPop(String key) {
            return redisTemplate.opsForList().leftPop(key);
        }

        /**
         * 【阻塞队列】 从左侧移出(key对应的)list中的第一个元素, 并将该元素返回
         *
         * 注: 此方法是阻塞的， 即: 若(key对应的)list中的所有元素都被pop移出了，此时，再进行pop的话，
         *     会阻塞timeout这么久，然后返回null
         * 注: 此方法是阻塞的， 即: 若redis中不存在对应的key,那么会阻塞timeout这么久，然后返回null
         * 注: 若将(key对应的)list中的所有元素都pop完了，那么该key会被删除
         *
         * 提示: 若阻塞过程中， 目标key-list出现了，且里面有item了，那么会立马停止阻塞, 进行元素移出并返回
         *
         * @param key
         *            定位list的key
         * @param timeout
         *            超时时间
         * @param unit
         *            timeout的单位
         * @return  移出的那个元素
         * @date 2020/3/9 14:33:56
         */
        public static Object lLeftPop(String key, long timeout, TimeUnit unit) {
            return redisTemplate.opsForList().leftPop(key, timeout, unit);
        }

        /**
         * 与{@link RedisListUtil#lLeftPop(String)}类比即可， 不过是从list右侧移出元素
         */
        public static Object lRightPop(String key) {
            return redisTemplate.opsForList().rightPop(key);
        }

        /**
         * 与{@link RedisListUtil#lLeftPop(String, long, TimeUnit)}类比即可， 不过是从list右侧移出元素
         */
        public static Object lRightPop(String key, long timeout, TimeUnit unit) {
            return redisTemplate.opsForList().rightPop(key, timeout, unit);
        }

        /**
         * 【非阻塞队列】 从sourceKey对应的sourceList右侧移出一个item, 并将这个item推
         *              入(destinationKey对应的)destinationList的左侧
         *
         * 注: 若sourceKey对应的list中没有item了，则立马认为(从sourceKey对应的list中pop出来的)item为null,
         *     null并不会往destinationKey对应的list中push。
         *     追注: 此时，此方法的返回值是null。
         *
         * 注: 若将(sourceKey对应的)list中的所有元素都pop完了，那么该sourceKey会被删除。
         *
         * @param sourceKey
         *            定位sourceList的key
         * @param destinationKey
         *            定位destinationList的key
         *
         * @return 移动的这个元素
         * @date 2020/3/9 15:06:59
         */
        public static Object lRightPopAndLeftPush(String sourceKey, String destinationKey) {
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
        }

        /**
         * 【阻塞队列】 从sourceKey对应的sourceList右侧移出一个item, 并将这个item推
         *            入(destinationKey对应的)destinationList的左侧
         *
         * 注: 若sourceKey对应的list中没有item了，则阻塞等待, 直到能从sourceList中移出一个非null的item(或等待时长超时);
         *     case1: 等到了一个非null的item, 那么继续下面的push操作，并返回这个item。
         *     case2: 超时了，还没等到非null的item, 那么pop出的结果就未null,此时并不会往destinationList进行push。
         *            此时，此方法的返回值是null。
         *
         * 注: 若将(sourceKey对应的)list中的所有元素都pop完了，那么该sourceKey会被删除。
         *
         * @param sourceKey
         *            定位sourceList的key
         * @param destinationKey
         *            定位destinationList的key
         * @param timeout
         *            超时时间
         * @param unit
         *            timeout的单位
         *
         * @return 移动的这个元素
         * @date 2020/3/9 15:06:59
         */
        public static Object lRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout,
                                                  TimeUnit unit) {
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
        }

        /**
         * 设置(key对应的)list中对应索引位置index处的元素为item
         *
         * 注: 若key不存在，则会抛出org.springframework.data.redis.RedisSystemException
         * 注: 若索引越界，也会抛出org.springframework.data.redis.RedisSystemException
         *
         * @param key
         *            定位list的key
         * @param index
         *            定位list中的元素的索引
         * @param item
         *            要替换成的值
         * @date 2020/3/9 15:39:50
         */
        public static void lSet(String key, long index, String item) {
            redisTemplate.opsForList().set(key, index, item);
        }

        /**
         * 通过索引index, 获取(key对应的)list中的元素
         *
         * 注: 若key不存在 或 index超出(key对应的)list的索引范围，那么返回null
         *
         * @param key
         *            定位list的key
         * @param index
         *            定位list中的item的索引
         *
         * @return  list中索引index对应的item
         * @date 2020/3/10 0:27:23
         */
        public static Object lIndex(String key, long index) {
            return redisTemplate.opsForList().index(key, index);
        }

        /**
         * 获取(key对应的)list中索引在[start, end]之间的item集
         *
         * 注: 含start、含end。
         * 注: 当key不存在时，获取到的是空的集合。
         * 注: 当获取的范围比list的范围还要大时，获取到的是这两个范围的交集。
         *
         * 提示: 可通过RedisListUtil.lRange(key, 0, -1)来获取到该key对应的整个list
         *
         * @param key
         *            定位list的key
         * @param start
         *            起始元素的index
         * @param end
         *            结尾元素的index
         *
         * @return  对应的元素集合
         * @date 2020/3/10 0:34:59
         */
        public static List<String> lRange(String key, long start, long end) {
            List<String> result = redisTemplate.opsForList().range(key, start, end);
            return result;
        }

        /**
         * 获取(key对应的)list
         *
         * @see RedisListUtil#lRange(String, long, long)
         *
         * @param key
         *            定位list的key
         * @return  (key对应的)list
         * @date 2020/3/10 0:46:50
         */
        public static List<String> lWholeList(String key) {
            List<String> result = redisTemplate.opsForList().range(key, 0, -1);
            return result;
        }

        /**
         * 获取(key对应的)list的size
         *
         * 注: 当key不存在时，获取到的size为0.
         *
         * @param key
         *            定位list的key
         *
         * @return list的size。
         *
         * @date 2020/3/10 0:48:40
         */
        public static long lSize(String key) {
            Long size = redisTemplate.opsForList().size(key);
            if (size == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return size;
        }

        /**
         * 删除(key对应的)list中，前expectCount个值等于item的项
         *
         * 注: 若expectCount == 0， 则表示删除list中所有的值等于item的项.
         * 注: 若expectCount > 0，  则表示删除从左往右进行
         * 注: 若expectCount < 0，  则表示删除从右往左进行
         *
         * 注: 若list中,值等于item的项的个数少于expectCount时，那么会删除list中所有的值等于item的项。
         * 注: 当key不存在时, 返回0。
         * 注: 若lRemove后， 将(key对应的)list中没有任何元素了，那么该key会被删除。
         *
         * @param key
         *            定位list的key
         * @param expectCount
         *            要删除的item的个数
         * @param item
         *            要删除的item
         *
         * @return  实际删除了的item的个数
         * @date 2020/3/10 0:52:57
         */
        public static long lRemove(String key, long expectCount, String item) {
            Long actualCount = redisTemplate.opsForList().remove(key, expectCount, item);
            if (actualCount == null) {
                throw new RedisUtil.RedisOpsResultIsNullException();
            }
            return actualCount;
        }

        /**
         * 裁剪(即: 对list中的元素取交集。)
         *
         * 举例说明: list中的元素索引范围是[0, 8], 而这个方法传入的[start, end]为 [3, 10]，
         *          那么裁剪就是对[0, 8]和[3, 10]进行取交集， 得到[3, 8], 那么裁剪后
         *          的list中，只剩下(原来裁剪前)索引在[3, 8]之间的元素了。
         *
         * 注: 若裁剪后的(key对应的)list就是空的,那么该key会被删除。
         *
         * @param key
         *            定位list的key
         * @param start
         *            要删除的item集的起始项的索引
         * @param end
         *            要删除的item集的结尾项的索引
         * @date 2020/3/10 1:16:58
         */
        public static void lTrim(String key, long start, long end) {
            redisTemplate.opsForList().trim(key, start, end);
        }
}
