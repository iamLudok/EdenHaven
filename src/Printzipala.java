package proiektua;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class Printzipala implements PropertyChangeListener
{
	Modeloa modeloa;
	Kontrolatzailea kontrolatzailea;
	
	JFrame loginFrame;
	JTextField nombreField;
	JPasswordField contrasenaField;
	JPanel loginPanel;
	
	JFrame mainFrame;
    JButton button;
    JButton button2;
    JButton button3;
    
    String opcion;
    JFrame opcionesFrame;
    JButton originalButton;
    


	
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea)
	{
		originalButton = new JButton();
		this.modeloa = modeloa;
		this.kontrolatzailea = kontrolatzailea;
		this.modeloa.addPropertyChangeListener(this);
	}
	
	void mostrarVentanaLogin() 
	{
		SwingUtilities.invokeLater(() -> 
		{
            loginFrame = new JFrame("Inicio de Sesión");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loginFrame.setUndecorated(true);
            
            kontrolatzailea.setIrudia(this);
            
            loginPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);

            loginFrame.setLayout(new BorderLayout());
            loginFrame.add(loginPanel, BorderLayout.CENTER);

            //Izena eta pasahitza
            nombreField = new JTextField(20);
            contrasenaField = new JPasswordField(20);

            loginPanel.add(new JLabel("Nombre:"), gbc);

            gbc.gridy++;
            loginPanel.add(nombreField, gbc);

            gbc.gridy++;
            loginPanel.add(new JLabel("Contraseña:"), gbc);

            gbc.gridy++;
            loginPanel.add(contrasenaField, gbc);

            JButton loginButton = new JButton("Iniciar Sesión");
            loginButton.setActionCommand("SAIOAHASI");
            loginButton.addActionListener(kontrolatzailea);
            
            gbc.gridy++;
            gbc.gridwidth = 2;
            loginPanel.add(loginButton, gbc);
            loginFrame.getContentPane().add(loginPanel);
            loginFrame.setVisible(true);
		}
        );
	}
	
	//////////////////////////////////////////////////////////
	void mostrarPantallaAdmin() 
	{
        JFrame adminFrame = new JFrame("Pantalla de Admin");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        adminFrame.setUndecorated(true);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(adminFrame.getWidth(), adminFrame.getHeight()));

        try 
        {      
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            ImageIcon worldMapIcon2 = new ImageIcon("ikonoak/mapa.jpg");
            JLabel worldMapLabel2 = new JLabel(worldMapIcon2);
            worldMapLabel2.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
            layeredPane.add(worldMapLabel2, JLayeredPane.DEFAULT_LAYER);
            
            //Argazkien tamaina
            int hostelWidth = 50;
            int hostelHeight = 50;

            ImageIcon africaIcon = new ImageIcon("ikonoak/babesetxe.png");
            africaIcon = new ImageIcon(africaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel africaLabel = new JLabel(africaIcon);
            africaLabel.setBounds(300, 200, hostelWidth, hostelHeight);
            layeredPane.add(africaLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon swedenIcon = new ImageIcon("ikonoak/babesetxe.png");
            swedenIcon = new ImageIcon(swedenIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel swedenLabel = new JLabel(swedenIcon);
            swedenLabel.setBounds(500, 750, hostelWidth, hostelHeight);
            layeredPane.add(swedenLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon chinaIcon = new ImageIcon("ikonoak/babesetxe.png");
            chinaIcon = new ImageIcon(chinaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel chinaLabel = new JLabel(chinaIcon);
            chinaLabel.setBounds(1650, 750, hostelWidth, hostelHeight);
            layeredPane.add(chinaLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon oceaniaIcon = new ImageIcon("ikonoak/babesetxe.png");
            oceaniaIcon = new ImageIcon(oceaniaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel oceaniaLabel = new JLabel(oceaniaIcon);
            oceaniaLabel.setBounds(900, 400, hostelWidth, hostelHeight);
            layeredPane.add(oceaniaLabel, JLayeredPane.PALETTE_LAYER);

            //Arratoia konfiguratu
            layeredPane.addMouseListener(new AdminClickListener(adminFrame, africaLabel, swedenLabel, chinaLabel, oceaniaLabel));

        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen del mapa o albergues: " + ex.getMessage());
        }

        adminFrame.getContentPane().add(layeredPane);
        adminFrame.pack();
        adminFrame.setVisible(true);
    }
    
    class AdminClickListener extends MouseAdapter 
    {
        private JFrame adminFrame;
        private JLabel africaLabel;
        private JLabel swedenLabel;
        private JLabel chinaLabel;
        private JLabel oceaniaLabel;

        public AdminClickListener(JFrame adminFrame, JLabel africaLabel, JLabel swedenLabel, JLabel chinaLabel, JLabel oceaniaLabel) 
        {
            this.adminFrame = adminFrame;
            this.africaLabel = africaLabel;
            this.swedenLabel = swedenLabel;
            this.chinaLabel = chinaLabel;
            this.oceaniaLabel = oceaniaLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) 
        {
            if (africaLabel.getBounds().contains(e.getPoint()) ||
                    swedenLabel.getBounds().contains(e.getPoint()) ||
                    chinaLabel.getBounds().contains(e.getPoint()) ||
                    oceaniaLabel.getBounds().contains(e.getPoint())) 
            {
                adminFrame.dispose();
                mostrarPantallaHuecos();
            }
        }
    }
    //////////////////////////////////////////////////////////
    
	void mostrarPantallaHuecos() 
	{
        JFrame frame = new JFrame("Programa Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));


        	button = new JButton("Hueco " + 1);
        	button.setActionCommand("HUECO");
        	button.addActionListener(kontrolatzailea);
            panel.add(button);
        
        	button2 = new JButton("Hueco " + 2);
        	button2.setActionCommand("HUECO2");
        	button2.addActionListener(kontrolatzailea);
            panel.add(button2);
            
            button3 = new JButton("Hueco " + 3);
            button3.setActionCommand("HUECO3");
            button3.addActionListener(kontrolatzailea);
            panel.add(button3);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
	
	void mostrarOpciones()
	{
		opcionesFrame = new JFrame("Opciones");
        opcionesFrame.setSize(300, 150);
        opcionesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(4, 1));

        JButton pozoButton = new JButton("POZO");
        pozoButton.setActionCommand("POZO");
        pozoButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(pozoButton);

        JButton ventiladorButton = new JButton("VENTILADOR");
        ventiladorButton.setActionCommand("VENTILADOR");
        ventiladorButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(ventiladorButton);

        JButton fugaButton = new JButton("ESTUFA");
        fugaButton.setActionCommand("ESTUFA");
        fugaButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(fugaButton);

        opcionesFrame.getContentPane().add(opcionesPanel);

        opcionesFrame.setLocationRelativeTo(mainFrame);
        opcionesFrame.setVisible(true);
	}
	
	private void argazkiaSartu(String argazkia, int lekua) {
		//String temp = argakia + ""
		switch(lekua) {
		case 1:
			button.setIcon(new ImageIcon("ikonoak/"+argazkia+".png"));
			button.setText("<html>POZO<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setVerticalTextPosition(SwingConstants.BOTTOM);
	        
	        opcionesFrame.dispose();
			break;
		case 2:
			button2.setIcon(new ImageIcon("ikonoak/"+argazkia+".png"));
			button2.setText("<html>POZO<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
			button2.setHorizontalTextPosition(SwingConstants.CENTER);
			button2.setVerticalTextPosition(SwingConstants.BOTTOM);
	        
	        opcionesFrame.dispose();
			break;
		case 3:
			button3.setIcon(new ImageIcon("ikonoak/"+argazkia+".png"));
			button3.setText("<html>POZO<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
			button3.setHorizontalTextPosition(SwingConstants.CENTER);
			button3.setVerticalTextPosition(SwingConstants.BOTTOM);
	        
	        opcionesFrame.dispose();
			break;
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String propietatea = e.getPropertyName();
		int lekua = (int) e.getNewValue();
		System.out.println("Kaixo3");
	
		switch (propietatea) {
		case Modeloa.PROPIETATEA:

			argazkiaSartu(Modeloa.PROPIETATEA, lekua);
			
			break;
		case Modeloa.PROPIETATEA2:

			argazkiaSartu(Modeloa.PROPIETATEA2, lekua);
			
			break;
		case Modeloa.PROPIETATEA3:
			
			argazkiaSartu(Modeloa.PROPIETATEA3, lekua);
			
			break;
		}
	}
	
	public static void main(String[] args) 
	{
		Modeloa modeloa = new Modeloa ();
		Kontrolatzailea kontrolatzailea = new Kontrolatzailea(modeloa);
		Printzipala main = new Printzipala(modeloa,kontrolatzailea);
		main.mostrarVentanaLogin();
	}


}
