package skynet;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Main entry point for the Skynet GUI.
 */
public class Main extends Application {

    private final Skynet skynet = new Skynet();
    private MediaPlayer backgroundMusic;

    @Override
    public void start(Stage stage) {
        try {
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            FXMLLoader fxmlLoader =
                    new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setSkynet(skynet);
            stage.show();
            playBackgroundMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        String musicFile = Main.class
                .getResource("/sounds/terminator_ost.mp3")
                .toExternalForm();

        Media music = new Media(musicFile);

        backgroundMusic = new MediaPlayer(music);

        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusic.setVolume(0.2);
        backgroundMusic.play();
    }
}
