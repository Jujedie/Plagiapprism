package app.metier;

public class Algorithme
{
	public Algorithme()
	{
		
	}
}

import java.util.*;
import java.nio.file.*;
import java.io.IOException;

public class MinHashDetector
{
	private static final int    NUM_HASHES = 1000       ;
	private        final long[] a                       ; // Coefficients 'a' pour h(x) = (ax + b) % c
	private        final long[] b                       ; // Coefficients 'b'
	private static final long   LARGE_PRIME = 2147483647; // Un nombre premier (2^31 - 1)

	public MinHashDetector()
	{
		Random rand = new Random(42); // Graine fixe pour la reproductibilité
		a = new long[NUM_HASHES];
		b = new long[NUM_HASHES];

		for (int i = 0; i < NUM_HASHES; i++)
		{
			a[i] = 1 + rand.nextInt((int) LARGE_PRIME - 1);
			b[i] =     rand.nextInt((int) LARGE_PRIME);
		}
	}

	/**
	 * Génère une signature de NUM_HASHES valeurs pour un texte donné.
	 */
	public int[] getSignature(String text)
	{
		Set<String> shingles = createShingles(text, 2); // n-grams de 3 mots
		int[] signature = new int[NUM_HASHES];
		Arrays.fill(signature, Integer.MAX_VALUE);

		for (String shingle : shingles)
		{
			int shingleHash = shingle.hashCode();
			
			for (int i = 0; i < NUM_HASHES; i++)
			{
				// Application de la i-ème fonction de hachage : (a*x + b) % c
				int hash = (int) ((a[i] * shingleHash + b[i]) % LARGE_PRIME);
				hash = Math.abs(hash); // Garder des valeurs positives

				if (hash < signature[i])
					signature[i] = hash;
			}
		}

		return signature;
	}

	/**
	 * Découpe le texte en n-grams de mots (shingles).
	 */
	private Set<String> createShingles(String text, int n)
	{
		String[] words = text.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
		Set<String> shingles = new HashSet<>();

		for (int i = 0; i <= words.length - n; i++)
		{
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < n; j++)
				sb.append(words[i + j]).append(" ");

			shingles.add(sb.toString().trim());
		}

		return shingles;
	}

	/**
	 * Compare deux signatures de 500 valeurs.
	 */
	public double calculateSimilarity(int[] sig1, int[] sig2)
	{
		int count = 0;
		for (int i = 0; i < NUM_HASHES; i++)
		{
			if (sig1[i] == sig2[i])
				count++;
		}
		return (double) count / NUM_HASHES;
	}
	public static void main(String[] args) throws IOException
	{
		MinHashDetector detector = new MinHashDetector();

		String doc1 = new String(Files.readAllBytes(Paths.get("Germinal_Texte_entier.txt")));
		String doc2 = new String(Files.readAllBytes(Paths.get("La_France_contre_les_robots_Texte_entier.txt")));

		long startTime = System.nanoTime();
		int[] sig1 = detector.getSignature(doc1);
		int[] sig2 = detector.getSignature(doc2);
		long endTime = System.nanoTime();
		long signatureTime = (endTime - startTime) / 1_000_000;

		double similarity = detector.calculateSimilarity(sig1, sig2);

		System.out.println("--- Analyse MinHash (1000 fonctions) ---");
		System.out.printf ("Estimation de la similitude de Jaccard : %.2f%%\n", similarity * 100);
		System.out.printf ("Temps pour générer les signatures : %d ms\n", signatureTime);

		System.out.println();

		System.out.println("        0% - 10% : Textes totalement différents.");
		System.out.println("       10% - 30% : Textes partageant le même sujet (quelques expressions communes).");
		System.out.println("       30% - 60% : Plagiat par paraphrase importante.");
		System.out.println("Au-dessus de 60% : C'est une preuve de copier-coller massif.");
	}
}
