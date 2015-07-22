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
package org.n52.sos.statistics.api.utils;

import java.io.IOException;
import java.util.Objects;

import org.elasticsearch.client.Client;
import org.n52.sos.statistics.api.utils.dto.KibanaConfigEntryDto;
import org.n52.sos.statistics.api.utils.dto.KibanaConfigHolderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KibanaImporter {

    private static Logger logger = LoggerFactory.getLogger(KibanaImporter.class);

    private final Client client;
    private final String kibanaIndexName;
    private final String statisticsIndexName;

    public KibanaImporter(Client client, String kibanaIndexName,String statisticsIndexName) {
    	Objects.requireNonNull(client);
        Objects.requireNonNull(kibanaIndexName);
        Objects.requireNonNull(statisticsIndexName);
        
        this.kibanaIndexName = kibanaIndexName;
        this.statisticsIndexName = statisticsIndexName;
        this.client = client;
    }

    public void importJson(String jsonString) throws JsonParseException, JsonMappingException, IOException {
        Objects.requireNonNull(jsonString);

        ObjectMapper mapper = new ObjectMapper();
        KibanaConfigHolderDto holder = mapper.readValue(jsonString, KibanaConfigHolderDto.class);

        for (KibanaConfigEntryDto dto : holder.getEntries()) {
            logger.debug("Importing {}", dto);
            client.prepareIndex(kibanaIndexName, dto.getType(), dto.getId()).setSource(dto.getSource()).setOperationThreaded(false).get();
        }
    }
    
    private String transformIndexName(KibanaConfigEntryDto dto) {
    	throw new UnsupportedOperationException();    	
    }
}