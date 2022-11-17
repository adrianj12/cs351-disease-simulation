package DiseaseSimulation;

// JavaFX mechanics
import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;

// JavaFX graphics
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

// JavaFX nodes/components
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.Duration;

public class GUI extends Application {

    // Reference to simulation field object
    private static Field field;

    // Canvas field to be modified by multiple methods
    private static Canvas simField;
    private static GraphicsContext gc;

    // Agent state colors
    private static Color healthy = Color.LAWNGREEN;
    private static Color vulnerable = Color.YELLOW;
    private static Color sick = Color.RED;
    private static Color immune = Color.LIGHTBLUE;
    private static Color dead = Color.BLACK;
    private static Color asymptomatic = Color.ORANGE;

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
        simField = new Canvas(field.width + 10, field.height + 10);
        gc = simField.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, field.width + 10, field.height + 10);
        layout.setCenter(simField);

        // Set up the agents on the sim field canvas before sim is started
        drawAgents();

        // Bottom start button
        HBox buttonBar = new HBox();

        Button startButton = new Button("Start Simulation");
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startButton.setVisible(false);
                main.startTime = System.currentTimeMillis();
                field.startAgents();
                timer time = new timer();
                time.start();
            }
        });

        // Stop button
        Button stopButton = new Button("Stop");
        stopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.stopAgents(field.allAgents);
                stopButton.setVisible(false);
            }
        });

        // Restart button
        Button restartButton = new Button("Restart");
        restartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                field.stopAgents(field.allAgents);
                startButton.setVisible(true);
                stopButton.setVisible(true);
                // setting new canvas
                simField = new Canvas(field.width+10, field.height+10);
                gc = simField.getGraphicsContext2D();
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, field.width+10, field.height+10);
                layout.setCenter(simField);
                try {
                    main.newGUI();
                    stage.close();
                } catch(IllegalStateException e){}
            }
        });

        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        buttonBar.getChildren().addAll(startButton, stopButton, restartButton);
        layout.setBottom(buttonBar);

        // Set up scene
        Scene scene = new Scene(layout, 800, 800);
        stage.setScene(scene);
        stage.show();

    }

    private class timer extends AnimationTimer {
        @Override
        public void handle(long now) {
            drawAgents();
        }
    }


    private void drawAgents() {

        for(Agent current : field.allAgents) {

            drawAgent(current);

        }

    }

    private void drawAgent(Agent agent) {

        State state = State.Normal;
        if (agent.dead) {
            state = State.Dead;
        } else if (agent.immune) {
            state = State.Immune;
        } else if (agent.vulnerable) {
            state = State.Vulnerable;
        }else if (agent.asymptomatic) {
            state = State.Asymptomatic;
        } else if (agent.sick) {
            state = State.Sick;
        }

        gc.setFill(state.color);
        gc.fillOval(agent.positionX, agent.positionY, 10, 10);

    }

    private enum State {

        Normal(healthy),
        Vulnerable(vulnerable),
        Sick(sick),
        Asymptomatic(asymptomatic),
        Dead(dead),
        Immune(immune);

        public Color color;

        State(Color color) {

            this.color = color;

        }

    }

}
