package org.n52.sos.statistics.sos.models;

import java.util.Map;
import java.util.Objects;

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
        Objects.requireNonNull(time);

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
