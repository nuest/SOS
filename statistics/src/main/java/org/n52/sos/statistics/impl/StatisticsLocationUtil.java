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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Singleton;

import org.elasticsearch.common.geo.GeoPoint;
import org.n52.sos.request.RequestContext;
import org.n52.sos.statistics.api.interfaces.IAdminStatisticsLocation;
import org.n52.sos.statistics.api.interfaces.IStatisticsLocationUtil;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.n52.sos.util.net.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;

/**
 * Utility class for mapping objects to Elasticsearch specific Geolocation type
 * objects
 * 
 */

@Singleton
public class StatisticsLocationUtil implements IStatisticsLocationUtil, IAdminStatisticsLocation {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsLocationUtil.class);

    private LocationDatabaseType dbType;

    private DatabaseReader reader;

    @Override
    public Map<String, Object> ip2SpatialData(IPAddress ip)
    {
        if (ip == null) {
            return null;
        }

        if (reader == null) {
            logger.warn("Location database is not initialized. Exiting.");
            return null;
        }

        try {
            Map<String, Object> holder = new HashMap<>();
            if (dbType == LocationDatabaseType.COUNTRY) {
                Country country = reader.country(ip.asInetAddress()).getCountry();
                holder.put(SosDataMapping.GEO_LOC_COUNTRY_CODE, country.getIsoCode());
            } else {
                CityResponse city = reader.city(ip.asInetAddress());
                Location loc = city.getLocation();
                holder.put(SosDataMapping.GEO_LOC_COUNTRY_CODE, city.getCountry().getIsoCode());
                holder.put(SosDataMapping.GEO_LOC_CITY_CODE, city.getCity().getName());
                holder.put(SosDataMapping.GEO_LOC_GEOPOINT, new GeoPoint(loc.getLatitude(), loc.getLongitude()));
            }
            return holder;
        } catch (Throwable e) {
            logger.error("Can't convert IP to GeoIp", e);
        }
        return null;
    }

    /**
     * Resolves source {@link IPAddress} if there were a proxy get the original
     * address
     * 
     * @param ctx
     *            holder of the address
     * @return caller source address
     */
    @Override
    public IPAddress resolveOriginalIpAddress(RequestContext ctx)
    {
        if (ctx.getForwardedForChain().isPresent()) {
            return ctx.getForwardedForChain().get().getOrigin();
        } else {
            return ctx.getIPAddress().orNull();
        }
    }

    @Override
    public void init(LocationDatabaseType type,
            String pathToDatabase)
    {
        Objects.requireNonNull(type);
        Objects.requireNonNull(pathToDatabase);
        logger.info("Init {} as type {} with file {}", getClass().toString(), type.toString(), pathToDatabase);
        dbType = type;
        try {
            File f = ResourceUtils.getFile(pathToDatabase);
            if (f == null) {
                logger.error("couldn't find file {}", pathToDatabase);
                return;
            }

            reader = new DatabaseReader.Builder(f).fileMode(FileMode.MEMORY_MAPPED).build();

            // mismatch
            if (!type.getGeoLite2Name().equals(reader.getMetadata().getDatabaseType())) {
                logger.error("DatabaseType {} not match with the databasefile {}. Exiting", type.toString(), pathToDatabase);
                close();
                return;
            }
        } catch (Throwable e) {
            logger.error("Couldn't initation geolocation database ", e);
            reader = null;
        }
    }

    @Override
    public void close()
    {
        try {
            reader.close();
        } catch (IOException e) {
            logger.error("Error during closing GeoLite reader", e);
        }
        reader = null;
    }
}
