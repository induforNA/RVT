package com.sayone.omidyar.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sayone on 5/10/16.
 */
public class SpredTable extends RealmObject {

    @PrimaryKey
    private long id;

    double moreThan;

    double lessThan;

    String rating;

    double spread;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMoreThan() {
        return moreThan;
    }

    public void setMoreThan(double moreThan) {
        this.moreThan = moreThan;
    }

    public double getLessThan() {
        return lessThan;
    }

    public void setLessThan(double lessThan) {
        this.lessThan = lessThan;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    @Override
    public String toString() {
        return "SpredTable{" +
                "id=" + id +
                ", moreThan=" + moreThan +
                ", lessThan=" + lessThan +
                ", rating='" + rating + '\'' +
                ", spread=" + spread +
                '}';
    }
}
