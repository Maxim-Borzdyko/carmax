package by.bntu.borzdyko.carmax.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${app.upload.dir:${user.home}}" + "\\CarmaxPictures\\Saved")
    private String UPLOAD_PATH;

    public String saveImage(MultipartFile file) {
        String fileName = null;

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            createUploadFolder();
            fileName = createFileName(file);
            upload(file, fileName);
        }
        return fileName;
    }

    public void deleteImage(String fileName){
        try {
            String stringPath = createPaths(fileName);
            Path filePath = Paths.get(stringPath);
            Files.delete(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createUploadFolder() {
        File uploadFolder = new File(UPLOAD_PATH);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }
    }

    private String createFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "." + file.getOriginalFilename();
    }

    private void upload(MultipartFile file, String fileName) {
        try {
            String filePath = createPaths(fileName);
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createPaths(String fileName) {
        return UPLOAD_PATH + "/" + fileName;
    }
}
