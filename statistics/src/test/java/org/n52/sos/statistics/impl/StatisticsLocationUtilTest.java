package org.n52.sos.statistics.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.statistics.api.interfaces.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.n52.sos.util.net.IPAddress;

public class StatisticsLocationUtilTest {

    @Test
    public void getIpAsCountry()
    {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.init(LocationDatabaseType.COUNTRY, "classpath:geolite/GeoLite2-Country.mmdb");
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);
        Assert.assertEquals("US", map.get(SosDataMapping.GEO_LOC_COUNTRY_CODE));
    }

    @Test
    public void getIpAsCity()
    {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.init(LocationDatabaseType.CITY, "classpath:geolite/GeoLite2-City.mmdb");
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);
        Assert.assertEquals("US", map.get(SosDataMapping.GEO_LOC_COUNTRY_CODE));
    }

    @Test
    public void wrongDatabaseAndType()
    {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.init(LocationDatabaseType.CITY, "classpath:geolite/GeoLite2-Country.mmdb");
        IPAddress ip = new IPAddress("67.20.172.183");
        Assert.assertNull(loc.ip2SpatialData(ip));
        // no harm done
    }
}
