package ru.savelying.getdate.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.Action;
import ru.savelying.getdate.dto.LikeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.savelying.getdate.utils.ConnectUtils.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LikeDAO {
    @Getter
    private final static LikeDAO instance = new LikeDAO();

    //language=POSTGRES-PSQL
    private final static String SELECT = "select \"like\" from likes where from_id = ? and to_id = ?";
    private final static String INSERT = "insert into likes(from_id, to_id, \"like\", match) VALUES (?, ?, ?, ?) on conflict(from_id, to_id) do update set \"like\" = ?, match = ?";

    public void writeLike(LikeDTO likeDTO) {
        try (Connection connection = getConnnect();
             PreparedStatement selectStatement = connection.prepareStatement(SELECT);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT)) {
            boolean isLike = likeDTO.getAction() == Action.LIKE;
            boolean isMatch = false;
            if (isLike) {
                selectStatement.setObject(1, likeDTO.getToId());
                selectStatement.setObject(2, likeDTO.getFromId());
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) isMatch = resultSet.getBoolean("like");
            }
            if (isMatch) {
                insertToLikes(insertStatement, likeDTO.getFromId(), likeDTO.getToId(), isLike, isMatch);
                insertToLikes(insertStatement, likeDTO.getToId(), likeDTO.getFromId(), isLike, isMatch);
            } else {
                insertToLikes(insertStatement, likeDTO.getFromId(), likeDTO.getToId(), isLike, isMatch);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertToLikes(PreparedStatement insertStatement, Long likeDTO1, Long likeDTO2, boolean isLike, boolean isMatch) throws SQLException {
        insertStatement.setObject(1, likeDTO1);
        insertStatement.setObject(2, likeDTO2);
        insertStatement.setObject(3, isLike);
        insertStatement.setObject(4, isMatch);
        insertStatement.setObject(5, isLike);
        insertStatement.setObject(6, isMatch);
        insertStatement.executeUpdate();
    }
}
