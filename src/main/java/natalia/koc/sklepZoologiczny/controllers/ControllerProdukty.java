package natalia.koc.sklepZoologiczny.controllers;

import natalia.koc.sklepZoologiczny.domain.Dostawa;
import natalia.koc.sklepZoologiczny.domain.Kategoria;
import natalia.koc.sklepZoologiczny.domain.Zwierzeta;
import natalia.koc.sklepZoologiczny.domain.Produkt;
import natalia.koc.sklepZoologiczny.repositories.DostawaRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.KategoriaRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.ZwierzetaRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.ProduktRepozytorium;
import natalia.koc.sklepZoologiczny.services.ProduktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/produkty")
@SessionAttributes({"dostawa", "produkt"})
public class ControllerProdukty {
    @Autowired
    private DostawaRepozytorium dostawaRepozytorium;
    @Autowired
    private ProduktRepozytorium produktRepozytorium;
    @Autowired
    private ZwierzetaRepozytorium zwierzetaRepozytorium;
    @Autowired
    private KategoriaRepozytorium kategoriaRepozytorium;
    @Autowired
    private ProduktService produktService;

    @Secured("ROLE_ADMIN")
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("produkt", new Produkt());
        return"formularzProduktu";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        model.addAttribute("produkt", produktRepozytorium.findById(id).get());
        return"formularzProduktu";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/zapisz")
    public String zapisz(@ModelAttribute("produkt") @Valid Produkt produkt, Errors result,
                         MultipartFile multipartFile) throws IOException {
        if(result.hasErrors()) {
            return "formularzProduktu";
        }
        produktService.saveProdukt(produkt, multipartFile);
        return "redirect:/produkty/szegolyProduktu/"+produkt.getId();
    }


    @ModelAttribute("opcjeDostawy")
    public List<Dostawa> loadDostawa() {
        return dostawaRepozytorium.findAll();
    }

    @ModelAttribute("zwierzeta")
    public List<Zwierzeta> loadZwierzeta() {
        return zwierzetaRepozytorium.findAll();
    }

    @ModelAttribute("kategoria")
    public List<Kategoria> loadKategorie() {
        return kategoriaRepozytorium.findAll();
    }
}
