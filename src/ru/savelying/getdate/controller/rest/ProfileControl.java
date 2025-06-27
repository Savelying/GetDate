package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static ru.savelying.getdate.utils.StringUtils.isBlank;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(REST_URL + PROFILE_URL)
@MultipartConfig
public class ProfileControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .addModule(new JavaTimeModule())
            .build();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (req.getParameter("id") != null) {
                Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
                if (optProfileDto.isPresent()) objectMapper.writeValue(writer, optProfileDto.get());
                else resp.sendError(SC_NOT_FOUND);
            } else objectMapper.writeValue(writer, profileService.getProfiles());
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        profileService.updateProfile(profileDTO);
        resp.sendRedirect(req.getHeader("referer"));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isBlank(req.getParameter("id")) && profileService.deleteProfile(Long.parseLong(req.getParameter("id")))) {
            log.info("Profile with id {} has been deleted", req.getParameter("id"));
            ProfileDTO profileDTO = (ProfileDTO) req.getSession().getAttribute("userDetails");
            if (req.getParameter("id").equals(profileDTO.getId().toString())) {
                req.getSession().invalidate();
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.sendRedirect(REGISTRATION_URL);
        } else {
            resp.sendError(SC_NOT_FOUND);
        }
    }
}
