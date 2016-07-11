package org.bookdash.android.data.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Rebecca Franks (rebecca.franks@dstvdm.com)
 * @since 2015/07/21 8:55 PM
 */
public class FileManager {
    private static final String TAG = "FileManager";

   /* public static void saveFile(String filesDir, byte[] bytes, String fileName) {
        File file = new File(filesDir, fileName);
        FileOutputStream outputStream = null;

        try {
            if (file.exists()) {
                Log.d(TAG, "File exists-  not saving again");
                return;
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
        } catch (Exception e) {
            Crashlytics.getInstance().core.logException(e);
            Log.e(TAG, "Exception:", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception:", e);
                }
            }
        }

    }*/

    public static boolean deleteFile(String filesDir, String fileName) {
        File file = new File(filesDir, fileName);
        return file.delete();
    }

    public static void deleteFolder(String folderLocation) {
        deleteRecursive(new File(folderLocation));
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] list = fileOrDirectory.listFiles();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    File tmpF = list[i];
                    if (tmpF.isDirectory()) {
                        deleteRecursive(tmpF);
                    }
                    tmpF.delete();
                }
            }
            if (!fileOrDirectory.delete()) {
                Log.e(TAG, "can't delete folder : " + fileOrDirectory);
            }
        }
    }

}
