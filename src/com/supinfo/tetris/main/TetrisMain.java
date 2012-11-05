package com.supinfo.tetris.main;

import com.supinfo.tetris.controller.TetrisGame;
import com.supinfo.tetris.model.*;
import com.supinfo.tetris.view.BoardGUI;
import com.supinfo.tetris.view.InfoGUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;

/**
 * Classe principale de l'application
 * 
 * @version 1.2
 * @author Charlie
 */
public class TetrisMain implements KeyListener
{
    private TetrisGame	jGame		= new TetrisGame();
    private BoardGUI	jBoardGUI   = new BoardGUI();
    private InfoGUI		jInfoGUI    = new InfoGUI(this.jGame);
    private JFrame		jFrame      = new JFrame("Tetris - Supinfo");

    public TetrisMain()
    {
        //On crée la fênetre principal à laquelle on ajoute les 2 panels
    	Container mainPanel = this.jFrame.getContentPane();
        
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(this.jBoardGUI, BorderLayout.CENTER);
        mainPanel.add(this.jInfoGUI,BorderLayout.EAST);
        
        
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*this.jFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            { System.exit(0); }
        });*/

        //On fait l'interface jBoardGUI observer les mises ï¿½ jour du jeu jGame
        this.jGame.addBoardListener(this.jBoardGUI);
        this.jGame.addObservateur(this.jInfoGUI);
        this.jFrame.addKeyListener(this);
        this.jFrame.pack();
        this.jFrame.setVisible(true);
    }

    /**
     * On écoute l'évènement keyPressed et on lance le traitement associé (mouvement de tetreminos)
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
    	switch (e.getKeyCode()) 
    	{
        	case KeyEvent.VK_LEFT  : this.jGame.move(Tetriminos.LEFT);
        	                         break;
        	case KeyEvent.VK_RIGHT : this.jGame.move(Tetriminos.RIGHT);
        	                         break;
        	case KeyEvent.VK_UP    : this.jGame.move(Tetriminos.ROTATE);
        	                         break;
        	case KeyEvent.VK_DOWN  : this.jGame.move(Tetriminos.DOWN);
        	                         break;
            case KeyEvent.VK_SPACE : this.jGame.move(Tetriminos.FALL);
            						 break;
            default:break;
    	}
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}

    //Méthode de départ
    public static void main(String[] args)
    {
        new TetrisMain();
    }
}
