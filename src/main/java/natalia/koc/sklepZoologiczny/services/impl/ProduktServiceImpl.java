package natalia.koc.sklepZoologiczny.services.impl;

import natalia.koc.sklepZoologiczny.domain.PhotoDesc;
import natalia.koc.sklepZoologiczny.domain.Produkt;
import natalia.koc.sklepZoologiczny.repositories.ProduktRepozytorium;
import natalia.koc.sklepZoologiczny.services.PhotoService;
import natalia.koc.sklepZoologiczny.services.ProduktService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProduktServiceImpl implements ProduktService {

    ProduktRepozytorium produktRepozytorium;
    PhotoService photoService;

    public ProduktServiceImpl(ProduktRepozytorium produktRepozytorium, PhotoService photoService) {
        this.produktRepozytorium = produktRepozytorium;
        this.photoService = photoService;
    }

    @Override
    public void saveProdukt(Produkt produkt, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() == false) {
            var photo = new PhotoDesc();
            photo.setFileName(multipartFile.getOriginalFilename());
            photo.setFileContent(multipartFile.getBytes());
            produkt.setPhoto(photo);
            photoService.saveFile(multipartFile);
        }
        produktRepozytorium.save(produkt);
    }
}
