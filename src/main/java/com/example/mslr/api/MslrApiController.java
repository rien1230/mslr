package com.example.mslr.api;

import com.example.mslr.api.dto.ReferendumDto;
import com.example.mslr.api.dto.ReferendumsResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mslr")
public class MslrApiController {

    private final MslrApiService apiService;

    public MslrApiController(MslrApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/referendums")
    public ReferendumsResponse referendumsByStatus(@RequestParam("status") String status) {
        return apiService.getReferendumsByStatus(status);
    }

    @GetMapping("/referendum/{id}")
    public ReferendumDto referendumById(@PathVariable Long id) {
        return apiService.getReferendumById(id);
    }

    @GetMapping("/referemdum/{id}")
    public ReferendumDto referemdumByIdTypo(@PathVariable Long id) {
        return apiService.getReferendumById(id);
    }
}
