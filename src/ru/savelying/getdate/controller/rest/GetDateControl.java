package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.LikeDTO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.dto.ProfileView;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.service.GetDateService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;
import static ru.savelying.getdate.utils.UrlUtils.*;

@WebServlet(REST_URL + GETDATE_URL)
public class GetDateControl extends HttpServlet {
    private final GetDateService getDateService = GetDateService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter(); BufferedReader reader = req.getReader()) {
            LikeDTO likeDTO = jsonMapper.readValue(reader, LikeDTO.class);
            ProfileDTO user = (ProfileDTO) req.getSession().getAttribute("user");
            likeDTO.setFromId(user.getId());
            Optional<ProfileView> profileView = getDateService.setNext(likeDTO);
            if (profileView.isPresent()) jsonMapper.writeValue(writer, profileView.get());
            else resp.sendError(SC_NOT_FOUND);
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getLocalizedMessage(), ex.getOriginalMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }
}
