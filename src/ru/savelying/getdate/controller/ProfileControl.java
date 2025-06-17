package ru.savelying.getdate.controller;

import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.service.ProfileService;

public class ProfileControl {
    private final ProfileService profileService;

    public ProfileControl(ProfileService profileService) {
        this.profileService = profileService;
    }

    public String work(String input) {
        return null;
    }
}
