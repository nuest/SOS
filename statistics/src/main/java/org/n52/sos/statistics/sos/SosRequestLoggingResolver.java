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

import org.n52.sos.gda.GetDataAvailabilityRequest;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.request.DeleteSensorRequest;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.request.GetCapabilitiesRequest;
import org.n52.sos.request.GetFeatureOfInterestRequest;
import org.n52.sos.request.GetObservationByIdRequest;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.request.GetResultRequest;
import org.n52.sos.request.GetResultTemplateRequest;
import org.n52.sos.request.InsertObservationRequest;
import org.n52.sos.request.InsertResultRequest;
import org.n52.sos.request.InsertResultTemplateRequest;
import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.request.UpdateSensorRequest;
import org.n52.sos.statistics.api.ElasticSearchSettings;
import org.n52.sos.statistics.api.interfaces.IAdminDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsLoggingResolver;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.sos.resolvers.DeleteSensorRequestResolver;
import org.n52.sos.statistics.sos.resolvers.DescribeSensorRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetCapabilitiesRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetDataAvailabilityRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetFeatureOfInterestRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetObservationByIdRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetObservationRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetResultRequestResolver;
import org.n52.sos.statistics.sos.resolvers.GetResultTemplateRequestResolver;
import org.n52.sos.statistics.sos.resolvers.InsertObservationRequestResolver;
import org.n52.sos.statistics.sos.resolvers.InsertResultRequestResolver;
import org.n52.sos.statistics.sos.resolvers.InsertResultTemplateRequestResolver;
import org.n52.sos.statistics.sos.resolvers.InsertSensorRequestResolver;
import org.n52.sos.statistics.sos.resolvers.UpdateSensorRequestResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosRequestLoggingResolver implements IStatisticsLoggingResolver {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchDataHandler.class);

    // FIXME remove new object in DI environment
    @Inject
    private IStatisticsDataHandler dataHandler = new ElasticSearchDataHandler();

    // ------------- Injected resolvers ----------------
    // FIXME remove new object in DI environment
    @Inject
    private GetCapabilitiesRequestResolver getCapabilitiesResolver = new GetCapabilitiesRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationRequestResolver getObservationRequestResolver = new GetObservationRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private DescribeSensorRequestResolver describeSensorResolver = new DescribeSensorRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private DeleteSensorRequestResolver deleteSensorRequestResolver = new DeleteSensorRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetDataAvailabilityRequestResolver getDataAvailabilityRequestResolver = new GetDataAvailabilityRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetFeatureOfInterestRequestResolver getFeatureOfInterestRequestResolver = new GetFeatureOfInterestRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationByIdRequestResolver getObservationByIdRequestResolver = new GetObservationByIdRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultRequestResolver getResultRequestResolver = new GetResultRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultTemplateRequestResolver getResultTemplateRequestResolver = new GetResultTemplateRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private InsertObservationRequestResolver insertObservationRequestResolver = new InsertObservationRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultRequestResolver insertResultRequestResolver = new InsertResultRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultTemplateRequestResolver insertResultTemplateRequestResolver = new InsertResultTemplateRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private InsertSensorRequestResolver insertSensorRequestResolver = new InsertSensorRequestResolver();

    // FIXME remove new object in DI environment
    @Inject
    private UpdateSensorRequestResolver updateSensorRequestResolver = new UpdateSensorRequestResolver();

    private AbstractServiceRequest<?> request;

    public SosRequestLoggingResolver() {
        // TODO remove if ES settings read with API
        ElasticSearchSettings settings = new ElasticSearchSettings(true);
        settings.setClusterName("embedded-cluster");
        settings.setIndexId("myindex");
        settings.setTypeId("mytype");
        ((IAdminDataHandler) dataHandler).init(settings);
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
        if (request instanceof DeleteSensorRequest) {
            // DeleteSensorRequest
            dataMap = deleteSensorRequestResolver.resolveAsMap((DeleteSensorRequest) request);
        } else if (request instanceof DescribeSensorRequest) {
            // DescribeSensor
            dataMap = describeSensorResolver.resolveAsMap((DescribeSensorRequest) request);
        } else if (request instanceof GetCapabilitiesRequest) {
            // GetCapabilities
            dataMap = getCapabilitiesResolver.resolveAsMap((GetCapabilitiesRequest) request);
        } else if (request instanceof GetDataAvailabilityRequest) {
            // GetDataAvailabilityRequest
            dataMap = getDataAvailabilityRequestResolver.resolveAsMap((GetDataAvailabilityRequest) request);
        } else if (request instanceof GetFeatureOfInterestRequest) {
            // GetFeatureOfInterestRequest
            dataMap = getFeatureOfInterestRequestResolver.resolveAsMap((GetFeatureOfInterestRequest) request);
        } else if (request instanceof GetObservationByIdRequest) {
            // GetObservationByIdRequest
            dataMap = getObservationByIdRequestResolver.resolveAsMap((GetObservationByIdRequest) request);
        } else if (request instanceof GetObservationRequest) {
            // GetObservationRequest
            dataMap = getObservationRequestResolver.resolveAsMap((GetObservationRequest) request);
        } else if (request instanceof GetResultRequest) {
            // GetResultRequest
            dataMap = getResultRequestResolver.resolveAsMap((GetResultRequest) request);
        } else if (request instanceof GetResultTemplateRequest) {
            // GetResultTemplateRequest
            dataMap = getResultTemplateRequestResolver.resolveAsMap((GetResultTemplateRequest) request);
        } else if (request instanceof InsertObservationRequest) {
            // InsertObservationRequest
            dataMap = insertObservationRequestResolver.resolveAsMap((InsertObservationRequest) request);
        } else if (request instanceof InsertResultRequest) {
            // InsertResultRequest
            dataMap = insertResultRequestResolver.resolveAsMap((InsertResultRequest) request);
        } else if (request instanceof InsertResultTemplateRequest) {
            // InsertResultTemplateRequest
            dataMap = insertResultTemplateRequestResolver.resolveAsMap((InsertResultTemplateRequest) request);
        } else if (request instanceof InsertSensorRequest) {
            // InsertSensorRequest
            dataMap = insertSensorRequestResolver.resolveAsMap((InsertSensorRequest) request);
        } else if (request instanceof UpdateSensorRequest) {
            // UpdateSensorRequest
            dataMap = updateSensorRequestResolver.resolveAsMap((UpdateSensorRequest) request);
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
