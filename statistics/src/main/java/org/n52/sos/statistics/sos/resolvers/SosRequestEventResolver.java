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
    private GetCapabilitiesRequestHandler getCapabilitiesResolver = new GetCapabilitiesRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationRequestHandler getObservationRequestResolver = new GetObservationRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private DescribeSensorRequestHandler describeSensorResolver = new DescribeSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private DeleteSensorRequestHandler deleteSensorRequestResolver = new DeleteSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetDataAvailabilityRequestHandler getDataAvailabilityRequestResolver = new GetDataAvailabilityRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetFeatureOfInterestRequestHandler getFeatureOfInterestRequestResolver = new GetFeatureOfInterestRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetObservationByIdRequestHandler getObservationByIdRequestResolver = new GetObservationByIdRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultRequestHandler getResultRequestResolver = new GetResultRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private GetResultTemplateRequestHandler getResultTemplateRequestResolver = new GetResultTemplateRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertObservationRequestHandler insertObservationRequestResolver = new InsertObservationRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultRequestHandler insertResultRequestResolver = new InsertResultRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertResultTemplateRequestHandler insertResultTemplateRequestResolver = new InsertResultTemplateRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private InsertSensorRequestHandler insertSensorRequestResolver = new InsertSensorRequestHandler();

    // FIXME remove new object in DI environment
    @Inject
    private UpdateSensorRequestHandler updateSensorRequestResolver = new UpdateSensorRequestHandler();

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
