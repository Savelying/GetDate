package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;

import java.util.regex.Pattern;

import static ru.savelying.getdate.utils.DateTimeUtils.getAge;
import static ru.savelying.getdate.utils.StringUtils.isBlank;

@NoArgsConstructor
public class EmailValidator {
    @Getter
    private final static EmailValidator instance = new EmailValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    private final static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ValidationResult validate(ProfileDTO profileDTO) {
        ValidationResult result = new ValidationResult();

        if (!isBlank(profileDTO.getNewEmail()) && !VALID_EMAIL_ADDRESS_REGEX.matcher(profileDTO.getNewEmail()).matches()) {
            result.addError("error.email.invalid");
        }

        if (!isBlank(profileDTO.getNewEmail()) && profileDAO.getAllEmails().contains(profileDTO.getNewEmail())) {
            result.addError("error.email.duplicate");
        }

        if (isBlank(profileDTO.getPassword()) || !profileDTO.getPassword().equals(profileDAO.getProfileByEmail(profileDTO.getEmail()).get().getPassword()))
            result.addError("error.password.invalid");

        if (!isBlank(profileDTO.getNewPassword())) {
            if (isBlank(profileDTO.getConfirmPassword()) || !profileDTO.getNewPassword().equals(profileDTO.getConfirmPassword())) {
                result.addError("error.password.new.mismatch");
            }
        } else if (isBlank(profileDTO.getConfirmPassword()) || !profileDTO.getPassword().equals(profileDTO.getConfirmPassword())) {
            result.addError("error.password.mismatch");
        }

        return result;
    }
}
