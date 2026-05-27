package com.example.mslr.repo;
import com.example.mslr.model.Referendum;
import com.example.mslr.model.ReferendumOption;
import com.example.mslr.model.ReferendumStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface ReferendumRepository extends JpaRepository<Referendum, Long> {
    List<Referendum> findByStatus(ReferendumStatus status);
}
