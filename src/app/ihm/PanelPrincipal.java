package app.ihm;

import app.Controleur;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class PanelPrincipal extends JPanel implements ActionListener
{
	private Controleur controleur;

	private JButton   btnChargerFichiers;
	private JButton   btnAnalyser;
	private JLabel    lblResultat;

	private JTextPane zoneTexteOriginal;
	private JTextPane zoneTexteSuspecte;

	public PanelPrincipal(Controleur controleur)
	{
		this.controleur = controleur;

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panelHaut   = new JPanel();
		JPanel panelCentre = new JPanel();
		JPanel panelBas    = new JPanel();

		this.btnChargerFichiers = new JButton("Charger deux fichiers");
		this.btnAnalyser = new JButton("Analyser");
		this.lblResultat = new JLabel(" ");

		this.zoneTexteOriginal = new JTextPane();
		this.zoneTexteSuspecte = new JTextPane();

		panelHaut.setLayout  (new FlowLayout(FlowLayout.CENTER, 15, 5));
		panelCentre.setLayout(new GridLayout(1, 2, 10, 0));
		panelBas.setLayout   (new FlowLayout(FlowLayout.CENTER));

		this.btnChargerFichiers.setPreferredSize(new Dimension(200, 35));
		this.btnAnalyser.setPreferredSize(new Dimension(200, 35));
		this.btnChargerFichiers.setFont  (new Font("Arial", Font.BOLD, 13));
		this.btnAnalyser.setFont         (new Font("Arial", Font.BOLD, 13));
		this.lblResultat.setFont         (new Font("Arial", Font.ITALIC, 12));

		this.zoneTexteOriginal.setEditable(false);
		this.zoneTexteSuspecte.setEditable(false);
		this.zoneTexteOriginal.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.zoneTexteSuspecte.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane scrollGauche = new JScrollPane(this.zoneTexteOriginal);
		JScrollPane scrollDroite = new JScrollPane(this.zoneTexteSuspecte);

		scrollGauche.setBorder(BorderFactory.createTitledBorder("Texte original"));
		scrollDroite.setBorder(BorderFactory.createTitledBorder("Texte suspect"));

		panelHaut.add(this.btnChargerFichiers);
		panelHaut.add(this.btnAnalyser);

		panelCentre.add(scrollGauche);
		panelCentre.add(scrollDroite);

		panelBas.add(this.lblResultat);

		this.add(panelHaut, BorderLayout.NORTH);
		this.add(panelCentre, BorderLayout.CENTER);
		this.add(panelBas, BorderLayout.SOUTH);

		this.btnChargerFichiers.addActionListener(this);
		this.btnAnalyser.addActionListener(this);
	}

	public void mettreAJourAffichage()
	{
		this.zoneTexteOriginal.setText(this.controleur.getTexteOriginal());
		this.zoneTexteSuspecte.setText(this.controleur.getTexteSuspecte());
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnChargerFichiers)
		{
			String cheminFichier1 = this.selectionnerFichier("Premier fichier");
			if (cheminFichier1 == null) return;

			String cheminFichier2 = this.selectionnerFichier("Second fichier");
			if (cheminFichier2 == null) return;

			this.lblResultat.setText("  Chargement des fichiers en cours...");
			this.controleur.chargerFichiers(cheminFichier1, cheminFichier2);
			this.mettreAJourAffichage();
			this.lblResultat.setText("  Fichiers charges avec succes. Pret pour l'analyse.");
		}

		if (e.getSource() == this.btnAnalyser)
		{
			this.lblResultat.setText("  Analyse en cours...");
			this.controleur.analyser();
			
			int  nombreSequences = this.controleur.getNombreSequencesPlagiees();
			long tempsExecution  = this.controleur.getTempsExecution();

			if (nombreSequences == 0)
			{
				this.lblResultat.setText("  Aucun plagiat detecte - Analyse terminee en " + tempsExecution + " ms");
			}
			else
			{
				surlignerSequencesPlagiees();
				this.lblResultat.setText("  " + nombreSequences + " sequence(s) plagiee(s) detectee(s) - Analyse terminee en " + tempsExecution + " ms");
			}
		}
	}

	private void surlignerSequencesPlagiees()
	{
		StyledDocument document = this.zoneTexteSuspecte.getStyledDocument();

		Style styleNormal = this.zoneTexteSuspecte.addStyle("Normal", null);
		StyleConstants.setBackground(styleNormal, Color.WHITE);
		document.setCharacterAttributes(0, document.getLength(), styleNormal, true);

		Style stylePlagiat = this.zoneTexteSuspecte.addStyle("Plagiat", null);
		StyleConstants.setBackground(stylePlagiat, Color.YELLOW);
		StyleConstants.setBold(stylePlagiat, true);

		int[] positionsDebut = this.controleur.getPositionsDebut();
		int[] positionsFin   = this.controleur.getPositionsFin();

		for (int i = 0; i < positionsDebut.length; i++)
		{
			int longueurDocument = document.getLength();
			int positionDebut    = Math.min(positionsDebut[i], longueurDocument);
			int positionFin      = Math.min(positionsFin[i], longueurDocument);
			int longueur         = positionFin - positionDebut;

			if (longueur > 0)
			{
				document.setCharacterAttributes(positionDebut, longueur, stylePlagiat, false);
			}
		}
	}

	private String selectionnerFichier(String titre)
	{
		FileDialog dialogueFichier = new FileDialog((JFrame) null, titre, FileDialog.LOAD);
		dialogueFichier.setVisible(true);

		String nomFichier = dialogueFichier.getFile();
		String dossier    = dialogueFichier.getDirectory();

		if (nomFichier != null && dossier != null)
		{
			return new File(dossier, nomFichier).getAbsolutePath();
		}

		return null;
	}
}
