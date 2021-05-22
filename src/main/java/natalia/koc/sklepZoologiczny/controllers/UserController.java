package natalia.koc.sklepZoologiczny.controllers;

import natalia.koc.sklepZoologiczny.domain.Historia;
import natalia.koc.sklepZoologiczny.domain.Profil;
import natalia.koc.sklepZoologiczny.domain.User;
import natalia.koc.sklepZoologiczny.repositories.HistoriaRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.ProfilRepozytorium;
import natalia.koc.sklepZoologiczny.repositories.UserRepository;
import natalia.koc.sklepZoologiczny.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProfilRepozytorium profilRepozytorium;
    @Autowired
    private HistoriaRepozytorium historiaRepozytorium;
    private Profil profil = new Profil();


    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userCommand", new Profil());
        return "user/profilForm";
    }

    @GetMapping("/profil")
    public String profil(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name);
        model.addAttribute("profil", profilRepozytorium.findByUser(user));
        model.addAttribute("user", userRepository.findByUsername(name));
        model.addAttribute("historia", historiaRepozytorium.findAllByUserAndCzyUserUsunal(user, false));
        return "user/profil";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userCommand")
                                           User userForm, BindingResult bindingResult, Optional<Integer> id, Model model) {
        if(bindingResult.hasErrors()) {
            return "user/registrationForm";
        }
        if(id.isPresent()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();//principal
            userService.saveUser(userForm);
            model.addAttribute("profil", profilRepozytorium.findByUser(userRepository.findByUsername(name)));
            model.addAttribute("user", userRepository.findByUsername(name));
            return "user/profil";
        } else {
            userService.saveUser(userForm);
            profil.setUser(userForm);
            profilRepozytorium.save(profil);
        }
        return "user/registrationSuccess";
    }

    @PostMapping("/registrationProfil")
    public String registrationProfil(@Valid @ModelAttribute("userCommand")
                                             Profil profil, Model model, Errors error, Optional<Integer> id) {
        if(error.hasErrors()) {
            return "user/profilForm";
        }
        if(id.isPresent()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            profil.setUser(userRepository.findByUsername(name));
            profilRepozytorium.save(profil);
            model.addAttribute("profil", profilRepozytorium.findByUser(userRepository.findByUsername(name)));
            model.addAttribute("user", userRepository.findByUsername(name));
            return "user/profil";
        } else {
            this.profil = profil;
            model.addAttribute("userCommand", new User());
        }
        return "user/registrationForm";
    }

    @GetMapping("/edycjaProfilu")
    public String edycjaProfilu(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("userCommand", profilRepozytorium.findByUser(userRepository.findByUsername(name)));
        return "user/profilForm";
    }

    @GetMapping("/edycjaUsera")
    public String edycjaUsera(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        model.addAttribute("userCommand", userRepository.findByUsername(name));
        return "user/registrationForm";
    }

    @GetMapping("/usunHistorie/{id}")
    public String usunHistorie(@PathVariable Integer id) {
        if(historiaRepozytorium.findById(id).get().getCzyAdminUsunal().equals(true)) {
            historiaRepozytorium.deleteById(id);
        } else {
            historiaRepozytorium.findById(id).get().setCzyUserUsunal(true);
            Historia his = historiaRepozytorium.findById(id).get();
            historiaRepozytorium.save(his);
        }
        return "redirect:/user/profil";
    }


    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setDisallowedFields("enabled", "roles");
    }
}