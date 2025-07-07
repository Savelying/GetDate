package ru.savelying.getdate.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileDAO {
    @Getter
    private final static ProfileDAO instance = new ProfileDAO();

    @SneakyThrows
    public static ProfileDAO getInstance() {
        Class.forName("org.postgresql.Driver");
        return instance;
    }

    public static final String dbName = "getdate";
    public static final String dbURL = "jdbc:postgresql://localhost:5432/" + dbName;
    public static final String dbUser = "postgres";
    public static final String dbPassword = "qwerty";


    public Profile createProfile(Profile profile) {
        String sql = "insert into profiles(email, password, name, birth_date, status, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, profile.getEmail());
            statement.setString(2, profile.getPassword());
            statement.setString(3, profile.getName());
            statement.setDate(4, Date.valueOf(profile.getBirthDate()));
            statement.setString(5, profile.getStatus().toString());
            statement.setString(6, profile.getRole().toString());
//            statement.executeUpdate("create table profiles(id bigserial not null primary key, email varchar not null unique, password varchar not null, name varchar, info text, gender varchar, birth_date date not null, status varchar not null, role varchar not null, reg_date timestamp not null default current_timestamp)");

            int insertCount = statement.executeUpdate();
            log.debug("Insert count : {}", insertCount);
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            profile.setId(rs.getLong("id"));
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProfile(Profile profile) {

        // Ссобираем строку запроса прямым способом конкатенации
//        StringBuilder query = new StringBuilder("UPDATE profiles SET");
//        query.append(" email = ?");
//        query.append(", password = ?");
//        query.append(", name = ?");
//        query.append(", info = ?");
//        query.append(", gender = ?");
//        query.append(", birth_date = ?");
//        query.append(", status = ?");
//        query.append(", role = ?");
//        query.append(", photo = ?");
//        query.append(" WHERE id = ?");
//        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
//             PreparedStatement statement = connection.prepareStatement(query.toString())) {
//            statement.setString(1, profile.getEmail());
//            statement.setString(2, profile.getPassword());
//            statement.setString(3, profile.getName());
//            statement.setString(4, profile.getInfo());
//            statement.setString(5, profile.getGender().toString());
//            statement.setDate(6, Date.valueOf(profile.getBirthDate()));
//            statement.setString(7, profile.getStatus().toString());
//            statement.setString(8, profile.getRole().toString());
//            statement.setString(9, profile.getPhotoFileName());
//            statement.setLong(10, profile.getId());
//            log.debug("Final update sql: {}", query);
//            int updateCount = statement.executeUpdate();
//            log.debug("Update count : {}", updateCount);

        // Собираем строку запроса с помощью форматирования с подставлением аргументов
        List<Object> args = new ArrayList<>();
        StringBuilder query = new StringBuilder("UPDATE profiles SET");
        if (profile.getEmail() != null) {
            query.append(" email = ?");
            args.add(profile.getEmail());
        }
        if (profile.getPassword() != null) {
            query.append(", password = ?");
            args.add(profile.getPassword());
        }
        if (profile.getName() != null) {
            query.append(", name = ?");
            args.add(profile.getName());
        }
        if ((profile.getInfo() != null)) {
            query.append(", info = ?");
            args.add(profile.getInfo());
        }
        if (profile.getGender() != null) {
            query.append(", gender = ?");
            args.add(profile.getGender().toString());
        }
        if (profile.getBirthDate() != null) {
            query.append(", birth_date = ?");
            args.add(Date.valueOf(profile.getBirthDate()));
        }
        if (profile.getStatus() != null) {
            query.append(", status = ?");
            args.add(profile.getStatus().toString());
        }
        if (profile.getRole() != null) {
            query.append(", role = ?");
            args.add(profile.getRole().toString());
        }
        if (profile.getPhotoFileName() != null) {
            query.append(", photo = ?");
            args.add(profile.getPhotoFileName());
        }
        if (profile.getId() != null) {
            query.append(" WHERE id = ?");
            args.add(profile.getId());
        }
        String updateQuery = query.toString();
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            for (int i = 0; i < args.size(); i++) statement.setObject(i + 1, args.get(i));
            log.debug("Final update sql: {}", statement);
            int updateCount = statement.executeUpdate();
            log.debug("Update count : {}", updateCount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProfile(Long id) {
        String sql = "delete from profiles where id = ?";
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int deleteCount = statement.executeUpdate();
            log.debug("Delete count : {}", deleteCount);
            return deleteCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileById(Long id) {
        String sql = "select * from profiles where id = ?";
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            Profile profile = null;
            if (rs.next()) profile = getProfileFromDB(rs);
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileByEmail(String email) {
        String sql = "select * from profiles where email = ?";
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            Profile profile = null;
            if (resultSet.next()) profile = getProfileFromDB(resultSet);
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Profile> getAllProfiles() {
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
            String sql = "select * from profiles";
            ResultSet resultSet = statement.executeQuery(sql);
            List<Profile> profiles = new ArrayList<>();
            while (resultSet.next()) profiles.add(getProfileFromDB(resultSet));
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getAllEmails() {
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
            String sql = "select * from profiles";
            ResultSet resultSet = statement.executeQuery(sql);
            Set<String> emails = new HashSet<>();
            while (resultSet.next()) emails.add(getProfileFromDB(resultSet).getEmail());
            return emails;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Profile getProfileFromDB(ResultSet resultSet) throws SQLException {
        Profile profile = new Profile();
        profile.setId(resultSet.getLong("id"));
        if (resultSet.getString("email") != null) profile.setEmail(resultSet.getString("email"));
        if (resultSet.getString("password") != null) profile.setPassword(resultSet.getString("password"));
        if (resultSet.getString("name") != null) profile.setName(resultSet.getString("name"));
        if (resultSet.getString("info") != null) profile.setInfo(resultSet.getString("info"));
        if (resultSet.getString("gender") != null) profile.setGender(Gender.valueOf(resultSet.getString("gender")));
        if (resultSet.getString("birth_date") != null) profile.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        if (resultSet.getString("status") != null) profile.setStatus(Status.valueOf(resultSet.getString("status")));
        if (resultSet.getString("role") != null) profile.setRole(Role.valueOf(resultSet.getString("role")));
        if (resultSet.getString("photo") != null) profile.setPhotoFileName(resultSet.getString("photo"));
        return profile;
    }
}

