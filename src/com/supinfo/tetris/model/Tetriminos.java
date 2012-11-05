package com.supinfo.tetris.model;

import java.awt.Color;
import java.awt.Point;

/**
 * Classe repr�sentant les Tetriminos
 *
 * @author Charlie
 *
 */
public class Tetriminos {
    //type de pi�ce

    public static final int L_PIECE = 0;
    public static final int J_PIECE = 1;
    public static final int I_PIECE = 2;
    public static final int Z_PIECE = 3;
    public static final int S_PIECE = 4;
    public static final int O_PIECE = 5;
    public static final int T_PIECE = 6;
    //mouvement de pi�ce
    public static final int LEFT = 10;
    public static final int RIGHT = 11;
    public static final int ROTATE = 12;
    public static final int DOWN = 13;
    public static final int FALL = 14;
    //type de la pi�ce
    private int jType;
    private int jRotation;
    private int jMaxRotate;
    //Point central de la pi�ce
    private Point jCentrePoint = new Point();
    //Tableau contenant les points en fonctions du point centrale de la pi�ce 
    private Point[] jBlocks = new Point[4];
    private Board jBoard = null;

    /**
     * Cr�e un Tetriminos
     *
     * @param type : type/forme de la pi�ce
     * @param board : TetrisBoard dans lequel sera le tetriminos
     */
    public Tetriminos(int type, Board board) {
        this.jType = type;
        this.jBoard = board;
        initializeBlocks();
    }

    /**
     * D�place le tetriminos dans la direction donn�e en param�tre
     *
     * @param direction : direction d�finie dans les variables final
     * @return true si le d�placement a r�ussi
     */
    public boolean move(int direction) {
        boolean result = true;

        //Descente normale de la pi�ce
        if (direction == FALL) {
            boolean loop = true;

            while (loop) {
                //On supprime la pi�ce et on la replace plus bas si on peut, sinon on s'arr�te
                this.jBoard.removePiece(this);
                this.jCentrePoint.y++; // Drop

                if (this.jBoard.willFit(this)) {
                    this.jBoard.addPiece(this, false);
                } else {
                    this.jCentrePoint.y--; // Undrop
                    this.jBoard.addPiece(this, true);
                    loop = false;
                    result = false;
                }
            }
        } else {
            this.jBoard.removePiece(this);

            //Selon la direction on va d�placer la pi�ce
            switch (direction) {
                case Tetriminos.LEFT:
                    this.jCentrePoint.x--;
                    break; // gauche
                case Tetriminos.RIGHT:
                    this.jCentrePoint.x++;
                    break; // droite
                case Tetriminos.DOWN:
                    this.jCentrePoint.y++;
                    break; // descend
                case Tetriminos.ROTATE:
                    this.rotateClockwise();
                    break; // tourne
            }

            //Si le d�placement n'est pas bon, on l'annule
            if (this.jBoard.willFit(this)) {
                this.jBoard.addPiece(this, true);
            } else {
                switch (direction) {
                    case Tetriminos.LEFT:
                        this.jCentrePoint.x++;
                        break; // droite
                    case Tetriminos.RIGHT:
                        this.jCentrePoint.x--;
                        break; // gauche
                    case Tetriminos.DOWN:
                        this.jCentrePoint.y--;
                        break; // monte
                    case Tetriminos.ROTATE:
                        this.rotateAntiClockwise();
                        break; // tourne
                }

                //On affiche � la nouvelle position (ou la m�me)
                this.jBoard.addPiece(this, true);
                result = false;
            }
        }

        return result;
    }

    public Point getCentrePoint() {
        return this.jCentrePoint;
    }

    public void setCentrePoint(Point point) {
        this.jCentrePoint = point;
    }

    public Point[] getRelativePoints() {
        return this.jBlocks;
    }

    public void setRelativePoints(Point[] blocks) {
        if (blocks != null) {
            jBlocks = blocks;
        }
    }

    public int getType() {
        return this.jType;
    }

