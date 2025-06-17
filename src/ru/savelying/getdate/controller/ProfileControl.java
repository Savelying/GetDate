package ru.savelying.getdate.controller;

import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.service.ProfileService;

import java.util.Optional;

public class ProfileControl {
    private final ProfileService profileService;

    public ProfileControl(ProfileService profileService) {
        this.profileService = profileService;
    }

    public String work(String input) {
        return null;
    }

    public String create(String create) {
        String[] params = create.split(", ");
        if (params.length != 3) return "Bad Request: need 3 parameters to create a profile";
        Profile profile = new Profile();
        profile.setEmail(params[0]);
        profile.setName(params[1]);
        profile.setInfo(params[2]);
        profileService.createProfile(profile);
        return "Created " + profile.toString();
    }

    public String update(String update) {
        String[] params = update.split(", ");
        if (params.length != 4) return "Bad Request: need 4 parameters to update a profile";
        Long id;
        try {
            id = Long.parseLong(params[0]);
        } catch (NumberFormatException e) {
            return "Bad Request: need a number for the ID";
        }
        Profile profile = new Profile();
        profile.setId(Long.valueOf(params[0]));
        profile.setEmail(params[1]);
        profile.setName(params[2]);
        profile.setInfo(params[3]);
        profileService.updateProfile(profile);
        return "Updated " + profile.toString();
    }

    public String delete(String delete) {
        String[] params = delete.split(", ");
        if (params.length != 1) return "Bad Request: need 1 parameter to delete a profile";
        Long id;
        try {
            id = Long.parseLong(params[0]);
        } catch (NumberFormatException e) {
            return "Bad Request: need a number for the ID";
        }
        boolean result = profileService.deleteProfile(id);
        if (!result) return "Profile does not exist";
        return "Deleted profile №" + id;
    }

    public String search(String search) {
        String[] params = search.split(", ");
        if (params.length != 1) return "Bad Request: need 1 parameter to search a profile";
        Long id;
        try {
            id = Long.parseLong(params[0]);
        } catch (NumberFormatException e) {
            return "Bad Request: need a number for the ID";
        }
        Optional<Profile> profileOptional = profileService.getProfile(id);
        if (profileOptional.isEmpty()) return "Profile does not exist";
        Profile profile = profileOptional.get();
        return "Finded " + profile.toString();
    }

    public String all() {
        return "All profiles:\n" + profileService.getProfiles().toString()/* + "\n"*/;
    }
}
