package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;

import static ru.savelying.getdate.utils.UrlUtils.*;

@WebServlet(MATCHES_URL)
public class MatchesControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = (ProfileDTO) req.getSession().getAttribute("user");
        ProfileFilter profileFilter = profileMapper.mapToFilter(req);
        req.setAttribute("profiles", profileService.getMatches(profileDTO.getId(), profileFilter));
        req.setAttribute("filter", profileFilter);
        req.getRequestDispatcher(getJspPath(MATCHES_URL)).forward(req, resp);
    }
}
