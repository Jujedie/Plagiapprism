package app.ihm;

import app.Controleur;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelPrincipal extends JPanel implements ActionListener
{
	private Controleur ctrl;

	private JButton btnImporter;
	private JButton btnAnalyser;

	public PanelPrincipal(Controleur ctrl)
	{
		this.ctrl = ctrl;

		this.setLayout(new BorderLayout());

		this.btnImporter = new JButton("Importer deux fichiers");
		this.btnAnalyser = new JButton("Analyser");

		this.add(btnImporter, BorderLayout.NORTH);
		this.add(btnAnalyser, BorderLayout.SOUTH);

		this.btnImporter.addActionListener(this);
		this.btnAnalyser.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnImporter)
		{
			String p1 = selectionnerFichier("Premier fichier");
			if (p1 == null) { return; }

			String p2 = selectionnerFichier("Second fichier");
			if (p2 == null) { return; }

			this.ctrl.setTextes(p1, p2);
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
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(titre);

		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().getAbsolutePath();
		}

		return null;
	}
}