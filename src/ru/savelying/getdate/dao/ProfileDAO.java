package ru.savelying.getdate.dao;

import lombok.AccessLevel;
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
    //    @Getter
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
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
            System.out.println(profile.toString());
//            statement.executeUpdate("create table profiles(id bigserial not null primary key, email varchar not null unique, password varchar not null, name varchar, info text, gender varchar, birth_date date not null, status varchar not null, role varchar not null, reg_date timestamp not null default current_timestamp)");

            int insertCount = statement.executeUpdate("insert into profiles(email, password, name, birth_date, status, role) VALUES ('" +
                    profile.getEmail() + "', '" +
                    profile.getPassword() + "', '" +
                    profile.getName() + "', '" +
                    Date.valueOf(profile.getBirthDate()) + "', '" +
                    profile.getStatus() + "', '" +
                    profile.getRole() + "')", Statement.RETURN_GENERATED_KEYS);
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
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
        Statement statement = connection.createStatement()) {
            // Ссобираем строку запроса прямым способом конкатенации
            StringBuilder query = new StringBuilder("UPDATE profiles SET email = '" + profile.getEmail() + "'");
            if (profile.getPassword() != null) query.append(", password = '" + profile.getPassword() + "'");
            if (profile.getName() != null) query.append(", name = '" + profile.getName() + "'");
            if (profile.getInfo() != null) query.append(", info = '" + profile.getInfo() + "'");
            if (profile.getGender() != null) query.append(", gender = '" + profile.getGender() + "'");
            if (profile.getBirthDate() != null) query.append(", birth_date = '" + Date.valueOf(profile.getBirthDate()) + "'");
            if (profile.getStatus() != null) query.append(", status = '" + profile.getStatus() + "'");
            if (profile.getRole() != null) query.append(", role = '" + profile.getRole() + "'");
            if (profile.getPhotoFileName() != null) query.append(", photo = '" + profile.getPhotoFileName() + "'");
            query.append(" WHERE id = '" + profile.getId() + "'");
            log.debug("Final udate sql: {}", query);
            int updateCount = statement.executeUpdate(query.toString());
            log.debug("Update count : {}", updateCount);

            // Собираем строку запроса с помощью форматирования с подставлением аргументов
//            List<Object> args = new ArrayList<>();
//            args.add(profile.getEmail());
//            args.add(profile.getPassword());
//            StringBuilder query = new StringBuilder("UPDATE profiles SET email = '%s', password = '%s'");
//            if (profile.getName() != null) {
//                query.append(", name = '%s'");
//                args.add(profile.getName());
//            }
//            if ((profile.getInfo() != null)) {
//                query.append(", info = '%s'");
//                args.add(profile.getInfo());
//            }
//            if (profile.getGender() != null) {
//                query.append(", gender = '%s'");
//                args.add(profile.getGender());
//            }
//            if (profile.getBirthDate() != null) {
//                query.append(", birth_date = '%s'");
//                args.add(Date.valueOf(profile.getBirthDate()));
//            }
//            if (profile.getStatus() != null) {
//                query.append(", status = '%s'");
//                args.add(profile.getStatus());
//            }
//            if (profile.getRole() != null) {
//                query.append(", role = '%s'");
//                args.add(profile.getRole());
//            }
//            if (profile.getPhotoFileName() != null) {
//                query.append(", photo = '%s'");
//                args.add(profile.getPhotoFileName());
//            }
//
//            query.append(" WHERE id = '%s'");
//            args.add(profile.getId());
//            String updateQuery = query.toString().formatted(args.toArray());
//            log.debug("Final udate sql: {}", updateQuery);
//            int updateCount = statement.executeUpdate(updateQuery);
//            log.debug("Update count : {}", updateCount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProfile(Long id) {
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
            int deleteCount = statement.executeUpdate("delete from profiles where id = " + id);
            log.debug("Delete count : {}", deleteCount);
            return deleteCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileById(Long id) {
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
//            String sql = "select * from profiles where id = " + id;
            ResultSet resultSet = statement.executeQuery("select * from profiles where id = " + id);
            Profile profile = null;
            if (resultSet.next()) profile = getProfileFromDB(resultSet);
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
             Statement statement = connection.createStatement()) {
//            String sql = "select * from profiles where email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery("select * from profiles where email = '" + email + "'");
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

