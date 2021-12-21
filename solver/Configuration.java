package solver;

import java.util.List;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Aagman Relan
 * November 2021
 */
public interface Configuration {

    // Tips
    // Include methods
    // - for the solver: is-goal, get-successors
    // - for get-successors: a copy constructor (can't declare here)
    // - for equality comparison and hashing
    // - for creating a displayable version the configuration
    boolean isSolution();
    List<Configuration> neighbors();
    boolean equals(Object obj);
    int hashCode();
    String toString();
}
