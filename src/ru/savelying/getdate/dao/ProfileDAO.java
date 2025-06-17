package ru.savelying.getdate.dao;

import ru.savelying.getdate.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileDAO {
    private final ConcurrentHashMap<Long, Profile> profiles;
    private final AtomicLong idStore;

    public ProfileDAO() {
        this.profiles = new ConcurrentHashMap<>();
        this.idStore = new AtomicLong();
    }

    public Profile createProfile(Profile profile) {
        long id = idStore.getAndIncrement();
        profile.setId(id);
        profiles.put(id, profile);
        return profile;
    }
    public void updateProfile(Profile profile) {
        profiles.put(profile.getId(), profile);
    }
    public boolean deleteProfile(Long id) {
        return profiles.remove(id) != null;
    }
    public Optional<Profile> getProfile(Long id) {
        return Optional.ofNullable(profiles.get(id));
    }

    public List<Profile> getAllProfiles() {
        return new ArrayList<>(profiles.values());
    }
}
