import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static Clip clip;
    static FloatControl volumeControl;

    static String[] playlist = {
            "src\\Push Thru - The Grey Room _ Golden Palms.wav",
            "src\\Pawn - The Grey Room _ Golden Palms.wav",
            "src\\At All Costs - The Grey Room _ Golden Palms.wav"
    };

    static int currentSongIndex = 0;
    static long pausePosition = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String choice = "";

        loadSong(playlist[currentSongIndex]);

        while (!choice.equals("Q")) {
            showMenu();
            choice = scanner.next().toUpperCase();

            switch (choice) {
                case "P" -> play();
                case "S" -> stop();
                case "U" -> pause();
                case "R" -> resume();
                case "N" -> nextSong();
                case "B" -> previousSong();
                case "V" -> setVolume(scanner);
                case "Q" -> quit();
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    // Load a song
    static void loadSong(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            System.out.println("Loaded: " + filePath);

        } catch (Exception e) {
            System.out.println("Error loading song: " + e.getMessage());
        }
    }

    // Play song
    static void play() {
        clip.start();
        System.out.println("Playing...");
    }

    // Stop song
    static void stop() {
        clip.stop();
        pausePosition = 0;
        System.out.println("Stopped.");
    }

    // Pause song
    static void pause() {
        pausePosition = clip.getMicrosecondPosition();
        clip.stop();
        System.out.println("Paused.");
    }

    // Resume song
    static void resume() {
        clip.setMicrosecondPosition(pausePosition);
        clip.start();
        System.out.println("Resumed.");
    }

    // Next song
    static void nextSong() {
        clip.stop();
        clip.close();

        currentSongIndex = (currentSongIndex + 1) % playlist.length;
        loadSong(playlist[currentSongIndex]);
        play();
    }

    // Previous song
    static void previousSong() {
        clip.stop();
        clip.close();

        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = playlist.length - 1;
        }

        loadSong(playlist[currentSongIndex]);
        play();
    }

    // Set volume
    static void setVolume(Scanner scanner) {
        System.out.print("Enter volume (0 - 100): ");
        int volume = scanner.nextInt();

        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();

        float gain = (max - min) * volume / 100 + min;
        volumeControl.setValue(gain);

        System.out.println("Volume set to " + volume + "%");
    }

    // Show menu
    static void showMenu() {
        System.out.println("\n=====================");
        System.out.println("   JAVA MUSIC PLAYER ");
        System.out.println("=====================");
        System.out.println("P = Play");
        System.out.println("U = Pause");
        System.out.println("R = Resume");
        System.out.println("S = Stop");
        System.out.println("N = Next Song");
        System.out.println("B = Previous Song");
        System.out.println("V = Volume");
        System.out.println("Q = Quit");
        System.out.print("Enter choice: ");
    }

    // Quit program
    static void quit() {
        clip.stop();
        clip.close();
        System.out.println("Goodbye 👋");
    }
}

