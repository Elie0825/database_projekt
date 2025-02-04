package se.elie.databas;

import java.time.LocalDate;

class WorkRole {

    private int roleId;
    private String title;
    private String description;
    private double salary;
    private LocalDate creationDate;

    public WorkRole(String title, String description, double salary, LocalDate creationDate) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.creationDate = creationDate;
    }

    public WorkRole(int roleId, String title, String description, double salary, LocalDate creationDate) {
        this(title, description, salary, creationDate);
        this.roleId = roleId;
    }

    // Getters and Setters
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
