package natalia.koc.sklepZoologiczny.services;

import natalia.koc.sklepZoologiczny.domain.Produkt;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ProduktService {
    void saveProdukt(Produkt produkt, MultipartFile multipartFile) throws IOException;

}
