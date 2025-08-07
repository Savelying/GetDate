package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.utils.PasswordUtils;

import java.util.Optional;

import static ru.savelying.getdate.utils.StringUtils.*;

@NoArgsConstructor
public class LogValidator {
    @Getter
    private final static LogValidator instance = new LogValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    public ValidationResult validate(ProfileDTO profileDTO) {
        Optional<Profile> profile = profileDAO.getProfileByEmail(profileDTO.getEmail());
        ValidationResult result = new ValidationResult();
        if (isBlank(profileDTO.getEmail()) || !VALID_EMAIL_ADDRESS_REGEX.matcher(profileDTO.getEmail()).matches())
            result.addError("error.email.invalid");
        else if (profile.isEmpty())
            result.addError("error.email.not.found");
        else if (isBlank(profileDTO.getPassword()) || !PasswordUtils.checkPassword(profileDTO.getPassword(), profile.get().getPassword()))
            result.addError("error.password.invalid");
        return result;
    }
}
