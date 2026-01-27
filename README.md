# Plagiapprism

Application Java de détection de plagiat entre deux textes, développée pour les maisons d'édition.

## Description

Plagiapprism analyse et compare deux documents textuels pour détecter les passages similaires. L'outil identifie automatiquement les séquences de mots identiques et les met en évidence dans une interface graphique intuitive.

## Prérequis

- Java JDK 25 ou supérieur

## Lancement de l'application

### Scripts automatiques

**Linux/Mac :**
```bash
chmod +x setup.sh
./setup.sh
```

**Windows :**
```batch
setup.bat
```

## Structure du projet

```
src/app/
├── Controleur.java              # Point d'entrée et contrôleur MVC
├── data/                        # Fichiers textes d'exemple
├── ihm/                         # Interface utilisateur (Swing)
│   ├── FramePrincipal.java
│   └── PanelPrincipal.java
└── metier/                      # Logique métier
    ├── DetecteurPlagiat.java    # Algorithme de détection
    └── LecteurFichier.java      # Lecture UTF-8
```

## Algorithme de détection

### Principe

L'algorithme utilise une approche par **indexation** pour optimiser la détection :

1. **Nettoyage** : Normalisation du texte (minuscules + suppression accents)
2. **Extraction** : Extraction des mots de 3+ caractères avec leurs positions (regex `\b[\p{L}\d]{3,}\b`)
3. **Indexation** : Création d'une HashMap associant chaque mot à ses positions dans le texte original
4. **Recherche** : Pour chaque mot du texte suspect, recherche dans la HashMap et extension des séquences
5. **Filtrage** : Conservation uniquement des séquences ≥ seuil configuré
6. **Marquage** : Évite les doublons en marquant les mots déjà traités

### Complexité

- **Temps** : O(n × m × k) dans le cas moyen
  - n = nombre de mots du texte suspect
  - m = occurrences moyennes d'un mot dans l'original
  - k = longueur moyenne des séquences
- **Espace** : O(n) pour la HashMap d'indexation