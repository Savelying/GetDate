package ru.savelying.getdate.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.savelying.getdate.dao.ProfileDAO;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Profile;

import java.util.Optional;
import java.util.regex.Pattern;

import static ru.savelying.getdate.utils.StringUtils.isBlank;

@NoArgsConstructor
public class LogValidator {
    @Getter
    private final static LogValidator instance = new LogValidator();
    private final ProfileDAO profileDAO = ProfileDAO.getInstance();

    private final static Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
            .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public ValidationResult validate(ProfileDTO profileDTO) {
        Optional<Profile> profile = profileDAO.getProfileByEmail(profileDTO.getEmail());
        ValidationResult result = new ValidationResult();
        if (isBlank(profileDTO.getEmail()) || !VALID_EMAIL_ADDRESS_REGEX.matcher(profileDTO.getEmail()).matches())
            result.addError("error.email.invalid");
        else if (profile.isEmpty())
            result.addError("error.email.not.found");
        else if (isBlank(profileDTO.getPassword()) || !profileDTO.getPassword().equals(profile.get().getPassword()))
            result.addError("error.password.invalid");
        return result;
    }
}
