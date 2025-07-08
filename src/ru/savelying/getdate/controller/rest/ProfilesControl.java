package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(REST_URL + PROFILES_URL)
@MultipartConfig
public class ProfilesControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            ProfileFilter filter = profileMapper.mapToFilter(req);
            jsonMapper.writeValue(writer, profileService.getProfiles(filter));
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }
}
