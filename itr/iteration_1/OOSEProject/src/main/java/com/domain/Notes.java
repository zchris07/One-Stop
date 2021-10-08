package com.domain;

import java.io.Serializable;

public class Notes implements Serializable {
    private Integer id;
    private String date_taken;
    private Integer notes_id;
    private String text;
    private String extension;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getDate_taken() { return date_taken;}

    public void setDate_taken(String date_taken) {this.date_taken = date_taken;}

    public Integer getNotes_id() { return notes_id;}

    public void setNotes_id(Integer notes_id) {this.notes_id = notes_id;}

    public String getText () {return text;}

    public void setText(String text) {this.text = text;}

    public String getExtension() {return extension;}

    public void setExtension(String text) {this.text = text;}

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", date_taken=" + date_taken + '\'' +
                ", Notes_id =" + notes_id +
                ", text=" + text +
                ", extension=" +extension +
                '}';
    }
}
