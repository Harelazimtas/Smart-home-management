package com.minesweeper.smart_home_management.model;

import java.util.Date;

public class Mission {
    private String name;
    private String description;
    private int id;
    private Date dueDate;
    private Status status;

    public Mission(){

    }

    public Mission(String name,String description,int id,Date dueDate){
        this.name=name;
        this.description=description;
        this.id=id;
        this.dueDate=dueDate;
        this.status= Status.NEW;
    }

    public Mission(String name,String description,int id,Date dueDate,Status status){
        this.name=name;
        this.description=description;
        this.id=id;
        this.dueDate=dueDate;
        this.status= status;
    }


    public Date getDueDate() {
        return dueDate;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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
                ", id=" + id +
                ", dueDate=" + dueDate +
                ", status=" + status +
                '}';
    }
}
