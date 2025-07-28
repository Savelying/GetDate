package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.LikeDTO;
import ru.savelying.getdate.dto.ProfileView;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.GetDateService;

import java.io.IOException;
import java.util.Optional;

import static ru.savelying.getdate.utils.UrlUtils.GETDATE_URL;
import static ru.savelying.getdate.utils.UrlUtils.getJspPath;

@WebServlet(GETDATE_URL)
public class GetDateControl extends HttpServlet {
    private final GetDateService getDateService = GetDateService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getNext(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getNext(req, resp);
    }

    private void getNext(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LikeDTO likeDTO = profileMapper.mapToLike(req);
        Optional<ProfileView> profileView = getDateService.setNext(likeDTO);
        if (profileView.isPresent()) {
            req.setAttribute("next", profileView.get());
            req.getRequestDispatcher(getJspPath(GETDATE_URL)).forward(req, resp);
        } else req.getRequestDispatcher(getJspPath("/end")).forward(req, resp);;
    }
}
