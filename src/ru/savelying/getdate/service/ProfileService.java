package ru.savelying.getdate.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.mapper.ProfileMapper;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileService {
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final ContentService contentService = ContentService.getInstance();

    @Getter
    private final static ProfileService instance = new ProfileService();

    public Long createProfile(ProfileDTO profileDTO) {
        return profileDAO.createProfile(profileMapper.mapFromDTO(profileDTO)).getId();
    }

    @SneakyThrows
    public void updateProfile(ProfileDTO profileDTO) {
        if (profileDTO.getName() == null)
            profileDTO.setName(profileDAO.getProfileById(profileDTO.getId()).get().getName());

        if (profileDTO.getEmail() == null)
            profileDTO.setEmail(profileDAO.getProfileById(profileDTO.getId()).get().getEmail());
        else if (profileDTO.getNewEmail() != null)
            profileDTO.setEmail(profileDTO.getNewEmail());

        if (profileDTO.getPassword() == null)
            profileDTO.setPassword(profileDAO.getProfileById(profileDTO.getId()).get().getPassword());
        else if (profileDTO.getNewPassword() != null)
            profileDTO.setPassword(profileDTO.getNewPassword());

        if (profileDTO.getGender() == null)
            profileDTO.setGender(profileDAO.getProfileById(profileDTO.getId()).get().getGender());

        if (profileDTO.getStatus() == null)
            profileDTO.setStatus(profileDAO.getProfileById(profileDTO.getId()).get().getStatus());

        if (profileDTO.getBirthDate() == null)
            profileDTO.setBirthDate(profileDAO.getProfileById(profileDTO.getId()).get().getBirthDate());

        if (profileDTO.getPhotoFileName() == null)
            profileDTO.setPhotoFileName(profileDAO.getProfileById(profileDTO.getId()).get().getPhotoFileName());
        else {
            profileDTO.setPhotoFileName(profileDTO.getId() + "-" + profileDTO.getPhotoImage().getSubmittedFileName());
            contentService.uploadPhoto(profileDTO.getPhotoFileName(), profileDTO.getPhotoImage().getInputStream());
        }
        profileDAO.updateProfile(profileMapper.mapFromDTO(profileDTO));
    }

    public boolean deleteProfile(long id) {
        return profileDAO.deleteProfile(id);
    }

    public Optional<ProfileDTO> getProfile(Long id) {
        if (id == null) return Optional.empty();
        return profileDAO.getProfileById(id).map(profileMapper::mapToDTO);
    }

    public List<ProfileDTO> getProfiles(ProfileFilter filter) {
        return profileDAO.getAllProfiles(filter).stream().map(profileMapper::mapToDTO).toList();
    }

    public Optional<ProfileDTO> login(ProfileDTO profileDTO) {
        return profileDAO.getProfileByEmail(profileDTO.getEmail())
                .filter(profile -> profile.getPassword().equals(profileDTO.getPassword()))
                .map(profileMapper::mapToDTO);
    }
}
