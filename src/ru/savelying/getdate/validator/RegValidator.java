package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;

import java.util.regex.Pattern;

import static ru.savelying.getdate.utils.DateTimeUtils.getAge;
import static ru.savelying.getdate.utils.StringUtils.isBlank;

@NoArgsConstructor
public class RegValidator {
    @Getter
    private final static RegValidator instance = new RegValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    private final static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ValidationResult validate(ProfileDTO profile) {
        ValidationResult result = new ValidationResult();

        if (isBlank(profile.getEmail()) || !VALID_EMAIL_ADDRESS_REGEX.matcher(profile.getEmail()).matches()) {
            result.addError("error.email.invalid");
        }

        if (!isBlank(profile.getEmail()) && profileDAO.getAllEmails().contains(profile.getEmail())) {
            result.addError("error.email.duplicate");
        }

        if (isBlank(profile.getPassword())) {
            result.addError("error.password.invalid");
        }

        if (profile.getBirthDate() != null) {
            if (getAge(profile.getBirthDate()) < 18 || getAge(profile.getBirthDate()) > 100)
                result.addError("error.age.invalid");
        }

        return result;
    }
}
