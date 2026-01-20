package app.metier;

import java.io.IOException;

public class DifferencesFichiers
{
	public String fichier1;
	public String fichier2;

	public DifferencesFichiers( String pathFichier1, String pathFichier2 )
	{
		try
		{
			this.fichier1 = LecteurFichier.lireFichier( pathFichier1 );
			this.fichier2 = LecteurFichier.lireFichier( pathFichier2 );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		
	}
}
