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
- **Détection de plagiat** : Identifie les passages potentiellement plagiés
- **Taux de similarité** : Calcule un pourcentage de similarité entre les textes
- **Analyse lexicale** : Comparaison mot à mot et phrase par phrase

### Interface utilisateur
- Interface graphique intuitive pour charger et comparer les textes
- Visualisation des résultats avec mise en évidence des passages similaires
- Export des rapports d'analyse

## Structure du projet

```
src/app/
├── Controleur.java                # Point d'entrée de l'application
├── ihm/                           # Interface utilisateur
│   ├── FramePrincipal.java        # Fenêtre principale
│   └── PanelPrincipal.java        # Panel principal
└── metier/                        # Logique métier
    └── Algorithme.java            # Algorithmes de détection de plagiat
```