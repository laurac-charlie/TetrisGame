package com.supinfo.tetris.observer;

/**
 * Pattern Observer : Interface Observable
 * @author Charlie
 *
 */
public interface Observable {
	/**
	 * Ajoute l'observateur donné en paramètre à la liste des observateurs
	 * @param observateur : un observateur appartenant à la liste
	 */
	public void addObservateur(Observateur observateur);
	/**
	 * Notifie à tous les observateurs une mise à jour
	 */
	public void notifyObservateurs();
	/**
	 * Notifie à tous les observateurs une mise à jour en envoyant un paramètre
	 * @param object : objet passé en paramètre aux observateurs
	 */
	public void notifyObservateurs(Object object);
	/**
	 * Enlève l'observateur donné en paramètre de la liste des observateurs
	 * @param observateur : un observateur appartenant à la liste
	 */
	public void removeObservateur(Observateur observateur);
	/**
	 * Supprime tous les observateurs
	 */
	public void removeObservateurs();
}
