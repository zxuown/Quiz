package org.example.quiz.domain.entity;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FIleUpload {

    public static String saveImg(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            Path uploadPath = Paths.get("F:\\JavaProjects\\Jsp\\Quiz\\data\\images");
            try(InputStream inputStream = multipartFile.getInputStream()){
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            }catch (IOException e){
                throw new IOException("Error with file");
            }
        }
        return null;
    }
}
