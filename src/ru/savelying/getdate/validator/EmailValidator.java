package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.utils.PasswordUtils;

import static ru.savelying.getdate.utils.StringUtils.*;

@NoArgsConstructor
public class EmailValidator {
    @Getter
    private final static EmailValidator instance = new EmailValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    public ValidationResult validate(ProfileDTO profileDTO) {
        ValidationResult result = new ValidationResult();

        if (!isBlank(profileDTO.getNewEmail()) && !VALID_EMAIL_ADDRESS_REGEX.matcher(profileDTO.getNewEmail()).matches())
            result.addError("error.email.invalid");

        if (!isBlank(profileDTO.getNewEmail()) && profileDAO.existsProfileByEmail(profileDTO.getNewEmail()))
            result.addError("error.email.duplicate");

        if (isBlank(profileDTO.getPassword()) || !PasswordUtils.checkPassword(profileDTO.getPassword(), profileDAO.getProfileByEmail(profileDTO.getEmail()).get().getPassword()))
            result.addError("error.password.invalid");

        if (!isBlank(profileDTO.getNewPassword())) {
            if (isBlank(profileDTO.getConfirmPassword()) || !profileDTO.getNewPassword().equals(profileDTO.getConfirmPassword()))
                result.addError("error.password.new.mismatch");
        } else if (isBlank(profileDTO.getConfirmPassword()) || !profileDTO.getPassword().equals(profileDTO.getConfirmPassword()))
            result.addError("error.password.mismatch");

        return result;
    }
}
