package ru.savelying.getdate.mapper;

import ru.savelying.getdate.dto.ProfileFilter;

public interface Mapper<From, To> {
    To mapToDTO(From from);
    From mapFromDTO(To to);
}
