package custom;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.sos.statistics.api.interfaces.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.sos.statistics.impl.ElasticSearchDataHandler;
import org.n52.sos.statistics.impl.StatisticsLocationUtil;
import org.n52.sos.statistics.sos.SosDataMapping;
import org.n52.sos.util.net.IPAddress;

public class ElasticSearchGeoPoint {

    static ElasticSearchDataHandler handler = ElasticSearchDataHandler.getInstance();

    static StatisticsLocationUtil loc = new StatisticsLocationUtil();

    @BeforeClass
    public static void init()
    {
        loc.init(LocationDatabaseType.CITY, "classpath:geolite/city.mmdb");
    }

    @Test
    public void addGeoPointToDatabase() throws Exception
    {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            IPAddress nasa = new IPAddress("217.20.130.99");
            Map<String, Object> geomap = loc.ip2SpatialData(nasa);
            data.put(SosDataMapping.GEO_LOC_FIELD, geomap);

            handler.persist(data);
        }
    }

    // @Test
    public void getMapping()
    {
        GetMappingsResponse resp = handler.getClient().admin().indices().prepareGetMappings("myindex").get();
        // prints out the index
        System.err.println(resp.mappings().keys());
        // values = a type
        System.err.println(resp.mappings().values().iterator().next().value.values());
    }

    @AfterClass
    public static void down()
    {
        loc.close();
        handler.close();
    }

}
