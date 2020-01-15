package org.bookdash.android.presentation;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.test.runner.AndroidJUnitRunner;

import java.lang.reflect.Method;

/**
 * Tests can fail for other reasons than code, it´ because of the animations and espresso sync and
 * emulator state (screen off or locked)
 * <p/>
 * Before all the tests prepare the device to run tests and avoid these problems.
 * <p/>
 * - Disable animations
 * - Disable keyguard lock
 * - Set it to be awake all the time (dont let the processor sleep)
 *
 * @see <a href="u2020 open source app by Jake Wharton">https://github.com/JakeWharton/u2020</a>
 * @see <a href="Daj gist">https://gist.github.com/daj/7b48f1b8a92abf960e7b</a>
 */
public final class CustomTestRunner extends AndroidJUnitRunner {

    private static final String TAG = "CustomTestRunner";

    @Override
    public void onStart() {

        runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context app = CustomTestRunner.this.getTargetContext().getApplicationContext();

                CustomTestRunner.this.disableAnimations(app);

                String name = CustomTestRunner.class.getSimpleName();
                unlockScreen(app, name);
                keepSceenAwake(app, name);
            }
        });

        super.onStart();
    }


    @Override
    public void finish(int resultCode, Bundle results) {
        super.finish(resultCode, results);
        enableAnimations(getContext());
    }
    private void keepSceenAwake(Context app, String name) {
        PowerManager power = (PowerManager) app.getSystemService(Context.POWER_SERVICE);
        power.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, name)
                .acquire();
    }

    private void unlockScreen(Context app, String name) {
        KeyguardManager keyguard = (KeyguardManager) app.getSystemService(Context.KEYGUARD_SERVICE);
        keyguard.newKeyguardLock(name).disableKeyguard();
    }

    void disableAnimations(Context context) {
        int permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(0.0f);
        }
    }

    void enableAnimations(Context context) {
        int permStatus = context.checkCallingOrSelfPermission(Manifest.permission.SET_ANIMATION_SCALE);
        if (permStatus == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(1.0f);
        }
    }

    private void setSystemAnimationsScale(float animationScale) {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
            Log.d(TAG, "Changed permissions of animations");
        } catch (Exception e) {
            Log.e(TAG, "Could not change animation scale to " + animationScale + " :'(");
        }
    }
}
