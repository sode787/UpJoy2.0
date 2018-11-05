//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.File;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

//Gui for the twitter application

public class GUISpyJoy extends Application {
    private String user;
    private int limit;
    private String fileLocation = "C:\\Users\\Henry\\Desktop\\twitterStash\\";
    private SearchTweetsRunner runner;
    private Stage window;

    public GUISpyJoy() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;
        primaryStage.setTitle("SpyJoy: Twitter Made Fun");
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #FFFFFF;");
        grid.setPadding(new Insets(20.0D, 10.0D, 10.0D, 20.0D));
        grid.setVgap(8.0D);
        grid.setHgap(10.0D);
        Label twitterName = new Label("Username:");
        GridPane.setConstraints(twitterName, 0, 0);
        TextField twitterInput = new TextField();
        twitterInput.setPromptText("@tsm_chriss");
        GridPane.setConstraints(twitterInput, 5, 0);
        Label limitLabel = new Label("Pages to search:");
        GridPane.setConstraints(limitLabel, 0, 1);
        final TextField limitInput = new TextField();
        limitInput.setPromptText("100");
        GridPane.setConstraints(limitInput, 5, 1);
        Label browseLabel = new Label("Export Folder:");
        GridPane.setConstraints(browseLabel, 0, 6);
        TextField browseInput = new TextField();
        browseInput.setPromptText("C:\\Users\\Henry\\Desktop\\twitterStash\\");
        browseInput.setMinWidth(50.0D);
        browseInput.setMaxWidth(2.147483647E9D);
        GridPane.setConstraints(browseInput, 1, 6, 15, 1);
        TextArea listTweets = new TextArea();
        listTweets.setWrapText(true);
        GridPane.setConstraints(listTweets, 0, 9, 15, 65);
        final Button browseFile = new Button("Browse");
        GridPane.setConstraints(browseFile, 15, 9);
        Button cancel = new Button("Cancel");
        GridPane.setConstraints(cancel, 14, 75);
        Button spy = new Button("  Start  ");
        GridPane.setConstraints(spy, 15, 75);
        limitInput.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    limitInput.setText(newValue.replaceAll("[^\\d]", ""));
                }

                if (newValue.matches("\\d+") && Integer.parseInt(limitInput.getText()) > 150) {
                    limitInput.setText("150");
                    limitInput.setText("1" + limitInput.getText().substring(1, limitInput.getLength()));
                }

            }
        });
        browseFile.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                browseFile.setPrefWidth((double)(browseFile.getText().length() * 7));
            }
        });
        browseFile.setOnAction((e) -> {
            System.out.println("browse");
            DirectoryChooser dirChoose = new DirectoryChooser();
            File selectedFile = dirChoose.showDialog((Window)null);
            if (selectedFile != null) {
                browseInput.setText(selectedFile.getAbsolutePath());
                this.fileLocation = browseInput.getText();
            } else {
                browseInput.setText("C:\\Users\\Henry\\Desktop\\twitterStash\\");
                this.fileLocation = browseInput.getText();
            }

        });
        cancel.setOnAction((e) -> {
            System.exit(0);
        });
        spy.setOnAction((e) -> {
            if (limitInput.getText().matches("\\d+")) {
                this.limit = Integer.parseInt(limitInput.getText());
            }

            if (this.limit == 0) {
                this.limit = 1;
            }

            this.user = twitterInput.getText();
            System.out.println(this.user);

            try {
                this.runner = new SearchTweetsRunner(this.getUser(), this.getLimit(), this.getFileLocation());
                listTweets.setText(this.runner.getTweets());
            } catch (Exception var6) {
                System.out.println("Holy shit something is wrong and I don't know what");
                System.out.println("you might have reached the streaming limit check back in 15 minutes");
            }

        });
        grid.getChildren().addAll(new Node[]{twitterName, twitterInput, limitLabel, limitInput, browseFile, cancel, spy, browseLabel, browseInput, listTweets});
        Scene scene = new Scene(grid, 615.0D, 800.0D);
        this.window.setScene(scene);
        Image icon = new Image("file:SpyCon.ico");
        this.window.getIcons().add(icon);
        this.window.show();
    }

    private String getUser() {
        return this.user;
    }

    private int getLimit() {
        return this.limit;
    }

    private String getFileLocation() {
        return this.fileLocation;
    }
}
