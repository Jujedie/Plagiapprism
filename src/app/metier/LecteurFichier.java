package app.metier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import java.nio.charset.StandardCharsets;

public class LecteurFichier
{
	public static String lireFichier(String cheminFichier)
	{
		try
		{
			StringBuilder  contenu = new StringBuilder();
			BufferedReader br      = new BufferedReader(new InputStreamReader(new FileInputStream(cheminFichier), StandardCharsets.UTF_8));

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