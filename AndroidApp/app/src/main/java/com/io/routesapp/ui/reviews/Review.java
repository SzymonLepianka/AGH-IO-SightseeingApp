package com.io.routesapp.ui.reviews;

public abstract class Review {
    Integer authorID;
    String content;

    public Integer getAuthorID() {
        return authorID;
    }

    public String getContent() {
        return content;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
