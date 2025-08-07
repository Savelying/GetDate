package ru.savelying.getdate.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.savelying.getdate.dto.ProfileView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;

public class JsonMapper {
    private final ObjectMapper objectMapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .configure(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .build();

    @Getter
    private static final JsonMapper instance = new JsonMapper();

    public void writeValue(Writer writer, Object value) throws IOException {
        objectMapper.writeValue(writer, value);
    }

    public <T> T readValue(InputStream in, Class<T> valueType) throws IOException {
        return objectMapper.readValue(in, valueType);
    }

    public <T> T readValue(Reader src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }

    public <T> T readValue(String src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }

    @SneakyThrows
    public String writeValueAsString(ProfileView profileView) {
        return objectMapper.writeValueAsString(profileView);
    }
}
