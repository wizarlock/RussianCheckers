package com.example.checkers.ui;

import static com.example.checkers.ui.Settings.APP_PREFERENCES;
import static com.example.checkers.ui.Settings.DEFAULT_MUSIC_ENABLED_KEY;
import static com.example.checkers.ui.Settings.DEFAULT_SOUNDS_ENABLED_KEY;
import static com.example.checkers.ui.Settings.MUSIC_SETTINGS;
import static com.example.checkers.ui.Settings.SOUNDS_SETTINGS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.checkers.R;
import com.example.checkers.data.ButtonSoundManager;
import com.example.checkers.data.MusicManager;


public class MainActivity extends AppCompatActivity implements MusicManager, ButtonSoundManager {
    private MediaPlayer appMusic;
    private SoundPool buttonSound;
    protected SharedPreferences settings;

    private int buttonSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addFragment(new Menu(this, this));

        appMusic = MediaPlayer.create(MainActivity.this, R.raw.defaultmusic);
        appMusic.setLooping(true);
        appMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);

        buttonSound = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_GAME).build()
                )
                .build();
        buttonSoundId = buttonSound.load(this, R.raw.buttonclick, 1);
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMusic(isMusicEnabled());
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateMusic(false);
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frPlace, fragment)
                .commit();
    }

    @Override
    public void setMusicEnabled(boolean enabled) {
        updateMusic(enabled);
        settings.edit().putBoolean(MUSIC_SETTINGS, enabled).apply();
    }

    @Override
    public boolean isMusicEnabled() {
        return settings.getBoolean(MUSIC_SETTINGS, DEFAULT_MUSIC_ENABLED_KEY);
    }

    private void updateMusic(boolean enabled) {
        if (enabled) {
            appMusic.start();
        } else if (appMusic.isPlaying()) {
            appMusic.pause();
        }
    }

    @Override
    public void setButtonSoundEnabled(boolean enabled) {
        if (enabled) buttonSound.play(buttonSoundId, 1, 1, 1, 0, 1);
        settings.edit().putBoolean(SOUNDS_SETTINGS, enabled).apply();
    }

    @Override
    public boolean isButtonSoundEnabled() {
        return settings.getBoolean(SOUNDS_SETTINGS, DEFAULT_SOUNDS_ENABLED_KEY);
    }


}