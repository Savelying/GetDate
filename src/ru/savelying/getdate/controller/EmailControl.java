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
import ru.savelying.getdate.validator.RegValidator;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static ru.savelying.getdate.utils.StringUtils.isBlank;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(EMAIL_URL)
@MultipartConfig
public class EmailControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final RegValidator validator = RegValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String toURL = null;
        if (req.getParameter("id") != null) {
            Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
            if (optProfileDto.isPresent()) {
                req.setAttribute("profile", optProfileDto.get());
                toURL = getJspPath(EMAIL_URL);
            }
        }
        if (toURL == null) resp.sendError(SC_NOT_FOUND);
        else req.getRequestDispatcher(toURL).forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        if (validator.validate(profileDTO).isValid()) {
            profileService.updateProfile(profileDTO);
            if (!isBlank(profileDTO.getEmail())) {
                log.warn("Profile with id {} changed email to {}", profileDTO.getId(), profileDTO.getEmail());
            }
            resp.sendRedirect(String.format(PROFILE_URL + "?id=%s", profileDTO.getId()));
        } else {
            req.setAttribute("errors", validator.validate(profileDTO).getErrors());
            req.setAttribute("profile", profileDTO);
            doGet(req, resp);
        }
    }
}
