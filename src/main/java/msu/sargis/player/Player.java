package msu.sargis.player;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Player {
    private static Player player;
    private final ExecutorService executorService;

    public Player() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public static Player getInstance() {
        if (player == null) player = new Player();
        return player;
    }

    public void playSound() {
        executorService.submit(() -> {
            try {
                URL url = this.getClass().getClassLoader().getResource("audio.wav");
                AudioInputStream audioIn;
                audioIn = AudioSystem.getAudioInputStream(url);

                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutDown(){
        executorService.shutdown();
    }
}
