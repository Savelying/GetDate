package ru.savelying.getdate.service;

import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.model.Profile;

import java.util.List;
import java.util.Optional;

public class ProfileService {
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();
    private final static ProfileService instance = new ProfileService();

    private ProfileService() {
    }

    public static ProfileService getInstance() {
        return instance;
    }

    public Profile createProfile(Profile profile) {
        return profileDAO.createProfile(profile);
    }

    public void updateProfile(Profile profile) {
        profileDAO.updateProfile(profile);
    }

    public boolean deleteProfile(long id) {
        return profileDAO.deleteProfile(id);
    }

    public Optional<Profile> getProfile(Long id) {
        if (id == null) return Optional.empty();
        return profileDAO.getProfile(id);
    }

    public List<Profile> getProfiles() {
        return profileDAO.getAllProfiles();
    }
}
