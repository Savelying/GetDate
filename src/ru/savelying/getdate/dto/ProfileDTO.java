package ru.savelying.getdate.dto;

import jakarta.servlet.http.Part;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDTO {
    Long id;
    String email;
    String newEmail;
    String password;
    String newPassword;
    String confirmPassword;
    String name;
    String info;
    Gender gender;
    LocalDate birthDate;
    Integer age;
    Status status;
    Part photoImage;
    String photoFileName;
    Role role;
}
