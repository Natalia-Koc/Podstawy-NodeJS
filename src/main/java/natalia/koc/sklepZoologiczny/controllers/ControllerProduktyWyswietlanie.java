package natalia.koc.sklepZoologiczny.controllers;

import natalia.koc.sklepZoologiczny.controllers.filters.Filter;
import natalia.koc.sklepZoologiczny.domain.Kategoria;
import natalia.koc.sklepZoologiczny.repositories.KategoriaRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.ProduktRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.kryteria.produktSpecyfikacje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.OperationNotSupportedException;
import java.util.List;


@Controller
@RequestMapping("/produkty")
@SessionAttributes({"dostawa", "produkt", "filter"})
public class ControllerProduktyWyswietlanie {
    private ProduktRepozytorium produktRepozytorium;
    private KategoriaRepozytorium kategoriaRepozytorium;

    public ControllerProduktyWyswietlanie(ProduktRepozytorium produktRepozytorium,
                                          KategoriaRepozytorium kategoriaRepozytorium) {
        this.produktRepozytorium = produktRepozytorium;
        this.kategoriaRepozytorium = kategoriaRepozytorium;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/usun/{id}/{page}/{size}")
    public String usun(@PathVariable Integer id, @PathVariable String page, @PathVariable String size) {
        produktRepozytorium.deleteById(id);
        return "redirect:/produkty/showList?page="+page+"&size="+size;
    }

    @Secured("ROLE_USER")
    @GetMapping("/szegolyProduktu/{id}")
    public String szegolyProduktu(Model model, @PathVariable Integer id) {
        model.addAttribute("produkt", produktRepozytorium.findById(id).get());
        return "szegolyProduktu";
    }

    @RequestMapping(value = "/showList", method = {RequestMethod.GET, RequestMethod.POST})
    public String showList(Model model,
                           @ModelAttribute("filter") Filter filter,
                           @PageableDefault(size = 5, sort = "nazwa") Pageable pageable) throws OperationNotSupportedException {
        Page page = null;
        switch (filter.getQueryMode()) {
            case NAMED_METHOD:
                page = produktRepozytorium.findProduktsByNazwaIgnoreCaseContainingOrAndOpisIgnoreCaseContaining(
                        filter.getPhrase(), filter.getPhrase(),  pageable);
                break;
            case NAMED_QUERY:
                page = produktRepozytorium.findProduktUsingNameQuery(
                        filter.getPhraseLIKE(),
                        filter.getMinCena(), filter.getMaxCena(),
                        filter.getMinOcena(), filter.getMaxOcena(),
                        filter.isKategoriaEmpty()?null: filter.getKategoria(),
                        pageable
                );
                break;
            case QUERY:
                page = produktRepozytorium.findProduktUsingQuery(
                        filter.getPhraseLIKE(),
                        filter.getMinCena(), filter.getMaxCena(),
                        filter.getMinOcena(), filter.getMaxOcena(),
                        filter.isKategoriaEmpty()?null: filter.getKategoria(),
                        pageable
                );
                break;
            case ENTITY_SpEL_AND_GRAPH:
                page = produktRepozytorium.findProduktUsingSpEL(filter, pageable);
                break;
            case CRITERIA:
                page = produktRepozytorium.findAll(
                        Specification.where(
                        produktSpecyfikacje.findByPhrase(filter.getPhraseLIKE()).
                                and(produktSpecyfikacje.findByPriceRange(filter.getMinCena(), filter.getMaxCena())).
                                and(produktSpecyfikacje.findByOcenaRange(filter.getMinOcena(), filter.getMaxOcena()))
                ), pageable);
                break;
            default: throw new OperationNotSupportedException(
                    String.format("Typ zapytania '%s' nie jest obsługiwany", filter.getQueryMode() ));

        }
        model.addAttribute("page", page);
        return "showList";
    }

    @RequestMapping(value = "/showList", method = {RequestMethod.GET, RequestMethod.POST}, params = {"clear"})
    public RedirectView clear(
            @ModelAttribute("filter") Filter filter,
            @PageableDefault(size = 5, sort = "nazwa") Pageable pageable) {
        filter.clear();
        return new RedirectView("showList"+parametry(pageable));
    }

    public String parametry(Pageable pageable) {
        return String.format("?page=%d&size=%d&sort=%s", pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().replace(": ", ","));
    }

    @ModelAttribute("filter")
    public Filter pustyFilter() {
        return new Filter();
    }

    @Secured("ROLE_USER")
    @InitBinder("produkt")
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
        dataBinder.addCustomFormatter(new CurrencyStyleFormatter(), "cena");
    }

    @ModelAttribute("kategorie")
    public List<Kategoria> loadKategorie() {
        return kategoriaRepozytorium.findAll();
    }

    @ModelAttribute("typyZapytan")
    public Filter.QUERY_MODE[] loadQueryModes() {
        return Filter.QUERY_MODE.values();
    }
}
