package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static ru.savelying.getdate.utils.UrlUtils.LANG_URL;

@WebServlet(LANG_URL)
public class LanguageControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie cookie = new Cookie("lang", req.getParameter("lang").equals("ru") ? "ru" : "en");

        resp.addCookie(cookie);
//        resp.addHeader("Set-Cookie", "lang=" + cookie.getValue());

        resp.sendRedirect(req.getHeader("referer"));
    }
}
