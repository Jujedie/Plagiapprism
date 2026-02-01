# Plagiapprism

Application Java de détection de plagiat entre deux textes, développée pour analyser la similarité textuelle avec précision.

---

## I. Description du Projet
Plagiapprism est un outil capable de détecter des suites de mots strictement identiques entre un texte original et un texte suspect. L'application traite les textes en amont pour ignorer les problèmes de casse et d'accents. Le résultat est présenté via une interface visuelle mettant en évidence les parties plagiées par surlignage.

---

## II. Architecture du Projet (MVC)
L'application repose sur une architecture **Modèle-Vue-Contrôleur** pour assurer une séparation nette entre les responsabilités :

* **Modèle** : Gère la logique métier, notamment l'extraction des mots et l'exécution de l'algorithme de détection.
* **Vue** : Interface graphique développée avec **Swing** et **AWT**, responsable de la saisie des textes et de l'affichage du surlignage.
* **Contrôleur** : Assure la coordination entre l'interface utilisateur et le moteur de calcul.

---

## III. Fonctionnement de l'Algorithme
L'analyse suit un processus structuré pour garantir rapidité et précision :

1.  **Initialisation** : Création des listes pour les mots et leurs positions respectives (original et suspect).
2.  **Extraction** : La méthode `extraire()` remplit ces listes en filtrant les mots d'au moins 3 caractères.
3.  **Indexation** : Création d'une `HashMap` avec les mots du texte original comme clés et leurs positions comme valeurs.
4.  **Détection** : Une boucle parcourt le texte suspect et compare les mots consécutifs dès qu'une correspondance est trouvée dans l'original.
5.  **Validation** : Une séquence est validée comme plagiat si elle atteint le nombre de mots minimal défini par l'utilisateur.

---

## IV. Profilage et Complexité
Conformément aux analyses théoriques et pratiques effectuées :

### Définitions des variables
* $n$ : Nombre total de mots dans le **texte original**.
* $m$ : Nombre total de mots dans le **texte suspecté**.
* $k$ : Longueur moyenne des séquences de plagiat détectées.

### Analyse de la complexité temporelle
* **Complexité théorique** : $O(n \times m \times k)$.
* **Pire des cas** : Si tout le texte suspecté correspond ($k = m$), la complexité peut atteindre $O(n \times m^2)$, voire $O(n^3)$ si $m \approx n$.
* **Complexité réelle** : En pratique, l'algorithme tend vers **$O(n \times m)$** car la variable $k$ est souvent bornée (100-200 mots) et l'utilisation d'un tableau `motsDejaUtilises` empêche de retraiter des séquences déjà identifiées.

---

## V. Choix Techniques
* **IDE** : Visual Studio Code.
* **Langage** : Java.
* **Bibliothèques** : Swing, AWT, Util.
