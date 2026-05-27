package com.example.mslr.controller;

import com.example.mslr.model.Referendum;
import com.example.mslr.model.ReferendumOption;
import com.example.mslr.model.ReferendumStatus;
import com.example.mslr.repo.ReferendumOptionRepository;
import com.example.mslr.repo.ReferendumRepository;
import com.example.mslr.repo.VoteRepository;
import com.example.mslr.service.VoteService;
import com.example.mslr.controller.VoteForm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/voter")
public class VoterController {

    private final ReferendumRepository referendumRepository;
    private final ReferendumOptionRepository optionRepository;
    private final VoteRepository voteRepository;
    private final VoteService voteService;

    public VoterController(ReferendumRepository referendumRepository,
                           ReferendumOptionRepository optionRepository, VoteRepository voteRepository,
                           VoteService voteService) {
        this.referendumRepository = referendumRepository;
        this.optionRepository = optionRepository;
        this.voteRepository= voteRepository;
        this.voteService = voteService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("allRefs", referendumRepository.findAll());
        model.addAttribute("activeRefs", referendumRepository.findByStatus(ReferendumStatus.OPEN));
        return "voter/dashboard";
    }

    @GetMapping("/referendums/{id}")
    public String viewReferendum(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails principal,
                                 Model model) {

        Referendum ref = referendumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found"));

        List<ReferendumOption> options = optionRepository.findByReferendum_Id(id);

        boolean alreadyVoted = voteService.hasVoted(principal.getUsername(), id);

        model.addAttribute("ref", ref);
        model.addAttribute("options", options);
        model.addAttribute("alreadyVoted", alreadyVoted);
        model.addAttribute("voteForm", new VoteForm());

        return "voter/referendum";
    }
    @GetMapping("/my-votes")
    public String myVotes(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
            Model model
    ) {
        String email = principal.getUsername();
        model.addAttribute("votes", voteRepository.findByVoter_EmailOrderByCastAtDesc(email));
        return "voter/my-votes";
    }

    @PostMapping("/referendums/{id}/vote")
    public String vote(@PathVariable Long id,
                       @ModelAttribute("voteForm") VoteForm voteForm,
                       @AuthenticationPrincipal UserDetails principal,
                       RedirectAttributes ra) {

        if (voteForm.getOptionId() == null) {
            ra.addFlashAttribute("error", "Please select an option before submitting.");
            return "redirect:/voter/referendums/" + id;
        }

        try {
            voteService.castVote(principal.getUsername(), id, voteForm.getOptionId());
            ra.addFlashAttribute("success", "Vote submitted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/voter/referendums/" + id;
    }
}
