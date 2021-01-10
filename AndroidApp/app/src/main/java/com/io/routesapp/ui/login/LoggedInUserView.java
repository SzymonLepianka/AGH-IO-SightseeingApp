package com.io.routesapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String username;
    private String email;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String username, String email) {
        this.displayName = displayName;
        this.username = username;
        this.email = email;
    }

    String getDisplayName() {
        return displayName;
    }
    String getUsername(){ return username; }
    String getEmail(){ return  email;};
}