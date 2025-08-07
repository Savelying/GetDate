package ru.savelying.getdate.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import ru.savelying.getdate.dto.ProfileView;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.utils.ConnectRedis;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheService {

    @Getter
    private final static CacheService instance = new CacheService();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final ConcurrentHashMap<Long, Queue<ProfileView>> profilesCache = new ConcurrentHashMap<>();

    @SneakyThrows
    public ProfileView setNext(Long id) {
        try (Jedis jedis = ConnectRedis.getConnnect()) {
            String json = jedis.lpop(id.toString());
            if (json == null) return null;
            return jsonMapper.readValue(json, ProfileView.class);
        }
    }

    public void setQueue(Long id, Queue<ProfileView> profilesQueue) {
        try (Jedis jedis = ConnectRedis.getConnnect()){
            for (ProfileView profileView : profilesQueue) {
                String json = jsonMapper.writeValueAsString(profileView);
                jedis.rpush(id.toString(), json);
            }
        }
    }
}
