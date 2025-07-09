package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;

import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(PROFILES_URL)
@MultipartConfig
public class ProfilesControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileFilter filter = profileMapper.mapToFilter(req);
        req.setAttribute("profiles", profileService.getProfiles(filter));
        req.setAttribute("filter", filter);
        req.getRequestDispatcher(getJspPath(PROFILES_URL)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        profileService.genSomeProfiles(Integer.parseInt(req.getParameter("n")));
        doGet(req, resp);
    }
}
