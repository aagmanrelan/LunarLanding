package puzzles.lunarlanding.model;

import solver.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents the Current Configuration of the Game. It tracks the Dimensions of the game board. The coordinates
 * of the Landing port, and the all the inputs. It also keeps track of all the pieces in a 2D string array
 * It also has a ArrayList of strings which have the data regarding the initial position of Robots and the explorer
 * @author Aagman Relan
 * December 2021
 */
public class LunarLandingConfig implements Configuration {

    public String[][] board;
    public final int BOARD_ROW;
    public final int BOARD_COL;
    public final int GOAL_ROW;
    public final int GOAL_COL;
    public ArrayList<String> inputs;

    /**
     * @param boardrow The Row dimension of the Game Board
     * @param boardcol The Column dimension of the Game Board
     * @param goalrow  The Row Coordinate of the Goal
     * @param goalcol   The Column coordinate of the Goal
     * @param inputs A String ArrayList that has the locations of all the Robots and Explorer
     */
    public LunarLandingConfig(int boardrow, int boardcol, int goalrow, int goalcol, ArrayList<String> inputs){
        BOARD_ROW = boardrow;
        BOARD_COL = boardcol;
        GOAL_ROW = goalrow;
        GOAL_COL = goalcol;
        this.inputs = inputs;

        board = new String[boardrow][boardcol];

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                if(board[i][j]==null){
                    board[i][j] = "_";
                }
            }
        }

        for(String input : inputs){
            String []i = input.split("\\s");
            int row = Integer.parseInt(i[1]);
            int col = Integer.parseInt(i[2]);
            board[row][col] = i[0];
        }
    }

    /**
     * This is a Copy Constructor Which makes a new LunarLandingConfig from the passed configuration
     * @param other The
     */
    protected LunarLandingConfig(LunarLandingConfig other){
        BOARD_ROW = other.BOARD_ROW;
        BOARD_COL = other.BOARD_COL;
        GOAL_ROW = other.GOAL_ROW;
        GOAL_COL = other.GOAL_COL;
        this.inputs = other.inputs;

        board = new String[BOARD_ROW][BOARD_COL];

        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                board[i][j] = other.board[i][j];
            }
        }
    }


    /**
     * @return True or False indicating if the current configuration is the solution or not.
     */
    @Override
    public boolean isSolution() {
        return board[GOAL_ROW][GOAL_COL].equals("E");
    }

    /**
     * This function generates and returns all the neighbors from the current LunarLandingConfiguration
     * @return Returns a List of neighbors of the current configuration
     */
    @Override
    public List<Configuration> neighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                    if(!board[i][j].equals("_")){
                       int k=j+1;
                       while(k<BOARD_COL){
                           if(!board[i][k].equals("_")){
                               LunarLandingConfig successor = new LunarLandingConfig(this);
                               String temp = successor.board[i][k-1];
                               successor.board[i][k-1]=successor.board[i][j];
                               successor.board[i][j]=temp;
                               neighbors.add(successor);
                               break;
                           }
                           k++;
                       }
                        k=j-1;
                       while (k>=0){
                           if(!board[i][k].equals("_")){
                               LunarLandingConfig successor = new LunarLandingConfig(this);
                               String temp = successor.board[i][k+1];
                               successor.board[i][k+1]=successor.board[i][j];
                               successor.board[i][j]=temp;
                               neighbors.add(successor);
                               break;
                           }
                           k--;
                       }
                        k = i+1;
                       while(k<BOARD_ROW){
                           if(!board[k][j].equals("_")){
                               LunarLandingConfig successor = new LunarLandingConfig(this);
                               String temp = successor.board[k-1][j];
                               successor.board[k-1][j]=successor.board[i][j];
                               successor.board[i][j]=temp;
                               neighbors.add(successor);
                               break;
                           }
                           k++;
                       }
                        k = i-1;
                       while(k>=0){
                           if(!board[k][j].equals("_")){
                               LunarLandingConfig successor = new LunarLandingConfig(this);
                               String temp = successor.board[k+1][j];
                               successor.board[k+1][j]=successor.board[i][j];
                               successor.board[i][j]=temp;
                               neighbors.add(successor);
                               break;
                           }
                           k--;
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * @return Return the Integer hash code based on the current configuration
     */
    @Override
    public int hashCode() {
        int hash = 0;
            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    if(board[i][j].length()==1){
                        if(!board[i][j].equals("_")){
                            hash = hash + (i+1)*(j+1)*board[i][j].charAt(0);
                        }
                    }
                }
            }
        return hash;
    }

    /**
     * @param obj : Generic Java Object
     * @return Boolean indicating if the obj is equal to the current object.
     */
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof LunarLandingConfig other){
            for(int i=0;i<BOARD_ROW;i++){
                for(int j=0;j<BOARD_COL;j++){
                    if(!board[i][j].equals(other.board[i][j])){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return  A String representation of the current Board
     */
    @Override
    public String toString() {
        StringBuilder s= new StringBuilder();
        for(int i=0;i<BOARD_ROW;i++){
            for(int j=0;j<BOARD_COL;j++){
                s.append(board[i][j]).append(" ");
            }
            s.append('\n');
        }
        return s.toString();
    }
}
