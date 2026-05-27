package com.example.mslr.api;

import com.example.mslr.api.dto.ReferendumDto;
import com.example.mslr.api.dto.ReferendumOptionsDto;
import com.example.mslr.api.dto.ReferendumsResponse;
import com.example.mslr.model.Referendum;
import com.example.mslr.model.ReferendumOption;
import com.example.mslr.model.ReferendumStatus;
import com.example.mslr.repo.ReferendumOptionRepository;
import com.example.mslr.repo.ReferendumRepository;
import com.example.mslr.repo.VoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MslrApiService {

    private final ReferendumRepository referendumRepository;
    private final ReferendumOptionRepository optionRepository;
    private final VoteRepository voteRepository;

    public MslrApiService(ReferendumRepository referendumRepository,
                          ReferendumOptionRepository optionRepository,
                          VoteRepository voteRepository) {
        this.referendumRepository = referendumRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
    }

    public ReferendumsResponse getReferendumsByStatus(String statusRaw) {
        ReferendumStatus status = parseStatus(statusRaw);

        List<Referendum> refs = referendumRepository.findByStatus(status);
        List<ReferendumDto> dtos = refs.stream().map(this::toDto).toList();

        return new ReferendumsResponse(dtos);
    }

    public ReferendumDto getReferendumById(Long id) {
        Referendum ref = referendumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Referendum not found"));

        return toDto(ref);
    }

    private ReferendumDto toDto(Referendum ref) {
        String status = ref.getStatus().name().toLowerCase();

        ReferendumOptionsDto optionsDto = buildOptions(ref.getId());

        return new ReferendumDto(
                String.valueOf(ref.getId()),
                status,
                ref.getTitle(),
                ref.getDescription(),
                optionsDto
        );
    }

    private ReferendumOptionsDto buildOptions(Long referendumId) {
        List<ReferendumOption> options = optionRepository.findByReferendum_Id(referendumId);

        List<Map<String, String>> optionItems = new ArrayList<>();
        for (ReferendumOption opt : options) {
            long votes = voteRepository.countByReferendum_IdAndOption_Id(referendumId, opt.getId());
            Map<String, String> obj = new LinkedHashMap<>();
            obj.put(String.valueOf(opt.getId()), opt.getText());
            obj.put("votes", String.valueOf(votes));

            optionItems.add(obj);
        }

        return new ReferendumOptionsDto(optionItems);
    }

    private ReferendumStatus parseStatus(String statusRaw) {
        if (statusRaw == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing status parameter");
        }

        String s = statusRaw.trim().toLowerCase();
        return switch (s) {
            case "open" -> ReferendumStatus.OPEN;
            case "closed" -> ReferendumStatus.CLOSED;
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "status must be 'open' or 'closed'"
            );
        };
    }
}
