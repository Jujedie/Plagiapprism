package app.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetecteurPlagiat
{
	private String texteOriginal = "";
	private String texteSuspecte = "";

	public int[] tabPositionsDebut = new int[0];
	public int[] tabPositionsFin   = new int[0];

	private long tempsExecution = 0;

	public void chargerTextes(String cheminFichier1, String cheminFichier2)
	{
		this.texteOriginal = LecteurFichier.lireFichier(cheminFichier1);
		this.texteSuspecte = LecteurFichier.lireFichier(cheminFichier2);
	}

	public void detecterPlagiats()
	{
		long tempsDebut = System.currentTimeMillis();

		if (texteOriginal.isEmpty() || texteSuspecte.isEmpty()) return;

		List<String>  lstMotsOriginal          = new ArrayList<>();
		List<Integer> lstPositionsMotsOriginal = new ArrayList<>();
		extraire(texteOriginal, lstMotsOriginal, lstPositionsMotsOriginal);
		
		List<String>  lstMotsSuspecte          = new ArrayList<>();
		List<Integer> lstPositionsMotsSuspecte = new ArrayList<>();
		extraire(texteSuspecte, lstMotsSuspecte, lstPositionsMotsSuspecte);

		Map<String, List<Integer>> mapIndexMotsOriginal = new HashMap<>();
		for (int indexMot = 0; indexMot < lstMotsOriginal.size(); indexMot++)
		{
			String mot = lstMotsOriginal.get(indexMot);
			mapIndexMotsOriginal.computeIfAbsent(mot, k -> new ArrayList<>()).add(indexMot);
		}

		List<Integer> lstPositionsDebutPlagiat = new ArrayList<>();
		List<Integer> lstPositionsFinPlagiat   = new ArrayList<>();

		boolean[] motsDejaUtilises = new boolean[lstMotsSuspecte.size()];

		for (int indexMotSuspecte = 0; indexMotSuspecte < lstMotsSuspecte.size(); indexMotSuspecte++)
		{
			if (motsDejaUtilises[indexMotSuspecte])
				continue;

			String motActuel = lstMotsSuspecte.get(indexMotSuspecte);

			if (!mapIndexMotsOriginal.containsKey(motActuel)) 
				continue;

			for (int indexMotOriginal : mapIndexMotsOriginal.get(motActuel))
			{
				int nombreMotsCorrespondants = 0;

				while (indexMotOriginal + nombreMotsCorrespondants < lstMotsOriginal.size() && 
				       indexMotSuspecte + nombreMotsCorrespondants < lstMotsSuspecte.size() && 
				       lstMotsOriginal.get(indexMotOriginal + nombreMotsCorrespondants).equals(lstMotsSuspecte.get(indexMotSuspecte + nombreMotsCorrespondants)))
				{
					nombreMotsCorrespondants++;
				}

				if (nombreMotsCorrespondants >= 3)
				{
					int positionDebut = lstPositionsMotsSuspecte.get(indexMotSuspecte);

					int indexDernierMot = indexMotSuspecte + nombreMotsCorrespondants - 1;
					int positionFin = lstPositionsMotsSuspecte.get(indexDernierMot) + lstMotsSuspecte.get(indexDernierMot).length();

					lstPositionsDebutPlagiat.add(positionDebut);
					lstPositionsFinPlagiat.add(positionFin);

					for (int decalage = 0; decalage < nombreMotsCorrespondants; decalage++)
					{
						motsDejaUtilises[indexMotSuspecte + decalage] = true;
					}

					indexMotSuspecte += nombreMotsCorrespondants - 1;
					break;
				}
			}
		}

		this.tabPositionsDebut = lstPositionsDebutPlagiat.stream().mapToInt(Integer::intValue).toArray();
		this.tabPositionsFin = lstPositionsFinPlagiat.stream().mapToInt(Integer::intValue).toArray();
		this.tempsExecution = System.currentTimeMillis() - tempsDebut;
	}

	public String getTexteOriginal()
	{
		return this.texteOriginal;
	}

	public String getTexteSuspecte()
	{
		return this.texteSuspecte;
	}

	public int getNombreSequences()
	{
		return this.tabPositionsDebut.length;
	}

	public long getTempsExecution()
	{
		return this.tempsExecution;
	}

	private void extraire(String texte, List<String> lstMots, List<Integer> lstPositions)
	{
		Matcher motsTrouves = Pattern.compile("\\b[\\p{L}\\d]{3,}\\b").matcher(texte.toLowerCase());

		while (motsTrouves.find())
		{
			lstMots.add(motsTrouves.group());
			lstPositions.add(motsTrouves.start());
		}
	}
}