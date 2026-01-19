package app.ihm;

import app.Controleur;

import javax.swing.JFrame;

public class FramePrincipal extends JFrame
{
	private PanelPrincipal panelPrincipal;

	public FramePrincipal(Controleur ctrl)
	{
		this.setTitle("Plagiapprism");
		this.setSize(750, 400);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		this.panelPrincipal = new PanelPrincipal(ctrl);

		this.add(this.panelPrincipal);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}