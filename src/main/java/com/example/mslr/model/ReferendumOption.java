package com.example.mslr.model;

import jakarta.persistence.*;

@Entity
public class ReferendumOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(optional = false)
    private Referendum referendum;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Referendum getReferendum() {
        return referendum;
    }
    public void setReferendum(Referendum referendum) {
        this.referendum = referendum;
    }
}
