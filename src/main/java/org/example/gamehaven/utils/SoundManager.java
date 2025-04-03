package org.example.gamehaven.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer backgroundPlayer;
    private double volume = 0.5; // Default volume (50%)

    private SoundManager() {
        // Private constructor for singleton
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }

        URL musicFile = getClass().getResource("/org/example/gamehaven/sounds/gameBackgroundMusic.mp3");
        if (musicFile != null) {
            Media sound = new Media(musicFile.toString());
            backgroundPlayer = new MediaPlayer(sound);
            backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            backgroundPlayer.setVolume(volume);
            backgroundPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (backgroundPlayer != null) {
            backgroundPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

    public void playWinSound() {
        playSoundEffect("/org/example/gamehaven/sounds/win.wav");
    }

    public void playLoseSound() {
        playSoundEffect("/org/example/gamehaven/sounds/lose.wav");
    }

    public void playDrawSound() {
        playSoundEffect("/org/example/gamehaven/sounds/draw.mp3");
    }

    private void playSoundEffect(String soundFile) {
        URL soundUrl = getClass().getResource(soundFile);
        if (soundUrl != null) {
            Media sound = new Media(soundUrl.toString());
            MediaPlayer soundEffectPlayer = new MediaPlayer(sound);
            soundEffectPlayer.setVolume(volume);
            soundEffectPlayer.play();
        }
    }
}