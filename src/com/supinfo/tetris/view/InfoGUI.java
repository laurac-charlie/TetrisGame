package com.supinfo.tetris.view;

import com.supinfo.tetris.controller.TetrisGame;
import com.supinfo.tetris.model.Board;
import com.supinfo.tetris.model.Tetriminos;
import com.supinfo.tetris.observer.Observable;
import com.supinfo.tetris.observer.Observateur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 * Classe gérant le panel d'affichage des informations sur le jeu en cours
 * 
 * @author Charlie
 *
 */
public class InfoGUI extends JPanel implements Observateur {

    private static final long serialVersionUID = 1L;
    private TetrisGame jGame = null;
    private NextPieceGUI jNextPieceGUI = new NextPieceGUI();
    private JButton jStartButton = new JButton("Commencer");
    private JButton jPauseButton = new JButton("Pause");
    private JLabel jScoreLabel = new JLabel("Score : 0");
    private JLabel jLevelLabel = new JLabel("Niveau : 1");

    public InfoGUI(TetrisGame jGame) {
        this.jGame = jGame;
        this.setLayout(new BorderLayout());

        //Panel d'informations : niveau, score etc...
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel nextPiecePanel = new JPanel(new BorderLayout());
        JPanel textPanel = new JPanel(new BorderLayout());

        this.jStartButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(this.jPauseButton);
        buttonPanel.add(this.jStartButton, BorderLayout.PAGE_END);

        nextPiecePanel.add(new JLabel("Pièce Suivante : "), BorderLayout.LINE_START);
        this.jNextPieceGUI.setPreferredSize(new Dimension(120, 160));
        nextPiecePanel.add(this.jNextPieceGUI, BorderLayout.AFTER_LAST_LINE);

        this.jLevelLabel.setPreferredSize(new Dimension(120, 30));
        textPanel.add(this.jScoreLabel, BorderLayout.BEFORE_FIRST_LINE);
        textPanel.add(this.jLevelLabel, BorderLayout.LINE_START);
        
        this.add(textPanel, BorderLayout.BEFORE_FIRST_LINE);
        this.add(buttonPanel, BorderLayout.PAGE_END);
        this.add(nextPiecePanel);

        //On commence ou recommence le jeu lors du click du boutton
        this.jStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfoGUI.this.jGame.startGame();
                //On rend le focus à la fênetre parente pour que l'évènement keypress soit bien reconnu
                InfoGUI.this.getFocusCycleRootAncestor().requestFocus();
                InfoGUI.this.jStartButton.setText("Commencer");
                InfoGUI.this.jStartButton.setEnabled(false);
                InfoGUI.this.jPauseButton.setEnabled(true);
            }
        });

        //On met le jeu en pause (ou on le redémarre) quand on click sur le boutton pause/continuer
        this.jPauseButton.setEnabled(false);
        this.jPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfoGUI.this.jGame.setPaused();
                if (!InfoGUI.this.jGame.isPaused()) {
                    InfoGUI.this.getFocusCycleRootAncestor().requestFocus();
                    InfoGUI.this.jPauseButton.setText("Pause");
                } else {
                    InfoGUI.this.jPauseButton.setText("Continuer");
                }
            }
        });
    }

    /**
     * Affiche la popup présentant la fin du jeu
     */
    public void stopGameNow() {
        //On affiche Game Over puis on propose le jeu
        JOptionPane.showMessageDialog(this.getFocusCycleRootAncestor(), "Game Over", "Message", JOptionPane.ERROR_MESSAGE);
        this.jPauseButton.setEnabled(false);
        this.jStartButton.setEnabled(true);
        this.jStartButton.setText("Recommencer");
    }

    @Override
    public void update(Observable observable) {
        //Si on observe bien un TetrisGame on met à jour les champs et on vérifie que le jeu ne s'est pas arrété
        try {
            TetrisGame game = (TetrisGame) observable;
            if (!game.isPlaying()) {
                this.stopGameNow();
            }
            this.jScoreLabel.setText("Score : " + (int) game.getScore());
            this.jLevelLabel.setText("Niveau : " + (int) game.getLevel());
            this.jNextPieceGUI.fnextPiece = game.getNextPiece();
            this.jNextPieceGUI.repaint();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }

    @Override
    public void update(Observable observable, Object object) {
    	//Si on observe bien un TetrisGame on met à jour les champs et on vérifie que le jeu ne s'est pas arrété
        try {
            TetrisGame game = (TetrisGame) observable;
            if (!game.isPlaying()) {
                this.stopGameNow();
            }
            this.jScoreLabel.setText("Score : " + (int) game.getScore());
            this.jLevelLabel.setText("Niveau : " + (int) game.getLevel());
            this.jNextPieceGUI.fnextPiece = game.getNextPiece();
            this.jNextPieceGUI.repaint();
        } catch (Exception e) {
            System.out.println(e.getStackTrace().toString());
        }
    }

    private class NextPieceGUI extends JPanel {

        private static final long serialVersionUID = 1L;
        private Tetriminos fnextPiece = null;

        @Override
        public void paintComponent(Graphics graphics) {
            this.setPreferredSize(this.getPreferredSize());
            int width = this.getBounds().width;
            int height = this.getBounds().height;

            // On crée l'image correspant à la case pièce suivante
            Image image = this.createImage(width, height);
            Graphics g = image.getGraphics();

            //On va dessiner le prochain tetriminos dans la case
            if (this.fnextPiece != null) {
            	//On crée un mini matrix pour la case
                int[][] matrix = new int[6][6];
                Point[] blocks = this.fnextPiece.getRelativePoints();

                //On initialise le cadre avec des cases vides
                for (int x = 0; x < 6; x ++)
                    for (int y = 0; y < 6; y ++)
                        matrix[x][y] = Board.EMPTY_BLOCK;
                
                //On place les valeurs du tetriminos concerné dans la mini matrix 
                for (int count = 0; count < 4; count++) {
                	//On met moins pour mettre le tetrinos dans le bon sens
                    int x = 3 - blocks[count].x;
                    int y = 2 + blocks[count].y;

                    matrix[x][y] = this.fnextPiece.getType();
                }

                int piece = Board.EMPTY_BLOCK;
                int size_y = 5;
                int size_x = 4;

                //On parcours notre matrix
                for (int cols = 0; cols < size_x; cols++) {
                    for (int rows = 0; rows < size_y; rows++) {
                        piece = matrix[rows + 1][cols + 1];

                        //On détermine la couleur du block puis on le paint
                        Color color = (piece != Board.EMPTY_BLOCK) ? Color.black : Color.WHITE;
                        g.setColor(Tetriminos.getPieceColor(piece));
                        drawBlock(g, (cols * width / size_x), (rows * height / size_y), (width / size_x), (height / size_y), color);
                    }
                }
            }

            //On dessine le plateau
            g.setColor(Color.black);
            g.drawRect(0, 0, width - 1, height - 1);
            graphics.drawImage(image, 0, 0, width, height, null);
        }

        /**
         * Dessine un block sur le plateau
         *
         * @param g : objet graphique du plateau
         * @param x : abscisse du block
         * @param y : ordonnée du block
         * @param width : largeur du block
         * @param height : hauteur du block
         * @param background : couleur du fond du block
         */
        private void drawBlock(Graphics g, int x, int y, int width, int height, Color background) {
            g.fillRect(x, y, width, height);
            g.setColor(background);
            g.drawRect(x, y, width, height);
        }

        /*@Override
         public Dimension getPreferredSize()
         {
         return new Dimension(200, 200);
         }*/
    }
}
