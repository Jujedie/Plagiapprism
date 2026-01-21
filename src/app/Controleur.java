package app;

import app.metier.DifferencesFichiers;

import app.ihm.FramePrincipal;

public class Controleur
{
	private DifferencesFichiers differencesFichiers;

	public Controleur()
	{
		this.differencesFichiers = new DifferencesFichiers();

		new FramePrincipal(this);
	}

	public String getTexteGauche()
	{
		return this.differencesFichiers.getTexte1();
	}

	public String getTexteDroite()
	{
		return this.differencesFichiers.getTexte2();
	}

	public void setTextes(String pathFichier1, String pathFichier2)
	{
		this.differencesFichiers.setTextes(pathFichier1, pathFichier2);
	}

	public void analyser()
	{
		this.differencesFichiers.calculerSimilarite();
	}

	public double getSimilarite()
	{
		return this.differencesFichiers.calculerSimilarite();
	}

	public String getInterpretation(double score)
	{
		return this.differencesFichiers.getInterpretation(score);
	}

	public long getTempsExecution()
	{
		return this.differencesFichiers.getTempsExecution();
	}

	public static void main(String[] args)
	{
		new Controleur();
	}
}