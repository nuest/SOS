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
package org.n52.sos.statistics.sos.models;

import java.util.Map;

import org.joda.time.DateTime;
import org.n52.sos.ogc.filter.TemporalFilter;
import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.statistics.api.AbstractElasticSearchDataHolder;

public class SosTimeJsonHolder extends AbstractElasticSearchDataHolder implements IJsonConverter {

    private DateTime timeInstant = null;

    private DateTime start = null;

    private DateTime end = null;

    private Integer duration = null;

    private String timeOperator;

    private SosTimeJsonHolder() {
    }

    public static SosTimeJsonHolder convert(Time time)
    {
        if (time == null) {
            return null;
        }

        SosTimeJsonHolder o = new SosTimeJsonHolder();
        if (time instanceof TimeInstant) {
            o.timeInstant = ((TimeInstant) time).getValue();

        } else if (time instanceof TimePeriod) {
            TimePeriod p = (TimePeriod) time;

            if (p.getDuration() == null) {
                if (p.getStart() != null && p.getEnd() != null) {
                    if (p.getEnd().compareTo(p.getStart()) >= 0) {
                        o.duration = (int) ((p.getEnd().getMillis() - p.getStart().getMillis()) / 1000);
                    }
                }
            } else {
                if (p.getDuration().getSeconds() != 0) {
                    o.duration = p.getDuration().getSeconds();
                }
            }

            if (p.getStart() != null) {
                o.start = p.getStart();
            }
            if (p.getEnd() != null) {
                o.end = p.getEnd();
            }
        }
        return o;
    }

    public static SosTimeJsonHolder convert(TemporalFilter temporalFilter)
    {
        SosTimeJsonHolder json = convert(temporalFilter.getTime());
        json.timeOperator = temporalFilter.getOperator().toString();
        return json;
    }

    @Override
    public Map<String, Object> getAsMap()
    {
        put("duration", duration);
        put("start", start);
        put("end", end);
        put("timeInstant", timeInstant);
        return dataMap;
    }

    public DateTime getTimeInstant()
    {
        return timeInstant;
    }

    public DateTime getStart()
    {
        return start;
    }

    public DateTime getEnd()
    {
        return end;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public String getTimeOperator()
    {
        return timeOperator;
    }

}
