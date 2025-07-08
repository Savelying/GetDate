package ru.savelying.getdate.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;
import ru.savelying.getdate.utils.ConnectUtils;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static ru.savelying.getdate.utils.ConnectUtils.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileDAO {
    @Getter
    private final static ProfileDAO instance = new ProfileDAO();

    @SneakyThrows
    public static ProfileDAO getInstance() {
        return instance;
    }


    public Profile createProfile(Profile profile) {
        String sql = "insert into profiles(email, password, name, birth_date, status, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
        try (Connection connection = ConnectUtils.getConnnect();
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
        try (Connection connection = ConnectUtils.getConnnect();
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
        try (Connection connection = ConnectUtils.getConnnect();
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
        try (Connection connection = ConnectUtils.getConnnect();
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

    public boolean existsProfileByEmail(String email) {
        String sql = "select * from profiles where email = ?";
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement  = connection.prepareStatement(sql)){
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Profile> getAllProfiles(ProfileFilter filter) {
        String sql = "select * from profiles where '' = ''";
        StringBuilder query = new StringBuilder(sql);
        List<Object> args = new ArrayList<>();
        if (filter.getNameStartWith() != null) {
            query.append(" and name like ?");
            args.add(filter.getNameStartWith() + "%");
        }
        if (filter.getEmailStartWith() != null) {
            query.append(" and email like ?");
            args.add(filter.getEmailStartWith() + "%");
        }
        if (filter.getStatus() != null) {
            query.append(" and status like ?");
            args.add(filter.getStatus().toString() + "%");
        }
        if (filter.getLowAge() != null) {
            query.append(" and birth_date <= ?");
            args.add(Date.valueOf(LocalDate.now().minusYears(filter.getLowAge())));
        }
        if (filter.getHighAge() != null) {
            query.append(" and birth_date >= ?");
            args.add(Date.valueOf(LocalDate.now().minusYears(filter.getHighAge())));
        }
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < args.size(); i++) statement.setObject(i + 1, args.get(i));
            statement.setQueryTimeout(dbQueryTimeout);
            statement.setFetchSize(dbFetchSize);
            statement.setMaxRows(dbMaxRows);
            log.debug("Final select sql: {}", statement);
            ResultSet resultSet = statement.executeQuery();
            List<Profile> profiles = new ArrayList<>();
            while (resultSet.next()) profiles.add(getProfileFromDB(resultSet));
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public Set<String> getAllEmails() {
//        String sql = "select * from profiles";
//        try (Connection connection = ConnectUtils.getConnnect();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//            ResultSet resultSet = statement.executeQuery();
//            Set<String> emails = new HashSet<>();
//            while (resultSet.next()) emails.add(getProfileFromDB(resultSet).getEmail());
//            return emails;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

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

