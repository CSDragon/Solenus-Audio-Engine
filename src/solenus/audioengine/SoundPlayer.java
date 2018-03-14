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

/**
 *
 * @author Chris Scott
 */
public class SoundPlayer 
{
    private static Clip mus1;
    private static Clip mus2;
    
    public static int loadMusic(File fileLoc)
    {
        AudioInputStream audioInputStream1;
        AudioInputStream audioInputStream2;
        
        try
        {
            // create AudioInputStream object
            audioInputStream1 = AudioSystem.getAudioInputStream(fileLoc.getAbsoluteFile());
            audioInputStream2 = AudioSystem.getAudioInputStream(fileLoc.getAbsoluteFile());
            // create clip reference
            mus1 = AudioSystem.getClip();
            mus2 = AudioSystem.getClip();
            // open audioInputStream to the clip
            mus1.open(audioInputStream1);
            mus2.open(audioInputStream2);

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
        mus1.start();
    }
    
    public static void pauseMus1()
    {
        mus1.stop();
    }
}
