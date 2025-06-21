package ru.savelying.getdate.mapper;

public interface Mapper<From, To> {
    To mapToDTO(From obj);
    From mapFromDTO(To obj);
}
