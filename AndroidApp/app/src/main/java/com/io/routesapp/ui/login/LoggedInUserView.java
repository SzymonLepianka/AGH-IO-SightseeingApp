package com.io.routesapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String username;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String username) {
        this.displayName = displayName;
        this.username = username;
    }

    String getDisplayName() {
        return displayName;
    }
    String getUsername(){ return username; }
}