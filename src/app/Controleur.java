package app;

import app.metier.DetecteurPlagiat;

import app.ihm.FramePrincipal;

public class Controleur
{
	private DetecteurPlagiat detecteur;

	public Controleur()
	{
		this.detecteur = new DetecteurPlagiat();
		new FramePrincipal(this);
	}

	public String getTexteOriginal()
	{
		return this.detecteur.getTexteOriginal();
	}

	public String getTexteSuspecte()
	{
		return this.detecteur.getTexteSuspecte();
	}

	public void chargerFichiers(String cheminFichier1, String cheminFichier2)
	{
		this.detecteur.chargerTextes(cheminFichier1, cheminFichier2);
	}

	public void analyser()
	{
		this.detecteur.detecterPlagiats();
	}

	public int getNombreSequencesPlagiees()
	{
		return this.detecteur.getNombreSequences();
	}

	public int[] getPositionsDebut()
	{
		return this.detecteur.positionsDebut;
	}

	public int[] getPositionsFin()
	{
		return this.detecteur.positionsFin;
	}

	public long getTempsExecution()
	{
		return this.detecteur.getTempsExecution();
	}

	public static void main(String[] args)
	{
		new Controleur();
	}
}