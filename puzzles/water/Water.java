package puzzles.water;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Aagman Relan
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            int amount = Integer.parseInt(args[0]);
            int [] bucketsCapacity = new int[args.length-1];

            int [] currentLevel = new int[args.length-1];
            for(int i=0;i<args.length-1;i++){
                bucketsCapacity[i] = Integer.parseInt(args[i+1]);
            }
            for(int i=0;i<args.length-1;i++){
                currentLevel[i] = 0;
            }

            // YOUR MAIN CODE HERE
            WaterConfiguration wc = new WaterConfiguration(amount,bucketsCapacity,currentLevel);
            wc.solve();
        }
    }
}
