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

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.iceland.util.net.IPAddress;
import org.n52.sos.statistics.api.interfaces.geolocation.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.sos.statistics.api.parameters.ObjectEsParameterFactory;

public class StatisticsLocationUtilTest {

    private static String countryDb = null;
    private static String cityDb = null;
    private static StatisticsLocationUtil loc = new StatisticsLocationUtil();

    @BeforeClass
    public static void setUp() throws URISyntaxException {
        countryDb = new File(StatisticsLocationUtilTest.class.getResource("/geolite/country.mmdb").toURI()).getAbsolutePath();
        cityDb = new File(StatisticsLocationUtilTest.class.getResource("/geolite/city.mmdb").toURI()).getAbsolutePath();
    }

    @Test
    public void getIpAsCountry() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.COUNTRY, countryDb);
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);
        Assert.assertEquals("US", map.get(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName()));
    }

    @Test
    public void getIpAsCity() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.CITY, cityDb);
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);

        Assert.assertNotNull(map);
        Assert.assertEquals("US", map.get(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName()));
        Assert.assertNotNull(map.get(ObjectEsParameterFactory.GEOLOC_CITY_NAME.getName()));
        Assert.assertNotNull(map.get(ObjectEsParameterFactory.GEOLOC_GEO_POINT.getName()));
    }

    @Test
    public void wrongDatabaseAndType() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.CITY, countryDb);
        IPAddress ip = new IPAddress("67.20.172.183");
        Assert.assertNull(loc.ip2SpatialData(ip));
        // no harm done
    }

    @Test
    public void localhostIPAsCountry() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.COUNTRY, countryDb);
        IPAddress ip = new IPAddress("127.0.0.1");
        Assert.assertNull(loc.ip2SpatialData(ip));
    }

}