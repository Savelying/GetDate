package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;

import static ru.savelying.getdate.utils.DateTimeUtils.getAge;
import static ru.savelying.getdate.utils.StringUtils.*;

@NoArgsConstructor
public class RegValidator {
    @Getter
    private final static RegValidator instance = new RegValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    public ValidationResult validate(ProfileDTO profileDTO) {
        ValidationResult result = new ValidationResult();

        if (isBlank(profileDTO.getEmail()) || !VALID_EMAIL_ADDRESS_REGEX.matcher(profileDTO.getEmail()).matches())
            result.addError("error.email.invalid");

        if (!isBlank(profileDTO.getEmail()) && profileDAO.existsProfileByEmail(profileDTO.getEmail()))
            result.addError("error.email.duplicate");

        if (isBlank(profileDTO.getPassword())) result.addError("error.password.invalid");
        else if (isBlank(profileDTO.getConfirmPassword()) || !profileDTO.getPassword().equals(profileDTO.getConfirmPassword()))
            result.addError("error.password.mismatch");

        if (profileDTO.getBirthDate() == null) result.addError("error.age.empty");
        else if (getAge(profileDTO.getBirthDate()) < 18 || getAge(profileDTO.getBirthDate()) > 100)
            result.addError("error.age.invalid");

        return result;
    }
}
