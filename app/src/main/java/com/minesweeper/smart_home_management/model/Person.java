package com.minesweeper.smart_home_management.model;

public class Person {
    public enum GROUP_STATUS
    {
        NONE,
        ADMIN,
        REQUEST_SENT,
        MEMBER
    }
    private  GROUP_STATUS status = GROUP_STATUS.NONE;
    private String name = "";
    private String phoneNumber = "";


    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Person() {

    }



    public GROUP_STATUS getStatus() {
        return status;
    }

    public void setStatus(GROUP_STATUS status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
