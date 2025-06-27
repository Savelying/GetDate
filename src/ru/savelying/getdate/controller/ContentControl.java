package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.service.ContentService;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static ru.savelying.getdate.utils.UrlUtils.CONTENT_URL;

@WebServlet(CONTENT_URL + "/*")
public class ContentControl extends HttpServlet {
private final static ContentService contentService = ContentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String contentPath = req.getRequestURI().replace("/content", "");
            resp.setContentType("application/octet-stream");
            contentService.downloadPhoto(contentPath, resp.getOutputStream());
        } catch (IOException e) {
            resp.sendError(SC_NOT_FOUND);
        }
    }
}
