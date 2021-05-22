package natalia.koc.sklepZoologiczny.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface PhotoService {

    void saveFile(MultipartFile multipartFile) throws IOException;
}
