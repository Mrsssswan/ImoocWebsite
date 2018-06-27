package mrsssswan.mall.commons;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;



public class TokenCache {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";
    //LRU算法 本地缓存
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(
                    //默认的数据加载实现，如果key没有对应的值，就调用这个方法进行加载
                    new CacheLoader<String, String>() {
                        @Override
                        public String load(String s) throws Exception {
                            return  "null";
                        }
                    }
            );

    public static void setKey(String key,String value){
            localCache.put(key,value);
    }
    public static String getKey(String key){
        String value=null;
        try {
            value = localCache.get(key);
            if("null".equals(value))
                return null;
            return value;
        } catch (ExecutionException e) {
            logger.error("本地缓存错误",e);
            return null;
        }
    }

}