    public void setType(int type) {
        this.jType = type;
        initializeBlocks();
    }

    /**
     * Initialise les diff�rents points de la pi�ce
     */
    private void initializeBlocks() {
        //Selon le type on va cr�er l'une des 7 pi�ces
        switch (this.jType) {
            case I_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(-1, 0);
                this.jBlocks[2] = new Point(1, 0);
                this.jBlocks[3] = new Point(2, 0);
                this.jMaxRotate = 2;
                break;

            case L_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(-1, 0);
                this.jBlocks[2] = new Point(-1, 1);
                this.jBlocks[3] = new Point(1, 0);
                this.jMaxRotate = 4;
                break;

            case J_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(-1, 0);
                this.jBlocks[2] = new Point(1, 0);
                this.jBlocks[3] = new Point(1, 1);
                this.jMaxRotate = 4;
                break;

            case Z_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(-1, 0);
                this.jBlocks[2] = new Point(0, 1);
                this.jBlocks[3] = new Point(1, 1);
                this.jMaxRotate = 2;
                break;

            case S_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(1, 0);
                this.jBlocks[2] = new Point(0, 1);
                this.jBlocks[3] = new Point(-1, 1);
                this.jMaxRotate = 2;
                break;

            case O_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(0, 1);
                this.jBlocks[2] = new Point(-1, 0);
                this.jBlocks[3] = new Point(-1, 1);
                this.jMaxRotate = 1;
                break;

            case T_PIECE:
                this.jBlocks[0] = new Point(0, 0);
                this.jBlocks[1] = new Point(-1, 0);
                this.jBlocks[2] = new Point(1, 0);
                this.jBlocks[3] = new Point(0, 1);
                this.jMaxRotate = 4;
                break;
        }
    }

    /**
     * Rotation horaire de la pi�ce
     */
    private void rotateClockwise() {
        //Si la pi�ce est autoris� � tourner
        if (this.jMaxRotate > 1) {
            this.jRotation++;

            if (this.jMaxRotate == 2 && this.jRotation == 2) {
                // Rotation Anti-Horaire
                rotateClockwiseNow();
                rotateClockwiseNow();
                rotateClockwiseNow();
            } else {
                rotateClockwiseNow();
            }
        }

        //On utilise modulo pour mettre � jour l'etat de rotation de la pi�ce
        this.jRotation = this.jRotation % this.jMaxRotate;
    }

    /**
     * Rotation anti-horaire (3 rotation horaire)
     */
    private void rotateAntiClockwise() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    /**
     * Fait la mise a jour pour la rotation
     */
    private void rotateClockwiseNow() {
        int temp = 0;
        for (int count = 0; count < 4; count++) {
            temp = this.jBlocks[count].x;

            jBlocks[count].x = -jBlocks[count].y;
            jBlocks[count].y = temp;
        }
    }

    /**
     * Renvoie un tetriminos al�atoirement
     *
     * @param board : TetrisBoard dans lequel on va plac� la pi�ce
     * @return : une pi�ce al�atoire
     */
    public static Tetriminos getRandomPiece(Board board) {
        return new Tetriminos((int) (Math.random() * 7), board);
    }

    /**
     * On r�cup�re une couleur pour la pi�ce selon son type
     *
     * @param typePiece : type de la pi�ce
     * @return Renvoi la couleur de la pi�ce
     */
    public static Color getPieceColor(int typePiece) {
        switch (typePiece) {
            case Tetriminos.L_PIECE:
                return Color.CYAN;
            case Tetriminos.J_PIECE:
                return new Color(206, 56, 173);  // violet
            case Tetriminos.I_PIECE:
                return new Color(41, 40, 206);   // blue fonc�
            case Tetriminos.Z_PIECE:
                return Color.RED;
            case Tetriminos.S_PIECE:
                return new Color(82, 154, 16);   // vert
            case Tetriminos.O_PIECE:
                return Color.GRAY;
            case Tetriminos.T_PIECE:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }
}
