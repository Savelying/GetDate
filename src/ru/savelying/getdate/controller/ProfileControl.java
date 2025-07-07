package ru.savelying.getdate.controller;

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
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static ru.savelying.getdate.utils.StringUtils.isBlank;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(PROFILE_URL + "/*")
@MultipartConfig
public class ProfileControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            Optional<ProfileDTO> optProfileDto = profileService.getProfile(Long.parseLong(req.getParameter("id")));
            if (optProfileDto.isPresent()) {
                if (req.getRequestURI().equals("/profile/pdf")) {
                    resp.setHeader("Content-Disposition", "attachment; filename=\"profile.pdf\"");
                    resp.setContentType("application/pdf");
                    resp.setCharacterEncoding("UTF-8");
                    ProfileDTO profileDto = optProfileDto.get();
                    try (OutputStream out = resp.getOutputStream()) {
                        Document pdf = new Document();
                        PdfWriter.getInstance(pdf, out);
                        profileMapper.getProfilePdf(pdf, profileDto);
                    } catch (DocumentException e) {
                        throw new IOException(e);
                    }
                } else {
                    req.setAttribute("profile", optProfileDto.get());
                    req.getRequestDispatcher(getJspPath(PROFILE_URL)).forward(req, resp);
                }
            } else {
                resp.sendError(SC_NOT_FOUND);
            }
        } else {
            req.setAttribute("profiles", profileService.getProfiles());
            req.getRequestDispatcher(getJspPath("/profiles")).forward(req, resp);
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
        String id = req.getParameter("id");
        if (!isBlank(id) && profileService.deleteProfile(Long.parseLong(id))) {
            log.info("Profile with id {} has been deleted", id);
            ProfileDTO profileDTO = (ProfileDTO) req.getSession().getAttribute("userDetails");
            if (profileDTO.getId().toString().equals(id)) {
                req.getSession().invalidate();
            }
            resp.setStatus(SC_NO_CONTENT);
            resp.sendRedirect(REGISTRATION_URL);
        } else {
            resp.sendError(SC_NOT_FOUND);
        }
    }
}
