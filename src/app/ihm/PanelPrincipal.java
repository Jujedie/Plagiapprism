package app.ihm;

import app.Controleur;

import java.io.File;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class PanelPrincipal extends JPanel implements ActionListener
{
	private Controleur ctrl;

	private JButton   btnChargerFichiers;
	private JButton   btnAnalyser;
	private JLabel    lblResultat;

	private JTextPane txtOriginal;
	private JTextPane txtSuspect;

	public PanelPrincipal(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel panelHaut   = new JPanel();
		JPanel panelCentre = new JPanel();
		JPanel panelBas    = new JPanel();

		this.btnChargerFichiers = new JButton("Charger deux fichiers");
		this.btnAnalyser = new JButton("Analyser");
		this.lblResultat = new JLabel(" ");

		this.txtOriginal = new JTextPane();
		this.txtSuspect = new JTextPane();

		panelHaut.setLayout  (new FlowLayout(FlowLayout.CENTER, 15, 5));
		panelCentre.setLayout(new GridLayout(1, 2, 10, 0));
		panelBas.setLayout   (new FlowLayout(FlowLayout.CENTER));

		this.btnChargerFichiers.setPreferredSize(new Dimension(200, 35));
		this.btnAnalyser.setPreferredSize(new Dimension(200, 35));
		this.btnChargerFichiers.setFont  (new Font("Arial", Font.BOLD, 13));
		this.btnAnalyser.setFont         (new Font("Arial", Font.BOLD, 13));
		this.lblResultat.setFont         (new Font("Arial", Font.ITALIC, 12));

		this.txtOriginal.setEditable(false);
		this.txtSuspect.setEditable(false);
		this.txtOriginal.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.txtSuspect.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane scrollGauche = new JScrollPane(this.txtOriginal);
		JScrollPane scrollDroite = new JScrollPane(this.txtSuspect);

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
		this.txtOriginal.setText(this.ctrl.getTexteOriginal());
		this.txtSuspect.setText(this.ctrl.getTexteSuspecte());
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
			this.ctrl.chargerFichiers(cheminFichier1, cheminFichier2);
			this.mettreAJourAffichage();
			this.lblResultat.setText("  Fichiers charges avec succes. Pret pour l'analyse.");
		}

		if (e.getSource() == this.btnAnalyser)
		{
			this.lblResultat.setText("  Analyse en cours...");
			this.ctrl.analyser();
			
			int  nombreSequences = this.ctrl.getNombreSequencesPlagiees();
			long tempsExecution  = this.ctrl.getTempsExecution();

			if (nombreSequences == 0)
			{
				this.lblResultat.setText("  Aucun plagiat detecte - Analyse terminee en " + tempsExecution + " ms");
			}
			else
			{
				this.surlignerSequencesPlagiees();
				this.lblResultat.setText("  " + nombreSequences + " sequence(s) plagiee(s) detectee(s) - Analyse terminee en " + tempsExecution + " ms");
			}
		}
	}

	private void surlignerSequencesPlagiees()
	{
		StyledDocument document = this.txtSuspect.getStyledDocument();

		Style styleNormal = this.txtSuspect.addStyle("Normal", null);
		StyleConstants.setBackground(styleNormal, Color.WHITE);
		document.setCharacterAttributes(0, document.getLength(), styleNormal, true);

		Style stylePlagiat = this.txtSuspect.addStyle("Plagiat", null);
		StyleConstants.setBackground(stylePlagiat, Color.YELLOW);
		StyleConstants.setBold(stylePlagiat, true);

		int[] positionsDebut = this.ctrl.getPositionsDebut();
		int[] positionsFin   = this.ctrl.getPositionsFin();

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