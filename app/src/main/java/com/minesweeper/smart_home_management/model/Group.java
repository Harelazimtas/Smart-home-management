package com.minesweeper.smart_home_management.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String adminPhone = "";
    private List<String> groupMembers;


    public Group()
    {

        groupMembers = new ArrayList<>();
    }


    public Group(String adminPhone)
    {
        this.adminPhone = adminPhone;
        groupMembers = new ArrayList<>();
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }


    public boolean addPersonToGroup(String p)
    {

        groupMembers.add(p);

        return true;
    }


    public String toString()
    {
        return "admin phone: " + getAdminPhone() + "\n" +
                "members: " + getGroupMembers();

    }

    public void setGroupMembers(List<String> memberIds) {
        this.groupMembers =memberIds;
    }
}
