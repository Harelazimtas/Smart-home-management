package com.minesweeper.smart_home_management.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupHeader = "";
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

    public String getGroupHeader() {
        return groupHeader;
    }

    public void setGroupHeader(String groupSubject) {
        this.groupHeader = groupSubject;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public boolean addPersonToGroup(String p)
    {

        groupMembers.add(p);

        return true;
    }

    public boolean removePersonFromGroup(String p)
    {
        for (String person: groupMembers) {
            if(p.equals(person))
            {
                groupMembers.remove(p);
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        return "admin phone: " + getAdminPhone() + "\n" +
                "members: " + getGroupMembers();

    }
}
