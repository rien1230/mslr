package com.example.mslr.repo;

import com.example.mslr.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface VoteRepository extends JpaRepository<Vote, Long> {

    boolean existsByVoter_IdAndReferendum_Id(Long voterId, Long referendumId);
    long countByReferendum_IdAndOption_Id(Long referendumId, Long optionId);
    List<Vote> findByVoter_EmailOrderByCastAtDesc(String email);
    @Query("""
        select v.option.id, count(v)
        from Vote v
        where v.referendum.id = :refId
        group by v.option.id
    """)
    List<Object[]> countVotesByOption(@Param("refId") Long refId);
}
