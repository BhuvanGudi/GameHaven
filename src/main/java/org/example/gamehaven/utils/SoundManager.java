package org.example.gamehaven.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer backgroundPlayer;
    private double volume = 0.5; // Default volume (50%)

    private SoundManager() {

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
            backgroundPlayer.pause();
            backgroundPlayer.setVolume(0);
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
        playGameEndSound("/org/example/gamehaven/sounds/win.wav");
    }

    public void playLoseSound() {
        playGameEndSound("/org/example/gamehaven/sounds/lose.wav");
    }

    public void playDrawSound() {
        playGameEndSound("/org/example/gamehaven/sounds/draw.mp3");
    }

    private void playGameEndSound(String soundFile) {
        double originalVolume = this.volume;

        setVolume(0);

        URL soundUrl = getClass().getResource(soundFile);
        if (soundUrl != null) {
            Media sound = new Media(soundUrl.toString());
            MediaPlayer effectPlayer = new MediaPlayer(sound);
            effectPlayer.setVolume(1.0);
            effectPlayer.play();

            effectPlayer.setOnEndOfMedia(() -> setVolume(originalVolume));
        }
    }
}