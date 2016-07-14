package org.bookdash.android.data.utils;

import android.util.Log;

import java.io.File;

public class FileManager {
    private static final String TAG = "FileManager";


    public static void deleteFolder(String folderLocation) {
        deleteRecursive(new File(folderLocation));
    }

    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] list = fileOrDirectory.listFiles();
            if (list != null) {
                for (File tmpF : list) {
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
