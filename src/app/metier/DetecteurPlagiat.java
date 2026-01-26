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

	public int[] positionsDebut = new int[0];
	public int[] positionsFin   = new int[0];

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

		// 1. Extraction rapide des mots et positions
		List<String>  motsOriginal          = new ArrayList<>();
		List<Integer> positionsMotsOriginal = new ArrayList<>();
		extraire(texteOriginal, motsOriginal, positionsMotsOriginal);
		
		List<String>  motsSuspecte          = new ArrayList<>();
		List<Integer> positionsMotsSuspecte = new ArrayList<>();
		extraire(texteSuspecte, motsSuspecte, positionsMotsSuspecte);

		// 2. Indexation du texte original pour la performance (Optimisation)
		// Créer un dictionnaire : mot -> liste des positions où il apparaît
		Map<String, List<Integer>> indexMotsOriginal = new HashMap<>();
		for (int indexMot = 0; indexMot < motsOriginal.size(); indexMot++)
		{
			String mot = motsOriginal.get(indexMot);
			indexMotsOriginal.computeIfAbsent(mot, k -> new ArrayList<>()).add(indexMot);
		}

		List<Integer> positionsDebutPlagiat = new ArrayList<>();
		List<Integer> positionsFinPlagiat = new ArrayList<>();
		boolean[] motDejaUtilise = new boolean[motsSuspecte.size()];

		// 3. Détection par extension (parcours du texte suspect)
		for (int indexMotSuspecte = 0; indexMotSuspecte < motsSuspecte.size(); indexMotSuspecte++)
		{
			// Si ce mot a déjà été marqué comme plagiat, on saute
			if (motDejaUtilise[indexMotSuspecte]) 
				continue;
			
			String motActuel = motsSuspecte.get(indexMotSuspecte);
			
			// Si ce mot n'existe pas dans le texte original, on saute
			if (!indexMotsOriginal.containsKey(motActuel)) 
				continue;

			// Pour chaque position où ce mot apparaît dans le texte original
			for (int indexMotOriginal : indexMotsOriginal.get(motActuel))
			{
				int nombreMotsCorrespondants = 0;
				
				// On compare et on s'étend à droite pour trouver combien de mots consécutifs correspondent
				while (indexMotOriginal + nombreMotsCorrespondants < motsOriginal.size() && 
				       indexMotSuspecte + nombreMotsCorrespondants < motsSuspecte.size() && 
				       motsOriginal.get(indexMotOriginal + nombreMotsCorrespondants).equals(motsSuspecte.get(indexMotSuspecte + nombreMotsCorrespondants)))
				{
					nombreMotsCorrespondants++;
				}

				// Si on trouve au moins 3 mots consécutifs identiques, c'est un plagiat
				if (nombreMotsCorrespondants >= 3)
				{
					// Position de début du plagiat dans le texte suspect
					int positionDebut = positionsMotsSuspecte.get(indexMotSuspecte);
					
					// Position de fin du plagiat = position du dernier mot + sa longueur
					int indexDernierMot = indexMotSuspecte + nombreMotsCorrespondants - 1;
					int positionFin = positionsMotsSuspecte.get(indexDernierMot) + motsSuspecte.get(indexDernierMot).length();
					
					positionsDebutPlagiat.add(positionDebut);
					positionsFinPlagiat.add(positionFin);
					
					// Marquer tous les mots de cette séquence comme déjà utilisés
					for (int decalage = 0; decalage < nombreMotsCorrespondants; decalage++)
					{
						motDejaUtilise[indexMotSuspecte + decalage] = true;
					}
					
					// Sauter cette séquence déjà détectée
					indexMotSuspecte += nombreMotsCorrespondants - 1;
					break;
				}
			}
		}
		
		// Convertir les listes en tableaux
		this.positionsDebut = positionsDebutPlagiat.stream().mapToInt(Integer::intValue).toArray();
		this.positionsFin = positionsFinPlagiat.stream().mapToInt(Integer::intValue).toArray();
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
		return this.positionsDebut.length;
	}

	public long getTempsExecution()
	{
		return this.tempsExecution;
	}

	private void extraire(String texte, List<String> listeMots, List<Integer> listePositions)
	{
		// Expression régulière : mot de 3+ caractères (lettres ou chiffres)
		Matcher motsTrouves = Pattern.compile("\\b[\\p{L}\\d]{3,}\\b").matcher(texte.toLowerCase());
		
		// Parcourir tous les mots trouvés par l'expression régulière
		while (motsTrouves.find())
		{
			listeMots.add(motsTrouves.group());      // Ajouter le mot trouvé
			listePositions.add(motsTrouves.start()); // Ajouter sa position dans le texte
		}
	}
}