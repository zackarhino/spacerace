package com.example.spacerace.database;

/**
 * Simple Note that holds a title, body, and metadata (id, date)
 * @author Zachary Allard
 */
public class Note {
    int id;
    String title;
    String body;
    String date;

    public Note(){

    }
    public Note(String title, String body, String date){
        this.title = title;
        this.body = body;
        this.date = date;
    }
    public Note(int id, String title, String body, String date){
        this.id = id;
        this.title = title;
        this.body = body;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
