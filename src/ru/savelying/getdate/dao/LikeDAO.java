package ru.savelying.getdate.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.Action;
import ru.savelying.getdate.dto.LikeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static ru.savelying.getdate.utils.ConnectUtils.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeDAO {
    @Getter
    private final static LikeDAO instance = new LikeDAO();

    //language=PostgreSQL
    private final static String LIKE = "with has_match as (update likes set match = true, created_date = current_timestamp where ? = true and from_id = ? and to_id = ? and \"like\" = true returning 1) insert into likes (from_id, to_id, \"like\", match) select ?, ?, ?, exists(select 1 from has_match)";

    @SneakyThrows
    public void writeLike(LikeDTO likeDTO) {
        try (Connection connection = getConnnect();
        PreparedStatement statement = connection.prepareStatement(LIKE)) {
            statement.setBoolean(1, likeDTO.getAction() == Action.LIKE);
            statement.setLong(2, likeDTO.getToId());
            statement.setLong(3, likeDTO.getFromId());
            statement.setLong(4, likeDTO.getFromId());
            statement.setLong(5, likeDTO.getToId());
            statement.setBoolean(6, likeDTO.getAction() == Action.LIKE);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
