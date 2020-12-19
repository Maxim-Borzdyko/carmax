package by.bntu.borzdyko.carmax.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    private static String fileName;
    private static String deleteFileName;

    @BeforeClass
    public static void init(){
        fileName = "image.jpg";
        deleteFileName = "del.jpg";
        File file = new File("/Users/Mi/CarmaxPictures/Saved" + "/" + fileName);
        File fileDelete = new File("/Users/Mi/CarmaxPictures/Saved" + "/" + deleteFileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveImage_fileNameAndMultipartFile_existFile() throws IOException {
        MockMultipartFile multipartFile
                = new MockMultipartFile(
                "file", "image.jpg",
                MediaType.IMAGE_PNG_VALUE, "".getBytes()
        );
        fileService.saveImage(multipartFile);
        assertTrue(Files.exists(Paths.get("/Users/Mi/CarmaxPictures/Saved" + "/" + fileName)));
        Files.delete(Paths.get("/Users/Mi/CarmaxPictures/Saved" + "/" + fileName));
    }

    @Test
    public void saveImage_nullFile_null() throws IOException {
        MockMultipartFile multipartFile = null;
        String fileNaming  = fileService.saveImage(multipartFile);
        assertNull(fileNaming);
    }

    @Test
    public void deleteImage_existingFileName_imageDeleted() throws IOException {
        fileService.deleteImage(deleteFileName);
        assertFalse(Files.exists(Paths.get("/Users/Mi/CarmaxPictures/Saved" + "/" + deleteFileName)));
    }

    @Test
    public void deleteImage_notExistingFileName_stackTrace() throws IOException {
        fileService.deleteImage("InvalidFileName");
    }
}