package com.squishy.rickandmortyguide.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable {

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

    protected Episode(Parcel in) {
        id = in.readInt();
        name = in.readString();
        episode = in.readString();
        characters = in.createStringArray();
        url = in.readString();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public String[] getCharacters() { return characters; }

    public void setCharacters(String[] chars) { characters = chars; }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(episode);
        parcel.writeStringArray(characters);
        parcel.writeString(url);
    }
}
