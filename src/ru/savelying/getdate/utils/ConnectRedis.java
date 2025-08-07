package ru.savelying.getdate.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ru.savelying.getdate.dao.query.Query;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@UtilityClass
public class ConnectRedis {

    public static final String HOST = ConfigFileUtils.getConfig("app.redis.host");
    public static final int PORT = Integer.parseInt(ConfigFileUtils.getConfig("app.redis.port") != null ? ConfigFileUtils.getConfig("app.redis.port") : "6379");
    public static final int EXP = Integer.parseInt(ConfigFileUtils.getConfig("app.redis.exp") != null ? ConfigFileUtils.getConfig("app.redis.exp") : "86400");

    private static JedisPool jedisPool;

    static {
        init();
    }

    @SneakyThrows
    private static void init() {
        jedisPool = new JedisPool(HOST, PORT);
    }

    @SneakyThrows
    public static Jedis getConnnect() {
        return jedisPool.getResource();
    }

    @SneakyThrows
    public static void closePool() {
        jedisPool.close();
    }
}
