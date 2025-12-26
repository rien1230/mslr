package com.example.mslr.service;

import com.example.mslr.repo.SccCodeRepository;
import org.springframework.stereotype.Service;

@Service
public class SccCodeService {
    private final SccCodeRepository sccCodeRepository;
    public SccCodeService(SccCodeRepository sccCodeRepository) {
        this.sccCodeRepository = sccCodeRepository;
    }
}
