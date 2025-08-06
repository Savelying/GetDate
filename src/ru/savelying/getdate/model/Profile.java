package ru.savelying.getdate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile {
    Long id;
    String email;
    String password;
    String name;
    String info;
    Gender gender;
    LocalDate birthDate;
    Status status;
    String photoFileName;
    Role role;
    int version = 0;

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", status=" + status +
                ", photoFileName='" + photoFileName + '\'' +
                ", role=" + role +
                '}';
    }
}
