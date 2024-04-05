package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.repository.AlbumRepository;
import com.example.demo.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Rest resource exposing Album REST features
 */
@RestController
public class AlbumController {

    final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<?> getAlbum(@PathVariable UUID id) {
        return ResponseEntity.ok(albumService.findById(id));
    }

    @GetMapping("/albums")
    public ResponseEntity<?> getAlbums(@RequestParam(required = false) Integer year,
                                       @RequestParam(required = false) String searchText,
                                       @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable) {
        if (year == null && searchText == null) {
            return ResponseEntity.ok(albumService.findAll(pageable));
        } else {
            SearchHits<Album> albumHits = albumService.searchAlbums(year, searchText, pageable);
            SearchPage<Album> albumPage = SearchHitSupport.searchPageFor(albumHits, pageable);
            return ResponseEntity.ok(albumPage);
        }
    }

    @GetMapping("/albums/year/facet")
    public ResponseEntity<?> getAlbumYearFacet() {
            return ResponseEntity.ok(albumService.getAlbumYearFacets());

    }
}
