package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Aagman Relan
 * November 2021
 */
public class LunarLandingPTUI implements Observer< LunarLandingModel, Object > {

    private LunarLandingModel model;


    public LunarLandingPTUI(String filename) throws IOException {
        final int BOARD_ROW;
        final int BOARD_COL;
        final int GOAL_ROW;
        final int GOAL_COL;
        ArrayList<String> inputs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
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
        this.model = new LunarLandingModel(startconfig);
        initializeView();
    }

    private void displayhelp(){
        System.out.println("(l)oad -  Abandon the current game and load a new puzzle from the provided file. " +
                "Enter: data/lunarlanding/puzzlename.txt");
        System.out.println("(r)eload - Reload the most recently successfully loaded file again. Enter: r");
        System.out.println("(c)hoose - Choose the figure that will move next. Enter: c 1 2");
        System.out.println("(g)o - Direct the chosen figure (from choose) (n)orth, (s)outh, (e)ast, or (w)est.");
        System.out.println("(h)int - Make a move for the user that will advance the puzzle state closer to the goal.");
        System.out.println("(s)how - Display the board");
        System.out.println("(a)ssist - Show all commands");
        System.out.println("(q)uit - Terminate the program");
    }

    private void displayboard(int moves){
        String [][]display = new String[this.model.currentConfig.BOARD_ROW+1][this.model.currentConfig.BOARD_COL+1];
        for(int j=1;j<this.model.currentConfig.BOARD_COL+1;j++){
            display[0][j] = Integer.toString(j-1);
        }
        for(int i=1;i<this.model.currentConfig.BOARD_ROW+1;i++){
            display[i][0] = Integer.toString(i-1);
        }
        display[0][0] = " ";
        System.out.println("Move Count: " +moves);
        for(int i=1;i<this.model.currentConfig.BOARD_ROW+1;i++){
            for(int j=1;j<this.model.currentConfig.BOARD_COL+1;j++){
                display[i][j] = this.model.currentConfig.board[i-1][j-1];
            }
        }
        display[this.model.currentConfig.GOAL_ROW+1][this.model.currentConfig.GOAL_COL+1]= "!"+display[this.model.currentConfig.GOAL_ROW+1][this.model.currentConfig.GOAL_COL+1] ;
        for(int i=0;i<this.model.currentConfig.BOARD_ROW+1;i++){
            for(int j=0;j<this.model.currentConfig.BOARD_COL+1;j++){
                System.out.print(display[i][j]+"  ");
            }
            System.out.println();
        }
    }
    private void run() throws IOException {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            if(this.model.gameSolved){
                System.out.println("Puzzle has been solved");
                break;
            }
            System.out.print( "game command: " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if ( words.length > 0 ) {
                if ( words[ 0 ].startsWith( "l" ) ) {
                    this.model.load(words[1]);
                    System.out.println("New Puzzle Successfully loaded!");
                }
                else if ( words[ 0 ].startsWith( "r" ) ) {
                    this.model.reset();
                }
                else if ( words[ 0 ].startsWith( "c" ) ) {
                    if(  this.model.choose(Integer.parseInt(words[1]),Integer.parseInt(words[2]))){
                        System.out.println("Command Successful");
                    }
                    else{
                        System.out.println("Command Failed");
                        System.out.println("Try choosing a real character this time");
                    }
                }
                else if ( words[ 0 ].startsWith( "g" ) ) {
                    if(words[1].startsWith("n")){
                        if(this.model.gonorth()){
                            System.out.println("Move Successful");
                        }else{
                            System.out.println("Choose a Player first!");
                        }
                    }
                    else if(words[1].startsWith("s")){
                        if(this.model.gosouth()){
                            System.out.println("Move Successful");
                        }else{
                            System.out.println("Choose a Player first!");
                        }
                    }
                    else if(words[1].startsWith("e")){
                        if(this.model.goeast()){
                            System.out.println("Move Successful");
                        }else{
                            System.out.println("Choose a Player first!");
                        }
                    }
                    else if(words[1].startsWith("w")){
                        if(this.model.gowest()){
                            System.out.println("Move Successful");
                        }else{
                            System.out.println("Choose a Player first!");
                        }
                    }
                    else{
                        System.out.println("Illegal Move");
                    }
                }
                else if ( words[ 0 ].startsWith( "h" ) ) {
                    if(this.model.Unsolvable){
                        System.out.println("The Board is unsolvable. Enter r to reload");
                    }
                    else
                        this.model.hint();
                }
                else if( words[ 0 ].startsWith( "s" )){
                    displayboard(this.model.Movecount);
                }
                else if(words[ 0 ].startsWith( "a" )){
                    displayhelp();
                }
                else if( words[ 0 ].startsWith( "q" )){
                    break;
                }
                else {
                    displayhelp();
                }
            }
        }
    }
    @Override
    public void update(LunarLandingModel lunarLandingModel, Object o) {
        displayboard(this.model.Movecount);
        if (this.model.currentConfig.isSolution()) {
            System.out.println("YOU WIN!");
            this.model.gameSolved = true;
        }



    }

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        LunarLandingPTUI l = new LunarLandingPTUI(args[0]);
        l.run();
    }
    public void initializeView() {
        this.model.addObserver( this );
        update( this.model, null );
    }


}
