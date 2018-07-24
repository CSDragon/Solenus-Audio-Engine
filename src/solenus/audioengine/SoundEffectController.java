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
import src.main.java.com.adonax.audiocue.AudioCue;
import src.main.java.com.adonax.audiocue.AudioCueInstanceEvent;
import src.main.java.com.adonax.audiocue.AudioCueListener;

/**
 * A Sound Effect Controller is an object that any object that uses sound effects should contain.
 * It manages the object's sounds and interfaces with the global sound controller.
 * @author Chris Scott
 */
public class SoundEffectController implements AudioCueListener
{
    
    /**Shortcut value for Sound objects*/
    public static final int SOUND = 0;
    /**Shortcut value for Bookmark Sound objects*/
    public static final int BOOKMARKSOUND = 1;
    /**Shortcut value for Looping Sound objects*/
    public static final int LOOPSOUND = 2;
    
    /**Default value for audio cue parallel cues*/
    public static final int DEFAULT_PARALLEL = 1;

    private long sfxRegNum;
    private HashMap<String, Sound> preloadSounds;
    private HashMap<String, AudioCue> preloadCues;
    
    /**
     * Default constructor.
     * Sets up the basics.
     */
    public SoundEffectController()
    {
        preloadSounds = new HashMap<>();
        preloadCues = new HashMap<>();
        sfxRegNum = GlobalSoundController.registerSoundEffectController(this);
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
        Sound s = new Sound(sourceFile, volumeType);
        
        //If we wanted to name this sound differently, do so here.
        if(name != null)
            s.setName(name);
        //Add the sound to the controller.
        preloadSounds.put(s.getName(), s);
        
        try
        {
            //Create the Audio Cue object
            AudioCue ac = AudioCue.makeStereoCue(sourceFile.toURI().toURL(), DEFAULT_PARALLEL);

            //Add audio listener
            ac.addAudioCueListener(this);
            
            //Change the name, if needed.
            if(name != null)
                ac.setName(name);
            
            //Add to the sound controller
            preloadCues.put(ac.getName(), ac);
            
            //open the cue
            ac.open();
        }
        catch(Exception e)
        {
            System.out.println("That's not a wav file");
        }
    }
    
    
    /**
     * Adds a sound to the preloaded sounds
     * @param s The sound to be added.
     */
    public void addSound(Sound s)
    {
        preloadSounds.put(s.getName(), s);
    }
    
    public void addCue(AudioCue ac)
    {
        preloadCues.put(ac.getName(), ac);
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
    public AudioCue getSound(String name)
    {
        return preloadCues.get(name);
    }

    
    
    
    //Audio Cue Overhead
    //<editor-fold desc="Listen Functions">
    
    /*
     * The following methods implement AudioCueListener.
     * 
     * (non-Javadoc)
     * @see com.adonax.audiocue.AudioCueListener#audioCueOpened(long, int, int, com.adonax.audiocue.AudioCue)
     */
    @Override
    public void audioCueOpened(long now, int threadPriority, int bufferSize, AudioCue source) 
    {
        System.out.println("AudioCueListener.open for AudioCue: " + source.getName() + " called at " + now + " milliseconds.");
        System.out.println("\tCue length = " + source.getFrameLength());
        System.out.println("\tThread priority = " + threadPriority);
        System.out.println("\tBuffer size = " + bufferSize);
    }

    @Override
    public void audioCueClosed(long now, AudioCue source) 
    {
        System.out.println("AudioCueListener.close for AudioCue: " + source.getName() + " called at " + now  + " milliseconds.");
    }

    @Override
    public void instanceEventOccurred(AudioCueInstanceEvent event)
    {
        switch (event.type)
        {
            case OBTAIN_INSTANCE:
                System.out.println("AudioCueListener.instanceEventOccurred " + "called at " + event.time + " milliseconds.");	
                System.out.println("\tObtainInstance called for " + event.source.getName() + ", instance #: " + event.instanceID);
                break;

            case LOOP:
                System.out.println("AudioCueListener.instanceEventOccurred " + "called at " + event.time + " milliseconds.");	
                System.out.println("\tLoop called for " + event.source.getName() + " instance #: " + event.instanceID);
                break;

            case RELEASE_INSTANCE:
                System.out.println("AudioCueListener.instanceEventOccurred " + "called at " + event.time + " milliseconds.");	
                System.out.println("\tReleaseInstance called for instance #: " + event.instanceID);	
                break;

            case START_INSTANCE:
                System.out.println("AudioCueListener.instanceEventOccurred " + "called at " + event.time + " milliseconds.");	
                System.out.println("\tStart called at sample frame: "  + event.frame);
                System.out.println("\tStart called for "  + event.source.getName() + " instance #: " + event.instanceID);
                break;

            case STOP_INSTANCE:
                System.out.println("AudioCueListener.instanceEventOccurred " + "called at " + event.time + " milliseconds.");
                System.out.println("\tStop called at sample frame: " + event.frame);
                System.out.println("\tStop called for " + event.source.getName() + " instance #: " + event.instanceID);
                break;

            default:
                break;
        }
    }
    
    //</editor-fold>
    
}
