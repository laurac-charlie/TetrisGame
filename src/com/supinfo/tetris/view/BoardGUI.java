package com.supinfo.tetris.view;

import com.supinfo.tetris.model.Board;
import com.supinfo.tetris.model.Tetriminos;
import com.supinfo.tetris.observer.Observable;
import com.supinfo.tetris.observer.Observateur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * Classe gérant l'interface graphique du plateau du jeu. La classe observe TetrisBoard pour
 * se mettre à jour
 *
 * @author Charlie
 *
 */
public class BoardGUI extends JPanel implements Observateur {

    private static final long serialVersionUID = 1L;
    private Board jBoard = null;

    @Override
    public void paintComponent(Graphics graphics) {
        int width = this.getBounds().width;
        int height = this.getBounds().height;

        // On cr�e l'image correspant au plateau où l'on va dessiner les blocks
        Image image = this.createImage(width, height);
        Graphics g = image.getGraphics();

        //Dessine les éléments de l'interface à partir du plateau
        if (this.jBoard != null) {
            int numCols = this.jBoard.getColumns();
            int numRows = this.jBoard.getRows();
            int piece = Board.EMPTY_BLOCK;

            //On parcours le plateau pour savoir ou créer les blocks
            for (int cols = 0; cols < numCols; cols++) {
                for (int rows = 0; rows < numRows; rows++) {
                    piece = this.jBoard.getPieceAt(cols, rows);

                    //On détermine la couleur du block puis on le paint
                    Color color = (piece != Board.EMPTY_BLOCK) ? Color.black : Color.WHITE;
                    g.setColor(Tetriminos.getPieceColor(piece));
                    drawBlock(g, (cols * width / numCols) + 1, (rows * height / numRows), (width / numCols), (height / numRows) - 1, color);
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
     * @param y : ordonn�e du block
     * @param width : largeur du block
     * @param height : hauteur du block
     * @param background : couleur du fond du block
     */
    private void drawBlock(Graphics g, int x, int y, int width, int height, Color background) {
        g.fillRect(x, y, width, height);
        g.setColor(background);
        g.drawRect(x, y, width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 600);
    }

    //On implémente les fonctions de mise à jour
    @Override
    public void update(Observable observateur) {
        //On met � jour le jBoard et l'interface graphique, si on a bien un objet de type TetrisBoard
        if (observateur instanceof Board) {
            this.jBoard = (Board) observateur;
            this.repaint();
        }
    }

    @Override
    public void update(Observable observateur, Object object) {
        if (observateur instanceof Board) {
            this.jBoard = (Board) observateur;
            this.repaint();
        }
    }
}
