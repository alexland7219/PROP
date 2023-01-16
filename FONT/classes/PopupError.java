package classes;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;
/*
 * Created by JFormDesigner on Thu Dec 15 17:15:59 CET 2022
 */



/** Classe que conté la interfície gràfica dels popups d'error.
 * @author Bernat Borràs
 */
public class PopupError extends JFrame {

	/**
	 * Constructora per defecte.
	 */
	public PopupError() {
		this.setTitle("S'ha produit un error!");
		initComponents();
	}

	/**
	 * Amagar el popup al pulsar el botó "D'acord".
	 * @param e MouseEvent.
	 */
	private void okButtonMouseReleased(MouseEvent e) {
		this.setVisible(false);
	}

	/**
	 * Assigna el missatge d'error al label corresponent.
	 */
	public void SetMissatge(String missatge) {
		label1.setText("<html><p style=\"width:600px\">" + missatge + "</p></html>");
	}

	/**
	 * Inicialitza els components de l'interfície.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Evaluation license - Bernat Borràs
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
			( 0, 0, 0, 0) , "", javax. swing. border. TitledBorder. CENTER, javax. swing. border
			. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
			. Color. red) ,dialogPane. getBorder( )) ); dialogPane. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
			propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( )
			; }} );
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new MigLayout(
					"insets dialog,hidemode 3",
					// columns
					"[fill]" +
					"[fill]",
					// rows
					"[]" +
					"[]" +
					"[]"));

				//---- label1 ----
				label1.setText("S'ha produit un error al importar el fitxer: file.txt");
				contentPanel.add(label1, "cell 0 1");
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setLayout(new MigLayout(
					"insets dialog,alignx right",
					// columns
					"[button,fill]",
					// rows
					null));

				//---- okButton ----
				okButton.setText("D'acord");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						okButtonMouseReleased(e);
					}
				});
				buttonBar.add(okButton, "cell 0 0");
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Evaluation license - Bernat Borràs
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}


