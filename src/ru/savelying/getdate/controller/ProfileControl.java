package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String toURL = "/not found";

        if (req.getParameter("id") != null) {
            Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
            if (optProfileDto.isPresent()) {
                req.setAttribute("profile", optProfileDto.get());
                toURL = "/WEB-INF/jsp/profile.jsp";
            }
        } else {
            req.setAttribute("profiles", profileService.getProfiles());
            toURL = "/WEB-INF/jsp/profiles.jsp";
        }

        req.getRequestDispatcher(toURL).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = getProfileDTO(req);
        Long id = profileService.createProfile(profileDTO);
        resp.sendRedirect("/profile?id=" + id);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = getProfileDTO(req);
        profileService.updateProfile(profileDTO);
        resp.sendRedirect("/profile?id=" + profileDTO.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getParameter("id").isBlank()) {
            profileService.deleteProfile(Long.parseLong(req.getParameter("id")));
        }
        resp.sendRedirect("/profile?id=all");
    }

    private ProfileDTO getProfileDTO(HttpServletRequest req) {
        ProfileDTO profileDTO = new ProfileDTO();
        if (!req.getParameter("id").isBlank()) profileDTO.setId(Long.parseLong(req.getParameter("id")));
        profileDTO.setEmail(req.getParameter("email"));
        profileDTO.setName(req.getParameter("name"));
        profileDTO.setInfo(req.getParameter("info"));
        profileDTO.setGender(Gender.valueOf(req.getParameter("gender")));
        if (req.getParameter("birthDate") != null && !req.getParameter("birthDate").isBlank())
            profileDTO.setBirthDate(LocalDate.parse(req.getParameter("birthDate")));
        return profileDTO;
    }

//    public String create(String create) {
//        String[] params = create.split(", ");
//        if (params.length != 3) return "Bad Request: need 3 parameters to create a profile";
//        Profile profile = new Profile();
//        profile.setEmail(params[0]);
//        profile.setName(params[1]);
//        profile.setInfo(params[2]);
//        profileService.createProfile(profile);
//        return "Created " + profile.toString();
//    }
//
//    public String update(String update) {
//        String[] params = update.split(", ");
//        if (params.length != 4) return "Bad Request: need 4 parameters to update a profile";
//        Long id;
//        try {
//            id = Long.parseLong(params[0]);
//        } catch (NumberFormatException e) {
//            return "Bad Request: need a number for the ID";
//        }
//        Profile profile = new Profile();
//        profile.setId(Long.valueOf(params[0]));
//        profile.setEmail(params[1]);
//        profile.setName(params[2]);
//        profile.setInfo(params[3]);
//        profileService.updateProfile(profile);
//        return "Updated " + profile.toString();
//    }
//
//    public String delete(String delete) {
//        String[] params = delete.split(", ");
//        if (params.length != 1) return "Bad Request: need 1 parameter to delete a profile";
//        Long id;
//        try {
//            id = Long.parseLong(params[0]);
//        } catch (NumberFormatException e) {
//            return "Bad Request: need a number for the ID";
//        }
//        boolean result = profileService.deleteProfile(id);
//        if (!result) return "Profile does not exist";
//        return "Deleted profile №" + id;
//    }
//
//    public Optional<Profile> search(Long id) {
//        return profileService.getProfile(id);
//    }
//
//    public List<Profile> all() {
//        return profileService.getProfiles();
//    }
//
//
//    public String search(String search) {
//        String[] params = search.split(", ");
//        if (params.length != 1) return "Bad Request: need 1 parameter to search a profile";
//        Long id;
//        try {
//            id = Long.parseLong(params[0]);
//        } catch (NumberFormatException e) {
//            return "Bad Request: need a number for the ID";
//        }
//        Optional<Profile> profileOptional = profileService.getProfile(id);
//        if (profileOptional.isEmpty()) return "Profile does not exist";
//        Profile profile = profileOptional.get();
//        return "Finded " + profile.toString();
//    }
//
//    public String all() {
//        return "All profiles:\n" + profileService.getProfiles().toString()/* + "\n"*/;
//    }
}
