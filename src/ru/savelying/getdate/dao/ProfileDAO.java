package ru.savelying.getdate.dao;

import lombok.Getter;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ProfileDAO {
    @Getter
    private final static ProfileDAO instance = new ProfileDAO();
    private final ConcurrentHashMap<Long, Profile> profiles;
    private final AtomicLong idStore;

    public ProfileDAO() {
        this.profiles = new ConcurrentHashMap<>();
        this.idStore = new AtomicLong(1L);

        //Генерируем список пользователей
        int i = 1;
        while (i <= 10) {
            Profile profile = new Profile();
            profile.setId(idStore.getAndIncrement());
            profile.setName("User-" + i);
            profile.setEmail("user" + i + "@email.com");
            profile.setPassword("123");
            profile.setBirthDate(LocalDate.parse(2000 + i + "-01-01"));
            profile.setInfo("I'm a user №" + i++);
            profile.setGender(Gender.OTHER);
            profile.setStatus(Status.INACTIVE);
            profile.setRole(Role.USER);
            profiles.put(profile.getId(), profile);
        }
        profiles.get(1L).setRole(Role.ADMIN);
        profiles.get(1L).setStatus(Status.ACTIVE);
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

    public Set<String> getAllEmails() {
        return profiles.values().stream().map(Profile::getEmail).collect(Collectors.toSet());
    }

    public Optional<Profile> getProfileByEmail(String email) {
        if (email == null) return Optional.empty();
        return profiles.values().stream().filter(profile -> profile.getEmail().equals(email)).findFirst();
    }
}
