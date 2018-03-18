/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import solenus.audioengine.SoundPlayer;

/**
 * A Bookmarked Sound behaves much like a Sound, but contains the ability to jump to pre-defined timestamps. 
 * Bookmarked Sounds can be created from text files as well as audio files. 
 * Creating from a text file will create the Bookmarked Sound by read the audio file information as well as bookmark locations and timestamps from the text file.
 * Creating from an audio file will create bookmarks at the start and end of the sound.
 * @author Chris Scott
 */
public class BookmarkSound extends Sound
{
    private Bookmark[] bookmarks;
    
    public BookmarkSound(File source)
    {
        super(source);
       
    }
    
    /**
     * Loads the audio and creates the bookmarks
     * @param f The file to be loaded, either the audio file itself, or a text file containing metadata.
     * @return SUCESS for Success, FAIL for Fail

     */
    @Override
    public int load(File f)
    {
        if(f.getName().substring(f.getName().length()-4).equals(".txt"))
        {
            try
            {
                BufferedReader in = new BufferedReader(new FileReader(f));
                sourceFile = new File(in.readLine().substring(15));
                super.load(sourceFile);
                bookmarks = new Bookmark[Integer.parseInt(in.readLine().substring(21))];
                for(int i = 0; i< bookmarks.length; i++)
                    bookmarks[i] = new Bookmark(Long.parseLong(in.readLine()), in.readLine());
                in.close();
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
            return 1;
        }
        else
        {
            //Attempt to load the sound file. 
            //If it fails, go no further.
            if(super.load(f) == FAIL)
                return FAIL;
            
            //Load the default bookmarks.
            bookmarks = new Bookmark[2];
            bookmarks[0] = new Bookmark(0,"Start");
            bookmarks[1] = new Bookmark(soundClip.getMicrosecondLength(), "End");
            
        }
        
        return SUCCESS;

    }
    
    
    
    

    /**
     * Gets the timecode from the requested bookmark. 
     * @param name The name of the bookmark we're searching for.
     * @return The timecode that the bookmark points to.
     */
    public long getBookmarkTimecode(String name)
    {
        //Check each bookmark too see if it's the one we're looking for.
        //In theory we could make this faster if the array was sorted by name, but currently it is sorted by timecode.
        for (Bookmark b : bookmarks) 
        {
            if (b != null && b.getName().equals(name)) 
                return b.getTimeCode();
        }
        
        //Failcase, return the start of the sound.
        return 0l;
    }
    
    public void jumpToMark(String name)
    {
        System.out.println(soundClip.getMicrosecondPosition());
        System.out.println(soundClip.getMicrosecondLength());
        System.out.println(getBookmarkTimecode(name));

        soundClip.setMicrosecondPosition(getBookmarkTimecode(name));
    }
    
    /**
     * Standard getter/setter
     * @return bookmarks array.
     */
    public Bookmark[] getBookmarks()
    {
        return bookmarks;
    }
    
}
