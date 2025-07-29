package ru.savelying.getdate.utils;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class UrlUtils {
    public static final String PROFILE_URL = "/profile";
    public static final String PROFILES_URL = "/profiles";
    public static final String EMAIL_URL = "/email";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String REGISTRATION_URL = "/registration";
    public static final String LANG_URL = "/language";
    public static final String CONTENT_URL = "/content";
    public static final String GETDATE_URL = "/getdate";
    public static final String MATCHES_URL = "/matches";
    public static final String REST_URL = "/api/v1";
    public static final String BASE_CONTENT_PATH = ConfigFileUtils.getConfig("app.base.content.path");

    public static final Set<String> PRIVATE_PATHS = Set.of(PROFILE_URL, EMAIL_URL, REST_URL, GETDATE_URL);

    public static final Set<String> ENTRY_PATHS = Set.of(LOGIN_URL, REGISTRATION_URL);

    public static String getJspPath(String url) {
        return "/WEB-INF/jsp" + url + ".jsp";
    }

    public static String getProfilePhotoPath(Long id, String fileName) {
        return "/profiles/" + id + "/" + fileName;
    }

}
