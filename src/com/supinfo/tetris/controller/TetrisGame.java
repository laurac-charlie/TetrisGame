package com.supinfo.tetris.controller;

import com.supinfo.tetris.model.Board;
import com.supinfo.tetris.model.Tetriminos;
import com.supinfo.tetris.observer.*;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Classe contrôlant le jeu observable par une interface graphique
 *
 * @author Charlie
 */
public class TetrisGame implements Observable {

    public static final int END = 0;
    public static final int START = 1;
    public static final int PAUSE = 2;
    private Board jBoard = null;
    private ArrayList<Observateur> jScoreObservateurs = new ArrayList<>();
    private Tetriminos jCurrPiece = null;
    private Tetriminos jNextPiece = null;
    private Thread jGameThread = null;
    private int jLevel = 0;
    private int jScore = 0;
    private int jTotalLines = 0;
    private int jDelay = 500;
    private boolean jPlaying = false;
    private boolean jPaused = false;

    /**
     * Crèe un nouveau jeu
     */
    public TetrisGame() {
        this.jBoard = new Board(15, 20);
        this.jPlaying = false;
    }

    /**
     * Commence un nouveau s'il n'y en a pas d'autre en cours
     */
    public void startGame() {
        if (jPlaying == false) {
            this.jBoard.resetBoard();
            this.jTotalLines = 0;
            this.jScore = 0;
            this.jLevel = 1;
            this.jDelay = 500;
            this.jPlaying = true;
            this.jPaused = false;
            this.jCurrPiece = null;
            this.jNextPiece = null;

            // Initialise et démarre le thread du jeu
            this.jGameThread = new GameThread();
            this.jGameThread.start();
        }
    }

    /**
     * Arrête le jeu en cours
     */
    public void stopGame() {
        this.jPlaying = false;
        this.jScore = 0;
        this.jLevel = 1;
        this.notifyObservateurs();
    }

    public Tetriminos getCurrentPiece() {
        return this.jCurrPiece;
    }

    public void setCurrentPiece(Tetriminos currPiece) {
        this.jCurrPiece = currPiece;
    }

    public Tetriminos getNextPiece() {
        return this.jNextPiece;
    }

    public void setNextPiece(Tetriminos nextPiece) {
        this.jNextPiece = nextPiece;
    }

    public boolean isPaused() {
        return this.jPaused;
    }

    public void setPaused() {
        if (this.jPlaying) {
            this.jPaused = !this.jPaused;
        }
    }

    public boolean isPlaying() {
        return this.jPlaying;
    }

    /**
     * On démarre un nouveau jeu si aucun n'est en cours et on n'arrête celui en
     * cours s'il y en a un.
     *
     * @param playing : true - Démarre le jeu s'il n'est pas démarré. false -
     * Arrête le jeu en cours.
     */
    public void setPlaying(boolean playing) {
        if (playing) {
            this.jPlaying = false;
        } else {
            startGame();
        }
    }

    /**
     * Si le mouvement est possible, on l'effectue
     *
     * @param direction La direction vers laquelle on bouge la piéce
     * @return true si le mouvement a été effectué false sinon
     */
    public boolean move(int direction) {
        boolean result = false;

        //Si le jeu est bien en cours
        if (this.jCurrPiece != null && this.jPlaying == true && this.jPaused == false) {
            if (direction == Tetriminos.DOWN || direction == Tetriminos.FALL)
            {
                //Si on ne peut plus descendre, on va changer de pièce
                if (this.jCurrPiece.move(direction) == false)
                    this.jCurrPiece = null;
                else
                    result = true;
            } 
            else
                result = this.jCurrPiece.move(direction);
        }

        return result;
    }

    public int getScore() {
        return this.jScore;
    }

    public void setScore(int score) {
        this.jScore = score;
    }

    public int getTotalLines() {
        return this.jTotalLines;
    }

    public void setTotalLines(int totalLines) {
        this.jTotalLines = totalLines;
    }

    /**
     * Ajoute un observateur au plateau du jeu
     *
     * @param observateur : l'interface graphique qui va observé
     */
    public void addBoardListener(Observateur observateur)//(BoardListener listener)
    {
        this.jBoard.addObservateur(observateur);
    }

    @Override
    public void addObservateur(Observateur observateur) {
        this.jScoreObservateurs.add(observateur);
    }

    @Override
    public void notifyObservateurs() {
        //On notifie la mise à jour du plateau et du jeu
        this.jBoard.notifyObservateurs();
        for (Observateur o : this.jScoreObservateurs) {
            o.update(this);
        }
    }

    @Override
    public void notifyObservateurs(Object object) {
        this.jBoard.notifyObservateurs(object);
        for (Observateur o : this.jScoreObservateurs) {
            o.update(this, this.jScore);
        }
    }

    @Override
    public void removeObservateur(Observateur observateur) {
        this.jScoreObservateurs.remove(observateur);
    }

    @Override
    public void removeObservateurs() {
        this.jScoreObservateurs.removeAll(this.jScoreObservateurs);
    }

    public int getLevel() {
        return this.jLevel;
    }

