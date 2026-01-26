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
				contenu.append(ligne).append(" ");
			}

			br.close();
			return contenu.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}
}