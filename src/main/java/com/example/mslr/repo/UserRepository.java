package com.example.mslr.repo;
import com.example.mslr.model.Role;
import com.example.mslr.model.User;
import com.example.mslr.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //Returns if email is used already or not.
    boolean existsByEmail(String email);
    long countByRole(Role role);

    //For find and saving users
    // Counts voters only (treat EC as not a voter)
    @Query("select count(u) from User u where lower(u.email) <> 'ec@referendum.gov.sr'")
    long countRegisteredVoters();
}
