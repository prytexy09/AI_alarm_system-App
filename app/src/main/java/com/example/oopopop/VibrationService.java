package com.example.oopopop;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

public class VibrationService extends Service {

    private static final String TAG = "VibrationService";
    private Vibrator vibrator;
    Thread vibt, vibt2;
    MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        vibt = new Thread(() -> {
            while (true) {
                Log.i("THR", String.valueOf(vibt.isInterrupted()));
                if (vibt.isInterrupted())
                    return;
                // Вибрируем 5 секунд
                vibrator.vibrate(1000);

                // Ждем 10 секунд
                SystemClock.sleep(3000);
                Log.i(TAG, "vibrating");

            }
        });
        vibt.start();
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();

        return START_STICKY; // Перезапускаем сервис, если он был убит системой
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (vibt != null)
            vibt.interrupt();
        if (player != null)
            player.stop();
        Log.d(TAG, "Service destroyed");
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
