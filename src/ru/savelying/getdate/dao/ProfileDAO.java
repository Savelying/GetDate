package ru.savelying.getdate.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dao.query.ProfileQueryBuilder;
import ru.savelying.getdate.dao.query.Query;
import ru.savelying.getdate.dto.ProfileFilter;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;
import ru.savelying.getdate.utils.ConnectUtils;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static ru.savelying.getdate.utils.ConnectUtils.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileDAO {
    @Getter
    private final static ProfileDAO instance = new ProfileDAO();

    private List<String> sortableColumns;

    //language=POSTGRES-PSQL
    private final static String INSERT = "insert into profiles(email, password, name, birth_date, status, role) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String DELETE = "delete from profiles where id = ?";
    private final static String UPDATE = "update profiles set id = id";
    private final static String SELECT = "select id, email, password, name, info, gender, birth_date, status, role, photo from profiles where '' = ''";

    //Генерируем пул профилей (юзеров)
    public void genSomeProfiles(int n) {
        int i = 0;
        while (i < n) {
            Profile profile = new Profile();
            profile.setName("User " + ++i);
            profile.setEmail("user-" + i + "@email.com");
            profile.setPassword("" + i);
            profile.setInfo("I'm a user №" + i);
            switch ((int) (Math.random() * 2) + 1) {
                case 1 -> profile.setGender(Gender.MALE);
                case 2 -> profile.setGender(Gender.FEMALE);
                case 3 -> profile.setGender(Gender.OTHER);
            }
            profile.setBirthDate(Date.valueOf((1950 + (int) (Math.random() * 55)) + "-" + ((int) (Math.random() * 11) + 1) + "-" + ((int) (Math.random() * 27) + 1)).toLocalDate());
            profile.setStatus((int) (Math.random() * 2) % 5 == 0 ? Status.ACTIVE : Status.INACTIVE);
            profile.setRole(Role.USER);
            createProfile(profile);
            updateProfile(profile);
            log.info("Create profile id: {} with name: {}", profile.getId(), profile.getName());

        }
    }

    public Profile createProfile(Profile profile) {
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
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
            log.debug("Insert profile id: {} with name: {}", profile.getId(), profile.getName());
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProfile(Long id) {
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            int deleteCount = statement.executeUpdate();
            log.debug("Delete count : {}", deleteCount);
            return deleteCount > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProfile(Profile profile) {
        Query query = new ProfileQueryBuilder(UPDATE)
                .addEmail(profile.getEmail())
                .addPassword(profile.getPassword())
                .addName(profile.getName())
                .addInfo(profile.getInfo())
                .addGender(profile.getGender())
                .addBirthDate(profile.getBirthDate())
                .addStatus(profile.getStatus())
                .addPhoto(profile.getPhotoFileName())
                .build(profile.getId());
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = ConnectUtils.getPreparedStatement(connection, query)) {
            int updateCount = statement.executeUpdate();
            log.debug("Update count : {}", updateCount);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileById(Long id) {
        Query query = new ProfileQueryBuilder(SELECT)
                .addId(id)
                .build();
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = ConnectUtils.getPreparedStatement(connection, query)) {
            ResultSet rs = statement.executeQuery();
            Profile profile = null;
            if (rs.next()) profile = getProfileFromDB(rs);
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileByEmail(String email) {
        Query query = new ProfileQueryBuilder(SELECT)
                .addEmail(email)
                .build();
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = ConnectUtils.getPreparedStatement(connection, query)) {
            ResultSet resultSet = statement.executeQuery();
            Profile profile = null;
            if (resultSet.next()) profile = getProfileFromDB(resultSet);
            return Optional.ofNullable(profile);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsProfileByEmail(String email) {
        Query query = new ProfileQueryBuilder(SELECT)
                .addEmail(email)
                .build();
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = ConnectUtils.getPreparedStatement(connection, query)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Profile> getAllProfiles(ProfileFilter filter) {
        Query query = new ProfileQueryBuilder(SELECT)
                .addNameStartWith(filter.getNameStartWith())
                .addEmailStartWith(filter.getEmailStartWith())
                .addStatus(filter.getStatus())
                .addRole(filter.getRole())
                .addLowAge(filter.getLowAge())
                .addHighAge(filter.getHighAge())
                .addSortBy(getSortColumn(filter.getSortBy()))
                .addPageAndPageSize(filter.getPageNo(), filter.getPageSize())
                .build();
        try (Connection connection = ConnectUtils.getConnnect();
             PreparedStatement statement = ConnectUtils.getPreparedStatement(connection, query)) {
            statement.setQueryTimeout(dbQueryTimeout);
            statement.setFetchSize(dbFetchSize);
            statement.setMaxRows(dbMaxRows);
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

    private String getSortColumn(String sortColumn) {
        if (sortableColumns == null) {
            try (Connection connection = getConnnect()) {
                ResultSet columns = connection.getMetaData().getColumns(null, null, "profiles", null);
                sortableColumns = new ArrayList<>();
                while (columns.next())
                    if (columns.getString("REMARKS") != null && columns.getString("REMARKS").equals("sortable"))
                        sortableColumns.add(columns.getString("COLUMN_NAME"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (sortColumn != null && sortableColumns.contains(sortColumn)) return sortColumn;
        return "id";
    }

    private Profile getProfileFromDB(ResultSet resultSet) throws SQLException {
        Profile profile = new Profile();
        profile.setId(resultSet.getLong("id"));
        if (resultSet.getString("email") != null) profile.setEmail(resultSet.getString("email"));
        if (resultSet.getString("password") != null) profile.setPassword(resultSet.getString("password"));
        if (resultSet.getString("name") != null) profile.setName(resultSet.getString("name"));
        if (resultSet.getString("info") != null) profile.setInfo(resultSet.getString("info"));
        if (resultSet.getString("gender") != null) profile.setGender(Gender.valueOf(resultSet.getString("gender")));
        if (resultSet.getString("birth_date") != null)
            profile.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
        if (resultSet.getString("status") != null) profile.setStatus(Status.valueOf(resultSet.getString("status")));
        if (resultSet.getString("role") != null) profile.setRole(Role.valueOf(resultSet.getString("role")));
        if (resultSet.getString("photo") != null) profile.setPhotoFileName(resultSet.getString("photo"));
        return profile;
    }
}

