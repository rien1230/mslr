package com.example.mslr.repo;

import com.example.mslr.model.ReferendumOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferendumOptionRepository extends JpaRepository<ReferendumOption, Long> {
    List<ReferendumOption> findByReferendum_Id(Long referendumId);


}
