package com.example.demo.config;

import com.example.demo.model.Album;
import com.example.demo.service.AlbumService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Francis Bédier
 * @since 2024-04-05
 */
@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultESServiceIT {
    @Container
    private static final ESContainerConfig container = new ESContainerConfig();

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private AlbumService albumService;

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @AfterAll
    static void tearDown() {
        container.stop();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(container.isRunning());
    }

    @Test
    void testAlbumFacets() {
        Map<String, Long> albumYearFacets = albumService.getAlbumYearFacets();
        assertEquals(42, albumYearFacets.size());
    }

    @Test
    void testAlbumSearch() {
        SearchHits<Album> yourLifeResults = albumService.searchAlbums(1998, "your life", Pageable.unpaged());
        assertEquals(3, yourLifeResults.getTotalHits());
        // verify that all hits have "your" or "life" or "your life" (ignoring case) in the title or artist
        yourLifeResults.forEach(hit -> {
            Album album = hit.getContent();
            assertTrue(album.getTitle().toLowerCase().contains("your") ||
                    album.getTitle().toLowerCase().contains("life") ||
                    album.getArtist().toLowerCase().contains("your") ||
                    album.getArtist().toLowerCase().contains("life") ||
                    album.getArtist().toLowerCase().contains("your life") ||
                    album.getTitle().toLowerCase().contains("your life"));
        });




    }

    @Test
    void testAlbumGet() {
        Album existingAlbum = albumService.findById(UUID.fromString("05841201-1a45-47c7-a895-d487677bab71")).orElseThrow();
        assertEquals("Wesley Willis", existingAlbum.getTitle());
    }

//    private void recreateIndex() {
//        if (template.indexOps(Album.class).exists()) {
//            template.indexOps(Album.class).delete();
//            template.indexOps(Album.class).create();
//        }
//    }
}
