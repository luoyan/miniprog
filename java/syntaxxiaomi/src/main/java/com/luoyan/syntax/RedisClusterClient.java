package com.luoyan.syntax;

import com.xiaomi.miliao.zookeeper.ZKFacade;
//import com.xiaomi.miui.ad.query.common.ConstantHelper;
import com.xiaomi.miui.ad.thrift.model.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexfang on 2014/8/5.
 */
//@Service
public class RedisClusterClient {
    private final Logger LOGGER = LoggerFactory.getLogger(RedisClusterClient.class);
    private JedisCluster readJedisCluster = null;

    public JedisCluster getReadJedisCluster() {
        if (null == readJedisCluster) {
            synchronized (this) {
                if (null == readJedisCluster) {
                    String readRedisClusterHosts =
                            "zc-stage1-miui-ad01.bj:6380;zc-stage1-miui-ad01.bj:6381;zc-stage1-miui-ad01.bj:6382";
                    if (ZKFacade.getZKSettings().getEnvironmentType().isProduction()) {
                        readRedisClusterHosts = "lg-miui-data-redis00.bj:6380;lg-miui-data-redis00.bj:6381;" +
                                "lg-miui-data-redis01.bj:6380;lg-miui-data-redis01.bj:6381;" +
                                "lg-miui-data-redis02.bj:6380;lg-miui-data-redis02.bj:6381;" +
                                "lg-miui-data-redis03.bj:6380;lg-miui-data-redis03.bj:6381;" +
                                "lg-miui-data-redis04.bj:6380;lg-miui-data-redis04.bj:6381;" +
                                "lg-miui-data-redis05.bj:6380;lg-miui-data-redis05.bj:6381;lg-miui-data-redis05.bj:6382;" +
                                "lg-miui-data-redis06.bj:6380;lg-miui-data-redis06.bj:6381";
                    }
                    readJedisCluster = new JedisCluster(
                            parseJedisClusterNodes(readRedisClusterHosts),
                            ConstantHelper.REDIS_READ_TIME_OUT);
                    LOGGER.info("Initialized read jedis cluster");
                }
            }
        }
        return readJedisCluster;
    }

    private Set<HostAndPort> parseJedisClusterNodes(String redisClusterHosts) {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String[] redisClusterHostsArr = redisClusterHosts.split(Constants.REDIS_VALUE_GROUP_SEPARATOR);
        for (String redisClusterHostsItem : redisClusterHostsArr) {
            String[] redisClustHostsSubItems = redisClusterHostsItem.split(Constants.REDIS_VALUE_ITEM_SEPARATOR);
            if (2 == redisClustHostsSubItems.length) {
                jedisClusterNodes.add(new HostAndPort(redisClustHostsSubItems[0],
                        Integer.parseInt(redisClustHostsSubItems[1])));
            }
        }
        return jedisClusterNodes;
    }
}
