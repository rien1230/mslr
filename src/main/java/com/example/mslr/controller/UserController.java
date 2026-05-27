package com.example.mslr.controller;

import com.example.mslr.service.SccCodeService;
import com.example.mslr.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SccCodeService sccCodeService;

    public UserController(UserService userService, SccCodeService sccCodeService) {
        this.userService = userService;
        this.sccCodeService = sccCodeService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("form") RegisterForm form,
            BindingResult bindingResult,
            @RequestParam(value = "qrFile", required = false) MultipartFile qrFile,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (form.getPassword() == null || !form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
            return "register";
        }

        if (userService.emailExists(form.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "An account with this email already exists");
            return "register";
        }

        boolean hasTypedScc = form.getSccCode() != null && !form.getSccCode().isBlank();
        boolean hasQr = (qrFile != null && !qrFile.isEmpty());

        if (!hasTypedScc && !hasQr) {
            bindingResult.rejectValue("sccCode", "scc.missing",
                    "Enter an SCC code OR upload the QR image (image_0.png ... image_19.png).");
            return "register";
        }

        if (hasTypedScc && hasQr) {
            bindingResult.rejectValue("sccCode", "scc.tooMany",
                    "Please choose ONE: type the SCC code OR upload the QR image (not both).");
            return "register";
        }

        String sccToUse;

        if (hasQr) {
            String filename = qrFile.getOriginalFilename();
            sccToUse = sccCodeService.sccFromQrFilename(filename);

            if (sccToUse == null) {

                bindingResult.rejectValue("sccCode", "qr.invalid",
                        "Invalid QR file. Please upload image_0.png ... image_19.png.");
                return "register";
            }

            form.setSccCode(sccToUse);
        } else {
            sccToUse = form.getSccCode().trim();
        }

        try {
            sccCodeService.consume(sccToUse);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("sccCode", "scc.invalid", ex.getMessage());
            return "register";
        }

        userService.registerVoter(form);

        return "redirect:/login";
    }
}
