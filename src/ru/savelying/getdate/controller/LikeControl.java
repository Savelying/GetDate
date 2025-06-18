package ru.savelying.getdate.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.service.LikeService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/like")
public class LikeControl extends HttpServlet {
    private String servletName;
    private final LikeService likeService = LikeService.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletName = config.getServletName();
        System.out.println("init servlet " + servletName);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = 0;
        if (req.getParameter("id") != null) id = Long.parseLong(req.getParameter("id"));

        try (PrintWriter writer = resp.getWriter()) {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("UTF-8");
            writer.write("<h2>");
            writer.write("<p> RequestURI: " + req.getRequestURI() + "</p>");
            writer.write("<p> User agent header: " + req.getHeader("User-Agent") + "</p>");
            writer.write("<p> Likes count: " + likeService.getLikesByProfileId(id) + "</p>");
            writer.write("</h2>");
        }
    }

    @Override
    public void destroy() {
        System.out.println("destroy servlet " + servletName);
    }

    //    public int count() {
//        return 10;
//    }
}
