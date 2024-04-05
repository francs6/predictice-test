package com.example.demo.repository.impl;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.LongTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.example.demo.model.Album;
import com.example.demo.repository.AlbumRepositoryCustom;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom repository implementation for Album providing advanced search features
 *
 * @author fbedier
 */
@Repository
public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom {

    @Autowired
    private ElasticsearchOperations esOperations;

    @Override
    public SearchHits<Album> searchAlbums(Integer year, String searchTxt, Pageable pageable) {
        Query query = buildQuery(searchTxt, year, pageable, true);

        return esOperations.search(query, Album.class);
    }

    @Override
    public Map<String, Long> getAlbumYearFacets() {
        Query query = NativeQuery.builder().withMaxResults(0).withAggregation("years", Aggregation.of(a -> a.terms(ta -> ta.field("year").size(1000)))).withQuery(q -> q.matchAll(ma -> ma)).build();

        SearchHits<Album> searchHits = esOperations.search(query, Album.class);

        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        if (aggregations == null || aggregations.aggregations().isEmpty()) return new HashMap<>();

        Map<String, Long> res = new HashMap<>();
        if(aggregations.aggregations().get(0).aggregation().getAggregate().isLterms()) {
            Buckets<LongTermsBucket> buckets = aggregations.aggregations().get(0).aggregation().getAggregate().lterms().buckets();
            buckets.array().forEach(bucket -> res.put(bucket.key()+"", bucket.docCount()));
        } else if (aggregations.aggregations().get(0).aggregation().getAggregate().isSterms()) {
            Buckets<StringTermsBucket> buckets = aggregations.aggregations().get(0).aggregation().getAggregate().sterms().buckets();
            buckets.array().forEach(bucket -> res.put(bucket.key().toString(), bucket.docCount()));
        }

        return res;
    }

    /**
     * Build a query based on search text and year
     * @param searchTxt search text
     * @param year year
     * @param pageable pageable
     * @param isNativeQuery true if native query, false if criteria query
     * @return the query
     */
    Query buildQuery(String searchTxt, Integer year, Pageable pageable, boolean isNativeQuery) {
        Query query;
        if (!isNativeQuery) {

            if (year == null && Strings.isBlank(searchTxt)) {
                query = NativeQuery.builder().withQuery(q -> q.matchAll(ma -> ma)).withPageable(pageable).build();
            } else {
                Criteria criteria = null;
                if (year != null) {
                    criteria = new Criteria("year").is(year);
                }
                if (searchTxt != null) {
                    if (criteria == null) {
                        criteria = new Criteria("title").contains(searchTxt).or(new Criteria("artist").contains(searchTxt));
                    } else {
                        criteria = criteria.subCriteria(new Criteria("title").contains(searchTxt).fuzzy("ONE").or(new Criteria("artist").contains(searchTxt)));

                    }
                }
                query = new CriteriaQuery(criteria, pageable);
            }
        } else {
            NativeQueryBuilder builder = NativeQuery.builder();
            if (Strings.isNotBlank(searchTxt)) {
                builder.withQuery(q -> q.multiMatch(m -> m.fields("title", "artist").query(searchTxt).type(TextQueryType.BestFields)
                        //.fuzziness("AUTO")
                        //.slop(1)
                ));
            } else {
                builder.withQuery(q -> q.matchAll(ma -> ma));
            }

            if (year != null) {
                builder.withFilter(q -> q.match(m -> m.field("year").query(year)));
            }
            query = builder.withPageable(pageable).build();
        }

        return query;
    }
}
