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
import ru.savelying.getdate.validator.Validator;

import java.io.IOException;

@Slf4j
@WebServlet("/registration")
@MultipartConfig
public class RegControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final Validator validator = Validator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/jsp/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        if (!validator.validate(profileDTO).isValid()) {
            req.setAttribute("errors", validator.validate(profileDTO).getErrors());
            req.setAttribute("profile", profileDTO);
            doGet(req, resp);
        } else {
            log.trace("Account create successful with email={}", profileDTO.getEmail());
            Long id = profileService.createProfile(profileDTO);
            resp.sendRedirect("/profile?id=" + id);
        }
    }
}
