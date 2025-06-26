package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@Slf4j
@WebServlet("/profile")
@MultipartConfig
public class ProfileControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String toURL = null;
        if (req.getParameter("id") != null) {
            Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
            if (optProfileDto.isPresent()) {
                req.setAttribute("profile", optProfileDto.get());
                toURL = "WEB-INF/jsp/profile.jsp";
            }
        } else {
            req.setAttribute("profiles", profileService.getProfiles());
            toURL = "WEB-INF/jsp/profiles.jsp";
        }
        if (toURL == null) resp.sendError(SC_NOT_FOUND);
        else req.getRequestDispatcher(toURL).forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        System.out.println(profileDTO.getPhotoImage() + " : " + profileDTO.getPhotoFileName());
        profileService.updateProfile(profileDTO);
        resp.sendRedirect("/profile?id=" + profileDTO.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getParameter("id").isBlank()) {
            profileService.deleteProfile(Long.parseLong(req.getParameter("id")));
        }
        resp.sendRedirect("/profile");
    }
}
