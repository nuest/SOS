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

import java.util.Map;
import java.util.Objects;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.n52.sos.statistics.api.ElasticSearchSettings;
import org.n52.sos.statistics.api.interfaces.IAdminDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ElasticSearchDataHandler implements IStatisticsDataHandler, IAdminDataHandler {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchDataHandler.class);

    private Node node;

    private Client client;

    private ElasticSearchSettings settings;

    public ElasticSearchDataHandler() {
        // TODO load it from config file
        settings = new ElasticSearchSettings(true);
        settings.setClusterName("embedded-cluster");
        settings.setIndexId("myindex");
        settings.setTypeId("mytype");
        init(settings);
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

        logger.debug("Persisting {}", dataMap);
        IndexResponse response = client.prepareIndex(settings.getIndexId(), settings.getTypeId()).setSource(dataMap).setOperationThreaded(false).get();
        return response;
    }

    @Override
    public IndexResponse persist(Map<String, Object> dataMap) throws Exception
    {
        return persist(dataMap, null);
    }

    @Override
    public void deleteIndex(String index)
    {
        client.admin().indices().prepareDelete(index).get();
    }

    @Override
    public void createSchema()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client getClient()
    {
        return client;
    }

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
}
