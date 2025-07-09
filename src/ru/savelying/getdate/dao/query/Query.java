package ru.savelying.getdate.dao.query;

import java.util.List;

public record Query(String sql, List<Object> args) {
}
