package com.example.mslr.controller;
import org.springframework.ui.Model;
import com.example.mslr.service.SccCodeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scc-codes")
public class SccCodeController {
    private final SccCodeService sccCodeService;


    public SccCodeController( SccCodeService sccCodeService) {
        this.sccCodeService = sccCodeService;
    }
    @GetMapping
    public String listSccCodes(Model model){
        model.addAttribute("codes", sccCodeService.findAll());
        return "scc/list";
    }
    @GetMapping("/new")
    public String showNewSccCode(Model model){
        model.addAttribute("form", new SccCodeForm());
        return "scc/new";
    }
    @PostMapping
    public String createSccCode(
            @Valid @ModelAttribute("form") SccCodeForm form,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "scc/new";
    }
        String code= form.getCode();
        if (sccCodeService.exists(code)){
            bindingResult.rejectValue("code", "code.exists", "That SCC code already exists.");
            return "scc/new";
        }
        sccCodeService.createNewCode(code);
        return "redirect:/scc-codes";
    }

    }




