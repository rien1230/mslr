package com.example.mslr.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Referendum {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    @Enumerated(EnumType.STRING)
    private ReferendumStatus status = ReferendumStatus.CLOSED;
    @OneToMany(mappedBy = "referendum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReferendumOption> options = new ArrayList<>();
    @Column(nullable = false)
    private boolean locked = false;
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ReferendumStatus getStatus() {
        return status;
    }
    public void setStatus(ReferendumStatus status) {
        this.status = status;
    }
    public List<ReferendumOption> getOptions() {
        return options;
    }
    public void setOptions(List<ReferendumOption> options) {
        this.options = options;
    }


}
