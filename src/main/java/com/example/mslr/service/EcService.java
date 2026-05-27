package com.example.mslr.service;

import com.example.mslr.model.Referendum;
import com.example.mslr.model.ReferendumOption;
import com.example.mslr.model.ReferendumStatus;
import com.example.mslr.repo.ReferendumOptionRepository;
import com.example.mslr.repo.ReferendumRepository;
import com.example.mslr.repo.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EcService {

    private final ReferendumRepository referendumRepository;
    private final ReferendumOptionRepository optionRepository;
    private final VoteRepository voteRepository;

    public EcService(ReferendumRepository referendumRepository,
                     ReferendumOptionRepository optionRepository,
                     VoteRepository voteRepository) {
        this.referendumRepository = referendumRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
    }

    public record OptionStat(Long id, String text, long votes) {}
    public record ReferendumRow(Long id, String title, String description,
                                ReferendumStatus status, boolean locked,
                                List<OptionStat> options) {}

    @Transactional(readOnly = true)
    public List<ReferendumRow> getDashboardRows() {
        List<Referendum> refs = referendumRepository.findAll();

        List<ReferendumRow> rows = new ArrayList<>();
        for (Referendum ref : refs) {
            List<ReferendumOption> opts = optionRepository.findByReferendum_Id(ref.getId());

            Map<Long, Long> voteCounts = voteRepository.countVotesByOption(ref.getId())
                    .stream()
                    .collect(Collectors.toMap(
                            r -> (Long) r[0],
                            r -> (Long) r[1]
                    ));

            List<OptionStat> optionStats = opts.stream()
                    .map(o -> new OptionStat(o.getId(), o.getText(), voteCounts.getOrDefault(o.getId(), 0L)))
                    .toList();

            rows.add(new ReferendumRow(
                    ref.getId(),
                    ref.getTitle(),
                    ref.getDescription(),
                    ref.getStatus(),
                    ref.isLocked(),
                    optionStats
            ));
        }

        rows.sort(Comparator.comparing(ReferendumRow::id).reversed());
        return rows;
    }

    @Transactional
    public void createReferendum(String title, String description, List<String> optionTexts) {
        String t = safe(title);
        if (t.isBlank()) throw new IllegalArgumentException("Title is required.");

        List<String> cleaned = optionTexts.stream()
                .map(this::safe)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();

        if (cleaned.size() < 2) throw new IllegalArgumentException("Add at least 2 options.");

        Referendum ref = new Referendum();
        ref.setTitle(t);
        ref.setDescription(safe(description));
        ref.setStatus(ReferendumStatus.CLOSED);
        ref.setLocked(false);

        referendumRepository.save(ref);

        for (String optText : cleaned) {
            ReferendumOption opt = new ReferendumOption();
            opt.setText(optText);
            opt.setReferendum(ref);
            optionRepository.save(opt);
        }
    }

    @Transactional
    public void editReferendum(Long refId,
                               String title,
                               String description,
                               List<Long> optionIds,
                               List<String> optionTexts,
                               List<Long> deleteOptionIds,
                               List<String> addOptionLines) {

        Referendum ref = referendumRepository.findById(refId)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found"));

        if (ref.isLocked()) {
            throw new IllegalStateException("This referendum is read-only because it has been opened before.");
        }

        // Update title/description
        String t = safe(title);
        if (t.isBlank()) throw new IllegalArgumentException("Title is required.");
        ref.setTitle(t);
        ref.setDescription(safe(description));
        referendumRepository.save(ref);

        // Delete selected options
        Set<Long> toDelete = deleteOptionIds == null ? Set.of() : new HashSet<>(deleteOptionIds);
        if (!toDelete.isEmpty()) {
            for (Long id : toDelete) optionRepository.deleteById(id);
        }

        // Update existing option texts
        if (optionIds != null && optionTexts != null && optionIds.size() == optionTexts.size()) {
            for (int i = 0; i < optionIds.size(); i++) {
                Long optId = optionIds.get(i);
                if (toDelete.contains(optId)) continue;

                String newText = safe(optionTexts.get(i));
                if (newText.isBlank()) continue;

                ReferendumOption opt = optionRepository.findById(optId)
                        .orElseThrow(() -> new IllegalArgumentException("Option not found: " + optId));

                if (!opt.getReferendum().getId().equals(refId)) {
                    throw new IllegalArgumentException("Option does not belong to this referendum.");
                }
                opt.setText(newText);
                optionRepository.save(opt);
            }
        }

        // Add new options (one per line)
        List<String> additions = (addOptionLines == null ? List.<String>of() : addOptionLines).stream()
                .map(this::safe)
                .flatMap(s -> Arrays.stream(s.split("\\R"))) // split lines
                .map(this::safe)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();

        for (String add : additions) {
            ReferendumOption opt = new ReferendumOption();
            opt.setText(add);
            opt.setReferendum(ref);
            optionRepository.save(opt);
        }

        // Ensure still has at least 2 options
        if (optionRepository.findByReferendum_Id(refId).size() < 2) {
            throw new IllegalStateException("A referendum must have at least 2 options.");
        }
    }

    // The opening and closing
    @Transactional
    public void openReferendum(Long refId) {
        Referendum ref = referendumRepository.findById(refId)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found"));

        // first time opening -> lock forever
        if (!ref.isLocked()) ref.setLocked(true);

        ref.setStatus(ReferendumStatus.OPEN);
        referendumRepository.save(ref);
    }

    @Transactional
    public void closeReferendum(Long refId) {
        Referendum ref = referendumRepository.findById(refId)
                .orElseThrow(() -> new IllegalArgumentException("Referendum not found"));

        ref.setStatus(ReferendumStatus.CLOSED);
        referendumRepository.save(ref);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
