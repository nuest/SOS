package org.n52.sos.statistics.api.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;

public class FileDownloader {

    /**
     * Download the url to the specified location
     * 
     * @param url
     *            url to download
     * @param outfilePath
     *            outputfile location
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void downloadFile(String url,
            String outfilePath) throws FileNotFoundException, IOException {
        Objects.requireNonNull(url);
        Objects.requireNonNull(outfilePath);

        URL fileUrl = new URL(url);
        File out = new File(outfilePath);
        FileUtils.copyURLToFile(fileUrl, out);
    }

    public static void gunzipFile(String filePath) throws FileNotFoundException, IOException, CompressorException {
        File file = new File(filePath);
        String outPath = null;
        final byte[] buff = new byte[1024];

        if (!file.getName().endsWith("gz")) {
            throw new IOException("File is not ends with .gz extension");
        } else {
            outPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 2);
        }

        FileOutputStream out = new FileOutputStream(outPath);
        GzipCompressorInputStream gzFile = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file)));
        int n = 0;
        while (-1 != (n = gzFile.read(buff))) {
            out.write(buff, 0, n);
        }
        out.close();
        gzFile.close();
    }
}
