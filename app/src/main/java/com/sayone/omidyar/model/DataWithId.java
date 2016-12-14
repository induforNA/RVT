package com.sayone.omidyar.model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by sayone on 8/12/16.
 */

public class DataWithId {

    ArrayList<String> id;

    public ArrayList<String> getId() {
        return id;
    }

    public void setId(ArrayList<String> id) {
        this.id = id;
    }

    public DataWithId(ArrayList<String> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DataWithId{" +
                "id=" + id +
                '}';
    }
}
