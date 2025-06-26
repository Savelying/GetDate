package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.model.exception.DuplicateEmailException;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@Slf4j
@WebServlet("/email")
@MultipartConfig
public class EmailControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String toURL = null;
        if (req.getParameter("id") != null) {
            Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
            if (optProfileDto.isPresent()) {
                req.setAttribute("profile", optProfileDto.get());
                toURL = "WEB-INF/jsp/email.jsp";
            }
        }
        if (toURL == null) resp.sendError(SC_NOT_FOUND);
        else req.getRequestDispatcher(toURL).forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        try {
            profileService.updateProfile(profileDTO);
            log.info("Email {} was successfully updated in profile id={}", profileDTO.getEmail(), profileDTO.getId());
            resp.sendRedirect("/profile?id=" + profileDTO.getId());
        } catch (DuplicateEmailException e) {
            resp.sendError(SC_BAD_REQUEST);
        }
    }
}
