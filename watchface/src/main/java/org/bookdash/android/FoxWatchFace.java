package org.bookdash.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;


public class FoxWatchFace extends CanvasWatchFaceService {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");
    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);
    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;
    private Bitmap backgroundScaledBitmap;

    @Override
    public FoxWatchFace.Engine onCreateEngine() {
        return new FoxWatchFace.Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<FoxWatchFace.Engine> mWeakReference;

        EngineHandler(FoxWatchFace.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            FoxWatchFace.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        private static final String FONT_NAME_WATCHFACE = "fonts/minyna.ttf";
        final Handler updateTimeHandler = new FoxWatchFace.EngineHandler(this);
        boolean registeredTimeZoneReceiver = false;
        Paint backgroundPaint;
        Bitmap owlBackgroundBitmap;
        Paint textTimePaint;
        Paint textPaintSmall;
        boolean ambient;
        ZonedDateTime currentTime;
        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                AndroidThreeTen.init(getApplication());
                currentTime = ZonedDateTime.now();
            }
        };
        int tapCount;

        float xOffset;
        float xOffsetDate;
        float yOffset;
        float yOffsetDate;

        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean lowBitAmbient;
        private BatteryStatusHelper batteryStatusHelper;
        private float yOffsetBattery;
        private float xOffsetBattery;
        private Bitmap owlVectorAmbient;
        private Bitmap ambientScaledBitmap;

        @Override
        public void onDestroy() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            super.onDestroy();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (backgroundScaledBitmap == null || backgroundScaledBitmap.getWidth() != width || backgroundScaledBitmap
                    .getHeight() != height) {
                backgroundScaledBitmap = Bitmap
                        .createScaledBitmap(owlBackgroundBitmap, width, height, true /* filter */);

            }
            if (ambientScaledBitmap == null || ambientScaledBitmap.getWidth() != width || ambientScaledBitmap
                    .getHeight() != height) {
                ambientScaledBitmap = Bitmap.createScaledBitmap(owlVectorAmbient, width, height, true /* filter */);

            }
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(ambientScaledBitmap, 0, 0, null);
            } else {
                canvas.drawBitmap(backgroundScaledBitmap, 0, 0, null);

            }

            currentTime = ZonedDateTime.now();
            String time = currentTime.format(TIME_FORMATTER);
            if (isInAmbientMode()) {
                int colorWhite = ContextCompat.getColor(getApplicationContext(), R.color.white);
                textTimePaint.setColor(colorWhite);
                textPaintSmall.setColor(colorWhite);
            } else {
                int colorBlack = ContextCompat.getColor(getApplicationContext(), R.color.black);
                textTimePaint.setColor(colorBlack);
                textPaintSmall.setColor(colorBlack);
            }
            canvas.drawText(time, xOffset, yOffset, textTimePaint);

            String date = currentTime.format(DATE_TIME_FORMATTER);
            canvas.drawText(date, xOffsetDate, yOffsetDate, textPaintSmall);

            canvas.drawText(batteryStatusHelper.getBatteryPercentage() + "%", xOffsetBattery, yOffsetBattery,
                    textPaintSmall);

            super.onDraw(canvas, bounds);

        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = FoxWatchFace.this.getResources();
            boolean isRound = insets.isRound();
            xOffset = resources.getDimension(R.dimen.fox_x_offset);
            xOffsetDate = resources.getDimension(R.dimen.fox_x_date_offset);

            float textSize = resources
                    .getDimension(isRound ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);
            float textSizeSmall = resources
                    .getDimension(isRound ? R.dimen.digital_text_size_round_small : R.dimen.digital_text_size_small);
            textTimePaint.setTextSize(textSize);
            textPaintSmall.setTextSize(textSizeSmall);

        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (ambient != inAmbientMode) {
                ambient = inAmbientMode;
                if (lowBitAmbient) {
                    textTimePaint.setAntiAlias(!inAmbientMode);
                    textPaintSmall.setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    tapCount++;
                    backgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(),
                            tapCount % 2 == 0 ? R.color.background : R.color.background2));
                    break;
            }
            invalidate();
        }

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            AndroidThreeTen.init(getApplication());
            setWatchFaceStyle(
                    new WatchFaceStyle.Builder(FoxWatchFace.this).setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                            .setAmbientPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                            .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                            // .setStatusBarGravity(Gravity.TOP | Gravity.RIGHT)
                            .setShowUnreadCountIndicator(true)
                            //.setShowSystemUiTime(false)
                            .setAcceptsTapEvents(true).build());
            Resources resources = FoxWatchFace.this.getResources();
            yOffset = resources.getDimension(R.dimen.fox_y_offset);
            yOffsetDate = resources.getDimension(R.dimen.fox_y_date_offset);
            yOffsetBattery = resources.getDimension(R.dimen.fox_y_battery_offset);
            xOffsetBattery = resources.getDimension(R.dimen.fox_x_battery_offset);
            backgroundPaint = new Paint();
            backgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.background));
            owlBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
            textTimePaint = new Paint();
            textTimePaint = createTextPaint(ContextCompat.getColor(getApplicationContext(), R.color.digital_text));
            textPaintSmall = new Paint();
            textPaintSmall = createTextPaint(ContextCompat.getColor(getApplicationContext(), R.color.black));

            owlVectorAmbient = BitmapFactory.decodeResource(getResources(), R.drawable.slim_fox);
            currentTime = ZonedDateTime.now();
            batteryStatusHelper = new BatteryStatusHelper(getApplicationContext());
        }

        private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            Typeface typeface = Typeface.createFromAsset(getAssets(), FONT_NAME_WATCHFACE);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.

                currentTime = ZonedDateTime.now();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (registeredTimeZoneReceiver) {
                return;
            }
            registeredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            FoxWatchFace.this.registerReceiver(timeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!registeredTimeZoneReceiver) {
                return;
            }
            registeredTimeZoneReceiver = false;
            FoxWatchFace.this.unregisterReceiver(timeZoneReceiver);
        }

        /**
         * Starts the {@link #updateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #updateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }
    }
}
