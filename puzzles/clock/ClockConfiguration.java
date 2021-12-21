package puzzles.clock;
import solver.Configuration;
import solver.Solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
/**
 * @author Aagman Relan
 */

/**
 * This is the ClockConfiguration class , it defines the ClockConfiguration
 * Every ClockConfiguration has hours
 * a start and an end
 * hours are the number of hours which the clock is going to have
 * start is the start point for our puzzle
 * end is the end point for our puzzle.
 */
public class ClockConfiguration implements Configuration {

    private int hours;
    private int start;
    private int end;

    private static int totalConfigurations=1;

    /**
     *
     * @param hours Number of hours on the clock
     * @param start Starting point of the puzzle
     * @param end Ending point of the puzzle
     */
    public ClockConfiguration(int hours,int start,int end){
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * @return if the current Clockconfiguration is the solution or not.
     */
    @Override
    public boolean isSolution() {
        return start==end;
    }

    /**
     * @return A list containing the neighbors of the current ClockConfiguration
     */
    @Override
    public List<Configuration> neighbors() {
        List<Configuration> neighbors= new ArrayList<>();
        int previous = start -1;
        int forward = start +1;

        if(previous<1){
            previous = hours;
        }
        if(forward>hours){
            forward=1;
        }

        neighbors.add(new ClockConfiguration(hours,previous,end));
        neighbors.add(new ClockConfiguration(hours,forward,end));
        totalConfigurations+=2;
        return neighbors;
    }

    /**
     *
     * @param   obj ClockConfiguration
     * @return It will return a boolean which will contain whether a clockConfiguration is
     * equal to the current ClockConfiguration
     */

    @Override
    public boolean equals(Object obj){
        if(obj instanceof ClockConfiguration c){
            return c.end == end && c.start == start && c.hours == hours;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return start;
    }

    /**
     * This method will create an Instance of the Solver class which will call the solve method.
     * The method will return a predecessor map which will then be searched for winning/ accepted configuration
     * if it is there a path to get to the configuration is printed otherwise no solution is printed.
     */
    public void solve(){
        Solver clocksolver = new Solver();
        HashMap<Configuration,Configuration> solution = clocksolver.solve(this);

        System.out.println("Hours: "+hours+", Start: "+start+", End: "+end);
        System.out.println("Total Configs: "+totalConfigurations);
        if(solution.containsKey(new ClockConfiguration(hours,end,end))){
            System.out.println("Unique Configs: "+solution.keySet().size());
            ClockConfiguration key = new ClockConfiguration(hours,end,end);
                Stack<ClockConfiguration> s = new Stack<>();
                s.add(key);
                while (key!=null) {
                     key = (ClockConfiguration) solution.get(key);
                        s.add(key);
                }
                s.pop();
                int i=0;
                while(!s.isEmpty()){
                        System.out.println("Step "+ i++ +": "+s.pop().start);
                }
        }
        else{
            System.out.println("Unique Configs: "+solution.keySet().size());
            System.out.println("No Solution");
        }

    }
}
