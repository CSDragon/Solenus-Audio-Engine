/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import solenus.audioengine.datatype.BookmarkSound;
import solenus.audioengine.datatype.Sound;

/**
 *
 * @author Chris Scott
 */
public class SoundPlayer 
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
    
    
    
    private static BookmarkSound mus1;
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
    }
    
    
    public static double getGlobalVolume(int volType)
    {
        return globalVolumes[volType];
    }
    
    public static double getGlobalOverallVolume()
    {
        return globalOverallVolume;
    }
    
    
    public static int loadMusic(File fileLoc)
    {
        
        try
        {
            mus1 = new BookmarkSound(fileLoc);
            

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
        mus1.jumpToMark(name);
    }
    

}
