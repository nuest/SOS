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
package org.n52.sos.statistics.sos.handlers.requests;

import java.util.Map;

import javax.inject.Inject;

import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.statistics.api.AbstractElasticSearchDataHolder;
import org.n52.sos.statistics.api.interfaces.IEventHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsLocationUtil;
import org.n52.sos.statistics.impl.StatisticsLocationUtil;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.n52.sos.util.net.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSosRequestHandler<T extends AbstractServiceRequest<?>> extends AbstractElasticSearchDataHolder implements IEventHandler<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractSosRequestHandler.class);

    protected T request;

    // FIXME remove new object in DI environment
    @Inject
    protected IStatisticsLocationUtil locationUtil = new StatisticsLocationUtil();

    private AbstractSosRequestHandler<?> init()
    {

        // Global constants
        put(SosDataMapping.SERVICE_FIELD, request.getOperationKey().getService());
        put(SosDataMapping.VERSION_FIELD, request.getOperationKey().getVersion());
        put(SosDataMapping.OPERATION_NAME_FIELD, request.getOperationKey().getOperation());

        // requestcontext
        IPAddress ip = locationUtil.resolveOriginalIpAddress(request.getRequestContext());
        put(SosDataMapping.IP_ADDRESS_FIELD, ip);
        put(SosDataMapping.SOURCE_GEOLOC_FIELD, locationUtil.ip2SpatialData(ip));
        put(SosDataMapping.PROXIED_REQUEST_FIELD, request.getRequestContext().getForwardedForChain().isPresent());
        return this;
    }

    @Override
    public Map<String, Object> resolveAsMap(T request)
    {
        this.request = request;
        init();
        resolveConcreteRequest();
        return dataMap;
    }

    protected abstract void resolveConcreteRequest();

}
