package ru.savelying.getdate.mapper;

import jakarta.servlet.http.HttpServletRequest;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Status;

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
        if (obj.getBirthDate() != null)
            profileDTO.setAge(Math.toIntExact(ChronoUnit.YEARS.between(obj.getBirthDate(), LocalDate.now())));
        profileDTO.setStatus(obj.getStatus());
        return profileDTO;
    }

    @Override
    public Profile mapFromDTO(ProfileDTO obj) {
        Profile profile = new Profile();
        if (obj.getId() != null) profile.setId(obj.getId());
        if (obj.getName() != null) profile.setName(obj.getName());
        if (obj.getEmail() != null) profile.setEmail(obj.getEmail());
        if (obj.getPassword() != null) profile.setPassword(obj.getPassword());
        if (obj.getInfo() != null) profile.setInfo(obj.getInfo());
        if (obj.getGender() != null) profile.setGender(obj.getGender());
        if (obj.getBirthDate() != null) profile.setBirthDate(obj.getBirthDate());
        if (obj.getStatus() != null) profile.setStatus(obj.getStatus());
        return profile;
    }

    public ProfileDTO getProfileDTO(HttpServletRequest req) {
        ProfileDTO profileDTO = new ProfileDTO();
        if (req.getParameter("id") != null) profileDTO.setId(Long.parseLong(req.getParameter("id")));
        if (req.getParameter("email") != null) profileDTO.setEmail(req.getParameter("email"));
        if (req.getParameter("password") != null) profileDTO.setPassword(req.getParameter("password"));
        if (req.getParameter("name") != null) profileDTO.setName(req.getParameter("name"));
        if (req.getParameter("info") != null) profileDTO.setInfo(req.getParameter("info"));
        if (req.getParameter("gender") != null) profileDTO.setGender(Gender.valueOf(req.getParameter("gender")));
        if (req.getParameter("status") != null) profileDTO.setStatus(Status.valueOf(req.getParameter("status")));
        if (req.getParameter("birthDate") != null && !req.getParameter("birthDate").isBlank())
            profileDTO.setBirthDate(LocalDate.parse(req.getParameter("birthDate")));
        return profileDTO;
    }
}
