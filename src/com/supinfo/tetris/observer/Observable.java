package com.supinfo.tetris.observer;

/**
 * Pattern Observer : Interface Observable
 * @author Charlie
 *
 */
public interface Observable {
	/**
	 * Ajoute l'observateur donn� en param�tre � la liste des observateurs
	 * @param observateur : un observateur appartenant � la liste
	 */
	public void addObservateur(Observateur observateur);
	/**
	 * Notifie � tous les observateurs une mise � jour
	 */
	public void notifyObservateurs();
	/**
	 * Notifie � tous les observateurs une mise � jour en envoyant un param�tre
	 * @param object : objet pass� en param�tre aux observateurs
	 */
	public void notifyObservateurs(Object object);
	/**
	 * Enl�ve l'observateur donn� en param�tre de la liste des observateurs
	 * @param observateur : un observateur appartenant � la liste
	 */
	public void removeObservateur(Observateur observateur);
	/**
	 * Supprime tous les observateurs
	 */
	public void removeObservateurs();
}
