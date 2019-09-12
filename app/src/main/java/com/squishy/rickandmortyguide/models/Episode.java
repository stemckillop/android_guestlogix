package com.squishy.rickandmortyguide.models;

public class Episode {

    private int id;
    private String name;
    private String episode;
    private String[] characters;
    private String url;

    public Episode(int id, String name, String episode, String[] characters, String url)
    {
        this.id = id;
        this.name = name;
        this.episode = episode;
        this.characters = characters;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }
}
