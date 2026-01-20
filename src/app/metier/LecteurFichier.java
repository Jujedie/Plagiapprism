package app.metier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecteurFichier
{
	//-----------------------
	// Lecture du fichier
	//-----------------------
	public static String lireFichier( String cheminFichier ) throws IOException
	{
		StringBuilder  contenu = new StringBuilder();

		BufferedReader br      = new BufferedReader(new FileReader(cheminFichier));

		// A chaque ligne, on ajoute un espace
		String ligne;
		while ((ligne = br.readLine()) != null)
			contenu.append(ligne).append(" ");

		br.close();

		// Nettoyage
		contenu = new StringBuilder( LecteurFichier.nettoyerTexte( contenu.toString() ) );

		return contenu.toString();
	}

	//-----------------------
	// Nettoyage du texte
	//-----------------------
	private static String nettoyerTexte( String texte )
	{
		// Normalisation, on ne garde que les lettres et chiffres
		return texte.toLowerCase().replaceAll("[^a-z0-9]", " ");
	}
}
