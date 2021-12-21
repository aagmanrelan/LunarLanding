package puzzles.lunarlanding.gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * This Class is responsible to handle all the GUI components and display them correctly.
 * @author Aagman Relan
 * December 2021
 */
public class LunarLandingGUI extends Application
        implements Observer< LunarLandingModel, Object > {
/*
    These are the components that will be used throughout the life of the GUI and hence it is important that they
    always remain in scope. Hence, they are declared here.
 */
    private LunarLandingModel gamemodel;
    private final Background GREY = new Background(new  BackgroundFill(Color.GREY, null, null));
    private final Background WHITE = new Background(new  BackgroundFill(Color.WHITE, null, null));

    private ArrayList<Button> buttons;
    private Label label;
    private Button North;
    private Button South;
    private Button East;
    private Button West;
    private Button Hint;
    private Stage stage;
    private BorderPane borderPane;

    /**
     * This method returns the Image of the explorer from the resourses folder.
     * @return An Image of the Game's Explorer object
     */
    public Image getExplorer(){
        return new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/explorer.png")));
    }

    /**
     * The method just returns the Image of the Robot specified by the arguments passed into the method.
     * @param i The position of the Figure whose image will be returned
     * @return : The image of the specified Robot figure.
     */
    public Image getRobots(int i){
        return switch (i) {
            case 0 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-blue.png")));
            case 1 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-green.png")));
            case 2 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-lightblue.png")));
            case 3 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-orange.png")));
            case 4 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-pink.png")));
            case 5 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-purple.png")));
            case 6 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-white.png")));
            case 7 -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/robot-yellow.png")));
            default -> new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("resources/lander.png")));
        };
    }

    /**
     * This method produces all the components that will be required to play the game.
     * @return The game grid complete with all the robots and the explorer objects and the Lander object
     */
    private GridPane gameGrid(){
        GridPane grid = new GridPane();
        this.buttons.clear();
        int i=0;
        for(int row=0;row<this.gamemodel.currentConfig.BOARD_ROW; ++row){
            for(int col=0;col<this.gamemodel.currentConfig.BOARD_COL; ++col){
                Button button = new Button();
                buttons.add(button);
                button.setBackground(GREY);
                if(gamemodel.currentConfig.board[row][col].equals("E")){
                    button.setGraphic(new ImageView(getExplorer()));
                }
                else if(!(gamemodel.currentConfig.board[row][col].equals("_"))){
                    button.setGraphic(new ImageView(getRobots(5)));
                }
                else if(gamemodel.currentConfig.GOAL_ROW==row && gamemodel.currentConfig.GOAL_COL==col){
                    button.setGraphic(new ImageView(getRobots(-1)));
                    button.setBackground(WHITE);
                }
                button.setPrefSize(100,100);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction((event)-> this.gamemodel.choose(finalRow, finalCol));

                grid.add(button,col,row);
            }

        }

        return grid;
    }

    /**
     * It places the game grid into its correct position. This method also produces menu buttons which are used
     * to control the player actions in the game. It also sets the position of the label which will display all
     * the game's instructions
     * @return The BorderPane which contains all the game elements.
     */
    public BorderPane gameboard(){
        BorderPane borderpane = new BorderPane();
        this.borderPane = borderpane;
        borderpane.setCenter(gameGrid());
        VBox buttons = new VBox();
        North = new Button("NORTH");
        South = new Button("SOUTH");
        East = new Button("EAST");
        West = new Button("WEST");
        North.setOnAction((event)-> gamemodel.gonorth());
        South.setOnAction((event)-> gamemodel.gosouth());
        East.setOnAction((event)-> gamemodel.goeast());
        West.setOnAction((event)-> gamemodel.gowest());
        buttons.getChildren().addAll(North,South,East,West);
        buttons.setPadding(new Insets(10, 10, 20, 10));
        buttons.setSpacing(10);
        VBox morebuttons = new VBox();
        Hint = new Button("HINT");
        Button Load = new Button("LOAD");
        Button Reload = new Button("RELOAD");
        Reload.setOnAction((event)-> gamemodel.reset());
        Hint.setOnAction((event)-> gamemodel.hint());
        Load.setOnAction((event)->{
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.setInitialDirectory(new File("data/lunarlanding"));
            File selectedfile = fileChooser.showOpenDialog(stage);
            try {
                gamemodel.load(selectedfile.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        morebuttons.getChildren().addAll(Hint,Load,Reload);
        morebuttons.setPadding(new Insets(10, 10, 20, 10));
        morebuttons.setSpacing(10);
        borderpane.setLeft(morebuttons);
        borderpane.setRight(buttons);
        borderpane.setTop(label);
        BorderPane.setAlignment(label, Pos.CENTER);
        return borderpane;
    }

    /**
     * This will start the GUI
     * @param stage The stage which will be used to display the game.
     */
    @Override
    public void start( Stage stage ) {
        this.stage = stage;
        stage.setTitle( "Lunar Landing" );
        Scene scene = new Scene( gameboard());
        stage.setScene( scene );
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Everytime there is a change in the game model, the update is called to change the GUI to reflect the change
     * of model
     * @param lunarLandingModel The lunar llanding model
     * @param o: Arguments if any
     */
    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        if (Platform.isFxApplicationThread()) {
            North.setDisable(false);
            South.setDisable(false);
            East.setDisable(false);
            West.setDisable(false);
            Hint.setDisable(false);
            label.setText("Choose a Player");
            if (this.gamemodel.PlayerRow != -1 && this.gamemodel.PlayerColumn != -1) {
                label.setText("YOU CHOOSE Explorer at Row: " + this.gamemodel.PlayerRow + ", Column: " + this.gamemodel.PlayerColumn);
            }
            this.borderPane.setCenter(gameGrid());
            int k = 0;
            for(int row=0;row<this.gamemodel.currentConfig.BOARD_ROW; ++row){
                for(int col=0;col<this.gamemodel.currentConfig.BOARD_COL; ++col) {

                    if (gamemodel.currentConfig.board[row][col].equals("E")) {
                        //buttons.get(k).setBackground(GREY);
                        buttons.get(k++).setGraphic(new ImageView(getExplorer()));

                    } else if (!(gamemodel.currentConfig.board[row][col].equals("_"))) {
                        buttons.get(k).setBackground(GREY);
                        buttons.get(k++).setGraphic(new ImageView(getRobots(5)));
                    } else if (gamemodel.currentConfig.GOAL_ROW == row && gamemodel.currentConfig.GOAL_COL == col) {
                        buttons.get(k).setBackground(WHITE);
                        buttons.get(k++).setGraphic(new ImageView(getRobots(-1)));
                    }
                    else{
                        buttons.get(k).setBackground(GREY);
                        buttons.get(k++).setGraphic(null);
                    }
                }
            }
            while(k<buttons.size()){
                buttons.get(k).setGraphic(null);
                buttons.get(k).setBackground(null);
                k++;
            }
            if(this.gamemodel.currentConfig.isSolution()){
                label.setText("YOU WON!!!");
                North.setDisable(true);
                South.setDisable(true);
                East.setDisable(true);
                West.setDisable(true);
                Hint.setDisable(true);
            }
            if(this.gamemodel.Unsolvable){
                label.setText("The Game is Unsolvable. Press Reload/Load Button");
            }
            this.stage.sizeToScene();
        }
    }

    /**
     * This method runs after the GUI is terminated.
     */
    @Override
    public void stop(){
        System.out.println("Stop and clean");
    }

    public static void main( String[] args ) {
        Application.launch( args );

    }

    /**
     * This method initializes the game model and connects the GUI to the game model
     * @throws Exception Only thrown if the file specified is not found.
     */
    @Override
    public void init() throws Exception{
        List<String> args = getParameters().getRaw();
        buttons = new ArrayList<>();
        final int BOARD_ROW;
        final int BOARD_COL;
        final int GOAL_ROW;
        final int GOAL_COL;
        ArrayList<String> inputs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(args.get(0)));
        String line= reader.readLine();
        String [] indexes = line.split("\\s");
        BOARD_ROW = Integer.parseInt(indexes[0]);
        BOARD_COL = Integer.parseInt(indexes[1]);
        GOAL_ROW = Integer.parseInt(indexes[2]);
        GOAL_COL = Integer.parseInt(indexes[3]);
        line = reader.readLine();
        while (line.length()!=0){
            inputs.add(line);
            line = reader.readLine();
        }
        LunarLandingConfig startconfig = new LunarLandingConfig(BOARD_ROW,BOARD_COL,GOAL_ROW,GOAL_COL,inputs);
        this.gamemodel = new LunarLandingModel(startconfig);
        InitializeView();
        label = new Label("Welcome! Choose a Player");
        System.out.println("Game Model Initialization Complete");
    }

    /**
     * This method add the current state to the list of observers that is maintained by the game model
     * The model is then updated.
     */
    public void InitializeView(){
        this.gamemodel.addObserver(this);
        update(this.gamemodel,null);
    }
}
