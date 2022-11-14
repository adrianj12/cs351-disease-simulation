package DiseaseSimulation;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.animation.AnimationTimer;

import java.time.Duration;

public class GUI extends Application {

    // Reference to simulation field object
    private static Field field;

    // Canvas field to be modified by multiple methods
    private static Canvas simField;
    private static GraphicsContext gc;

    // Agent state colors
    Color vulnerable = Color.BLUE;
    Color sick = Color.YELLOW;
    Color immune = Color.GREEN;
    Color dead = Color.BLACK;
    Color asymptomatic = Color.RED;

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

        // Set up blank white simulation canvas background
        simField = new Canvas(field.width, field.height);
        gc = simField.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, field.width, field.height);
        layout.setCenter(simField);

        // Set up the agents on the sim field canvas before sim is started
        drawAgents();

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
        HBox buttonBar = new HBox();

        Button startButton = new Button("Start Simulation");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.startAgents();
            }
        });

        // Stop button
        Button stopButton = new Button("Stop");
        stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //field.stopAgents();
            }
        });

        // Restart button
        Button restartButton = new Button("Restart");
        stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //field.stopAgents();
                field.startAgents();
            }
        });

        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        buttonBar.getChildren().addAll(startButton, stopButton, restartButton);
        layout.setBottom(buttonBar);

        // Set up scene
        Scene scene = new Scene(layout, 800, 800);
        stage.setScene(scene);
        stage.show();

        // ***** Main simulation loop *****
        AnimationTimer a = new AnimationTimer() {

            private long nextTime = 0; // For incrementing animation timer counter

            @Override
            public void handle(long now) {

                if(now > nextTime) {

                    drawAgents();

                    // Updates GUI every 500 milliseconds
                    nextTime = now + Duration.ofMillis(500).toNanos();

                }

            }
        };

    }

    private void drawAgents() {

        for(Agent current : field.allAgents) {

            drawAgent(current);

        }

    }

    private void drawAgent(Agent agent) {

        State state = State.Vulnerable;

        if(agent.vulnerable) {
            state = State.Vulnerable;
        } else if (agent.sick) {
            state = State.Sick;
        } else if (agent.asymptomatic) {
            state = State.Asymptomatic;
        } else if (agent.dead) {
            state = State.Dead;
        } else if (agent.immune) {
            state = State.Immune;
        }

        gc.setFill(state.color);
        gc.fillOval(agent.positionX, agent.positionY, 10, 10);

    }

    private enum State {

        Vulnerable(Color.BLUE),
        Sick(Color.YELLOW),
        Asymptomatic(Color.ORANGERED),
        Dead(Color.BLACK),
        Immune(Color.GREEN);

        public Color color;

        private State(Color color) {

            this.color = color;

        }

    }

}
