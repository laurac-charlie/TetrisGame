           package com.supinfo.tetris.observer;

/**
 * Pattern Observer : Interface Observable
 * @author Charlie
 *
 */
public interface Observateur {
	/**
	 * Met à jour l'observeur quand l'objet observable utilise notifyObservateurs
	 * @param : observable objet envoyant la notification
	 */
	public void update(Observable observable);
	/**
	 * Met à jour l'observeur quand l'objet observable utilise notifyObservateurs, en recevant un paramètre
	 * @param observable : objet envoyant la notification
	 * @param object : objet passé en paramètre lors de la notification
	 */
	public void update(Observable observable,Object object);
}
