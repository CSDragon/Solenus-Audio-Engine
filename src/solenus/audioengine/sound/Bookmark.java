/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.sound;

/**
 * Datatype for audio objects creating a pair between a timecode (in microseconds) and a name.
 * Bookmarks should be contained in a Data Structure
 * @author Chris Scott
 */
public class Bookmark 
{
    private long timeCode;
    private String name;
    
    /**
     * 
     * @param _name The bookmark's name, which is used for finding it. 
     * @param _timeCode Time to set this bookmark at, in microseconds.
     */
    public Bookmark (String _name, long _timeCode)
    {
        timeCode = _timeCode;
        name = _name;
    }
    
    /**
     * @return The time code.
     */
    public long getTimeCode() 
    {
        return timeCode;
    }

    /**
     * @return The name.
     */
    public String getName() 
    {
        return name;
    }
    
    
}
