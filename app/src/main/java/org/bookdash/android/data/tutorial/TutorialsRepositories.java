package org.bookdash.android.data.tutorial;

import android.content.Context;
import android.support.annotation.NonNull;


public class TutorialsRepositories {
    private TutorialsRepositories() {
        //no instance
    }

    private static TutorialsRepository repository = null;

    public synchronized static TutorialsRepository getInstance(@NonNull Context context) {
        if (null == repository) {
            repository = new TutorialsRepositoryImpl(context);
        }
        return repository;
    }
}
