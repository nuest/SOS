/**
 * Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.statistics.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.joda.time.DateTimeZone;
import org.n52.sos.statistics.api.ElasticSearchSettings;
import org.n52.sos.statistics.api.interfaces.IAdminDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ElasticSearchDataHandler implements IStatisticsDataHandler, IAdminDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchDataHandler.class);

    // TODO remove the Singleton Pattern
    private static ElasticSearchDataHandler _INSTANCE;

    // TODO not optimal for testing purposes only - DNU: also, inject the singleton instead of using getInstance()!!
    public synchronized static ElasticSearchDataHandler getInstance()
    {
        if (_INSTANCE == null) {
            _INSTANCE = new ElasticSearchDataHandler();
            ElasticSearchSettings settings = new ElasticSearchSettings(true);
            settings.setClusterName("ogc-statistics-cluster");
            settings.setIndexId("ogc-statistics-index");
            settings.setTypeId("mytype");

            _INSTANCE.init(settings);
        }
        return _INSTANCE;
    }

    private Node node;

    private Client client;

    private ElasticSearchSettings settings;

    public ElasticSearchDataHandler() {
    }

    @Override
    public IndexResponse persist(Map<String, Object> dataMap,
            Class<?> clazz) throws Exception
    {
        if (client == null) {
            // TODO change it iceland-exception-like exception
            throw new Exception("Client is not initialized. Data will not be persisted.");
        }
        if (!settings.isLoggingEnabled()) {
            return null;
        }
        dataMap.put(SosDataMapping.TIMESTAMP_FIELD, Calendar.getInstance(DateTimeZone.UTC.toTimeZone()));
        logger.debug("Persisting {}", dataMap);
        IndexResponse response = client.prepareIndex(settings.getIndexId(), settings.getTypeId()).setSource(dataMap).setOperationThreaded(false).get();
        return response;
    }

    @Override
    public IndexResponse persist(Map<String, Object> dataMap) throws Exception
    {
        return persist(dataMap, null);
    }

    // -------- ADMIN INTERFACE ------------//
    @Override
    public void createSchema()
    {
        IndicesAdminClient indices = client.admin().indices();

        // TODO this ugliness will be extracted during another user story
        Map<String, Object> stringField = new HashMap<String, Object>();
        stringField.put("type", "string");
        stringField.put("index", "not_analyzed");

        Map<String, Object> dateField = new HashMap<String, Object>();
        stringField.put("type", "date");

        Map<String, Object> geoPointField = new HashMap<String, Object>();
        geoPointField.put("type", "geo_point");

        Map<String, Object> properties = new HashMap<String, Object>();
        Map<String, Object> mapping = new HashMap<String, Object>();
        Map<String, Object> subproperties = new HashMap<String, Object>();
        Map<String, Object> submap = new HashMap<String, Object>();

        properties.put("properties", mapping);

        mapping.put(SosDataMapping.GEO_LOC_FIELD, subproperties);

        subproperties.put("properties", submap);
        submap.put(SosDataMapping.GEO_LOC_CITY_CODE, stringField);
        submap.put(SosDataMapping.GEO_LOC_COUNTRY_CODE, stringField);
        submap.put(SosDataMapping.GEO_LOC_GEOPOINT, geoPointField);

        if (indices.prepareExists(settings.getIndexId()).get().isExists()) {
            logger.info("Index {} already exists", settings.getIndexId());

            // update mapping
            PutMappingResponse resp = indices.preparePutMapping(settings.getIndexId()).setType(settings.getTypeId()).setIgnoreConflicts(true).setSource(properties).get();
            resp.toString();
        } else {
            // create new
            logger.info("Index {} not exists creating a new one now.", settings.getIndexId());

            CreateIndexResponse resp = indices.prepareCreate(settings.getIndexId()).addMapping(settings.getTypeId(), properties).get();

        }

    }

    private void createMetadataType()
    {
        Map<String, Object> stringField = new HashMap<String, Object>();
        stringField.put("type", "string");
        stringField.put("index", "not_analyzed");
        Map<String, Object> dateField = new HashMap<String, Object>();
        stringField.put("type", "date");

        Map<String, Object> properties = new HashMap<String, Object>();
        Map<String, Object> mapping = new HashMap<String, Object>();

        properties.put("properties", mapping);
        mapping.put("version", stringField);
        mapping.put("creation_date", dateField);
        mapping.put("modification_date", dateField);

        IndicesAdminClient indices = client.admin().indices();
        indices.prepareCreate(settings.getTypeId()).addMapping(METADATA_TYPE_NAME, properties);
    }

    @Override
    public void deleteIndex(String index)
    {
        client.admin().indices().prepareDelete(index).get();
    }

    @Override
    public Client getClient()
    {
        return client;
    }

    @Override
    public void init(ElasticSearchSettings settings)
    {
        this.settings = settings;
        logger.info("Initializing ElasticSearch Statatistics connection");
        logger.info("Settings {}", settings.toString());

        Objects.requireNonNull(settings.getIndexId());
        Objects.requireNonNull(settings.getTypeId());

        if (settings.isLoggingEnabled()) {
            if (settings.isInLan()) {
                initLanMode(settings);
            } else {
                initRemoteMode(settings);
            }

            // TODO should check database version in metadata and update
            createSchema();
        } else {
            logger.info("Statistics collection is not enabled. Data will not will be collected.");
        }

    }

    /**
     * Starts client mode in local LAN mode.
     */
    private void initLanMode(ElasticSearchSettings settings)
    {
        Objects.requireNonNull(settings.getClusterName());
        node = NodeBuilder.nodeBuilder().client(true).clusterName(settings.getClusterName()).node();
        client = node.client();
        logger.info("ElasticSearch data handler starting in LAN mode");

    }

    /**
     * Starts client mode in {@link TransportClient} remote mode
     */
    private void initRemoteMode(ElasticSearchSettings settings)
    {
        Objects.requireNonNull(settings.getAddress());
        logger.info("ElasticSearch data handler starting in Remote mode");
        throw new UnsupportedOperationException();
    }

    @PreDestroy
    public void close()
    {
        if (!node.isClosed()) {
            logger.info("Closing ElasticSearch data handler");
            node.close();
        }
    }

    @Override
    public boolean isLoggingEnabled()
    {
        return settings.isLoggingEnabled();
    }
}
