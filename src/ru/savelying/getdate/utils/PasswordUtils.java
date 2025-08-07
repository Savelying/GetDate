package ru.savelying.getdate.utils;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

@UtilityClass
public class PasswordUtils {

    public static String generatePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String passwordFromDTO, String passwordFromDB) {
        return BCrypt.checkpw(passwordFromDTO, passwordFromDB);
    }
}
