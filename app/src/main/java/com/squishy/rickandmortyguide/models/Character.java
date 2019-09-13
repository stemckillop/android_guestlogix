package com.squishy.rickandmortyguide.models;

public class Character {
    public String name;
    public String status;
    public String image;
    public String created;
    public String from;
    public String species;

    public Character(String n, String s, String i, String c, String f, String spe) {
        this.name = n;
        this.status = s;
        this.image = i;
        this.created = c;
        this.from = f;
        this.species = spe;
    }
}