    public void setLevel(int jLevel) {
        this.jLevel = jLevel;
    }

    /**
     * Thread principal du jeu
     *
     * @author Charlie
     *
     */
    private class GameThread extends Thread {

        @Override
        public void run() {
            while (TetrisGame.this.jPlaying) {
                if (TetrisGame.this.jPaused) {
                    //if (!this.isInterrupted()) this.interrupt();
                    //Si le jeu est en pause, on arrête pas le thread mais on ne fait plus rien
                	//en attendant que paused repasse à vrai
                } 
                else 
                {
                    if (TetrisGame.this.jCurrPiece == null) 
                    {
                        int completeLines = 0;

                        //On teste avant tout si aucune ligne n'a été complétée
                        for (int rows = TetrisGame.this.jBoard.getRows() - 1; rows >= 0; rows--) 
                        {
                            boolean complete = true;

                            //Si aucune colonne de la ligne n'est vide, elle est compléte et on réalise le traitement
                            for (int cols = 0; cols < TetrisGame.this.jBoard.getColumns(); cols++) 
                            {
                                if (TetrisGame.this.jBoard.getPieceAt(cols, rows) == Board.EMPTY_BLOCK)
                                    complete = false;
                            }

                            if (complete) 
                            {
                                // Supprime la ligne complétée
                                TetrisGame.this.jBoard.removeRow(rows);

                                //Comme la ligne qui était à cette emplacement a été supprimée, la nouvelle ligne
                                //s'y trouvant doit étre vérifiée. On n'annule donc l'incrémentation
                                rows++;

                                // On incrémente les totals de lignes
                                completeLines++;
                                TetrisGame.this.jTotalLines++;


                                //Selon le niveau, on accélère le jeu en réduisant le délai
                                switch (TetrisGame.this.jTotalLines) 
                                {
                                    case 10:
                                        TetrisGame.this.jDelay = 400;
                                        TetrisGame.this.jLevel = 2;
                                        break;
                                    case 20:
                                        TetrisGame.this.jDelay = 300;
                                        TetrisGame.this.jLevel = 3;
                                        break;
                                    case 30:
                                        TetrisGame.this.jDelay = 200;
                                        TetrisGame.this.jLevel = 4;
                                        break;
                                    case 40:
                                        TetrisGame.this.jDelay = 150;
                                        TetrisGame.this.jLevel = 5;
                                        break;
                                    case 50:
                                        TetrisGame.this.jDelay = 120;
                                        TetrisGame.this.jLevel = 6;
                                        break;
                                    case 60:
                                        TetrisGame.this.jDelay = 100;
                                        TetrisGame.this.jLevel = 7;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                        if (completeLines > 0) {
                            //Plus on compléte de lignes à la fois, plus le score augmente
                            switch (completeLines) {
                                //On gagne 10 points pour une seule ligne
                                case 1:
                                    TetrisGame.this.jScore += 10;
                                    break;
                                //30 points pour 2 lignes
                                case 2:
                                    TetrisGame.this.jScore += 30;
                                    break;
                                //50 points pour 3 lignes
                                case 3:
                                    TetrisGame.this.jScore += 50;
                                    break;
                                //50 points pour 3 lignes
                                case 4:
                                    TetrisGame.this.jScore += 80;
                                    break;
                                //Si le compte est supérieur alors on multiplie le compte par 100 (impossible)
                                default:
                                    TetrisGame.this.jScore += completeLines * 100;
                                    break;
                            }
                        }

                        if (TetrisGame.this.jNextPiece != null)
                            TetrisGame.this.jCurrPiece = TetrisGame.this.jNextPiece;
                        else
                            TetrisGame.this.jCurrPiece = Tetriminos.getRandomPiece(TetrisGame.this.jBoard);

                        TetrisGame.this.jNextPiece = Tetriminos.getRandomPiece(TetrisGame.this.jBoard);
                        TetrisGame.this.jCurrPiece.setCentrePoint(new Point(TetrisGame.this.jBoard.getColumns() / 2, 1));

                        //On notifie les changements (score, niveau, prochaine pièce)
                        TetrisGame.this.notifyObservateurs();

                        if (TetrisGame.this.jBoard.willFit(TetrisGame.this.jCurrPiece)) {
                            //On ajoute la nouvelle pièce si elle s'imbrique
                            TetrisGame.this.jBoard.addPiece(TetrisGame.this.jCurrPiece, true);
                        } else {
                            // Sinon on l'ajoute mais on arrête le jeu
                            TetrisGame.this.jBoard.addPiece(TetrisGame.this.jCurrPiece, true);
                            TetrisGame.this.stopGame();
                            break;
                        }
                    } else {
                        try 
                        {
                            GameThread.sleep(TetrisGame.this.jDelay);
                        } catch (InterruptedException e) {
                            System.out.println("Exception e: " + e.getMessage().toString());
                        }
                        //On fait descendre la pièce actuelle d'un cran après l'attente
                        TetrisGame.this.move(Tetriminos.DOWN);
                    }
                }
            }
        }
    }
}
