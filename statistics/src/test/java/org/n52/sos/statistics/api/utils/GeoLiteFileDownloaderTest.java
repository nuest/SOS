package org.n52.sos.statistics.api.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class GeoLiteFileDownloaderTest {

    @Test
    public void downloadFiletoClassPath() throws URISyntaxException, IOException {
        ClassLoader cl = GeoLiteFileDownloader.class.getClassLoader();
        URL geoliteFolder = cl.getResource("/geolite");
        if (geoliteFolder != null) {
            Files.deleteIfExists(Paths.get(geoliteFolder.toURI()));
        }

        GeoLiteFileDownloader.downloadDefaultDatabases();

        Assert.assertTrue(new File(cl.getResource("geolite/city.mmdb").toURI().getPath()).exists());
        Assert.assertTrue(new File(cl.getResource("geolite/country.mmdb").toURI().getPath()).exists());
    }
}
