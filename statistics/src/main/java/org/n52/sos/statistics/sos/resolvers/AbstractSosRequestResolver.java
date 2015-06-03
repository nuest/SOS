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
package org.n52.sos.statistics.sos.resolvers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.statistics.api.StatisticsDataMapping;
import org.n52.sos.statistics.api.interfaces.IStatisticsLocationUtil;
import org.n52.sos.statistics.impl.StatisticsLocationUtil;
import org.n52.sos.util.net.IPAddress;

public abstract class AbstractSosRequestResolver<T extends AbstractServiceRequest<?>> implements IRequestResolver {

    protected final Map<String, Object> dataMap;

    protected T request;

    // FIXME remove new object in DI environment
    @Inject
    protected IStatisticsLocationUtil locationUtil = new StatisticsLocationUtil();

    protected AbstractSosRequestResolver() {
        dataMap = new HashMap<>();
    }

    public AbstractSosRequestResolver<?> init(T request)
    {
        this.request = request;
        // Global constants
        put(StatisticsDataMapping.TIMESTAMP_FIELD, Calendar.getInstance(DateTimeZone.UTC.toTimeZone()));
        put(StatisticsDataMapping.SERVICE_FIELD, request.getOperationKey().getService());
        put(StatisticsDataMapping.VERSION_FIELD, request.getOperationKey().getVersion());
        put(StatisticsDataMapping.OPERATION_NAME_FIELD, request.getOperationKey().getOperation());

        // requestcontext
        IPAddress ip = locationUtil.resolveOriginalIpAddress(request.getRequestContext());
        put(StatisticsDataMapping.IP_ADDRESS_FIELD, ip);
        put(StatisticsDataMapping.SOURCE_GEOLOC_FIELD, locationUtil.ip2GeoPoint(ip));
        put(StatisticsDataMapping.PROXIED_REQUEST_FIELD, request.getRequestContext().getForwardedForChain().isPresent());
        return this;
    }

    public Map<String, Object> put(String key,
            Object value)
    {
        if (key == null || value == null) {
            return dataMap;
        }
        dataMap.put(key, value);
        return dataMap;
    }
}
