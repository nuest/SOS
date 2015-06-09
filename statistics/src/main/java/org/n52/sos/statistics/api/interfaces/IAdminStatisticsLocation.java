package org.n52.sos.statistics.api.interfaces;

public interface IAdminStatisticsLocation {
    /**
     * The country or the city database indicator.
     */
    public enum LocationDatabaseType {
        CITY("GeoLite2-City"), COUNTRY("GeoLite2-Country");

        private final String geoLite2Name;

        private LocationDatabaseType(String geoLite2Name) {
            this.geoLite2Name = geoLite2Name;
        }

        public String getGeoLite2Name()
        {
            return geoLite2Name;
        }

    }

    /**
     * Initialize the memory database. The path and the
     * {@code LocationDatabaseType} MUST match.
     * 
     * @param type
     *            type of the loaded database from file.
     * @param pathToDatabase
     *            can be classpath:<path> or file:\<absolute path> string to the
     *            appropriate file.
     */
    public void init(LocationDatabaseType type,
            String pathToDatabase);

    public void close();
}
