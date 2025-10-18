package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        String key = (breed == null) ? null : breed.toLowerCase(Locale.ROOT);

        if (key != null && cache.containsKey(key)) {
            return cache.get(key);
        }

        callsMade++;
        try {
            List<String> result = fetcher.getSubBreeds(breed);
            if (key != null) {
                cache.put(key, result);
            }
            return result;
        } catch (BreedNotFoundException e) {
            throw new BreedNotFoundException(breed);
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
