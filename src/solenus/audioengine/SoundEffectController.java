/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine;

import java.io.File;
import java.util.HashMap;
import solenus.audioengine.sound.BookmarkSound;
import solenus.audioengine.sound.LoopSound;
import solenus.audioengine.sound.Sound;

/**
 * A Sound Effect Controller is an object that any object that uses sound effects should contain.
 * It manages the object's sounds and interfaces with the global sound controller.
 * @author Chris Scott
 */
public class SoundEffectController 
{
    
    /**Shortcut value for Sound objects*/
    public static final int SOUND = 0;
    /**Shortcut value for Bookmark Sound objects*/
    public static final int BOOKMARKSOUND = 1;
    /**Shortcut value for Looping Sound objects*/
    public static final int LOOPSOUND = 2;

    private long sfxRegister;
    private HashMap<String, Sound> preloadSounds;
    
    /**
     * Default constructor.
     * Sets up the basics.
     */
    public SoundEffectController()
    {
        preloadSounds = new HashMap<>();
        sfxRegister = GlobalSoundController.registerSoundEffectController(this);
    }
    
    /**
     * @param sourceFile The source file.
     * @param soundType The type of Sound this Sound is (Bookmark, Looping, etc). Default: 0 for generic sound.
     * @param volumeType The volume slider this Sound uses. Default: 0 for default volume slider.
     * @param name The name of the sound. Input null to default to the Sound's default name.
     */
    public void addSound(File sourceFile, int soundType, int volumeType, String name)
    {
        //Create the Sound object
        Sound s;
        switch (soundType)
        {
            case BOOKMARKSOUND:
                s = new BookmarkSound(sourceFile, volumeType);
                break;
                
            case LOOPSOUND:
                s = new LoopSound(sourceFile, volumeType);
                break;
                
            default:
                s = new Sound(sourceFile, volumeType);
                break;
        }
        
        //If we wanted to name this sound differently, doso here.
        if(name != null)
            s.setName(name);
        //Add the sound to the controller.
        preloadSounds.put(s.getName(), s);
    }
    
    
    /**
     * Adds a sound to the preloaded sounds
     * @param s The sound to be added.
     */
    public void addSound(Sound s)
    {
        preloadSounds.put(s.getName(), s);
    }
    
    
    /**
     * Ends a Sound Effect Controller, stopping all its sounds and closing their input streams.
     */
    public void end()
    {
        //for each Sound
        for(Sound s : preloadSounds.values())
        {
            //stop playback and close the stream
            s.stop();
            s.close();
        }
        //clear the map
        preloadSounds.clear();
    }
    
    
    /**
     * Gets a sound by name.
     * @param name The name of the sound we're looking for.
     * @return The sound
     */
    public Sound getSound(String name)
    {
        return preloadSounds.get(name);
    }
    
}
