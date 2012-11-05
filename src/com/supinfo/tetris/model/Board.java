package com.supinfo.tetris.model;

import com.supinfo.tetris.observer.Observable;
import com.supinfo.tetris.observer.Observateur;
import java.awt.*;
import java.util.*;

/**
 * Classe repr�sentant le plateau en m�moire sans interface graphique
 * 
 * @author Charlie
 */
public class Board implements Observable
{
    //Valeur d'un bloc vide
    public static final int EMPTY_BLOCK = -1;

    private ArrayList<Observateur> jBoardObservateurs =new ArrayList<>() ;
    private int[][] jMatrix;
    private int     jColumns;
    private int     jRows;

    /**
     * Cr�e un TetrisBoard 
     * 
     * @param cols   Le nombre de colonnes.
     * @param rows   Le nombre de lignes.
     */
    public Board(int cols, int rows)
    {
        this.jColumns = cols;
        this.jRows    = rows;

        //On initialise le plateau
        this.resetBoard();
    }

    /**
     * Met tous les blocs du plateau � vide
     */
    public void resetBoard()
    {
        this.jMatrix  = new int[this.jColumns][this.jRows];
       	
        for (int cols = 0; cols < this.jColumns; cols++) 
    	    for (int rows = 0; rows < this.jRows; rows++) 
    	    	this.jMatrix[cols][rows] = Board.EMPTY_BLOCK;
    }

    public int getColumns()
    {
    	return jColumns;
    }

    public void setColumns(int columns)
    {
        this.jColumns = columns;
        //On r�initialise toutes les entr�es du TetrisBoard en cas de modification
        this.resetBoard();
    }

    public int getRows()
    {
    	return this.jRows;
    }

    public void setRows(int rows)
    {
		this.jRows = rows;
		//On r�initialise toutes les entr�es du TetrisBoard en cas de modification
		resetBoard();
    }   

    /**
     * Renvoi la valeur du bloc aux coordonn�es x et y
     * 
     * @param x      Coordonn�e X
     * @param y      Coordonn�e Y
     * @return valeur du bloc
     */
    public int getPieceAt(int x, int y)
    {
    	return this.jMatrix[x][y];
    }

    /**
     * Change la valeur du bloc aux coordonn�es x et y
     * 
     * @param x      Coordonn�e X
     * @param y      Coordonn�e Y
     * @param value  La valeur � mettre
     */
    public void setPieceAt(int x, int y, int value)
    {
        this.jMatrix[x][y] = value;
    }

    /**
     * Ajoute une pi�ce au plateau
     * 
     * @param piece  La pi�ce a ajout�e
     * @param notify Si ce param�tre est vrai on notifie l'ajout
     */
    public void addPiece(Tetriminos piece, boolean notify)
    {
        if (piece != null)
        {
            Point   centre = piece.getCentrePoint();
            Point[] blocks = piece.getRelativePoints();

        	for (int count = 0; count < 4; count++) 
        	{
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;
        
        	    this.jMatrix[x][y] = piece.getType();
        	}
                    
        	if (notify) this.notifyObservateurs();
        }
    }

    /**
     * Retire la pi�ce du plateau
     * 
     * @param piece  La pi�ce a retir�
     */
    public void removePiece(Tetriminos piece)
    {
        if (piece != null)	
        {
        	Point   centre = piece.getCentrePoint();
            Point[] blocks = piece.getRelativePoints();

            for (int count = 0; count < 4; count++) 
        	{
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;
            
        	    this.jMatrix[x][y] = Board.EMPTY_BLOCK;
        	}
        }
    }

    /**
     * Supprime la ligne et descend toutes les lignes qui sont au dessus.
     * La modification sera notifi� au plateau.
     * 
     * @param row : l'index de la ligne a supprim�e
     */
    public void removeRow(int row)
    {
    	//On fait descendre d'un cran toutes lignes au dessus
    	for (int tempRow = row; tempRow > 0; tempRow--) 
    	    for (int tempCol = 0; tempCol < this.jColumns; tempCol++) 
    		    this.jMatrix[tempCol][tempRow] = this.jMatrix[tempCol][tempRow - 1];
    
    	
    	for (int tempCol = 0; tempCol < this.jColumns; tempCol++)
    	    this.jMatrix[tempCol][0] = Board.EMPTY_BLOCK;
    
    	//On notifie le changement � l'interface graphique
    	this.notifyObservateurs();
    }

    /**
     * On teste si la pi�ce s'imbriquera correctement dans le plateau
     * 
     * @param piece : la pi�ce a test�
     * @return Vrai si la pi�ce s'embrique, faux dans le cas contraire
     */
    public boolean willFit(Tetriminos piece)
    {
        boolean result = true;

        //On v�rifie que l'on a bien une pi�ce
        if (piece != null)
        {
            Point   centre = piece.getCentrePoint();
            Point[] blocks = piece.getRelativePoints();
    
            //On parcours les dimensions de la pi�ce pour v�rifier la validit� du positionnement de chaque point
        	for (int count = 0; count < 4 && result == true; count++) 
        	{
        	    int x = centre.x + blocks[count].x;
        	    int y = centre.y + blocks[count].y;
        	    
                // On v�rifie qu'on est dans les limites du plateau
        	    if (x < 0 || x >= this.jColumns || y < 0 || y >= this.jRows)
        		    result = false;
        	     
        	    //On v�rifie que le prochain emplacement du point est libre
                if (result && this.jMatrix[x][y] != Board.EMPTY_BLOCK) result = false;
        	}
        }
        else
        	return false;
        
    	return result;
    }

    @Override
    public void addObservateur(Observateur observateur)
    {
        this.jBoardObservateurs.add(observateur);
    }

    @Override
    public void notifyObservateurs()
    {
        for(Observateur o : this.jBoardObservateurs)
            o.update(this);
    }
    
    @Override
	public void notifyObservateurs(Object object)
	{
        for(Observateur o : this.jBoardObservateurs)
            o.update(this, object);
	}
	
    @Override
	public void removeObservateur(Observateur observateur)
	{
		this.jBoardObservateurs.remove(observateur);
	}
	
    @Override
	public void removeObservateurs()
	{
		this.jBoardObservateurs.removeAll(this.jBoardObservateurs);
	}
    
}
