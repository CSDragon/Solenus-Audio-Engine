/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.sound;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import solenus.audioengine.GlobalSoundController;

/**
 * Generic Sound file for the Solenus Audio Engine.
 * @author Chris Scott
 */
public class Sound 
{
    /** Return constant for success. */
    public static final int SUCCESS = 1;
    /** Return constant for failure. */
    public static final int FAIL = 0;
    /** Return constant for the volume being too high. */
    public static final int VOLHIGH = -1;
    /** Return constant for the volume being too low. */
    public static final int VOLLOW = -2;
    
    //private BigClip soundClip;
    protected Clip soundClip;
    protected boolean loaded = false;
    protected File sourceFile;
    protected FloatControl volumeControl;
    protected boolean playing = false;
    protected String name;
    
    private int soundType;
    
     /**
     * Creates a Sound object set to a specific volume slider, and loads the file.
     * @param _sourceFile The file to be loaded.
     * @param _soundType 
     */
    public Sound(File _sourceFile, int _soundType)
    {
        sourceFile = _sourceFile;
        
        //call load so that subclasses can override
        load(sourceFile); 
        soundType = _soundType;
    }
    
    
    /**
     * Creates a Sound object and loads the file
     * @param _sourceFile The file to be loaded.
     */
    public Sound(File _sourceFile)
    {
        this(_sourceFile, GlobalSoundController.GLOBALGENERIC);
    }
    
    
    /**
     * Backup Sound constructor. Creates an empty Sound object.
     */
    public Sound()
    {
    }
    
    /**
     * Loads a target audio file.
     * @param f The location of the file to load.
     * @return SUCESS for Success, FAIL for Fail
     */
    public int load(File f)
    {
        //if something happens, the audio might not be in a playable state anymore.
        loaded = false;
        
        try
        {
            //open file and decode
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f.getAbsoluteFile());
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),baseFormat.getChannels()*2, baseFormat.getSampleRate(), false);
            
            //get decoded audio stream
            AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
            //get soundclip
            //soundClip = new BigClip(AudioSystem.getClip());
            soundClip = AudioSystem.getClip();

            
            //open clip
            soundClip.open(decodedAudioInputStream);
            //access the volume controler
            volumeControl = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
            
            //set that we've loaded.
            loaded = true;
            //TODO: This works for all 3-length extensions like .mp3 and .wav, but should be more foolproof 
            name = f.getName().substring(0, f.getName().length()-4);
        }
        catch(Exception e)
        {
            System.err.println(e);
            return FAIL;
        }
        return SUCCESS;
    }
    
    /**
     * Sets the volume of the soundclip.
     * @param volume The volume you want to set the sound too. range of 0.0-1.0. Volume can go up to 2.0, but this distorts the sound rather than make it louder.
     * @return SUCCESS if success. VOLHIGH if the volume requested was too high. VOLLOW if the volume requested was too low. FAIL a different error occurred (see stack trace). 
     */
    public int setVolume(double volume)
    {
        try     
        {
            //Take into account all volume sliders.
            double trueVolume = volume * GlobalSoundController.getGlobalVolume(soundType) * GlobalSoundController.getGlobalOverallVolume();

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
     * Plays the sound. Starts playback where the sound last left off, where we've seeked to, or the beginning if full stopped.
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
     * Plays the sound from the start.
     */
    public void playFromStart()
    {
        //start from the beginning and play.
        seek(0);
        play();
    }
    
    /**
     * Pauses playback, retaining current location.
     */
    public void pause()
    {
        if(!loaded)
        {
            System.err.println("Error: Attempted to pause unloaded sound. ("+sourceFile.getAbsolutePath()+")");
            return;
        }
        soundClip.stop();
        playing = false;
    }
    
    /**
     * Stops playback (And resets the current location to the start)
     */
    public void stop()
    {
        if(!loaded)
        {
            System.err.println("Error: Attempted to stop unloaded sound. ("+sourceFile.getAbsolutePath()+")");
            return;
        }
        
        soundClip.stop();
        seek(0);
        playing = false;
    }
    
    /**
     * Closes the sound clip.
     */
    public void close()
    {
        soundClip.close();
        playing = false;
        loaded = false;
    }
    
    /**
     * Seeks the clip.
     * @param position The position in the clip, in microseconds, to seek the clip to.
     */
    public void seek(long position)
    {
        soundClip.setMicrosecondPosition(position);
    }
    
    /**
     * Gets the length of the clip.
     * @return Clip length in microseconds.
     */
    public long getLength()
    {
        return soundClip.getMicrosecondLength();
    }
    
    /**
     * Gets the location of the clip
     * @return Clip position in microseconds.
     */
    public long getCurrentLocation()
    {
        return soundClip.getMicrosecondPosition();
    }
    
    /**
     * Getter for 'name'
     * @return This object's 'name'.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Setter for 'name'.
     * @param n The new name.
     */
    public void setName(String n)
    {
        name = n;
    }
    
}
