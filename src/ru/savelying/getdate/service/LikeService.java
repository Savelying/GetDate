package ru.savelying.getdate.service;

public class LikeService {
    private static final LikeService instance = new LikeService();

    private LikeService() {
    }

    public static LikeService getInstance() {
        return instance;
    }

    public long getLikesByProfileId(long id) {
        return 100+id;
    }
}
