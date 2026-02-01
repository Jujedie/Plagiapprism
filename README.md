# Plagiapprism

[cite_start]Application Java de détection de plagiat entre deux textes, développée pour analyser la similarité textuelle avec précision[cite: 9, 22].

---

## I. Description du Projet
[cite_start]Plagiapprism est un outil capable de détecter des suites de mots strictement identiques entre un texte original et un texte suspect[cite: 24]. [cite_start]L'application traite les textes en amont pour ignorer les problèmes de casse et d'accents[cite: 24]. [cite_start]Le résultat est présenté via une interface visuelle mettant en évidence les parties plagiées par surlignage[cite: 27].

---

## II. Architecture du Projet (MVC)
[cite_start]L'application repose sur une architecture **Modèle-Vue-Contrôleur** pour assurer une séparation nette entre les responsabilités[cite: 131]:

* [cite_start]**Modèle** : Gère la logique métier, notamment l'extraction des mots et l'exécution de l'algorithme de détection[cite: 131].
* [cite_start]**Vue** : Interface graphique développée avec **Swing** et **AWT**, responsable de la saisie des textes et de l'affichage du surlignage[cite: 52, 131].
* [cite_start]**Contrôleur** : Assure la coordination entre l'interface utilisateur et le moteur de calcul[cite: 131].

---

## III. Fonctionnement de l'Algorithme
L'analyse suit un processus structuré pour garantir rapidité et précision :

1.  [cite_start]**Initialisation** : Création des listes pour les mots et leurs positions respectives (original et suspect)[cite: 82].
2.  [cite_start]**Extraction** : La méthode `extraire()` remplit ces listes en filtrant les mots d'au moins 3 caractères[cite: 83].
3.  [cite_start]**Indexation** : Création d'une `HashMap` avec les mots du texte original comme clés et leurs positions comme valeurs[cite: 85].
4.  [cite_start]**Détection** : Une boucle parcourt le texte suspect et compare les mots consécutifs dès qu'une correspondance est trouvée dans l'original[cite: 89, 90].
5.  [cite_start]**Validation** : Une séquence est validée comme plagiat si elle atteint le nombre de mots minimal défini par l'utilisateur[cite: 91].

---

## IV. Profilage et Complexité
[cite_start]Conformément aux analyses théoriques et pratiques effectuées[cite: 96]:

### Définitions des variables
* [cite_start]$n$ : Nombre total de mots dans le **texte original**[cite: 98].
* [cite_start]$m$ : Nombre total de mots dans le **texte suspecté**[cite: 99].
* [cite_start]$k$ : Longueur moyenne des séquences de plagiat détectées[cite: 100].

### Analyse de la complexité temporelle
* [cite_start]**Complexité théorique** : $O(n \times m \times k)$[cite: 97].
* [cite_start]**Pire des cas** : Si tout le texte suspecté correspond ($k = m$), la complexité peut atteindre $O(n \times m^2)$, voire $O(n^3)$ si $m \approx n$.
* [cite_start]**Complexité réelle** : En pratique, l'algorithme tend vers **$O(n \times m)$** car la variable $k$ est souvent bornée (100-200 mots) et l'utilisation d'un tableau `motsDejaUtilises` empêche de retraiter des séquences déjà identifiées[cite: 121, 122, 123].

---

## V. Choix Techniques
* [cite_start]**IDE** : Visual Studio Code[cite: 50].
* [cite_start]**Langage** : Java[cite: 51].
* [cite_start]**Bibliothèques** : Swing, AWT, Util[cite: 52, 53].
