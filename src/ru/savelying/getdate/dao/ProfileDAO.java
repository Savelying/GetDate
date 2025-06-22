package ru.savelying.getdate.dao;

import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileDAO {
    private final static ProfileDAO instance = new ProfileDAO();
    private final ConcurrentHashMap<Long, Profile> profiles;
    private final AtomicLong idStore;

    public static ProfileDAO getInstance() {
        return instance;
    }

    public ProfileDAO() {
        this.profiles = new ConcurrentHashMap<>();
        this.idStore = new AtomicLong(1L);

        //Генерируем список пользователей
        int i=1;
        while (i <= 10) {
            Profile profile = new Profile();
            profile.setId(idStore.getAndIncrement());
            profile.setName("User-" + i);
            profile.setEmail("user" + i + "@email.com");
            profile.setBirthDate(LocalDate.parse(2000 + i + "-01-01"));
            profile.setInfo("I'm a user №" + i++);
            profile.setGender(Gender.OTHER);
            profile.setStatus(Status.ACTIVE);
            profiles.put(profile.getId(), profile);
        }
    }

    public Profile createProfile(Profile profile) {
        long id = idStore.getAndIncrement();
        profile.setId(id);
        profile.setStatus(Status.INACTIVE);
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
