package com.edutrack.academic.dto;

public class StudentResponse {

    private Long id;
    private String registerNumber;
    private String name;
    private String email;
    private String department;
    private String batch;

    public StudentResponse() {
    }

    public StudentResponse(Long id, String registerNumber, String name, String email, String department, String batch) {
        this.id = id;
        this.registerNumber = registerNumber;
        this.name = name;
        this.email = email;
        this.department = department;
        this.batch = batch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
