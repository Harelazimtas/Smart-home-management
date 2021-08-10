package com.minesweeper.smart_home_management.model;

public class Person {
    public enum GROUP_STATUS
    {
        NONE,
        ADMIN,
        MEMBER
    }
  // private String pid = "";
    private  GROUP_STATUS status = GROUP_STATUS.NONE;
    private String name = "";
    private String phoneNumber = "";


    public Person(String name, String phoneNumber) {
        //this.pid = pid;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Person() {

    }

 //   public String getPid() {
  //      return pid;
   // }

    //public void setPid(String pid) {
     //   this.pid = pid;
   // }

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
