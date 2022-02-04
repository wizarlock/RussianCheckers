package com.example.checkers.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.checkers.data.ButtonSoundManager;
import com.example.checkers.databinding.FragmentSettingsBinding;
import com.example.checkers.data.MusicManager;


public class Settings extends Fragment {

    private FragmentSettingsBinding binding;
    public static final String APP_PREFERENCES = "mSettings";
    public static final String MUSIC_SETTINGS = "music";
    public static final boolean DEFAULT_MUSIC_ENABLED_KEY = true;
    public static final String SOUNDS_SETTINGS = "settings";
    public static final boolean DEFAULT_SOUNDS_ENABLED_KEY = true;
    public static final String HINTS_SETTINGS = "hints";
    public static final boolean DEFAULT_HINTS_ENABLED_KEY = true;

    private final MusicManager musicManager;
    private final ButtonSoundManager buttonSoundManager;

    public Settings(MusicManager musicManager, ButtonSoundManager buttonSoundManager) {
        this.musicManager = musicManager;
        this.buttonSoundManager = buttonSoundManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.music.setChecked(musicManager.isMusicEnabled());
        binding.music.setOnCheckedChangeListener((switchView, checked) -> {
            buttonSoundManager.setButtonSoundEnabled(buttonSoundManager.isButtonSoundEnabled());
            musicManager.setMusicEnabled(checked);
        });
        binding.sounds.setChecked(buttonSoundManager.isButtonSoundEnabled());
        binding.sounds.setOnCheckedChangeListener((switchView, checked) -> buttonSoundManager.setButtonSoundEnabled(checked));
        binding.back.setOnClickListener(v -> {
            buttonSoundManager.setButtonSoundEnabled(buttonSoundManager.isButtonSoundEnabled());
            requireFragmentManager().popBackStackImmediate();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        binding = null;
    }
}