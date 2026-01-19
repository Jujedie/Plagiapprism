package app;

import app.metier.Algorithme;

import app.ihm.FramePrincipal;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Controleur
{
	private Algorithme algorithme;

	public Controleur()
	{
		this.algorithme = new Algorithme();

		new FramePrincipal(this);
	}

	public void importer(String path1, String path2)
	{
		try
		{
			String t1 = new String(Files.readAllBytes(Paths.get(path1)));
			String t2 = new String(Files.readAllBytes(Paths.get(path2)));

			this.algorithme.setTextes(t1, t2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void analyser()
	{
		this.algorithme.calculerSimilarite();
	}

	public double getSimilarite()
	{
		return this.algorithme.calculerSimilarite();
	}

	public String getInterpretation(double score)
	{
		return this.algorithme.getInterpretation(score);
	}

	public long getTempsExecution()
	{
		return this.algorithme.getTempsExecution();
	}

	public static void main(String[] args)
	{
		new Controleur();
	}
}