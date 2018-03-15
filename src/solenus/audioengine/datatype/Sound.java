/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.datatype;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import solenus.audioengine.SoundPlayer;

/**
 *
 * @author chrsc
 */
public class Sound 
{
    public static final int VOLHIGH = -1;
    public static final int VOLLOW = -2;
    
    private Clip soundClip;
    private boolean loaded = false;
    private File sourceFile;
    private FloatControl volumeControl;
    private boolean playing = false;
    
    private int soundType;
    
    long duration; 
    long curLoc;
    
    
    /**
     * Creates a Sound object and loads the file
     * @param _sourceFile The file to be loaded.
     */
    public Sound(File _sourceFile)
    {
        sourceFile = _sourceFile;
        load(sourceFile);
        soundType = SoundPlayer.GLOBALGENERIC;
    }
    
    /**
     * Creates a Sound object set to a specific volume slider, and loads the file.
     * @param _sourceFile The file to be loaded.
     * @param _soundType 
     */
    public Sound(File _sourceFile, int _soundType)
    {
        this(_sourceFile);
        soundType = _soundType;
    }
    
    /**
     * Backup Sound constructor. Creates an empty Sound object.
     */
    public Sound()
    {
    }
    
    /**
     * 
     * @param f The location of the file to load.
     * @return 1 for Success, 0 for Fail
     */
    public int load(File f)
    {
        try
        {
            // create AudioInputStream object
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
            // create clip reference
            soundClip = AudioSystem.getClip();
            // open audioInputStream to the clip
            soundClip.open(audioInputStream);
            
            //access the volume controler
            volumeControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
            
            //set that we've loaded.
            loaded = true;
            duration = soundClip.getMicrosecondLength();
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return 1;
    }
    
    /**
     * Sets the volume of the soundclip.
     * @param volume The volume you want to set the sound too. range of 0.0-1.0. Volume can go up to 2.0, but this distorts the sound rather than make it louder.
     * @return 1 if success. -1 if the volume requested was too high. -2 if the volume requested was too low. 0 a different error occurred (see stack trace). 
     */
    public int setVolume(double volume)
    {
        try     
        {
            //Take into account all volume sliders.
            double trueVolume = volume * SoundPlayer.getGlobalVolume(soundType) * SoundPlayer.getGlobalOverallVolume();

            //convert %based volume to DB based volume. Then set it
            float dB = (float)(Math.log(trueVolume)/Math.log(10.0)*20.0);
            volumeControl.setValue(dB);
        } 
        catch (Exception ex) 
        {
            //catch math and volume intensity errors.
            ex.printStackTrace();
            if (volume > 2.0)
                return VOLHIGH;
            if (volume < 0.0)
                return VOLLOW;
            return 0;
        }
        return 1;
    }
    
    /**
     * Plays the sound. Starts playback where the sound last left off.
     */
    public void play()
    {
        if(!loaded)
        {
            System.err.println("Error: Attempted to play unloaded sound. ("+sourceFile.getAbsolutePath()+")");
            return;
        }
        soundClip.start();
        playing = true;
    }
    
    /**
     * Stops playback.
     */
    public void pause()
    {
        if(!loaded)
        {
            System.err.println("Error: Attempted to pause unloaded sound. ("+sourceFile.getAbsolutePath()+")");
            return;
        }
        curLoc = soundClip.getMicrosecondPosition();
        soundClip.stop();
        playing = false;
    }
    
}
