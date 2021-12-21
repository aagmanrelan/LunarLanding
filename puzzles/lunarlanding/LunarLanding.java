package puzzles.lunarlanding;

import puzzles.lunarlanding.model.LunarLandingConfig;
import puzzles.water.WaterConfiguration;
import solver.Configuration;
import solver.Solver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
/**
 * This file is responsible to take in the command line arguments and make the game configuration  and  then solve the
 * game and display it on the console step by step.
 * @author Aagman Relan
 * December 2021
 */
public class LunarLanding {
    /**
     *This method prints out the current state of the game from its current configuration
     * @param currentConfig The current configuration of the game.
     */
    private static void displayboard(LunarLandingConfig currentConfig){
        String [][]display = new String[currentConfig.BOARD_ROW+1][currentConfig.BOARD_COL+1];
        for(int j=1;j<currentConfig.BOARD_COL+1;j++){
            display[0][j] = Integer.toString(j-1);
        }
        for(int i=1;i<currentConfig.BOARD_ROW+1;i++){
            display[i][0] = Integer.toString(i-1);
        }
        display[0][0] = " ";

        for(int i=1;i<currentConfig.BOARD_ROW+1;i++){
            for(int j=1;j<currentConfig.BOARD_COL+1;j++){
                display[i][j] = currentConfig.board[i-1][j-1];
            }
        }
        display[currentConfig.GOAL_ROW+1][currentConfig.GOAL_COL+1]= "!"+display[currentConfig.GOAL_ROW+1][currentConfig.GOAL_COL+1] ;
        for(int i=0;i<currentConfig.BOARD_ROW+1;i++){
            for(int j=0;j<currentConfig.BOARD_COL+1;j++){
                System.out.print(display[i][j]+"  ");
            }
            System.out.println();
        }
    }
    /**
     * This is the main method, it takes in the command line argument and reads from the specified file to construct the
     * game. The method then calls on the Solver's solve method to solve the puzzle
     * The solved puzzle is then displayed in step by step progression.
     * @param args The path to the game txt file which will be used to construct the game.
     * @throws IOException The exception is only thrown if the file specified does not exist.
     */
    public static void main( String[] args ) throws IOException {
         final int BOARD_ROW;
         final int BOARD_COL;
         final int GOAL_ROW;
         final int GOAL_COL;
        ArrayList<String> inputs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
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
        Solver s = new Solver();
        HashMap<Configuration,Configuration> h = s.solve(startconfig);


        LunarLandingConfig winningconfig=null;
        for(Configuration configuration: h.keySet()){
            if(configuration.isSolution()){
                winningconfig = (LunarLandingConfig) configuration;
            }
        }

        if (winningconfig != null) {
            Stack<LunarLandingConfig> steps = new Stack<>();
            steps.add(winningconfig);

            while(winningconfig!=null){
                winningconfig = (LunarLandingConfig) h.get(winningconfig);
                steps.add(winningconfig);
            }
            steps.pop();
            int i=0;
            while(!steps.isEmpty()){
                System.out.println();
                System.out.println("Step "+ i++ +":");
                displayboard(steps.pop());
            }
        }
        else{
            System.out.println("No Solution");
        }


    }
}
