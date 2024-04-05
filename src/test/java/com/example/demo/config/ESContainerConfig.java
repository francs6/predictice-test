package com.example.demo.config;

import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Francis Bédier
 * @since 2024-04-05
 */
public class ESContainerConfig extends ElasticsearchContainer {

    private static final String ELASTIC_SEARCH_DOCKER = "elasticsearch:7.17.0";
    private static final String CLUSTER_NAME = "cluster.name";
    private static final String ELASTIC_SEARCH = "elasticsearch";
    private static final String DISCOVERY_TYPE = "discovery.type";
    private static final String DISCOVERY_TYPE_SINGLE_NODE = "single-node";
    private static final String XPACK_SECURITY_ENABLED = "xpack.security.enabled";

    public ESContainerConfig() {
        super(DockerImageName.parse(ELASTIC_SEARCH_DOCKER)
                .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch"));
        addFixedExposedPort(9200, 9200);
        addEnv(DISCOVERY_TYPE, DISCOVERY_TYPE_SINGLE_NODE);
        addEnv(XPACK_SECURITY_ENABLED, Boolean.FALSE.toString());
        addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
