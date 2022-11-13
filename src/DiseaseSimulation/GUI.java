package DiseaseSimulation;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    // Reference to simulation field object
    private static Field field;

    public GUI() { }

    public void open(Field field) {

        this.field = field;
        launch();

    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Disease Simulation - by Adrian Abeyta and Daniel Morales-Garcia");
        stage.setResizable(false);

        BorderPane layout = new BorderPane();

        // Top title text
        VBox header = new VBox();
        Text title = new Text("Disease Simulation");
        title.setFont(Font.font(null, FontWeight.BOLD, 16));

        Text subtitle = new Text("press Start Simulation button to begin");

        header.getChildren().addAll(title, subtitle);
        BorderPane.setAlignment(header, Pos.CENTER);
        layout.setTop(header);

        // Set up blank white simulation canvas
        Canvas simField = new Canvas(field.width, field.height);
        GraphicsContext gc = simField.getGraphicsContext2D();;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, field.width, field.height);
        layout.setCenter(simField);

        // Agent representation
        gc.setFill(Color.BLACK);
        gc.fillOval(10, 10, 5, 5);

        // Setup log window components
        VBox logWindow = new VBox();
        Text logTitle = new Text("Agent Status Log\n\n");
        logTitle.setFont(Font.font(null, FontWeight.BOLD, 14));

        Text log = new Text();
        log.setText("Log text");

        logWindow.getChildren().addAll(logTitle, log);
        BorderPane.setAlignment(logWindow, Pos.TOP_LEFT);
        logWindow.setPrefWidth(400);
        layout.setRight(logWindow);

        // Bottom start button
        Button startButton = new Button("Start Simulation");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.startAgents();
            }
        });
        BorderPane.setAlignment(startButton, Pos.CENTER);
        layout.setBottom(startButton);

        // Set up scene
        Scene scene = new Scene(layout, 800, 800);
        stage.setScene(scene);
        stage.show();

    }

}
