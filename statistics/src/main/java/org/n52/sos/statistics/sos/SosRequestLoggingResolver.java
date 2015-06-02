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
package org.n52.sos.statistics.sos;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.request.GetCapabilitiesRequest;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsLoggingResolver;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.sos.resolvers.DescribeSensorRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetCapabilitiesRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetObservationRequestResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosRequestLoggingResolver implements IStatisticsLoggingResolver {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchDataHandler.class);

    @Inject
    private IStatisticsDataHandler dataHandler;

    // ------------- Injected resolvers ----------------
    @Inject
    private GetCapabilitiesRequestResolver getCapabilitiesResolver;

    @Inject
    private GetObservationRequestResolver getObservationResolver;

    @Inject
    private DescribeSensorRequestResolver describeSensorResolver;

    private AbstractServiceRequest<?> request;

    public SosRequestLoggingResolver(AbstractServiceRequest<?> request) {
        this.request = request;
    }

    public SosRequestLoggingResolver() {
        this(null);
    }

    @Override
    public void run()
    {
        Objects.requireNonNull(request);
        resolve(request);
    }

    private void resolve(AbstractServiceRequest<?> request)
    {
        Map<String, Object> dataMap = null;
        if (request instanceof GetCapabilitiesRequest) {
            // GetCapabilities
            dataMap = getCapabilitiesResolver.init((GetCapabilitiesRequest) request).resolveAsMap();
        } else if (request instanceof DescribeSensorRequest) {
            // DescribeSensor
            dataMap = describeSensorResolver.init((DescribeSensorRequest) request).resolveAsMap();
        } else if (request instanceof GetObservationRequest) {
            // GetObservation
            dataMap = getObservationResolver.init((GetObservationRequest) request).resolveAsMap();
        } else {
            logger.error("No mapping found for the request {}", request.getClass().getName());
            return;
        }

        try {
            dataHandler.persist(dataMap);
        } catch (Throwable e) {
            logger.error("Can't persist request", e);
        }
    }

    public AbstractServiceRequest<?> getRequest()
    {
        return request;
    }

    public void setRequest(AbstractServiceRequest<?> request)
    {
        this.request = request;
    }

}
