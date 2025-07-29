package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static ru.savelying.getdate.utils.UrlUtils.MATCHES_URL;
import static ru.savelying.getdate.utils.UrlUtils.REST_URL;

@WebServlet(REST_URL + MATCHES_URL)
public class MatchesControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            ProfileDTO user = (ProfileDTO) req.getSession().getAttribute("user");
            ProfileFilter profileFilter = profileMapper.mapToFilter(req);
            jsonMapper.writeValue(writer, profileService.getMatches(user.getId(), profileFilter));
        } catch (DatabindException ex) {
         req.setAttribute("errors", List.of(ex.getMessage()));
         resp.sendError(SC_BAD_REQUEST);
        }
    }
}
