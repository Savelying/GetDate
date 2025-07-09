package ru.savelying.getdate.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileFilter {
    String nameStartWith;
    String emailStartWith;
    Integer lowAge;
    Integer highAge;
    Status status;
    Role role;
    String sortBy;
    Integer pageNo;
    Integer pageSize;
}
