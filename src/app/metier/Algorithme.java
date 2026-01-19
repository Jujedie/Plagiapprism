package app.metier;

import java.util.HashSet;
import java.util.Set;

public class Algorithme
{
	private long dernierTempsExecution;

	private String texte1 = "";
	private String texte2 = "";

	public void setTextes(String t1, String t2)
	{
		this.texte1 = t1;
		this.texte2 = t2;
	}

	public double calculerSimilarite()
	{
		if (texte1.isEmpty() || texte2.isEmpty()) { return 0; }

		long debut = System.nanoTime();

		Set<String> mots1 = extraire(texte1);
		Set<String> mots2 = extraire(texte2);

		double score = calculerJaccard(mots1, mots2);

		long fin = System.nanoTime();
		this.dernierTempsExecution = (fin - debut) / 1_000_000;

		return score * 100;
	}

	public String getInterpretation(double similarite)
	{
		if (similarite < 10) return "Textes totalement différents.";
		if (similarite < 30) return "Sujet similaire (expressions communes).";
		if (similarite < 60) return "Plagiat par paraphrase importante.";

		return "Preuve de copier-coller massif.";
	}

	public long getTempsExecution() { return dernierTempsExecution; }

	private Set<String> extraire(String texte)
	{
		String texteNettoye = texte.toLowerCase().replaceAll("[^a-zàâäéèêëïîôùûü0-9\\s]", "");

		Set<String> mots = new HashSet<>();
		for (String m : texteNettoye.split("\\s+"))
		{
			if (!m.isEmpty()) mots.add(m);
		}

		return mots;
	}

	private double calculerJaccard(Set<String> s1, Set<String> s2)
	{
		if (s1.isEmpty() && s2.isEmpty()) { return 1.0; }

		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);

		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);

		return (double) intersection.size() / union.size();
	}
}