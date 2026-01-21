package app.metier;

import java.io.BufferedReader;
import java.io.FileReader;

public class LecteurFichier
{
	public static String lireFichier(String cheminFichier)
	{
		try
		{
			StringBuilder contenu = new StringBuilder();

			BufferedReader br = new BufferedReader(new FileReader(cheminFichier));

			String ligne;
			while ((ligne = br.readLine()) != null)
			{
				if ( ligne != "" )
				{
					contenu.append(ligne).append(" ");
				}
				else
				{
					contenu.append(ligne).append("");
				}
			}

			br.close();

			contenu = new StringBuilder( LecteurFichier.nettoyerTexte(contenu.toString()));

			return contenu.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	private static String nettoyerTexte( String texte )
	{
		String texteNettoye = texte.toLowerCase();

		texteNettoye.replaceAll("[àâä]" , "a");
		texteNettoye.replaceAll("[éèêë]", "e");
		texteNettoye.replaceAll("[ïî]"  , "i");
		texteNettoye.replaceAll("ô"     , "o");
		texteNettoye.replaceAll("[ùûü]" , "u");

		return texteNettoye;
	}
}