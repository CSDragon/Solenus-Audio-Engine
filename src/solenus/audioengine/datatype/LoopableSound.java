/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import solenus.audioengine.SoundPlayer;

/**
 * A Loopable Sound behaves much like a Bookmarked Sound, but contains looping data.
 * While a Loopable Sound can have other bookmarks, it must have defined bookmarks for the start and end of the loop.
 * If not explicitly set, the StartLoop and EndLoop bookmarks will be set at the start and end of the sound.
 * A Loopable Sound will return to the StartLoop bookmark any time it is past the EndLoop bookmark.
 * The loop points can be changed at any time.
 * You can set a maximum number of loops.
 * @author Chris Scott
 */
public class LoopableSound extends BookmarkSound
{
    private int loopStart;
    private int loopEnd;
    private boolean loadedFromDataFile;
    
    /**
     * Creates a LoopableSound object set to a specific volume slider, and loads the file.
     * @param _sourceFile The file to be loaded.
     * @param _soundType The sound type this Sound is.
     */
    public LoopableSound(File _sourceFile, int _soundType)
    {
        //Actual differences in construction between this and Sound are done when Sound calls load().
        super(_sourceFile, _soundType);
        
        //Because of the way we've nested load() into Super constructors, to give loopStart and loopEnd default values would overwrite them when finishing the super constructor.
        //This means if you load an audio file instead of a data file, loopStart and loopEnd will never be set.
        if(!loadedFromDataFile)
        {
            loopStart = 0;
            loopEnd = 1;
        }
    }
    
    /**
     * Basic LoopableSound constructor with no sound type
     * @param _sourceFile The file to be loaded.
     */
    public LoopableSound(File _sourceFile)
    {
        this(_sourceFile, SoundPlayer.GLOBALGENERIC);
    }
    
    /**
     * Reads audio source, bookmark, and loop data from a text file.
     * @param in The file buffer we're reading from
     * @throws IOException 
     */
    @Override
    public void readTextFile(BufferedReader in) throws IOException
    {
        //See constructor for why this is nessisary.
        loadedFromDataFile = true;
        
        //get bookmark information from BookmarkSound's version of this function.
        super.readTextFile(in);
        
        /* Find the Bookmark Loop Type. 
         * 0 = Default bookmarks 0 and 1.
         * 1 = Declared bookmarks "loopStart" and "loopEnd".
         * 2 = Explicit bookmark names in the next two lines of the file.
         */
        switch(Integer.parseInt(in.readLine().substring(20)))
        {
            case 0: //Default bookmarks 0 and 1.
                //Set them to 0 and 1.
                loopStart = 0;
                loopEnd = 1;
            break;
        
            case 1: // Declared bookmarks "loopStart" and "loopEnd".
                //find loopStart and loopEnd. If one can't be found, set it to default.
                loopStart = indexOfBookmark("loopStart");
                if (getLoopStart() == -1)
                    loopStart = 0;

                loopEnd = indexOfBookmark("loopEnd");
                if (getLoopEnd() == -1)
                    loopEnd = 1;
            break;
        
            case 2: //Explicit bookmark names in the next two lines of the file.
                //Get the names to search for.
                String ls = in.readLine();
                String le = in.readLine();

                //Search for the names. Set to defaults if not found.
                setLoopStart(indexOfBookmark(ls));
                if (loopStart == -1)
                    loopStart = 0;

                setLoopEnd(indexOfBookmark(le));
                if (loopEnd == -1)
                    loopEnd = 1;
            break;
        }
    }
    
    /**
     * Checks the sound's position in it's loop, and seeks the sound if nessisary.
     * Has modes for both a hard cut, where the sound moves directly to the loop beginning regardless of anything, and for a soft cut, where the sound seeks to where it would have been if it had looped exactly at the loop end.
     * The soft cut is more useful for making 60fps games loop better, as a hard cut could rewind the song up to 16ms.
     * @param hardLoop If the loop should be a hard cut or not.
     */
    public void checkLoop(boolean hardLoop)
    {
        //check how long it's been since we were supposed to loop.
        long timeDiff = soundClip.getMicrosecondPosition() - bookmarks[getLoopEnd()].getTimeCode();
        
        //if we're past the loop time, loop
        if(timeDiff >= 0)
        {
            if(hardLoop)
                //hard loop moves directly to the loop start
                soundClip.setMicrosecondPosition(bookmarks[getLoopStart()].getTimeCode());
            else
                //soft loop adjusts for time 
                soundClip.setMicrosecondPosition(bookmarks[getLoopStart()].getTimeCode() + timeDiff);
        }
    }
    
    //<editor-fold desc="getters and setters">

    /**
     * @return the loopStart
     */
    public int getLoopStart() 
    {
        return loopStart;
    }

    /**
     * @param loopStart the loopStart to set
     */
    public void setLoopStart(int loopStart) 
    {
        this.loopStart = loopStart;
    }

    /**
     * @return the loopEnd
     */
    public int getLoopEnd() 
    {
        return loopEnd;
    }

    /**
     * @param loopEnd the loopEnd to set
     */
    public void setLoopEnd(int loopEnd) 
    {
        this.loopEnd = loopEnd;
    }
    
    //</editor-fold>

}
