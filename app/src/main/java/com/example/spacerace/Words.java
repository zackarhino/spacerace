package com.example.spacerace;

import java.util.ArrayList;

/**
 * This class holds all the Word Generator words.
 * I was initially going to create a string resource file, but it is limited in the sense that
 * it's difficult to get a random ID from the resources
 * @version 1.0.0
 * @author Zachary Allard
 */
public class Words {
    public static final ArrayList<String> SPACE_WORDS = new ArrayList<String>(){{
            add("Nebula");
            add("Galaxy");
            add("Star");
            add("Planet");
            add("Sol");
            add("Mercury");
            add("Venus");
            add("Earth");
            add("Mars");
            add("Jupiter");
            add("Saturn");
            add("Uranus");
            add("Neptune");
            add("Pluto");
            add("Solar System");
            add("Celestial");
            add("Light Year");
            add("Moon");
            add("Asteroid");
            add("Meteor");
            add("Orbit");
            add("Rover");
            add("NASA");
            add("SpaceX");
            add("Armstrong");
            add("Challenger");
    }};
    public static final ArrayList<String> NERDY_WORDS = new ArrayList<String>(){{
            add("Phaser");
            add("Lightsaber");
            add("Cantina");
            add("Arwing");
            add("Falcon");
            add("Plasma");
            add("Picard");
            add("Star Log");
            add("Red Alert");
            add("Slippy");
            add("Andross");
            add("Cyborg");
            add("Cyberpunk");
            add("Major Tom");
            add("Luma");
            add("Matrix");
            add("Robocop");
            add("Novakid");
            add("Solo");
            add("Sci-fi");
    }};
    public static final ArrayList<String> FUN_WORDS = new ArrayList<String>(){{
            add("Jazz Music");
            add("Puppies");
            add("Coffee");
            add("Bass Guitar");
            add("Painting");
            add("Television");
            add("MIDI");
            add("Hello, World!");
            add("Snacks");
            add("Console");
            add("Computer");
            add("Friends");
            add("Family");
            add("Shopping");
            add("Whistle");
            add("Singing");
            add("Piano");
            add("Keyboard");
            add("Hiking");
            add("Semitone");
            add("Pop");
    }};

    // Adds all the words from the other lists so that it's always up to date
    public static final ArrayList<String> getAllWords(){
        ArrayList<String> ALL_WORDS = new ArrayList<>();
        ALL_WORDS.addAll(SPACE_WORDS);
        ALL_WORDS.addAll(NERDY_WORDS);
        ALL_WORDS.addAll(FUN_WORDS);
        return ALL_WORDS;
    }
}
