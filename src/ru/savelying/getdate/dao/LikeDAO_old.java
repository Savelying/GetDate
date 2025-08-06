//package ru.savelying.getdate.dao;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import ru.savelying.getdate.dto.Action;
//import ru.savelying.getdate.dto.LikeDTO;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import static ru.savelying.getdate.utils.ConnectUtils.getConnnect;
//
//@Slf4j
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//public class LikeDAO {
//    @Getter
//    private final static LikeDAO instance = new LikeDAO();
//
//    //language=PostgreSQL
//    private final static String SELECT = "select \"like\" from likes where from_id = ? and to_id = ?";
//    //language=PostgreSQL
//    private final static String INSERT = "insert into likes(from_id, to_id, \"like\", match) VALUES (?, ?, ?, ?) on conflict(from_id, to_id) do update set \"like\" = ?, match = ?, created_date = current_timestamp";
//
//    @SneakyThrows
//    public void writeLike(LikeDTO likeDTO) {
//        Connection connection = null;
//        PreparedStatement selectStatement = null;
//        PreparedStatement insertStatement = null;
//        try {
//            connection = getConnnect();
//            connection.setAutoCommit(false);
//            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
//            selectStatement = connection.prepareStatement(SELECT);
//            insertStatement = connection.prepareStatement(INSERT);
//            boolean isLike = likeDTO.getAction() == Action.LIKE;
//            boolean isMatch = false;
//            if (isLike) {
//                selectStatement.setObject(1, likeDTO.getToId());
//                selectStatement.setObject(2, likeDTO.getFromId());
//                ResultSet resultSet = selectStatement.executeQuery();
//                if (resultSet.next()) isMatch = resultSet.getBoolean("like");
//            } else {
//                insertToLikes(insertStatement, likeDTO.getFromId(), likeDTO.getToId(), isLike, isMatch);
//                insertStatement.addBatch();
//            }
//            if (isMatch) {
//                insertToLikes(insertStatement, likeDTO.getFromId(), likeDTO.getToId(), isLike, isMatch);
//                insertStatement.addBatch();
//                insertToLikes(insertStatement, likeDTO.getToId(), likeDTO.getFromId(), isLike, isMatch);
//                insertStatement.addBatch();
//            } else {
//                insertToLikes(insertStatement, likeDTO.getFromId(), likeDTO.getToId(), isLike, isMatch);
//                insertStatement.addBatch();
//            }
//            insertStatement.executeBatch();
//            connection.commit();
//        } catch (Exception e) {
//            if (connection != null) connection.rollback();
//            throw new RuntimeException(e);
//        } finally {
//            if (selectStatement != null) selectStatement.close();
//            if (insertStatement != null) insertStatement.close();
//            if (connection != null) connection.close();
//        }
//    }
//
//    private static void insertToLikes(PreparedStatement insertStatement, Long likeDTO1, Long likeDTO2, boolean isLike, boolean isMatch) throws SQLException {
//        insertStatement.setObject(1, likeDTO1);
//        insertStatement.setObject(2, likeDTO2);
//        insertStatement.setObject(3, isLike);
//        insertStatement.setObject(4, isMatch);
//        insertStatement.setObject(5, isLike);
//        insertStatement.setObject(6, isMatch);
//    }
//}
