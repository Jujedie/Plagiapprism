package app.metier;

import java.io.BufferedReader;
import java.io.FileReader;

public class LecteurFichier
{
	// -----------------------
	// Lecture du fichier
	// -----------------------
	public static String lireFichier(String cheminFichier)
	{
		try
		{
			StringBuilder contenu = new StringBuilder();

			BufferedReader br = new BufferedReader(new FileReader(cheminFichier));

			// A chaque ligne, on ajoute un espace
			String ligne;
			while ((ligne = br.readLine()) != null)
				if (ligne != "")
					contenu.append(ligne).append(" ");
				else
					contenu.append(ligne).append("");

			br.close();

			// Nettoyage
			contenu = new StringBuilder(LecteurFichier.nettoyerTexte(contenu.toString()));

			return contenu.toString();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	// -----------------------
	// Nettoyage du texte
	// -----------------------
	private static String nettoyerTexte(String texte)
	{
		String texteNettoye = texte.toLowerCase();

		texteNettoye = texteNettoye.replaceAll("[àâä]", "a");
		texteNettoye = texteNettoye.replaceAll("[éèêë]", "e");
		texteNettoye = texteNettoye.replaceAll("[ïî]", "i");
		texteNettoye = texteNettoye.replaceAll("[ô]", "o");
		texteNettoye = texteNettoye.replaceAll("[ùûü]", "u");
		texteNettoye = texteNettoye.replaceAll("[^a-z0-9\\s]", " ");

		return texteNettoye;
	}
}
