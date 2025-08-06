package ru.savelying.getdate.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.LikeDAO;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.Action;
import ru.savelying.getdate.dto.LikeDTO;
import ru.savelying.getdate.dto.ProfileView;
import ru.savelying.getdate.mapper.ProfileMapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetDateService {
    private final LikeDAO likeDAO = LikeDAO.getInstance();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final ConcurrentHashMap<Long, Queue<ProfileView>> profilesCache = new ConcurrentHashMap<>();

    @Getter
    private final static GetDateService instance = new GetDateService();

//    public Optional<ProfileView> setNext(LikeDTO likeDTO) {
//        if (likeDTO.getAction() != Action.SKIP) likeDAO.writeLike(likeDTO);
//        return profileDAO.getSuitableProfile(likeDTO.getFromId(), 1).map(profileMapper::mapToView);
//    }

    public Optional<ProfileView> setNext(LikeDTO likeDTO) {
        if (likeDTO.getAction() != Action.SKIP) likeDAO.writeLike(likeDTO);
        Queue<ProfileView> profilesQueue = profilesCache.get(likeDTO.getFromId());
        if (profilesQueue == null || profilesQueue.isEmpty()) {
            profilesQueue = profileDAO.getSuitableProfiles(likeDTO.getFromId(), 3);
            profilesCache.put(likeDTO.getFromId(), profilesQueue);
        }
        return Optional.ofNullable(profilesQueue.poll());
    }
}