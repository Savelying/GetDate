package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;
import ru.savelying.getdate.validator.RegValidator;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.savelying.getdate.utils.StringUtils.isBlank;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(REST_URL + PROFILE_URL)
@MultipartConfig
public class ProfileControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final RegValidator validator = new RegValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            if (req.getParameter("id") != null) {
                Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
                if (optProfileDto.isPresent()) jsonMapper.writeValue(writer, optProfileDto.get());
                else resp.sendError(SC_NOT_FOUND);
            }
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()) {
            ProfileDTO profileDTO = jsonMapper.readValue(reader, ProfileDTO.class);
            if (validator.validate(profileDTO).isValid()) {
                Long id = profileService.createProfile(profileDTO);
                log.info("Account create successful with email={} and id={}", profileDTO.getEmail(), id);
            } else {
                req.setAttribute("errors", validator.validate(profileDTO).getErrors());
                req.setAttribute("profile", profileDTO);
                resp.sendError(SC_BAD_REQUEST);
            }
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getLocalizedMessage(), ex.getOriginalMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part jsonPart = req.getPart("json");
        if (jsonPart != null) {
            Part photoPart = req.getPart("photo");
            try (InputStream in = jsonPart.getInputStream()) {
                ProfileDTO profileDTO = jsonMapper.readValue(in, ProfileDTO.class);
                if (photoPart != null && !isBlank(photoPart.getSubmittedFileName())) profileDTO.setPhotoImage(photoPart);
                else profileDTO.setPhotoImage(null);
                profileService.updateProfile(profileDTO);
            } catch (DatabindException ex) {
                req.setAttribute("errors", List.of(ex.getLocalizedMessage(), ex.getOriginalMessage()));
                resp.sendError(SC_BAD_REQUEST);
            }
        } else resp.sendError(SC_NOT_FOUND);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (!isBlank(id) && profileService.deleteProfile(Long.parseLong(id))) {
            ProfileDTO profileDTO = (ProfileDTO) req.getSession().getAttribute("user");
            if (profileDTO.getId().toString().equals(id)) req.getSession().invalidate();
            resp.setStatus(SC_NO_CONTENT);
        } else resp.sendError(SC_NOT_FOUND);
    }
}
