package org.n52.sos.statistics.api.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeoLiteFileDownloader {
    private static final Logger logger = LoggerFactory.getLogger(GeoLiteFileDownloaderTest.class);

    public static void downloadDefaultDatabases() {
        Properties geoliteProps = new Properties();
        InputStream file = GeoLiteFileDownloaderTest.class.getClassLoader().getResourceAsStream("geolitepaths.properties");
        try {
            geoliteProps.load(file);
            String cityUrl = geoliteProps.getProperty("url.city", "http://geolite.maxmind.com/download/geoip/database/GeoLite2-City.mmdb.gz");
            String countryUrl = geoliteProps.getProperty("url.country", "http://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.mmdb.gz");
            String folder = geoliteProps.getProperty("outpath.folder", "geolite");
            String cityOutPath = geoliteProps.getProperty("outpath.city", "city.mmdb.gz");
            String countryOutPath = geoliteProps.getProperty("outpath.country", "county.mmdb.gz");

            // create folder
            folder = GeoLiteFileDownloaderTest.class.getResource("/").toURI().getPath().concat(folder);
            try {
                FileUtils.forceMkdir(new File(folder));
            } catch (IOException e) {
                // directory already exists
            }

            cityOutPath = folder.concat("/").concat(cityOutPath);
            countryOutPath = folder.concat("/").concat(countryOutPath);

            FileDownloader.downloadFile(cityUrl, cityOutPath);
            FileDownloader.downloadFile(countryUrl, countryOutPath);
            FileDownloader.gunzipFile(cityOutPath);
            FileDownloader.gunzipFile(countryOutPath);

        } catch (IOException e) {
            logger.error("Error during", e);
        } catch (URISyntaxException e) {
            logger.error(null, e);
        } catch (Throwable e) {
            logger.error(null, e);
        }
    }
}
