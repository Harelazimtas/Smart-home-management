package com.minesweeper.smart_home_management.model;

import java.util.Date;

public class Mission {
    private String name;
    private String description;
    private int idPerson;
    private Date dueDate;
    private Status status;

    public Mission(){

    }

    public Mission(String name,String description,int idPerson,Date dueDate){
        this.name=name;
        this.description=description;
        this.idPerson=idPerson;
        this.dueDate=dueDate;
        this.status= Status.NEW;
    }

    public Mission(String name,String description,int idPerson,Date dueDate,Status status){
        this.name=name;
        this.description=description;
        this.idPerson=idPerson;
        this.dueDate=dueDate;
        this.status= status;
    }


    public Date getDueDate() {
        return dueDate;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idPerson=" + idPerson +
                ", dueDate=" + dueDate +
                ", status=" + status +
                '}';
    }
}