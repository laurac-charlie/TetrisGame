           package com.supinfo.tetris.observer;

/**
 * Pattern Observer : Interface Observable
 * @author Charlie
 *
 */
public interface Observateur {
	/**
	 * Met � jour l'observeur quand l'objet observable utilise notifyObservateurs
	 * @param : observable objet envoyant la notification
	 */
	public void update(Observable observable);
	/**
	 * Met � jour l'observeur quand l'objet observable utilise notifyObservateurs, en recevant un param�tre
	 * @param observable : objet envoyant la notification
	 * @param object : objet pass� en param�tre lors de la notification
	 */
	public void update(Observable observable,Object object);
}
