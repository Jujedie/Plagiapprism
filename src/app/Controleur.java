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

	public void importer(String fichier1, String fichier2)
	{
		this.differencesFichiers.setTextes( fichier1, fichier2 );
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