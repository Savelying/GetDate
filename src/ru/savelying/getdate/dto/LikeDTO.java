package ru.savelying.getdate.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LikeDTO {
    Long fromId;
    Long toId;
    Action action;
}
