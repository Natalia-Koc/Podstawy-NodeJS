package natalia.koc.sklepZoologiczny.controllers;

import natalia.koc.sklepZoologiczny.domain.*;
import natalia.koc.sklepZoologiczny.repositories.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/koszyk")
public class koszykController {
    private ProduktRepozytorium produktRepozytorium;
    private KoszykRepozytorium koszykRepozytorium;
    private UserRepository userRepository;
    private DostawaRepozytorium dostawaRepozytorium;
    private HistoriaRepozytorium historiaRepozytorium;
    Random rand = new Random();

    public koszykController(ProduktRepozytorium produktRepozytorium,
                            KoszykRepozytorium koszykRepozytorium,
                            UserRepository userRepository,
                            DostawaRepozytorium dostawaRepozytorium,
                            HistoriaRepozytorium historiaRepozytorium) {
        this.produktRepozytorium = produktRepozytorium;
        this.koszykRepozytorium = koszykRepozytorium;
        this.userRepository = userRepository;
        this.dostawaRepozytorium = dostawaRepozytorium;
        this.historiaRepozytorium = historiaRepozytorium;
    }

    @GetMapping("/dodajDoKoszyka/{id}/{page}/{size}")
    public String dodajDoKoszyka(@PathVariable Integer id, @PathVariable String page, @PathVariable String size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Produkt produkt = produktRepozytorium.findById(id).get();
        User user = userRepository.findByUsername(name);
        Koszyk koszyk = koszykRepozytorium.findByUserAndNazwa(user, produkt.getNazwa());
        if(koszyk == null) {
            koszyk = new Koszyk(1, produkt.getNazwa(), produkt.getCena(), user);
        } else {
            koszyk.setIlosc(koszyk.getIlosc() + 1);
            koszyk.setCena(koszyk.getCena() + produkt.getCena());
            koszyk.setUser(user);
        }
        koszykRepozytorium.save(koszyk);
        return "redirect:/produkty/showList?page="+page+"&size="+size;
    }

    @GetMapping("/koszyk")
    public String koszyk(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name);
        model.addAttribute("koszyk", koszykRepozytorium.findAllByUser(user));
        Float koszt = 0.0f;
        for (Koszyk koszyk : koszykRepozytorium.findAllByUser(user)) {
            koszt = koszt + koszyk.getCena();
        }
        model.addAttribute("kwota", koszt);
        model.addAttribute("dostawa", dostawaRepozytorium.findAll());
        return "zamowienia/koszyk";
    }

    @GetMapping("/usunKoszyk/{id}")
    public String usunKoszyk(@PathVariable Integer id, Model model) {
        Koszyk koszyk = koszykRepozytorium.findById(id).get();
        var ilosc = koszyk.getIlosc();
        if(ilosc > 1) {
            koszyk.setCena(koszyk.getCena() - (koszyk.getCena()/koszyk.getIlosc()));
            koszyk.setIlosc(koszyk.getIlosc() - 1);
            koszykRepozytorium.save(koszyk);
        } else {
            koszykRepozytorium.deleteById(id);
        }
        return "redirect:/koszyk/koszyk";
    }

    @PostMapping("/zamowienie")
    public String zamowienie(@Valid @ModelAttribute("dostawa") Dostawa dostawa, Errors errors, Model model) {
        if(errors.hasErrors()) {
            return "redirect:/koszyk/koszyk";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name);
        Float koszt = 0.0f;
        for (Koszyk koszyk : koszykRepozytorium.findAllByUser(user)) {
            koszt = koszt + koszyk.getCena();
        }koszt = koszt+11.0f;
        Integer nrZam = rand.nextInt(89999)+10000;
        Historia historia = new Historia(nrZam, "Nieoplacone", LocalDate.now(), koszt, null, user, false, false);
        historiaRepozytorium.save(historia);
        List<Koszyk> koszyk1 = koszykRepozytorium.findAllByUser(user);
        for (Koszyk koszyk : koszykRepozytorium.findAllByUser(user)) {
            koszykRepozytorium.deleteById(koszyk.getId());
        }
        model.addAttribute("kwota", koszt);
        model.addAttribute("nrZam", nrZam);
        model.addAttribute("koszyk",koszyk1);
        return "zamowienia/zamowienie";
    }
}
