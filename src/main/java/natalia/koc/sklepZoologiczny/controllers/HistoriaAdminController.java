package natalia.koc.sklepZoologiczny.controllers;

import natalia.koc.sklepZoologiczny.domain.Historia;
import natalia.koc.sklepZoologiczny.repositories.HistoriaRepozytorium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class HistoriaAdminController {
    @Autowired
    private HistoriaRepozytorium historiaRepozytorium;

    @GetMapping("/zamowienia")
    public String zamowienia(Model model){
        model.addAttribute("historia", historiaRepozytorium.findAllByCzyAdminUsunal(false));
        return "admin/zamowienia";
    }

    @GetMapping("/zmienStan/{id}")
    public String zamowienie(@PathVariable Integer id) {
        String stan = historiaRepozytorium.findById(id).get().getStanZamowienia();
        if(stan.equals("Nieoplacone")) {
            historiaRepozytorium.findById(id).get().setStanZamowienia("Opłacone");
        } else if(stan.equals("Opłacone")) {
            historiaRepozytorium.findById(id).get().setStanZamowienia("Wysłane");
            historiaRepozytorium.findById(id).get().setPrzewidywanyCzasDostawy(LocalDate.now().plusDays(3));
        }
        Historia his = historiaRepozytorium.findById(id).get();
        historiaRepozytorium.save(his);
        return "redirect:/admin/zamowienia";
    }

    @GetMapping("/usunHistorie/{id}")
    public String usunHistorie(@PathVariable Integer id) {
        if(historiaRepozytorium.findById(id).get().getCzyUserUsunal().equals(true)) {
            historiaRepozytorium.deleteById(id);
        } else {
            historiaRepozytorium.findById(id).get().setCzyAdminUsunal(true);
            Historia his = historiaRepozytorium.findById(id).get();
            historiaRepozytorium.save(his);
        }
        return "redirect:/admin/zamowienia";
    }
}
