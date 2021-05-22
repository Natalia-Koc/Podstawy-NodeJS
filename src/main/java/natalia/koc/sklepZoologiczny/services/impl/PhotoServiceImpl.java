package natalia.koc.sklepZoologiczny.services.impl;

import natalia.koc.sklepZoologiczny.services.PhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
@PropertySource("config.properties")
public class PhotoServiceImpl implements PhotoService {

    @Value("${files.location}")
    private String photoDir;

    @Override
    public void saveFile(MultipartFile multipartFile) throws IOException {
        new File(photoDir).mkdirs();
        var path = Path.of(photoDir, multipartFile.getOriginalFilename());
        var fos = new FileOutputStream(path.toFile());
        fos.write(multipartFile.getBytes());
        fos.close();
    }
}
