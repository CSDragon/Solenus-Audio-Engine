/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import solenus.audioengine.sound.BookmarkSound;
import solenus.audioengine.sound.LoopSound;
import solenus.audioengine.sound.Sound;

/**
 *
 * @author Chris Scott
 */
public class GlobalSoundController 
{
    
    
    //Global Volume constants. Add as nessiary.
    private static int globalVolumeTypes = 4;
    /** Constant for Generic Sounds. Use when declaring a Sound object as Generic, or attempting to access the global volume for Generic Sounds*/
    public static final int GLOBALGENERIC = 0;
    /** Constant for Music Sounds. Use when declaring a Sound object as Music, or attempting to access the global volume for Music Sounds*/
    public static final int GLOBALMUSIC = 1;
    /** Constant for Sound Effect Sounds. Use when declaring a Sound object as a Sound Effect, or attempting to access the global volume for Sound Effect Sounds*/
    public static final int GLOBALSFX = 2;
    /** Constant for Voice Sounds. Use when declaring a Sound object as Voice, or attempting to access the global volume for Voice Sounds*/
    public static final int GLOBALVOICE = 3;
    
    //globalVolumes contains individual volume sliders for types of sounds.    
    private static double[] globalVolumes;
   
    //globalOverallVolume is an overall volume slider.
    private static double globalOverallVolume;
    
    //sound effect controller control
    private static HashMap<Long, SoundEffectController> soundEffects;
    private static long numSFX;
    private static long lastSFXIndex;
    
    
    //TESTING Variables
    private static LoopSound mus1;
    private static Clip mus2;
    private static double volTest =0.91;
    
    /**
     * Initializes the sound system.
     */
    public static void initialize()
    {
        globalVolumes = new double[globalVolumeTypes];
        for(int i = 0; i < globalVolumeTypes; i++)
            globalVolumes[i] = 1.0;
        
        globalOverallVolume = 1.0;
        
        //Initialise SFX Controller map. 
        soundEffects = new HashMap<>();
        numSFX = 0;
        lastSFXIndex = -1; //-1 because registration increments.
    }
    
    
    /**
     * Registers a Sound Effect Controller to the Global Sound Controller
     * @param sfxc the Sound Effect Controller to be registered.
     * @return The index value the Sound Effect Controller will be registered to.
     */
    public static long registerSoundEffectController(SoundEffectController sfxc)
    {
        //increment numSFX, and sfxIndex.
        numSFX++;
        lastSFXIndex++;
        //Add the Sound Effect Controller to the map.
        soundEffects.put(lastSFXIndex, sfxc);
        return lastSFXIndex;
    }
    
    /**
     * Kills a Sound Effect Controller, ending all sounds it is playing, closing all inputstreams, and removing it from the Global Sound Controller's map.
     * @param index The Sound Effect Controller to remove.
     */
    public static void killSoundEffectController(long index)
    {
        SoundEffectController sfxc = soundEffects.get(index);
        //If there actually was a value there.
        if(sfxc != null)
        {
            //end all sounds, remove from sounds, deitterate numSFX
            sfxc.end();
            soundEffects.remove(index, sfxc);
            numSFX--;
        }
    }
    
    
    
    //<editor-fold desc="getters and setters">
    
    public static double getGlobalVolume(int volType)
    {
        return globalVolumes[volType];
    }
    
    public static double getGlobalOverallVolume()
    {
        return globalOverallVolume;
    }
    
    public static void setGlobalVolume(int volType, double volume)
    {
        if(volType < globalVolumeTypes && volType >= 0)
            globalVolumes[volType] = volume;
        
    }
    
    //</editor-fold>

    
    /////////////////////
    //TESTING FUNCTIONS//
    /////////////////////
    
    public static int loadMusic(File fileLoc)
    {
        
        try
        {
            mus1 = new LoopSound(fileLoc);
            

           // mus2.start();
        }
        catch(Exception e)
        {
            System.err.println(e);
            return -1;
        }
        
        
        return 1;
    }
    
    public static void playMus1()
    {
        
        
        try     
        {
            System.out.println("VT: "+ volTest);
            float dB = (float)(Math.log(volTest)/Math.log(10.0)*20.0);
            System.out.println("DB: "+ dB);
            mus1.setVolume(volTest);
            volTest *= 0.9;
            mus1.play();
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        

    }
    
    public static void pauseMus1()
    {
        mus1.pause();
    }
    
    public static void goBook(String name)
    {
        mus1.checkLoop(false);
    }
    

}
