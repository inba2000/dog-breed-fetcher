package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private final BreedFetcher underlyingFetcher;
    private final Map<String, List<String>> cache;
    private int callsMade = 0;
    
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.underlyingFetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Check if the result is already cached
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }
        
        // Not in cache, call the underlying fetcher
        callsMade++;
        
        try {
            List<String> result = underlyingFetcher.getSubBreeds(breed);
            // Cache the successful result
            cache.put(breed, result);
            return result;
        } catch (BreedNotFoundException e) {
            // Don't cache exceptions - let them propagate
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}