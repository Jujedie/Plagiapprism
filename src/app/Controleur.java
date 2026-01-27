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

	public void analyser(int nombreMotsMin)
	{
		this.detecteur.analyser(nombreMotsMin);
	}

	public int getNombreSequencesPlagiees()
	{
		return this.detecteur.getNombreSequences();
	}

	public int[] getTabPositionsDebut()
	{
		return this.detecteur.getTabPositionsDebut();
	}

	public int[] getTabPositionsFin()
	{
		return this.detecteur.getTabPositionsFin();
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