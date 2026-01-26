# Plagiapprism

Application de détection de plagiat entre deux textes, développée en Java pour les maisons d'édition.

## Description

Plagiapprism est un outil d'analyse textuelle qui permet de détecter les similitudes et le plagiat potentiel entre deux documents textuels. Destiné aux maisons d'édition, cet outil aide à vérifier l'originalité des manuscrits et à identifier les passages similaires entre différents textes.

## Prérequis

- Java JDK 8 ou supérieur
- Un terminal (bash sous Linux/Mac, cmd/PowerShell sous Windows)

## Lancement de l'application

### Sous Linux/Mac

Utilisez le script de configuration fourni :

```bash
chmod +x setup.sh
./setup.sh
```

### Sous Windows

Utilisez le script batch fourni :

```batch
setup.bat
```

### Lancement manuel

Si vous préférez compiler et lancer manuellement :

1. Compilation :
```bash
javac -encoding UTF-8 -d bin src/app/*.java src/app/ihm/*.java src/app/metier/*.java
```

2. Exécution :
```bash
java -cp bin app.Controleur
```

## Fonctionnalités

L'application offre les fonctionnalités suivantes :

- **Comparaison de textes** : Compare deux documents textuels pour détecter les similitudes
- **Détection de plagiat** : Identifie les passages similaires (séquences de 3 mots ou plus)
- **Analyse lexicale avancée** : Extraction et comparaison de mots (3 caractères minimum)
- **Support complet de l'UTF-8** : Gestion correcte des caractères accentués et spéciaux
- **Mesure de performance** : Calcul et affichage du temps d'exécution

### Interface utilisateur
- Interface graphique intuitive pour charger et comparer les textes
- Visualisation des résultats avec mise en évidence des passages similaires
- Affichage du nombre de séquences détectées et du temps d'exécution

## Structure du projet

```
src/app/
├── Controleur.java                # Point d'entrée de l'application
├── data/                          # Fichiers textes d'exemple
│   ├── Germinal_Texte_entier.txt
│   ├── La_France_contre_les_robots_Texte_entier.txt
│   ├── Le_Rire._Essai_sur_la_signification_du_comique.txt
│   ├── Les_Pierres_de_Venise_Texte_entier.txt
│   └── Lettres_de_mon_moulin_Texte_entier.txt
├── ihm/                           # Interface utilisateur
│   ├── FramePrincipal.java        # Fenêtre principale
│   └── PanelPrincipal.java        # Panel principal
└── metier/                        # Logique métier
    ├── DetecteurPlagiat.java      # Algorithme de détection de plagiat
    └── LecteurFichier.java        # Lecture de fichiers en UTF-8
```

## Algorithme de détection

### Principe
L'algorithme utilise une approche par **indexation de mots** pour détecter les séquences communes :

1. **Extraction** : Les mots (3+ caractères) sont extraits et normalisés (minuscules)
2. **Indexation** : Les positions des mots du texte original sont stockées dans une HashMap
3. **Recherche** : Pour chaque mot du texte suspect, on cherche les correspondances dans l'original
4. **Extension** : Les séquences de mots identiques sont étendues autant que possible
5. **Seuil** : Seules les séquences de 3 mots ou plus sont considérées comme du plagiat

### Complexité algorithmique
- **Pire cas** : O(n × m × k)
  - n = nombre de mots dans le texte suspect
  - m = nombre d'occurrences d'un mot dans le texte original
  - k = longueur moyenne des séquences correspondantes
- **Cas moyen** : O(n × m) grâce à l'indexation par HashMap et au marquage des mots déjà traités

### Encodage
Tous les fichiers sont lus en **UTF-8** (via `StandardCharsets.UTF_8`), garantissant la prise en charge correcte des caractères accentués français.