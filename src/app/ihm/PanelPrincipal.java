package app.ihm;

import app.Controleur;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

public class PanelPrincipal extends JPanel implements ActionListener
{
	private Controleur ctrl;

	private JButton btnImporter;
	private JButton btnAnalyser;

	private JTextArea txtAreaGauche;
	private JTextArea txtAreaDroite;

	public PanelPrincipal(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		this.btnImporter = new JButton("Importer deux fichiers");
		this.btnAnalyser = new JButton("Analyser");

		// Création et configuration du JPanel texte
		JPanel panelTexte = new JPanel();
		panelTexte.setLayout(new GridLayout(1, 2));

		// Création des zones de textes
		this.txtAreaGauche = new JTextArea(this.ctrl.getTexteGauche());
		this.txtAreaDroite = new JTextArea(this.ctrl.getTexteDroite());


		// Configuration des zones de textes
		this.txtAreaGauche.setColumns(40);
		this.txtAreaGauche.setLineWrap(true);
		this.txtAreaGauche.setEditable(false);
		this.txtAreaDroite.setColumns(40);
		this.txtAreaDroite.setLineWrap(true);
		this.txtAreaDroite.setEditable(false);

		// Création des JScrollPane
		JScrollPane scrollPaneGauche = new JScrollPane(this.txtAreaGauche);
		JScrollPane scrollPaneDroite = new JScrollPane(this.txtAreaDroite);


		panelTexte.add(scrollPaneGauche);
		panelTexte.add(scrollPaneDroite);

		this.add(this.btnImporter, BorderLayout.NORTH );
		this.add(panelTexte      , BorderLayout.CENTER);
		this.add(this.btnAnalyser, BorderLayout.SOUTH );

		this.btnImporter.addActionListener(this);
		this.btnAnalyser.addActionListener(this);
	}

	public void majIHM()
	{
		this.txtAreaGauche.setText(limiterTexte(this.ctrl.getTexteGauche()));
		this.txtAreaDroite.setText(limiterTexte(this.ctrl.getTexteDroite()));
	}

	private String limiterTexte(String texte)
	{
		final int MAX_CHARS = 500000; // 500 000 caractères maximum
		
		if (texte == null || texte.length() <= MAX_CHARS)
		{
			return texte;
		}
		
		return texte.substring(0, MAX_CHARS) + "\n\n[... Texte tronqué - " + (texte.length() - MAX_CHARS) + " caractères omis pour des raisons de performance ...]";
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnImporter)
		{
			String p1 = this.selectionnerFichier("Premier fichier");
			if (p1 == null) { return; }

			String p2 = this.selectionnerFichier("Second fichier");
			if (p2 == null) { return; }

			this.ctrl.setTextes(p1, p2);
			this.majIHM();
			JOptionPane.showMessageDialog(this, "Fichiers importés avec succès !", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
		}

		if (e.getSource() == this.btnAnalyser)
		{
			double similarite = this.ctrl.getSimilarite();
			JOptionPane.showMessageDialog(this, String.format("Similarité : %.2f%%", similarite) + " - " + this.ctrl.getInterpretation(similarite), "Résultat de l'analyse", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private String selectionnerFichier(String titre)
	{
		FileDialog fd = new FileDialog((JFrame) null, titre, FileDialog.LOAD);

		fd.setVisible(true);

		// Si l’utilisateur a sélectionné un fichier
		String file = fd.getFile();
		String dir = fd.getDirectory();

		if (file != null && dir != null)
		{
			return new File(dir, file).getAbsolutePath();
		}

		return null;
	}
}