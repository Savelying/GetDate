package ru.savelying.getdate.mapper;

import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Profile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProfileMapper implements Mapper<Profile, ProfileDTO> {
    private final static ProfileMapper instance = new ProfileMapper();

    private ProfileMapper() {
    }

    public static ProfileMapper getInstance() {
        return instance;
    }

    @Override
    public ProfileDTO mapToDTO(Profile obj) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(obj.getId());
        profileDTO.setName(obj.getName());
        profileDTO.setEmail(obj.getEmail());
        profileDTO.setInfo(obj.getInfo());
        profileDTO.setGender(obj.getGender());
//        profileDTO.setBirthDate(obj.getBirthDate());
        profileDTO.setAge(Math.toIntExact(ChronoUnit.YEARS.between(obj.getBirthDate(), LocalDate.now())));
        return profileDTO;
    }

    @Override
    public Profile mapFromDTO(ProfileDTO obj) {
        Profile profile = new Profile();
        profile.setId(obj.getId());
        profile.setName(obj.getName());
        profile.setEmail(obj.getEmail());
        profile.setInfo(obj.getInfo());
        profile.setGender(obj.getGender());
        profile.setBirthDate(obj.getBirthDate());
        return profile;
    }
}
