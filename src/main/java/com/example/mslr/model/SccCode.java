package com.example.mslr.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "scc_code")
public class SccCode {

    @Id
    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "used", nullable = false)
    private boolean used = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
