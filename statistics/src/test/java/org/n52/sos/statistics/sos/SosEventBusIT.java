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

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.event.SosEventBus;
import org.n52.sos.event.events.RequestEvent;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.request.RequestContext;
import org.n52.sos.statistics.ElasticSearchAwareTest;
import org.n52.sos.util.net.IPAddress;
import org.springframework.beans.factory.annotation.Autowired;

public class SosEventBusIT extends ElasticSearchAwareTest {

    @Autowired
    private SosStatisticsLoggerListener listener;

    @Test
    public void sendSosEventToElasticSearch() throws InterruptedException
    {
        ROLLBACK = false;

        RequestContext ctx = new RequestContext();
        ctx.setIPAddress(new IPAddress("241.56.199.99"));

        DescribeSensorRequest request = new DescribeSensorRequest();
        request.setRequestContext(ctx);
        request.setProcedure("http://www.test.ru/producer1");
        request.setService("sos");
        request.setVersion("10.1");
        request.setProcedureDescriptionFormat("my-format");

        RequestEvent evt = new RequestEvent(request);

        SosEventBus.getInstance().register(listener);
        SosEventBus.fire(evt);

        // wait for the other thread to stop, hopefully
        Thread.sleep(3000);

        SearchResponse response = getEmbeddedClient().prepareSearch(settings.getIndexId()).setTypes(settings.getTypeId()).get();

        logger.debug(response.toString());
        SearchHit hit = response.getHits().getAt(0);
        Assert.assertNotNull(hit);
        Assert.assertThat(hit.sourceAsMap().values(), CoreMatchers.hasItem(request.getOperationName()));

    }
}
