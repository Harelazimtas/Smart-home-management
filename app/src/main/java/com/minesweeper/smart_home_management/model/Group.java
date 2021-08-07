package com.minesweeper.smart_home_management.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String groupHeader;
    private String groupAdminPhone;
    private List<String> groupMembers;


    public Group()
    {

        groupMembers = new ArrayList<String>();
    }


    public Group(String adminPhone)
    {
        this.groupAdminPhone = adminPhone;
        groupMembers = new ArrayList<String>();
    }

    public String getGroupId() {
        return groupHeader;
    }

    public void setGroupId(String groupSubject) {
        this.groupHeader = groupSubject;
    }

    public String getAdminPhone() {
        return groupAdminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.groupAdminPhone = adminPhone;
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
}
