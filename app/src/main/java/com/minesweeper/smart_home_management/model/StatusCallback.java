package com.minesweeper.smart_home_management.model;

public interface StatusCallback {
    void getStatusDB(String str);
    void noStatus(String str);

}
