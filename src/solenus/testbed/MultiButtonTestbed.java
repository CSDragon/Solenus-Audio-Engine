
package solenus.testbed;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import solenus.audioengine.SoundPlayer;

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

        but4 = new JButton();
        but4.addActionListener(this);
        gameFrame.add(but4, BorderLayout.SOUTH);

        but5 = new JButton();
        but5.addActionListener(this);
        gameFrame.add(but5, BorderLayout.CENTER);
        
        
        //turn on the window
        gameFrame.setVisible(true);
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
            SoundPlayer.loadMusic(new File("Audio/Beatdown.wav"));
        if(e.getSource() == but2)
            SoundPlayer.playMus1();
        if(e.getSource() == but3)
            SoundPlayer.pauseMus1();
    }
    
    
}
