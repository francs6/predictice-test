package com.example.demo.repository;

import com.example.demo.model.Album;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Map;

/**
 * Custom repository for Album providing advanced search features
 *
 * @author fbedier
 */
public interface AlbumRepositoryCustom {
    /**
     * Search albums by year and text
     *
     * @param year      year to search
     * @param searchTxt text to search
     * @param pageable  paging information
     * @return search results
     */
    SearchHits<Album> searchAlbums(Integer year, String searchTxt, Pageable pageable);

    /**
     * Get album year facets
     *
     * @return album year facets
     */
    Map<String, Long> getAlbumYearFacets();
}
