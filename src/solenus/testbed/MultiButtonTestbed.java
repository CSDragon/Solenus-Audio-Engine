
package solenus.testbed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import solenus.audioengine.GlobalSoundController;
import solenus.audioengine.SoundEffectController;

/**
 * MultiButtonTestbed
 * @author Chris Scott
 * This is a generic multi-button testbed.
 * It gives you a number of buttons you can press to test other programs.
 */
public class MultiButtonTestbed implements ActionListener
{
   
    private static JFrame gameFrame;
    
//list of buttons
    private static JButton but1;
    private static JButton but2;
    private static JButton but3;
    private static JButton but4;
    private static JButton but5;
    
    private SoundEffectController sfxc;
    private SoundEffectController sfxc2;
        
    //Generic main
    public static void main(String[] args) 
    {
        new MultiButtonTestbed();
    }
    
    public MultiButtonTestbed()
    {
        //Set up window
        gameFrame = new JFrame("Multibuton Testbed");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(400, 400);

        
        //create and add the buttons you've made
        but1 = new JButton("Load");
        but1.addActionListener(this);
        gameFrame.add(but1, BorderLayout.NORTH);

        but2 = new JButton("Play");
        but2.addActionListener(this);
        gameFrame.add(but2, BorderLayout.EAST);

        but3 = new JButton("Stop");
        but3.addActionListener(this);
        gameFrame.add(but3, BorderLayout.WEST);

        but4 = new JButton("beatdown");
        but4.addActionListener(this);
        gameFrame.add(but4, BorderLayout.SOUTH);

        but5 = new JButton();
        but5.addActionListener(this);
        gameFrame.add(but5, BorderLayout.CENTER);
        
        
        //turn on the window
        gameFrame.setVisible(true);
        
        //System Under Test
        //set up the sound player
        GlobalSoundController.initialize();
        sfxc = new SoundEffectController();
        sfxc2 = new SoundEffectController();
    }

    @Override
    /**
     * Action Listener implementation
     * Activates actions based on the button pressed.
     */
    public void actionPerformed(ActionEvent e) 
    {
        //Put the button actions here.
        if(e.getSource() == but1)
        {
            sfxc.addSound(new File("audio/skaia.mp3"), 0, 0, "beatdown");
        }
        if(e.getSource() == but2)
        {
            sfxc.getSound("beatdown").play(0.05d);
            
        }
        if(e.getSource() == but3)
            sfxc.getSound("beatdown").stop(0);
        if(e.getSource() == but4)
            sfxc.addSound(new File("audio/beatdown.wav"), 0, 0, "beatdown");
        if(e.getSource() == but5)
            sfxc.getSound("beatdown").start(0);
        //if(e.getSource() == but5)
        //    GlobalSoundController.goBook("In Vento");

    }
    
    
}
