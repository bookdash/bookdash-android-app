package org.bookdash.android.data.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/21 8:13 PM
 */
public class ZipManager {

    private static final String TAG = "ZipManager";

    public void unzip(String _zipFile, String _targetLocation) {

        //create target location folder if not exist
        boolean directoryExtracted = directoryChecker("", _targetLocation);
        if (directoryExtracted) {
            Log.d(TAG, "Directory exists, not unzipping");
            return;
        }
        try {
            File archive = new File(_zipFile);

            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, _targetLocation);
            }
            zipfile.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception:", e);
        }
    }

    private void createDir(File dir) {
        if (dir.exists()) {
            return;
        }
        Log.v(TAG, "Creating dir " + dir.getName());
        if (!dir.mkdirs()) {
            throw new RuntimeException("Can not create dir " + dir);
        }
    }

    private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                            String outputDir) throws IOException {

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createDir(outputFile.getParentFile());
        }

        Log.v(TAG, "Extracting: " + entry);
        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private static final int BUF_SIZE = 0x1000; // 4K

    private static long copy(InputStream from, OutputStream to)
            throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    private boolean directoryChecker(String location, String dir) {
        File f = new File(location, dir);
        if (f.exists()) {
            return true;
        }
        if (!f.isDirectory()) {
            f.mkdirs();
            return false;
        }
        return false;
    }
}
