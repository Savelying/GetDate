package ru.savelying.getdate.service;

import jakarta.servlet.ServletOutputStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.savelying.getdate.utils.UrlUtils.BASE_CONTENT_PATH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentService {
    @Getter
    private static ContentService instance = new ContentService();

    @SneakyThrows
    public void uploadPhoto(String contentPath, InputStream inputStream) {
        Path contentFullPath = Path.of(BASE_CONTENT_PATH, contentPath);
        Files.createDirectories(contentFullPath.getParent());

        //Блок записи файла целиком
        Files.write(contentFullPath, inputStream.readAllBytes());

        /*//Блок побайтовой записия файла
        OutputStream outputStream = Files.newOutputStream(contentFullPath, CREATE, TRUNCATE_EXISTING);
        writeContent(inputStream, outputStream);*/
    }

    @SneakyThrows
    public void downloadPhoto(String contentPath, ServletOutputStream outputStream) {
        String decodedPath = URLDecoder.decode(contentPath, StandardCharsets.UTF_8);
        Path contentFullPath = Path.of(BASE_CONTENT_PATH, decodedPath);

        //Блок выгрузи файла целиком
        outputStream.write(Files.readAllBytes(contentFullPath));

        /*//Блок побайтовой выгрузки файла
        InputStream inputStream;
        if (decodedPath.startsWith("/app/"))
            inputStream = ContentService.class.getClassLoader().getResourceAsStream("/WEB-INF" + contentPath.replaceFirst("/app", ""));
        else inputStream = Files.exists(contentFullPath) ? Files.newInputStream(contentFullPath) : null;
        if (inputStream == null) throw new FileNotFoundException();
        writeContent(inputStream, outputStream);*/
    }

    private void writeContent(InputStream inputStream, OutputStream outputStream) {
        try (inputStream; outputStream) {
            int currentByte;
            while ((currentByte = inputStream.read()) != -1) outputStream.write(currentByte);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
