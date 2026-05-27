package com.example.mslr.service;

import com.example.mslr.model.*;
import com.example.mslr.repo.ReferendumOptionRepository;
import com.example.mslr.repo.ReferendumRepository;
import com.example.mslr.repo.UserRepository;
import com.example.mslr.repo.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ReferendumRepository referendumRepository;
    private final ReferendumOptionRepository optionRepository;

    public VoteService(VoteRepository voteRepository,
                       UserRepository userRepository,
                       ReferendumRepository referendumRepository,
                       ReferendumOptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.referendumRepository = referendumRepository;
        this.optionRepository = optionRepository;
    }

    public boolean hasVoted(String email, Long referendumId) {
        User voter = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        return voteRepository.existsByVoter_IdAndReferendum_Id(voter.getId(), referendumId);
    }

    @Transactional
    public void castVote(String email, Long referendumId, Long optionId) {

        User voter = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        Referendum ref = referendumRepository.findById(referendumId)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found: " + referendumId));

        if (ref.getStatus() != ReferendumStatus.OPEN) {
            throw new IllegalStateException("Referendum is closed.");
        }

        if (voteRepository.existsByVoter_IdAndReferendum_Id(voter.getId(), referendumId)) {
            throw new IllegalStateException("You have already voted in this referendum.");
        }

        ReferendumOption opt = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found: " + optionId));

        if (!opt.getReferendum().getId().equals(referendumId)) {
            throw new IllegalArgumentException("Selected option does not belong to this referendum.");
        }

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setReferendum(ref);
        vote.setOption(opt);
        vote.setCastAt(LocalDateTime.now());

        voteRepository.save(vote);

        autoCloseIfThresholdReached(referendumId, optionId);
    }

    private void autoCloseIfThresholdReached(Long referendumId, Long optionId) {

        Referendum ref = referendumRepository.findById(referendumId)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found: " + referendumId));

        if (ref.getStatus() != ReferendumStatus.OPEN) return;


        long totalVoters = userRepository.countByRole(Role.VOTER);
        if (totalVoters <= 0) return;

        long votesForThisOption = voteRepository.countByReferendum_IdAndOption_Id(referendumId, optionId);

        if (votesForThisOption * 2 >= totalVoters) {
            ref.setStatus(ReferendumStatus.CLOSED);
            referendumRepository.save(ref);
        }
    }
}





