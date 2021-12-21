package puzzles.lunarlanding.model;

import solver.Configuration;
import solver.Solver;
import util.Observer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This is the Game model, it will have all the methods that will be required by the GUI and the PTUI to run properly.
 * This will also keep track of some important game State variables.
 * @author Aagman Relan
 * December 2021
 */
public class LunarLandingModel {

    /*
    These are some of the Game state variables that will be tracked throughout the game session.
     */
    public LunarLandingConfig currentConfig;
    private List<Observer<LunarLandingModel, Object>> observers;
    public int PlayerRow;
    public int PlayerColumn;
    private LunarLandingConfig originalConfig;
    public Boolean gameSolved;
    public int Movecount;
    public boolean Unsolvable;

    public void addObserver(Observer<LunarLandingModel, Object> obs) {
        this.observers.add(obs);
    }

    /**
     * @param config The Model will initialize itself using the game configuration that is passed in its arguments.
     */
    public LunarLandingModel(LunarLandingConfig config) {
        this.currentConfig = config;
        this.PlayerRow = -1;
        this.PlayerColumn = -1;
        this.Movecount = 0;
        this.observers = new LinkedList<>();
        this.gameSolved = false;
        this.Unsolvable = false;
        this.originalConfig = new LunarLandingConfig(currentConfig);
    }

    /**
     * This method will be activated whenever out player clicks a button, it will check to see if a movable objects is at this location
     * @param row The row of the player
     * @param col The column of the player
     * @return A boolean indicating if the object is movable or not.
     */
    public boolean choose(int row, int col) {
        if (!currentConfig.board[row][col].equals("_")) {
            PlayerRow = row;
            PlayerColumn = col;
            announce(null);
            return true;
        }
        return false;
    }

    /**
     * This will make the game move one turn in the correct direction. This works as a hint to the player
     */
    public void hint() {

        Solver solver = new Solver();
        HashMap<Configuration, Configuration> Predecessormap = solver.solve(currentConfig);
        LunarLandingConfig winningconfig = null;
        for (Configuration configuration : Predecessormap.keySet()) {
            if (configuration.isSolution()) {
                winningconfig = (LunarLandingConfig) configuration;
            }
        }
        if (winningconfig != null) {
            Stack<LunarLandingConfig> steps = new Stack<>();
            steps.add(winningconfig);
            while (winningconfig != null) {
                winningconfig = (LunarLandingConfig) Predecessormap.get(winningconfig);
                steps.add(winningconfig);
            }
            steps.pop();
            steps.pop();
            Movecount++;
            currentConfig = new LunarLandingConfig(steps.pop());
        } else {
            Unsolvable = true;
        }
        announce(null);
    }

    /**
     * This will move the chosen player in the north direction if possible
     * @return Boolean indicating if the move is successful or not.
     */
    public boolean gonorth() {
        if (choose(PlayerRow, PlayerColumn)) {
            Movecount++;
            int k = PlayerRow - 1;
            while (k >= 0) {
                if (!currentConfig.board[k][PlayerColumn].equals("_")) {
                    String temp = currentConfig.board[k+1][PlayerColumn];
                    currentConfig.board[k + 1][PlayerColumn] = currentConfig.board[PlayerRow][PlayerColumn];
                    currentConfig.board[PlayerRow][PlayerColumn] = temp;
                    break;
                }
                k--;
            }
            announce(null);
            return true;
        } else {
            return false;
        }
    }
    /**
     * This will move the chosen player in the south direction if possible
     * @return Boolean indicating if the move is successful or not.
     */
    public boolean gosouth() {
        if (choose(PlayerRow, PlayerColumn)) {
            Movecount++;
            int k = PlayerRow + 1;
            while (k < currentConfig.BOARD_ROW) {
                if (!currentConfig.board[k][PlayerColumn].equals("_")) {
                    String temp = currentConfig.board[k-1][PlayerColumn];
                    currentConfig.board[k - 1][PlayerColumn] = currentConfig.board[PlayerRow][PlayerColumn];
                    currentConfig.board[PlayerRow][PlayerColumn] = temp;
                    break;
                }
                k++;
            }

            announce(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This will move the chosen player in the east direction, if possible
     * @return Boolean indicating if the move is successful or not.
     */
    public boolean goeast() {
        if (choose(PlayerRow, PlayerColumn)) {
            Movecount++;
            int k = PlayerColumn + 1;
            while (k < currentConfig.BOARD_COL) {
                if (!currentConfig.board[PlayerRow][k].equals("_")) {
                    String temp = currentConfig.board[PlayerRow][k - 1];
                    currentConfig.board[PlayerRow][k - 1] = currentConfig.board[PlayerRow][PlayerColumn];
                    currentConfig.board[PlayerRow][PlayerColumn] = temp;
                    break;
                }
                k++;
            }
            announce(null);
            return true;
        } else {
            return false;
        }
    }
    /**
     * This will move the chosen player in the west direction, if possible
     * @return Boolean indicating if the move is successful or not.
     */
    public boolean gowest() {
        if (choose(PlayerRow, PlayerColumn)) {
            Movecount++;
            int k = PlayerColumn - 1;
            while (k>=0) {
                if (!currentConfig.board[PlayerRow][k].equals("_")) {
                    String temp = currentConfig.board[PlayerRow][k + 1];
                    currentConfig.board[PlayerRow][k + 1] = currentConfig.board[PlayerRow][PlayerColumn];
                    currentConfig.board[PlayerRow][PlayerColumn] = temp;
                    break;
                }
                k--;
            }
            announce(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method will load the new game from the given file.
     * @param filename The name of the new game file
     * @throws IOException : Throws exception if the given file does not exist
     */
    public void load(String filename) throws IOException {
        String path =  filename;
        final int BOARD_ROW;
        final int BOARD_COL;
        final int GOAL_ROW;
        final int GOAL_COL;
        ArrayList<String> inputs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        String[] indexes = line.split("\\s");
        BOARD_ROW = Integer.parseInt(indexes[0]);
        BOARD_COL = Integer.parseInt(indexes[1]);
        GOAL_ROW = Integer.parseInt(indexes[2]);
        GOAL_COL = Integer.parseInt(indexes[3]);
        line = reader.readLine();
        while (line.length() != 0) {
            inputs.add(line);
            line = reader.readLine();
        }
        LunarLandingConfig startconfig = new LunarLandingConfig(BOARD_ROW, BOARD_COL, GOAL_ROW, GOAL_COL, inputs);
        this.currentConfig = startconfig;
        this.originalConfig = new LunarLandingConfig(BOARD_ROW, BOARD_COL, GOAL_ROW, GOAL_COL, inputs);
        announce(null);
    }

    /**
     * @return String representation of the Current state of the game
     */
    @Override
    public String toString() {
        return currentConfig.toString();
    }

    /**
     * This will reset the game model to its initial state.
     */
    public void reset() {
        currentConfig = new LunarLandingConfig(originalConfig);
        announce(null);

    }

    /**
     * This will announce the observers whenever there are updates to game model.
     * @param arg Arguments if any
     */
    private void announce(String arg) {
        for (var obs : this.observers) {
            obs.update(this, arg);
        }
    }
}
