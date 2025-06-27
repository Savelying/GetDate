package ru.savelying.getdate.mapper;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Profile;
import ru.savelying.getdate.model.Status;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;

import static ru.savelying.getdate.utils.DateTimeUtils.getAge;
import static ru.savelying.getdate.utils.StringUtils.isBlank;
import static ru.savelying.getdate.utils.UrlUtils.BASE_CONTENT_PATH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper implements Mapper<Profile, ProfileDTO> {
    @Getter
    private final static ProfileMapper instance = new ProfileMapper();

    @Override
    public ProfileDTO mapToDTO(Profile obj) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(obj.getId());
        profileDTO.setName(obj.getName());
        profileDTO.setEmail(obj.getEmail());
        profileDTO.setInfo(obj.getInfo());
        profileDTO.setGender(obj.getGender());
//        profileDTO.setBirthDate(obj.getBirthDate());
        if (obj.getBirthDate() != null) profileDTO.setAge(getAge(obj.getBirthDate()));
        profileDTO.setStatus(obj.getStatus());
        profileDTO.setPhotoFileName(obj.getPhotoFileName());
        profileDTO.setRole(obj.getRole());
        return profileDTO;
    }

    @Override
    public Profile mapFromDTO(ProfileDTO obj) {
        Profile profile = new Profile();
        if (obj.getId() != null) profile.setId(obj.getId());
        if (obj.getName() != null) profile.setName(obj.getName());
        if (obj.getEmail() != null) profile.setEmail(obj.getEmail());
        if (obj.getPassword() != null) profile.setPassword(obj.getPassword());
        if (obj.getInfo() != null) profile.setInfo(obj.getInfo());
        if (obj.getGender() != null) profile.setGender(obj.getGender());
        if (obj.getBirthDate() != null) profile.setBirthDate(obj.getBirthDate());
        if (obj.getStatus() != null) profile.setStatus(obj.getStatus());
        if (obj.getPhotoFileName() != null) profile.setPhotoFileName(obj.getPhotoFileName());
        return profile;
    }

    @SneakyThrows
    public ProfileDTO getProfileDTO(HttpServletRequest req) {
        ProfileDTO profileDTO = new ProfileDTO();
        if (!isBlank(req.getParameter("id"))) profileDTO.setId(Long.parseLong(req.getParameter("id")));
        if (!isBlank(req.getParameter("email"))) profileDTO.setEmail(req.getParameter("email"));
        if (!isBlank(req.getParameter("password"))) profileDTO.setPassword(req.getParameter("password"));
        if (!isBlank(req.getParameter("name"))) profileDTO.setName(req.getParameter("name"));
        if (!isBlank(req.getParameter("info"))) profileDTO.setInfo(req.getParameter("info"));
        if (!isBlank(req.getParameter("gender"))) profileDTO.setGender(Gender.valueOf(req.getParameter("gender")));
        if (!isBlank(req.getParameter("status"))) profileDTO.setStatus(Status.valueOf(req.getParameter("status")));
        if (req.getParameter("birthDate") != null && !isBlank(req.getParameter("birthDate")))
            profileDTO.setBirthDate(LocalDate.parse(req.getParameter("birthDate")));
        if (req.getPart("photo") != null && !isBlank(req.getPart("photo").getSubmittedFileName())) {
            profileDTO.setPhotoImage(req.getPart("photo"));
            profileDTO.setPhotoFileName(req.getPart("photo").getSubmittedFileName());
        }
        if (!isBlank(req.getParameter("newPassword")) && req.getParameter("newPassword").equals(req.getParameter("confirmPassword")))
            profileDTO.setPassword(req.getParameter("newPassword"));
        return profileDTO;
    }

    @SneakyThrows
    public Document getProfilePdf(Document pdf, ProfileDTO profileDTO) {
        pdf.open();
        PdfPTable table = new PdfPTable(2);
        table.addCell("Photo");
        if (profileDTO.getPhotoFileName() != null) {
            Image img = Image.getInstance(Path.of(BASE_CONTENT_PATH, URLDecoder.decode(profileDTO.getPhotoFileName(), StandardCharsets.UTF_8)).toAbsolutePath().toString());
            img.scaleToFit(200, 200);
            table.addCell(new PdfPCell(img));
        } else table.addCell("");
        table.addCell("Email");
        table.addCell(profileDTO.getEmail());
        table.addCell("Name");
        table.addCell(profileDTO.getName());
        table.addCell("Age");
        table.addCell(profileDTO.getAge().toString());
        table.addCell("Gender");
        table.addCell(profileDTO.getGender().toString());
        table.addCell("Info");
        table.addCell(profileDTO.getInfo());
        pdf.add(table);
        pdf.close();
        return pdf;
    }
}
