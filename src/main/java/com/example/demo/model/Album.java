package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

/**
 * Album entity
 */
@Document(indexName = "album")
public class Album {
    @Id
    UUID id;

    @Field(type = FieldType.Text)
    String title;

    @Field(type = FieldType.Text)
    String artist;

    @Field(type = FieldType.Integer)
    Integer year;

    @Field(type = FieldType.Text)
    String coverURL;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Integer getReleaseYear() {
        return getYear();
    }

    public void setReleaseYear(Integer year) {
        setYear(year);
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }
}
