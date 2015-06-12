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
package custom;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.sos.statistics.api.interfaces.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.impl.StatisticsLocationUtil;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.n52.sos.util.net.IPAddress;

public class ElasticSearchGeoPoint {

    static ElasticSearchDataHandler handler = ElasticSearchDataHandler.getInstance();

    static StatisticsLocationUtil loc = new StatisticsLocationUtil();

    @BeforeClass
    public static void init()
    {
        loc.init(LocationDatabaseType.CITY, "classpath:geolite/city.mmdb");
    }

    @Test
    public void addGeoPointToDatabase() throws Exception
    {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            IPAddress nasa = new IPAddress("217.20.130.99");
            Map<String, Object> geomap = loc.ip2SpatialData(nasa);
            data.put(SosDataMapping.GEO_LOC_FIELD, geomap);

            handler.persist(data);
        }
    }

    // @Test
    public void getMapping()
    {
        GetMappingsResponse resp = handler.getClient().admin().indices().prepareGetMappings("myindex").get();
        // prints out the index
        System.err.println(resp.mappings().keys());
        // values = a type
        System.err.println(resp.mappings().values().iterator().next().value.values());
    }

    @AfterClass
    public static void down()
    {
        loc.close();
        handler.close();
    }

}
