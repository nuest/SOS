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

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

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
import org.n52.sos.statistics.api.interfaces.IStatisticsDataHandler;
import org.n52.sos.statistics.api.interfaces.IStatisticsEventResolver;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.sos.handlers.requests.DeleteSensorRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.DescribeSensorRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetCapabilitiesRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetDataAvailabilityRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetFeatureOfInterestRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetObservationByIdRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetObservationRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetResultRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.GetResultTemplateRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.InsertObservationRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.InsertResultRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.InsertResultTemplateRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.InsertSensorRequestHandler;
import org.n52.sos.statistics.sos.handlers.requests.UpdateSensorRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class SosRequestEventResolver implements IStatisticsEventResolver {

    private static final Logger logger = LoggerFactory.getLogger(SosRequestEventResolver.class);

    // FIXME remove new object in DI environment
    @Inject
    private IStatisticsDataHandler dataHandler = ElasticSearchDataHandler.getInstance();

    // ------------- Injected resolvers ----------------
    // FIXME remove new object in DI environment
    @Inject
    private GetCapabilitiesRequestHandler getCapabilitiesHandler = new GetCapabilitiesRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationRequestHandler getObservationRequestHandler = new GetObservationRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private DescribeSensorRequestHandler describeSensorRequestHandler = new DescribeSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private DeleteSensorRequestHandler deleteSensorRequestHandler = new DeleteSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetDataAvailabilityRequestHandler getDataAvailabilityRequestHandler = new GetDataAvailabilityRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetFeatureOfInterestRequestHandler getFeatureOfInterestRequestHandler = new GetFeatureOfInterestRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationByIdRequestHandler getObservationByIdRequestHandler = new GetObservationByIdRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultRequestHandler getResultRequestHandler = new GetResultRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultTemplateRequestHandler getResultTemplateRequestHandler = new GetResultTemplateRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertObservationRequestHandler insertObservationRequestHandler = new InsertObservationRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultRequestHandler insertResultRequestHandler = new InsertResultRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultTemplateRequestHandler insertResultTemplateRequestHandler = new InsertResultTemplateRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertSensorRequestHandler insertSensorRequestHandler = new InsertSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private UpdateSensorRequestHandler updateSensorRequestHandler = new UpdateSensorRequestHandler();

    private AbstractServiceRequest<?> request;

    public SosRequestEventResolver() {
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
            dataMap = deleteSensorRequestHandler.resolveAsMap((DeleteSensorRequest) request);
        } else if (request instanceof DescribeSensorRequest) {
            // DescribeSensor
            dataMap = describeSensorRequestHandler.resolveAsMap((DescribeSensorRequest) request);
        } else if (request instanceof GetCapabilitiesRequest) {
            // GetCapabilities
            dataMap = getCapabilitiesHandler.resolveAsMap((GetCapabilitiesRequest) request);
        } else if (request instanceof GetDataAvailabilityRequest) {
            // GetDataAvailabilityRequest
            dataMap = getDataAvailabilityRequestHandler.resolveAsMap((GetDataAvailabilityRequest) request);
        } else if (request instanceof GetFeatureOfInterestRequest) {
            // GetFeatureOfInterestRequest
            dataMap = getFeatureOfInterestRequestHandler.resolveAsMap((GetFeatureOfInterestRequest) request);
        } else if (request instanceof GetObservationByIdRequest) {
            // GetObservationByIdRequest
            dataMap = getObservationByIdRequestHandler.resolveAsMap((GetObservationByIdRequest) request);
        } else if (request instanceof GetObservationRequest) {
            // GetObservationRequest
            dataMap = getObservationRequestHandler.resolveAsMap((GetObservationRequest) request);
        } else if (request instanceof GetResultRequest) {
            // GetResultRequest
            dataMap = getResultRequestHandler.resolveAsMap((GetResultRequest) request);
        } else if (request instanceof GetResultTemplateRequest) {
            // GetResultTemplateRequest
            dataMap = getResultTemplateRequestHandler.resolveAsMap((GetResultTemplateRequest) request);
        } else if (request instanceof InsertObservationRequest) {
            // InsertObservationRequest
            dataMap = insertObservationRequestHandler.resolveAsMap((InsertObservationRequest) request);
        } else if (request instanceof InsertResultRequest) {
            // InsertResultRequest
            dataMap = insertResultRequestHandler.resolveAsMap((InsertResultRequest) request);
        } else if (request instanceof InsertResultTemplateRequest) {
            // InsertResultTemplateRequest
            dataMap = insertResultTemplateRequestHandler.resolveAsMap((InsertResultTemplateRequest) request);
        } else if (request instanceof InsertSensorRequest) {
            // InsertSensorRequest
            dataMap = insertSensorRequestHandler.resolveAsMap((InsertSensorRequest) request);
        } else if (request instanceof UpdateSensorRequest) {
            // UpdateSensorRequest
            dataMap = updateSensorRequestHandler.resolveAsMap((UpdateSensorRequest) request);
        } else {
            logger.warn("No mapping found for the request {}", request.getClass().getName());
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
