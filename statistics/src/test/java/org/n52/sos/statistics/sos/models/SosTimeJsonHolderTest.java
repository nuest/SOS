package org.n52.sos.statistics.sos.models;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;

public class SosTimeJsonHolderTest {

    @Test
    public void timeInstant()
    {
        TimeInstant instant = new TimeInstant(DateTime.now());
        SosTimeJsonHolder ret = SosTimeJsonHolder.convert(instant);

        Assert.assertEquals(instant.getValue(), ret.getTimeInstant());
    }

    @Test
    public void timePeriodWithDuration()
    {
        TimePeriod period = new TimePeriod(DateTime.now(), DateTime.now().plusHours(3));
        Period duration = Period.seconds(3 * 60 * 60);
        period.setDuration(duration);
        SosTimeJsonHolder ret = SosTimeJsonHolder.convert(period);

        Assert.assertEquals(period.getStart(), ret.getStart());
        Assert.assertEquals(period.getEnd(), ret.getEnd());
        Assert.assertEquals((Integer) period.getDuration().getSeconds(), ret.getDuration());
    }

    @Test
    public void timePeriodNoDuration()
    {
        TimePeriod period = new TimePeriod(DateTime.now(), DateTime.now().plusHours(3));
        SosTimeJsonHolder ret = SosTimeJsonHolder.convert(period);

        Assert.assertEquals(period.getStart(), ret.getStart());
        Assert.assertEquals(period.getEnd(), ret.getEnd());
        Assert.assertEquals(Integer.valueOf(3 * 60 * 60), ret.getDuration());
    }

    @Test
    public void invalidStartEndTimePeriod()
    {
        TimePeriod period = new TimePeriod(DateTime.now().plusHours(3), DateTime.now());
        SosTimeJsonHolder ret = SosTimeJsonHolder.convert(period);

        Assert.assertEquals(period.getStart(), ret.getStart());
        Assert.assertEquals(period.getEnd(), ret.getEnd());
        Assert.assertNull(period.getDuration());
    }
}
