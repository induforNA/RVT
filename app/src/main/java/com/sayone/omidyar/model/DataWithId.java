package com.sayone.omidyar.model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by sayone on 8/12/16.
 */

public class DataWithId {

    ArrayList<String> id;

    ArrayList<String> email;

    public DataWithId(ArrayList<String> id, ArrayList<String> email) {
        this.id = id;
        this.email = email;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DataWithId{" +
                "id=" + id +
                ", email=" + email +
                '}';
    }
}
