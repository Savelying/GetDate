package ru.savelying.getdate.mapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Status;

import java.time.LocalDate;

import static ru.savelying.getdate.utils.DateTimeUtils.getAge;
import static ru.savelying.getdate.utils.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper implements Mapper<Profile, ProfileDTO> {
    @Getter
    private final static ProfileMapper instance = new ProfileMapper();

    @Override
    public ProfileDTO mapToDTO(Profile obj) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(obj.getId());
        profileDTO.setName(obj.getName());
        profileDTO.setEmail(obj.getEmail());
        profileDTO.setInfo(obj.getInfo());
        profileDTO.setGender(obj.getGender());
//        profileDTO.setBirthDate(obj.getBirthDate());
        if (obj.getBirthDate() != null) profileDTO.setAge(getAge(obj.getBirthDate()));
        profileDTO.setStatus(obj.getStatus());
        profileDTO.setPhotoFileName(obj.getPhotoFileName());
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
        if (obj.getPhotoFileName() != null) profile.setPhotoFileName(obj.getPhotoFileName());
        return profile;
    }

    @SneakyThrows
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
        if (req.getPart("photo") != null && !isBlank(req.getPart("photo").getSubmittedFileName())) {
            profileDTO.setPhotoImage(req.getPart("photo"));
            profileDTO.setPhotoFileName(req.getPart("photo").getSubmittedFileName());
        }
        return profileDTO;
    }
}
