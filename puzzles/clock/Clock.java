package puzzles.clock;

import solver.Solver;

/**
 * Main class for the "clock" puzzle.
 *
 * @author Aagman Relan
 */
public class Clock {
    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            int hours = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);

            ClockConfiguration ck = new ClockConfiguration(hours,start,end);
            ck.solve();
        }
    }
}
