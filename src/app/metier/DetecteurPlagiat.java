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
	private int[] tabPositionsFin;

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
		this.texteOriginal = DetecteurPlagiat.nettoyerTexte(LecteurFichier.lireFichier(cheminFichier1));
		this.texteSuspecte = DetecteurPlagiat.nettoyerTexte(LecteurFichier.lireFichier(cheminFichier2));
	}

	public void analyser(int nombreMotsMin)
	{
		long tempsDebut = System.currentTimeMillis();

		if (texteOriginal.isEmpty() || texteSuspecte.isEmpty()) 
		{
			return;
		}

		List<String>  lstMotsOriginal          = new ArrayList<String>();
		List<Integer> lstPositionsMotsOriginal = new ArrayList<Integer>();
		this.extraire(texteOriginal, lstMotsOriginal, lstPositionsMotsOriginal);

		List<String>  lstMotsSuspecte          = new ArrayList<String>();
		List<Integer> lstPositionsMotsSuspecte = new ArrayList<Integer>();
		this.extraire(texteSuspecte, lstMotsSuspecte, lstPositionsMotsSuspecte);

		// On crée une Map (Dictionnaire) : Clé = Mot, Valeur = Liste des positions (index) où il apparaît
		Map<String, List<Integer>> mapIndexMotsOriginal = new HashMap<String, List<Integer>>();
		for (int indexMot = 0; indexMot < lstMotsOriginal.size(); indexMot++)
		{
			String mot = lstMotsOriginal.get(indexMot);
			// Si le mot n'est pas dans la Map, on crée une nouvelle liste, puis on ajoute l'index actuel
			mapIndexMotsOriginal.computeIfAbsent(mot, k -> new ArrayList<Integer>()).add(indexMot);
		}

		// Listes temporaires pour stocker les résultats trouvés
		List<Integer> lstPositionsDebutPlagiat = new ArrayList<Integer>();
		List<Integer> lstPositionsFinPlagiat   = new ArrayList<Integer>();

		// Tableau pour marquer les mots déjà identifiés comme plagiat
		boolean[] motsDejaUtilises = new boolean[lstMotsSuspecte.size()];

		// On parcourt chaque mot du texte suspect
		for (int indexMotSuspecte = 0; indexMotSuspecte < lstMotsSuspecte.size(); indexMotSuspecte++)
		{
			// Si le mot fait déjà partie d'une séquence de plagiat détectée, on passe au suivant
			if (motsDejaUtilises[indexMotSuspecte])
			{
				continue;
			}

			String motActuel = lstMotsSuspecte.get(indexMotSuspecte);

			// Si le mot n'existe même pas dans l'original, inutile de chercher une suite
			if (!mapIndexMotsOriginal.containsKey(motActuel)) 
			{
				continue;
			}

			// Pour chaque endroit où ce mot apparaît dans le texte original
			for (int indexMotOriginal : mapIndexMotsOriginal.get(motActuel))
			{
				int nombreMotsCorrespondants = 0;

				// Tant que les mots se suivent et sont identiques dans les deux textes
				while (indexMotOriginal + nombreMotsCorrespondants < lstMotsOriginal.size() && 
					indexMotSuspecte + nombreMotsCorrespondants < lstMotsSuspecte.size() && 
					lstMotsOriginal.get(indexMotOriginal + nombreMotsCorrespondants)
									.equals(lstMotsSuspecte.get(indexMotSuspecte + nombreMotsCorrespondants)))
				{
					nombreMotsCorrespondants++;
				}

				// Si la longueur de la suite trouvée dépasse le seuil minimum pour être considérée comme du plagiat
				if (nombreMotsCorrespondants >= nombreMotsMin)
				{
					// Calcul de la position de fin (position début du dernier mot + sa longueur)
					int indexDernierMot = indexMotSuspecte + nombreMotsCorrespondants - 1;
					int positionFin     = lstPositionsMotsSuspecte.get(indexDernierMot) + lstMotsSuspecte.get(indexDernierMot).length();
					int positionDebut   = lstPositionsMotsSuspecte.get(indexMotSuspecte);

					// Enregistrement des coordonnées du bloc plagié
					lstPositionsDebutPlagiat.add(positionDebut);
					lstPositionsFinPlagiat.add(positionFin);

					// Marquage des mots comme "utilisés" pour ne pas les recompter
					for (int decalage = 0; decalage < nombreMotsCorrespondants; decalage++)
					{
						motsDejaUtilises[indexMotSuspecte + decalage] = true;
					}

					// On saute les mots déjà traités dans la boucle principale
					indexMotSuspecte += nombreMotsCorrespondants - 1;
					break;
				}
			}
		}

		// Conversion des listes en tableaux de int pour un accès plus rapide par l'interface
		this.tabPositionsDebut = lstPositionsDebutPlagiat.stream().mapToInt(Integer::intValue).toArray();
		this.tabPositionsFin   = lstPositionsFinPlagiat.stream().mapToInt(Integer::intValue).toArray();

		// Calcul du temps total d'exécution
		this.tempsExecution = System.currentTimeMillis() - tempsDebut;
	}

	public String getTexteOriginal()     { return this.texteOriginal;            }
	public String getTexteSuspecte()     { return this.texteSuspecte;            }
	public int[]  getTabPositionsDebut() { return this.tabPositionsDebut;        }
	public int[]  getTabPositionsFin()   { return this.tabPositionsFin;          }
	public int    getNombreSequences()   { return this.tabPositionsDebut.length; }
	public long   getTempsExecution()    { return this.tempsExecution;           }

	private void extraire(String texte, List<String> lstMots, List<Integer> lstPositions)
	{
		// Utilise une expression régulière pour extraire les mots + passage en minuscules pour ignorer la casse
		Matcher motsTrouves = Pattern.compile("\\b[\\p{L}\\d]+\\b").matcher(texte.toLowerCase());

		while (motsTrouves.find())
		{
			lstMots.add(motsTrouves.group());
			lstPositions.add(motsTrouves.start());
		}
	}

	// Remplace les caractères ayant des accents par leur équivalent sans accent
	private static String nettoyerTexte(String texte)
	{
		if (texte == null) return "";
		
		StringBuilder sb = new StringBuilder(texte.length());

		for (char caractere : texte.toLowerCase().toCharArray())
		{
			switch (caractere)
			{
				case 'à', 'â', 'ä'      -> sb.append('a');
				case 'é', 'è', 'ê', 'ë' -> sb.append('e');
				case 'ï', 'î'           -> sb.append('i');
				case 'ô'                -> sb.append('o');
				case 'ù', 'û', 'ü'      -> sb.append('u');
				case 'ç'                -> sb.append('c');
				default                 -> sb.append(caractere);
			}
		}

		return sb.toString();
	}
}