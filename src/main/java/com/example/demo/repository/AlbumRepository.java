package com.example.demo.repository;

import com.example.demo.dto.AlbumDTO;
import com.example.demo.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Album ES repository
 *
 * @author fbedier
 */
@Repository
public interface AlbumRepository extends ElasticsearchRepository<Album, UUID>, AlbumRepositoryCustom {

    @Query("{\"match_all\": {}}")
    Page<AlbumDTO> findAll2(Pageable pageable);
}
