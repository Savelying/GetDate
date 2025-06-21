package ru.savelying.getdate.service;

import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.ProfileMapper;

import java.util.List;
import java.util.Optional;

public class ProfileService {
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final static ProfileService instance = new ProfileService();

    private ProfileService() {
    }

    public static ProfileService getInstance() {
        return instance;
    }

    public Long createProfile(ProfileDTO profileDTO) {
        return profileDAO.createProfile(profileMapper.mapFromDTO(profileDTO)).getId();
    }

    public void updateProfile(ProfileDTO profileDTO) {
        if (profileDTO.getBirthDate() == null) {
            profileDTO.setBirthDate(profileDAO.getProfile(profileDTO.getId()).get().getBirthDate());
        }
        profileDAO.updateProfile(profileMapper.mapFromDTO(profileDTO));
    }

    public boolean deleteProfile(long id) {
        return profileDAO.deleteProfile(id);
    }

    public Optional<ProfileDTO> getProfile(Long id) {
        if (id == null) return Optional.empty();
        return profileDAO.getProfile(id).map(profileMapper::mapToDTO);
    }

    public List<ProfileDTO> getProfiles() {
        return profileDAO.getAllProfiles().stream().map(profileMapper::mapToDTO).toList();
    }
}
