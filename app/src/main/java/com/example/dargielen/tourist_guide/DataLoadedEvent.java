package com.example.dargielen.tourist_guide;

/**
 * Created by dargielen on 09.05.2017.
 */

public class DataLoadedEvent {

    private Attraction[] loadedAttractions;

    public DataLoadedEvent(Attraction[] attractions) {
        loadedAttractions = attractions;
    }

    public Attraction[] getLoadedAttractions() {
        return loadedAttractions;
    }
}
