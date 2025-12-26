package com.example.mslr.repo;
import com.example.mslr.model.SccCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SccCodeRepository extends JpaRepository<SccCode, Long> {
    Optional<SccCode> findByCode(String code);
    Optional<SccCode> findByCodeAndUsedFalse(String code);
    boolean existsByCode(String code);
}

