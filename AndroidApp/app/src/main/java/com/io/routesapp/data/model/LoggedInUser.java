package com.io.routesapp.data.model;

import java.util.HashMap;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String email;
    private HashMap<String, String> cookies;

    public LoggedInUser(String userId, String displayName, String email, HashMap<String, String> cookies) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.cookies = cookies;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() { return email; };

    public HashMap<String, String> getCookies() { return cookies; };

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }
}