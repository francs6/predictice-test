package com.example.demo.service;

import com.example.demo.dto.AlbumDTO;
import com.example.demo.model.Album;
import com.example.demo.repository.AlbumRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Album service
 *
 * @author fbedier
 */
@Service
public class AlbumService {

    @Autowired
    AlbumRepository albumRepository;

    /**
     * Param defining if data need to be loaded from scratch
     */
    @Value("${api.data.init: true}")
    Boolean isDataInit = true;

    /**
     * Initialize data if needed
     */
    @PostConstruct
    public void init() {
        if (isDataInit) {
            albumRepository.deleteAll();
            List<Album> albums = loadJsonFromFile("albums_sample.json", new TypeReference<>() {
            });
            albumRepository.saveAll(albums);
        }
    }

    public Optional<Album> findById(UUID id) {
        return albumRepository.findById(id);
    }

    public SearchHits<Album> searchAlbums(Integer year, String searchTxt, Pageable pageable) {
        return albumRepository.searchAlbums(year, searchTxt, pageable);
    }

    public Map<String, Long> getAlbumYearFacets() {
        return albumRepository.getAlbumYearFacets();
    }

    public Page<AlbumDTO> findAll(Pageable pageable) {
        return albumRepository.findAll2(pageable);
    }

    /**
     * Load json from file
     *
     * @param path  path to file
     * @param clazz class type
     * @param <E>   type
     * @return object
     */
    <E> E loadJsonFromFile(String path, TypeReference<E> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource(path);
            return mapper.readValue(resource.getInputStream(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot serialize Object %s from %s because of following problem", clazz.getType().getTypeName(), path), e);
        }
    }
}
