package skynet;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main.css GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private Skynet skynet;

    private final Image userImage = new Image(
            Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/You.png")));

    private final Image skynetImage = new Image(
            Objects.requireNonNull(
                    getClass().getResourceAsStream("/images/Arnold.png")));


    /**
     * Initializes the main window and displays the welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String welcomeMessage = "Welcome to SkyNET.\n"
                + "How may we assist you today?";

        dialogContainer.getChildren().add(
                DialogBox.getSkynetDialog(welcomeMessage, skynetImage, "OtherCommand")
        );
    }


    /**
     * Injects the Skynet instance.
     *
     * @param s the Skynet instance
     */
    public void setSkynet(Skynet s) {
        skynet = s;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = skynet.getResponse(input);
        String commandType = skynet.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSkynetDialog(response, skynetImage, commandType)
        );
        userInput.clear();
    }
}

