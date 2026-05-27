package com.example.mslr.controller;

import java.time.LocalDate;

public class RegisterForm {

    private String email;
    private String fullName;
    private LocalDate dob;

    private String password;
    private String confirmPassword;

    private String sccCode;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getSccCode() { return sccCode; }
    public void setSccCode(String sccCode) { this.sccCode = sccCode; }
}

