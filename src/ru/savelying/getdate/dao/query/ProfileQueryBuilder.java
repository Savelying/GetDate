package ru.savelying.getdate.dao.query;

import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileQueryBuilder {
    private final StringBuilder queryBuilder;
    private final List<Object> args;

    public ProfileQueryBuilder(String query) {
        this.queryBuilder = new StringBuilder(query);
        this.args = new ArrayList<>();
    }

    public ProfileQueryBuilder addId(Long id) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (id == null) return this;
            queryBuilder.append(" and id = ?");
            args.add(id);
        }
        return this;
    }

    public ProfileQueryBuilder addEmail(String email) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (email == null) return this;
            queryBuilder.append(" and email = ?");
            args.add(email);
        }
        if (queryBuilder.toString().startsWith("update ")) {
            if (email == null) return this;
            queryBuilder.append(", email = ?");
            args.add(email);
        }
        return this;
    }

    public ProfileQueryBuilder addEmailStartWith(String emailStartWith) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (emailStartWith == null) return this;
            queryBuilder.append(" and email like ?");
            args.add(emailStartWith + "%");
        }
        return this;
    }

    public ProfileQueryBuilder addPassword(String password) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (password == null) return this;
            queryBuilder.append(", password = ?");
            args.add(password);
        }
        return this;
    }

    public ProfileQueryBuilder addName(String name) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (name == null) return this;
            queryBuilder.append(", name = ?");
            args.add(name);
        }
        return this;
    }

    public ProfileQueryBuilder addNameStartWith(String nameStartWith) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (nameStartWith == null) return this;
            queryBuilder.append(" and name like ?");
            args.add(nameStartWith + "%");
        }
        return this;
    }

    public ProfileQueryBuilder addInfo(String info) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (info == null) return this;
            queryBuilder.append(", info = ?");
            args.add(info);
        }
        return this;
    }

    public ProfileQueryBuilder addGender(Gender gender) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (gender == null) return this;
            queryBuilder.append(", gender = ?");
            args.add(gender.toString());
        }
        return this;
    }

    public ProfileQueryBuilder addBirthDate(LocalDate birthDate) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (birthDate == null) return this;
            queryBuilder.append(", birth_date = ?");
            args.add(Date.valueOf(birthDate));
        }
        return this;
    }

    public ProfileQueryBuilder addLowAge(Integer lowAge) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (lowAge == null) return this;
            queryBuilder.append(" and birth_date <= ?");
            args.add(Date.valueOf(LocalDate.now().minusYears(lowAge)));
        }
        return this;
    }

    public ProfileQueryBuilder addHighAge(Integer highAge) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (highAge == null) return this;
            queryBuilder.append(" and birth_date >= ?");
            args.add(Date.valueOf(LocalDate.now().minusYears(highAge)));
        }
        return this;
    }

    public ProfileQueryBuilder addStatus(Status status) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (status == null) return this;
            queryBuilder.append(" and status = ?");
            args.add(status.toString());
        }
        if (queryBuilder.toString().startsWith("update ")) {
            if (status == null) return this;
            queryBuilder.append(", status = ?");
            args.add(status.toString());
        }
        return this;
    }

    public ProfileQueryBuilder addRole(Role role) {
        if (queryBuilder.toString().startsWith("select ")) {
            if (role == null) return this;
            queryBuilder.append(" and role = ?");
            args.add(role.toString());
        }
        if (queryBuilder.toString().startsWith("update ")) {
            if (role == null) return this;
            queryBuilder.append(", role = ?");
            args.add(role.toString());
        }
        return this;
    }

    public ProfileQueryBuilder addPhoto(String photo) {
        if (queryBuilder.toString().startsWith("update ")) {
            if (photo == null) return this;
            queryBuilder.append(", photo = ?");
            args.add(photo);
        }
        return this;
    }

    public Query build() {
        return new Query(queryBuilder.toString(), args);
    }

    public Query build(Long id) {
        queryBuilder.append(" where id = ?");
        args.add(id);
        return new Query(queryBuilder.toString(), args);
    }
}
