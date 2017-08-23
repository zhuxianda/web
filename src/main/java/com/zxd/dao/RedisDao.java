package com.zxd.dao;/*
  Created by IntelliJ IDEA.
  User: xiaozhang
  Date: 17-7-20
  Time: 下午3:54
  Desc:Someing
*/

import com.rits.cloning.Cloner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RedisDao implements InitializingBean {

    static Cloner cloner = new Cloner();
    @Value("${redisAddr}")
    private String redisAddr = "127.0.0.1:6379";
    @Value("${redisPassword}")
    private String redisPassword = "";
    @Value("${redisMaxActive}")
    private int redisMaxActive = 500;
    @Value("${redisMaxIdle}")
    private int redisMaxIdle = 500;
    @Value("${redisMaxWait}")
    private int redisMaxWait = 1000;
    @Value("${redisTTL}")
    private int redisTTL = 1800;

    private ShardedJedisPool pool;


    public void afterPropertiesSet() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(redisMaxActive);// 最大活动的对象个数
        config.setMaxIdle(redisMaxIdle);// 对象最大空闲时间
        config.setMaxWait(redisMaxWait);// 获取对象时最大等待时间
        config.setTestOnBorrow(true);
        String[] redisAddrs = redisAddr.split(";");
        String[][] redisAddrPorts = new String[redisAddrs.length][2];
        for (int i = 0; i < redisAddrPorts.length; i++) {
            String[] sa = redisAddrs[i].split(":");
            redisAddrPorts[i][0] = sa[0];
            redisAddrPorts[i][1] = sa[1];
        }
        List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(redisAddrs.length);
        for (String[] ra : redisAddrPorts) {
            JedisShardInfo jsi = new JedisShardInfo(ra[0], Integer.parseInt(ra[1]));
            if (StringUtils.hasText(redisPassword))
                jsi.setPassword(redisPassword);
            jdsInfoList.add(jsi);
        }
        pool = new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }

    public String get(String key) {

        ShardedJedis jds = null;
        String result = null;
        try {
            jds = pool.getResource();
            result = jds.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            pool.returnBrokenResource(jds);
        } finally {
            if (jds != null)
                pool.returnResource(jds);
            return result;
        }
    }

    public List<String> lrange(String key, long start, long end) {
        ShardedJedis jds = null;
        List<String> result = null;
        try {
            jds = pool.getResource();
            result = jds.lrange(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            pool.returnBrokenResource(jds);
        } finally {
            if (jds != null)
                pool.returnResource(jds);
            if (result == null) return new ArrayList<String>();
            return result;
        }
    }

    public String getInterest_redisAddr() {
        return redisAddr;
    }

    public void setInterest_redisAddr(String interest_redisAddr) {
        this.redisAddr = interest_redisAddr;
    }
}
