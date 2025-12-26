package com.example.mslr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SccCode {
    //Primary key + Auto-generated.
    @Id
    private String code;
    private boolean used;

    public String getCode(){
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public boolean getUsed(){
        return used;
    }
    public void setUsed(boolean used){
        this.used= used;
    }
}
