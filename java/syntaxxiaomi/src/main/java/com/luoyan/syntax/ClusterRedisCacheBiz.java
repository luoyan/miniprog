package com.luoyan.syntax;

//import com.xiaomi.miui.ad.query.cache.RedisClusterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by alexfang on 2014/8/5.
 */
//@Service
public class ClusterRedisCacheBiz {
    //@Autowired
    private RedisClusterClient redisClusterClient = new RedisClusterClient();


    public Set<String> getSet(String keyPrefix, String key) {
        return redisClusterClient.getReadJedisCluster().smembers(keyPrefix + key);
    }

    /*
   * 获取指定key的值，没有时返回空
   * */
    public String getString(String keyPrefix, String key) {
        String result = redisClusterClient.getReadJedisCluster().get(keyPrefix + key);
        return result == null ? "" : result;
    }


}
