package com.squishy.rickandmortyguide.models;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Character implements Comparable<Character> {
    public String name;
    public String status;
    public String image;
    public String created;
    public String from;
    public String species;
    private Date dateCreated;

    public Character(String n, String s, String i, String c, String f, String spe) {
        this.name = n;
        this.status = s;
        this.image = i;
        this.created = c;
        this.from = f;
        this.species = spe;

        try {
            SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
            dateCreated = d.parse(created);
        } catch (Exception e) {
            Log.e("Character", "Unable to parse date");
        }
    }

    @Override
    public int compareTo(Character o) {
        return dateCreated.compareTo(o.dateCreated);
    }
}
