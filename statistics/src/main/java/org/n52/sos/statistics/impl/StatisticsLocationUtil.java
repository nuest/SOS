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

import javax.inject.Singleton;

import org.elasticsearch.common.geo.GeoPoint;
import org.n52.sos.request.RequestContext;
import org.n52.sos.statistics.api.interfaces.IStatisticsLocationUtil;
import org.n52.sos.util.net.IPAddress;

/**
 * Utility class for mapping objects to Elasticsearch specific Geolocation type
 * objects
 * 
 */

@Singleton
public class StatisticsLocationUtil implements IStatisticsLocationUtil {

    @Override
    public GeoPoint ip2GeoPoint(IPAddress ip)
    {
        if (ip == null) {
            return null;
        }
        // TODO load it from database
        return new GeoPoint(33.812092, -117.918974);
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
}
