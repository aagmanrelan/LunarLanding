package puzzles.water;

import puzzles.clock.ClockConfiguration;
import solver.Configuration;
import solver.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * This class defines the WaterConfiguration
 * Every WaterConfiguration  has an amount which is the desired amount of water needed in the correct configuration
 * An Integer Array containing the maximum limits of water buckets can handle.
 * An integer Array containing the current level of the water buckets.
 *
 * @author Aagman Relan
 */

public class WaterConfiguration implements Configuration {
    private int amount;
    private int[] bucketsCapacity;
    private int [] currentLevel;
    private static int totalConfigurations=1;

    public WaterConfiguration(int amount, int[] bucketsCapacity,int [] currentLevel){
        this.amount = amount;
        this.bucketsCapacity = bucketsCapacity;
        this.currentLevel = currentLevel;
    }

    /**
     *
     * @param  obj WaterConfiguration
     * @return It will return a boolean which will contain whether a WaterConfiguration is
     * equal to the current WaterConfiguration
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof WaterConfiguration c){
            boolean result = amount == c.amount;
            for (int i=0;i< currentLevel.length;i++){
                result = result && (c.currentLevel[i] == currentLevel[i]);
            }
            return result;
        }
        return false;
    }

    /**
     * Generates the hash code fot the configuration
     * @return the hashcode for the WaterConfiguration
     */
    @Override
    public int hashCode(){
        int hash=0;
        for (int j : currentLevel) {
            hash = hash + j;
        }
        return hash;
    }

    /**
     *
     * @return a boolean indicating whether the current configuration is the accepted configuration
     */
    @Override
    public boolean isSolution() {
        for (int j : currentLevel) {
            if (j == amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will create an Instance of the Solver class which will call the solve method.
     *  The method will return a predecessor map which will then be searched for winning/ accepted configuration
     *  if it is there a path to get to the configuration is printed otherwise no solution is printed.
     */
    public void solve(){
        Solver waterSolver = new Solver();
        HashMap<Configuration,Configuration> solution = waterSolver.solve(this);
        System.out.print("Amount: "+amount+", Buckets: [");
        for(int i=0;i< currentLevel.length-1;i++){
            System.out.print(bucketsCapacity[i]+", ");
        }
        System.out.println(bucketsCapacity[currentLevel.length-1]+"]");
        System.out.println("Total Configs: "+totalConfigurations);
        System.out.println("Unique Configs: "+solution.keySet().size());
        WaterConfiguration winningnumber=null;
        for(Configuration w: solution.keySet()){
            if(w.isSolution()){
                winningnumber = (WaterConfiguration) w;
            }
        }
        if (winningnumber != null) {
            Stack<WaterConfiguration> w = new Stack<>();
            w.add(winningnumber);

            while(winningnumber!=null){
                winningnumber = (WaterConfiguration) solution.get(winningnumber);
                w.add(winningnumber);
            }
            w.pop();
            int i=0;
            while(!w.isEmpty()){
                System.out.println("Step "+ i++ +": "+w.pop());
            }
        }
        else{
            System.out.println("No Solution");
        }
    }

    /**
     *  This will return the neighbors of the current WaterConfiguration.
     *  Every WaterConfiguration bucket will have 3 cases,
     *  fill up the bucket, dump the bucket or transfer the bucket.
     * @return A list containing the neighbors of the current WaterConfiguration
     */
    @Override
    public List<Configuration> neighbors() {
        List<Configuration> neighbor = new ArrayList<>();
        for(int i=0;i< currentLevel.length;i++){
            int [] currentConfig;
            for(int k=0;k<currentLevel.length;k++){
                 currentConfig = currentLevel.clone();
                currentConfig[k]=bucketsCapacity[k];
                totalConfigurations++;
                neighbor.add(new WaterConfiguration(amount,bucketsCapacity,currentConfig));
            }
            for(int k=0;k< currentLevel.length;k++){
                currentConfig = currentLevel.clone();
                if( k!=i ){
                    if(currentConfig[k]<bucketsCapacity[k]){
                        int remainder = bucketsCapacity[k]-currentConfig[k];
                        if(currentConfig[i]>remainder){
                            currentConfig[k]+= remainder;
                            currentConfig[i]-= remainder;
                            totalConfigurations++;
                            neighbor.add(new WaterConfiguration(amount,bucketsCapacity,currentConfig));
                        }
                        else{
                            currentConfig[k]+=currentConfig[i];
                            currentConfig[i]=0;
                            totalConfigurations++;
                            neighbor.add(new WaterConfiguration(amount,bucketsCapacity,currentConfig));
                        }
                    }
                }
            }
            for (int k=0;k<currentLevel.length;k++){
                currentConfig = currentLevel.clone();
                currentConfig[k]= 0;
                totalConfigurations++;
                neighbor.add(new WaterConfiguration(amount,bucketsCapacity,currentConfig));
            }
        }
        return neighbor;
    }

    public String toString(){
        StringBuilder s = new StringBuilder("[");
        for(int i=0;i<currentLevel.length-1;i++){
            s.append(currentLevel[i]).append(", ");
        }
        s.append(currentLevel[currentLevel.length - 1]);
        return s+"]";
    }
}