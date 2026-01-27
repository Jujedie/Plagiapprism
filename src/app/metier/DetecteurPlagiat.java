package app.metier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetecteurPlagiat
{
	private String texteOriginal;
	private String texteSuspecte;

	private int[] tabPositionsDebut;
	private int[] tabPositionsFin  ;

	private long tempsExecution;


	public DetecteurPlagiat()
	{
		this.texteOriginal = "";
		this.texteSuspecte = "";

		this.tabPositionsDebut = new int[0];
		this.tabPositionsFin   = new int[0];

		this.tempsExecution = 0;
	}

	public void chargerTextes(String cheminFichier1, String cheminFichier2)
	{
		this.texteOriginal = LecteurFichier.lireFichier(cheminFichier1);
		this.texteOriginal = DetecteurPlagiat.nettoyerTexte(this.texteOriginal);
		this.texteSuspecte = LecteurFichier.lireFichier(cheminFichier2);
		this.texteSuspecte = DetecteurPlagiat.nettoyerTexte(this.texteSuspecte);
	}

	public void analyser(int nombreMotsMin)
	{
		long tempsDebut = System.currentTimeMillis();

		if (texteOriginal.isEmpty() || texteSuspecte.isEmpty()) return;

		List<String>  lstMotsOriginal          = new ArrayList<String >();
		List<Integer> lstPositionsMotsOriginal = new ArrayList<Integer>();
		this.extraire(texteOriginal, lstMotsOriginal, lstPositionsMotsOriginal);
		
		List<String>  lstMotsSuspecte          = new ArrayList<String >();
		List<Integer> lstPositionsMotsSuspecte = new ArrayList<Integer>();
		this.extraire(texteSuspecte, lstMotsSuspecte, lstPositionsMotsSuspecte);

		Map<String, List<Integer>> mapIndexMotsOriginal = new HashMap<String, List<Integer>>();
		for (int indexMot = 0; indexMot < lstMotsOriginal.size(); indexMot++)
		{
			String mot = lstMotsOriginal.get(indexMot);
			mapIndexMotsOriginal.computeIfAbsent(mot, k -> new ArrayList<Integer>()).add(indexMot);
		}

		List<Integer> lstPositionsDebutPlagiat = new ArrayList<Integer>();
		List<Integer> lstPositionsFinPlagiat   = new ArrayList<Integer>();

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

				if (nombreMotsCorrespondants >= nombreMotsMin)
				{
					int indexDernierMot = indexMotSuspecte + nombreMotsCorrespondants - 1;
					int positionFin     = lstPositionsMotsSuspecte.get(indexDernierMot) + lstMotsSuspecte.get(indexDernierMot).length();
					int positionDebut   = lstPositionsMotsSuspecte.get(indexMotSuspecte);

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

	public int[] getTabPositionsDebut()
	{
		return this.tabPositionsDebut;
	}

	public int[] getTabPositionsFin()
	{
		return this.tabPositionsFin;
	}

	public int getNombreSequences()
	{
		return this.tabPositionsDebut.length;
	}

	public long getTempsExecution()
	{
		return this.tempsExecution;
	}

	/**
	 * Extrait tous les mots d'au moins 3 caractères (lettres ou chiffres) du texte fourni,
	 * en les ajoutant à la liste lstMots et en enregistrant la position de début de chaque mot
	 * dans la liste lstPositions.
	 *
	 * Un mot est défini comme une séquence de caractères alphanumériques (lettres ou chiffres)
	 * d'au moins 3 caractères, délimitée par des bornes de mots.
	 *
	 * @param texte        Le texte à analyser.
	 * @param lstMots      La liste dans laquelle les mots extraits seront ajoutés (en minuscules).
	 * @param lstPositions La liste dans laquelle les positions de début de chaque mot seront ajoutées.
	 */
	private void extraire(String texte, List<String> lstMots, List<Integer> lstPositions)
	{
		Matcher motsTrouves = Pattern.compile("\\b[\\p{L}\\d]{3,}\\b").matcher(texte.toLowerCase());

		while (motsTrouves.find())
		{
			lstMots.add(motsTrouves.group());
			lstPositions.add(motsTrouves.start());
		}
	}

	private static String nettoyerTexte( String texte )
	{
		String texteNettoye = texte.toLowerCase();

		texteNettoye = texteNettoye.replaceAll("[àâä]" , "a");
		texteNettoye = texteNettoye.replaceAll("[éèêë]", "e");
		texteNettoye = texteNettoye.replaceAll("[ïî]"  , "i");
		texteNettoye = texteNettoye.replaceAll("ô"     , "o");
		texteNettoye = texteNettoye.replaceAll("[ùûü]" , "u");
		texteNettoye = texteNettoye.replaceAll("ç"     , "c");

		return texteNettoye;
	}
}