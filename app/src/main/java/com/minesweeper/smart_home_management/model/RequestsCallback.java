package com.minesweeper.smart_home_management.model;

import java.util.List;

public interface RequestsCallback {
    void getRequestsString(List<String> list);
    void noRequests(String str);
}
