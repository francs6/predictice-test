package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public interface IAlbum {
     UUID getId();

     String getTitle();

     String getArtist();

     Integer getYear();

     Integer getReleaseYear();

     String getCoverURL();
}
