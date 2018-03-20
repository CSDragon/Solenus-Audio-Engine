/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import solenus.audioengine.GlobalSoundController;

/**
 * A Bookmarked Sound behaves much like a Sound, but contains the ability to jump to pre-defined timestamps. 
 * Bookmarked Sounds can be created from text files as well as audio files. 
 * Creating from a text file will create the Bookmarked Sound by read the audio file information as well as bookmark locations and timestamps from the text file.
 * Creating from an audio file will create bookmarks at the start and end of the sound.
 * @author Chris Scott
 */
public class BookmarkSound extends Sound
{
    protected Bookmark[] bookmarks;
    
    /**
     * Creates a BookmarkSound object set to a specific volume slider, and loads the file.
     * @param _sourceFile The file to be loaded.
     * @param _soundType The sound type this Sound is.
     */
    public BookmarkSound(File _sourceFile, int _soundType)
    {
        super(_sourceFile, _soundType);
    }
    
    /**
     * Basic LoopableSound constructor with no sound type
     * @param _sourceFile The file to be loaded.
     */
    public BookmarkSound(File _sourceFile)
    {
        this(_sourceFile, GlobalSoundController.GLOBALGENERIC);
       
    }
    
    /**
     * Loads the audio and creates the bookmarks
     * @param f The file to be loaded, either the audio file itself, or a text file containing metadata.
     * @return SUCESS for Success, FAIL for Fail
     */
    @Override
    public int load(File f)
    {
        //Is this a metadata file, or a audio file?
        if(f.getName().substring(f.getName().length()-4).equals(".txt"))
        {
            //We've got a metadata file. Read in the information.
            try
            {
                //create the file reader
                BufferedReader in = new BufferedReader(new FileReader(f));
                
                //read the file
                readTextFile(in);
                
                //close the file
                in.close();
                
                //Actually load the audio file. Note: sourceFile was changed in readTextFile(in)
                super.load(sourceFile);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
            return SUCCESS;
        }
        else
        {
            //Attempt to load the sound file. 
            //If it fails, go no further.
            if(super.load(f) == FAIL)
                return FAIL;
            
            //Load the default bookmarks.
            bookmarks = new Bookmark[2];
            bookmarks[0] = new Bookmark("Start", 0);
            bookmarks[1] = new Bookmark("End", soundClip.getMicrosecondLength());
            
        }

        return SUCCESS;

    }
    
    /**
     * Reads audio source and bookmark data from a text file.
     * @param in The file buffer we're reading from
     * @throws IOException 
     */
    public void readTextFile(BufferedReader in) throws IOException
    {
        //Get the actual audio source file.
        sourceFile = new File(in.readLine().substring(15));
        
        //get the number of bookmarks.
        bookmarks = new Bookmark[Integer.parseInt(in.readLine().substring(21))];
        
        //create each bookmark.
        for(int i = 0; i< bookmarks.length; i++)
            bookmarks[i] = new Bookmark(in.readLine(), Long.parseLong(in.readLine()));
    }
    
    

    /**
     * Gets the requested bookmark. 
     * @param name The name of the bookmark we're searching for.
     * @return The bookmark that name points to.
     */
    public Bookmark getBookmark(String name)
    {
        //Check each bookmark too see if it's the one we're looking for.
        //In theory we could make this faster if the array was sorted by name, but currently it sorted by user preference. There should never bee too many bookmarks to make this show though.
        for (Bookmark b : bookmarks) 
        {
            if (b != null && b.getName().equals(name)) 
                return b;
        }
        
        //Failcase, return null.
        return null;
    }
    
    /**
     * Finds the index in the bookmarks array of a bookmark, by name.
     * @param name The name of the bookmark we're trying to find.
     * @return index of the bookmark with that name, or -1 if not found in the array.
     */
    public int indexOfBookmark(String name)
    {
        //In theory we could make this faster if the array was sorted by name, but currently it sorted by user preference. There should never bee too many bookmarks to make this show though.
        for(int i = 0; i< bookmarks.length; i++)
            if(bookmarks[i].getName().equals(name))
                return i;
        
        return -1;
    }
    
    /**
     * Finds the index in the bookmarks array of a bookmark. Compares based on name only.
     * @param b The bookmark we're trying to find.
     * @return index of the bookmark with that name, or -1 if not found in the array.
     */
    public int indexOfBookmark(Bookmark b)
    {
        //The name is the identifying part of a bookmark, so we don't care if the passed bookmark is actually different.
        return indexOfBookmark(b.getName());
    }
    
    /**
     * Seeks to a bookmark in the Sound.
     * @param name The name of the bookmark we're looking for.
     */
    public void seek(String name)
    {
        //find the bookmark
        Bookmark b = getBookmark(name);
        //make sure it's not null
        if(b!= null)
            soundClip.setMicrosecondPosition(b.getTimeCode());
        
        //bookmark was not found. Seek to the start.
        else
            soundClip.setMicrosecondPosition(0l);
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
