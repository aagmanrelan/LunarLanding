package solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Solver {
    private Queue<Configuration> queue ;
    private HashMap<Configuration,Configuration> PredecessorMap ;

    public Solver(){
        queue = new LinkedList<>();
        PredecessorMap = new HashMap<>();
    }

    /**
     *  This method will do a BFS algorithm to find the path tho the solution.
     *  It will put the unique configuration in a HashMAp and return it.
     * @param startConfig: The starting configuration of the puzzle
     * @return the HashMap which has the Key and Values as Configurations
     */
    public HashMap<Configuration,Configuration> solve(Configuration startConfig){
        queue.add(startConfig);
        PredecessorMap.put(startConfig,null);
        while(!queue.isEmpty()){

            Configuration current = queue.remove();
            if(current.isSolution()){
                return PredecessorMap;
            }
            else{
                for ( Configuration neighbor : current.neighbors()){
                    if(!PredecessorMap.containsKey(neighbor)){
                        queue.add(neighbor);
                        PredecessorMap.put(neighbor,current);

                    }
                }
            }
        }
        return PredecessorMap;
    }
}

