package com.example.mslr.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "referendum_id"})
)
public class Vote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private User voter;
    @ManyToOne(optional = false)
    private Referendum referendum;
    @ManyToOne(optional = false)
    private ReferendumOption option;
    private LocalDateTime castAt = LocalDateTime.now();
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getVoter() {
        return voter;
    }
    public void setVoter(User voter) {
        this.voter = voter;
    }
    public Referendum getReferendum() {
        return referendum;
    }
    public void setReferendum(Referendum referendum) {
        this.referendum = referendum;
    }
    public ReferendumOption getOption() {
        return option;
    }
    public void setOption(ReferendumOption option) {
        this.option = option;
    }
    public LocalDateTime getCastAt() {
        return castAt;
    }
    public void setCastAt(LocalDateTime castAt) {
        this.castAt = castAt;
    }

}
