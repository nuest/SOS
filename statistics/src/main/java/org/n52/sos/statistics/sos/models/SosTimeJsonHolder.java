package org.n52.sos.statistics.sos.models;

import java.util.Map;

import org.joda.time.DateTime;
import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.statistics.api.AbstractElasticSearchDataHolder;

public class SosTimeJsonHolder extends AbstractElasticSearchDataHolder implements IJsonConverter {

    private DateTime timeInstant;

    private DateTime start;

    private DateTime end;

    private Integer duration;

    private SosTimeJsonHolder() {
    }

    public static SosTimeJsonHolder convert(Time time)
    {
        SosTimeJsonHolder o = new SosTimeJsonHolder();
        if (time instanceof TimeInstant) {
            o.timeInstant = ((TimeInstant) time).getValue();

        } else if (time instanceof TimePeriod) {
            TimePeriod p = (TimePeriod) time;

            if (p.getDuration().getSeconds() != 0) {
                o.duration = p.getDuration().getSeconds();
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

    @Override
    public Map<String, Object> getAsMap()
    {
        put("duration", duration);
        put("start", start);
        put("end", end);
        put("timeInstant", timeInstant);
        return dataMap;
    }

}
