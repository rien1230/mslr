package com.example.mslr.controller;

import com.example.mslr.service.EcService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ec")
public class EcController {

    private final EcService ecService;

    public EcController(EcService ecService) {
        this.ecService = ecService;
    }

    @GetMapping
    public String dashboard(Model model,
                            @ModelAttribute("success") String success,
                            @ModelAttribute("error") String error) {

        model.addAttribute("rows", ecService.getDashboardRows());
        return "ec/dashboard";
    }

    @PostMapping("/referendums/new")
    public String create(@RequestParam String title,
                         @RequestParam(required = false) String description,
                         @RequestParam String optionsText,
                         RedirectAttributes ra) {
        try {
            List<String> lines = List.of(optionsText.split("\\R"));
            ecService.createReferendum(title, description, lines);
            ra.addFlashAttribute("success", "Referendum created.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ec";
    }

    @PostMapping("/referendums/{id}/edit")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam(required = false) String description,
                       @RequestParam(required = false) List<Long> optionIds,
                       @RequestParam(required = false) List<String> optionTexts,
                       @RequestParam(required = false) List<Long> deleteOptionIds,
                       @RequestParam(required = false) List<String> addOptionsText,
                       RedirectAttributes ra) {

        try {
            ecService.editReferendum(id, title, description, optionIds, optionTexts, deleteOptionIds, addOptionsText);
            ra.addFlashAttribute("success", "Referendum updated.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ec";
    }

    @PostMapping("/referendums/{id}/open")
    public String open(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.openReferendum(id);
            ra.addFlashAttribute("success", "Referendum opened (now read-only).");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ec";
    }

    @PostMapping("/referendums/{id}/close")
    public String close(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ecService.closeReferendum(id);
            ra.addFlashAttribute("success", "Referendum closed.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ec";
    }
}
