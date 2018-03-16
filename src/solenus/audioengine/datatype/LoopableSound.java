/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solenus.audioengine.datatype;

/**
 * A Loopable Sound behaves much like a Bookmarked Sound, but contains looping data.
 * While a Loopable Sound can have other bookmarks, it must have defined bookmarks for the start and end of the loop.
 * If not explicitly set, the StartLoop and EndLoop bookmarks will be set at the start and end of the sound.
 * A Loopable Sound will return to the StartLoop bookmark any time it is past the EndLoop bookmark.
 * The loop points can be changed at any time.
 * You can set a maximum number of loops.
 * @author Chris Scott
 */
public class LoopableSound 
{
    
}
