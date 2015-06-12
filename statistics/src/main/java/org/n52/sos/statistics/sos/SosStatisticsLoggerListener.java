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

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.n52.sos.event.SosEvent;
import org.n52.sos.event.SosEventListener;
import org.n52.sos.event.events.ExceptionEvent;
import org.n52.sos.event.events.RequestEvent;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.statistics.sos.resolvers.SosExceptionEventResolver;
import org.n52.sos.statistics.sos.resolvers.SosRequestEventResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

public class SosStatisticsLoggerListener implements SosEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SosStatisticsLoggerListener.class);

    private static final int THREAD_POOL_SIZE = 2;

    private final ExecutorService executorService;

    private static final Set<Class<? extends SosEvent>> EVENT_TYPES = ImmutableSet.<Class<? extends SosEvent>> of(RequestEvent.class, ExceptionEvent.class);

    public SosStatisticsLoggerListener() {
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    @Override
    public Set<Class<? extends SosEvent>> getTypes()
    {
        return EVENT_TYPES;
    }

    @Override
    public void handle(SosEvent sosEvent)
    {
        logger.debug("Event received: {}", sosEvent);
        // TODO if logging is disabled exit here
        // get the ElasticSearchDataHandler#settings#isEnabled
        try {
            if (sosEvent instanceof RequestEvent) {
                // TODO should be injecting a new instance from spring container
                // through a factory
                SosRequestEventResolver resolver = new SosRequestEventResolver();

                AbstractServiceRequest<?> request = ((RequestEvent) sosEvent).getRequest();
                resolver.setRequest(request);

                executorService.execute(resolver);
            } else if (sosEvent instanceof ExceptionEvent) {
                SosExceptionEventResolver resolver = new SosExceptionEventResolver();
                resolver.setException(((ExceptionEvent) sosEvent).getException());

                executorService.execute(resolver);
            } else {
                logger.trace("Unssupported type of event: {}", sosEvent.getClass());
                // TODO add a "catch all" event resolvert that simply logs the name of the event and when it occured
            }
        } catch (Throwable e) {
            logger.error("Can't handle event for statistics logging: {}", sosEvent, e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.executorService.shutdown();
    }
    
    

}
