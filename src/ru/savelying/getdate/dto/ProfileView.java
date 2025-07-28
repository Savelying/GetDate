package ru.savelying.getdate.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.savelying.getdate.model.Gender;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileView {
    Long id;
    String name;
    String info;
    Gender gender;
    Integer age;
    String photoFileName;
}
