package Bista;

import Modeloa.Erabiltzailea;
import Modeloa.Modeloa;
import irakurlea.Mqtt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import javax.swing.Timer;

import org.eclipse.paho.client.mqttv3.MqttException;

import Kontrolatzailea.Kontrolatzailea;
import Modeloa.Modeloa;

public class Printzipala implements PropertyChangeListener
{
	Modeloa modeloa; //MODELOA
	Kontrolatzailea kontrolatzailea; //KONTROLATZAILEA
	
	//PANTAILAK
	JFrame ordenagailuarenPANTAILA; //REBISATU
	JFrame loginPANTAILA;	
	public JFrame mainPANTAILA;
	JFrame aukerenPANTAILA;
	
	//JFIELD
	public JTextField izenaField;
	JPasswordField pasahitzaField;
	
	//BOTOIAK
    JButton button; //EZKERREKO BOTOIA
    JButton button2; //ERDIKO BOTOIA
    JButton button3; //ESKUINEKO BOTOIA
    JButton backButton; //ATZERA JOATEKO BOTOIA
    JButton editButton;
    
    //COLLECTIONS
    private Map<String, Erabiltzailea> erabiltzaileak; //ERABILTZAILEEN MAPA
     
   
    //INT
    //int behin=0;
    Mqtt mqtt;
    public static final String BROKER = "tcp://localhost:1883";
    public static final String CLENT_ID = "TemperatureIrakurle";
    public static final int QoS = 2;
           	
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea, Mqtt mqtt) //PRINTZIPALAREN KONSTRUKTOREA
	{
		this.modeloa = modeloa; //MODELOA
		this.kontrolatzailea = kontrolatzailea; //KONTROLATZAILEA
		this.mqtt = mqtt;
		this.modeloa.addPropertyChangeListener(this); //PROPERTY CHANGE
		
		erabiltzaileak = new HashMap<>(); //ERABILTZAILEEN HASH MAPA
		
		erabiltzaileakIrakurri(); //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
		
		modeloa.start();
	}
	
	private void erabiltzaileakIrakurri() //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("erabiltzaileak.txt"))) 
	    {
	        String linea;
	        while ((linea = br.readLine()) != null) 
	        {
	            String[] partes = linea.split(":");
	            if (partes.length >= 2) 
	            {
	                String nombre = partes[0].trim();
	                String contrasena = partes[1].trim();

	                Erabiltzailea erabiltzailea = new Erabiltzailea();
	                erabiltzailea.setPasahitza(contrasena);
	                erabiltzaileak.put(nombre, erabiltzailea);
	            }
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private void actualizarPantalla() 
	{
		if (mqtt != null) 
		{
			button.setText(String.valueOf(mqtt.valueTemperature)/*argazkia.toUpperCase()*/);
	        button2.setText(String.valueOf(mqtt.valueTemperature)/*argazkia.toUpperCase()*/);
	        button3.setText(String.valueOf(mqtt.valueTemperature)/*argazkia.toUpperCase()*/);
		} else 
		{
		    System.out.println("MQTT es null");
		}		
    }

	
	public Erabiltzailea getErabiltzailea(String nombre) 
	{
	    return erabiltzaileak.get(nombre);
	}
	
	private boolean isUsuarioRegistrado(String nombre) 
	{
	    return erabiltzaileak.containsKey(nombre);
	}
	
	private boolean isUsuarioContraseña(String nombre, String contrasena) 
	{
	    Erabiltzailea erabiltzailea = getErabiltzailea(nombre);

	    if (erabiltzailea != null) 
	    {
	        return contrasena.equals(erabiltzailea.getPasahitza());
	    }

	    return false;
	}
	
	public void loginPantailaErakutzi() 
	{
		SwingUtilities.invokeLater(() -> 
		{
            loginPANTAILA = new JFrame("SAIOA HASI");
            loginPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loginPANTAILA.setUndecorated(true);
            
            kontrolatzailea.setIrudia(this);
            
            JPanel loginPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);

            loginPANTAILA.setLayout(new BorderLayout());
            loginPANTAILA.add(loginPanel, BorderLayout.CENTER);

            //Izena eta pasahitza
            izenaField = new JTextField(20);
            pasahitzaField = new JPasswordField(20);

            loginPanel.add(new JLabel("Izena:"), gbc);

            gbc.gridy++;
            loginPanel.add(izenaField, gbc);

            gbc.gridy++;
            loginPanel.add(new JLabel("Pasahitza:"), gbc);

            gbc.gridy++;
            loginPanel.add(pasahitzaField, gbc);

            JButton loginButton = new JButton("HASI SAIOA");
            loginButton.setActionCommand("SAIOAHASI");
            loginButton.addActionListener(kontrolatzailea);
            
            gbc.gridy++;
            gbc.gridwidth = 2;
            loginPanel.add(loginButton, gbc);
            loginPANTAILA.getContentPane().add(loginPanel);
            loginPANTAILA.setVisible(true);
		});
	}
	
	/*
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
    */
	/*
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
    }*/
        
	public void mostrarPantallaHuecos()
	{
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));

	    button = new JButton("Hutsunea " + 1);
	    button.setActionCommand("HUECO");
	    button.addActionListener(kontrolatzailea);
	    botonesHuecosPanel.add(button);

	    button2 = new JButton("Hueco " + 2);
	    button2.setActionCommand("HUECO2");
	    button2.addActionListener(kontrolatzailea);
	    botonesHuecosPanel.add(button2);

	    button3 = new JButton("Hueco " + 3);
	    button3.setActionCommand("HUECO3");
	    button3.addActionListener(kontrolatzailea);
	    botonesHuecosPanel.add(button3);

	    JPanel botonAtrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	    backButton = new JButton("ATZERA");
	    backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(80, 30)); 
	    botonAtrasPanel.add(backButton);

	    editButton = new JButton("EDITATU");
	    editButton.setActionCommand("EDITATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);
	    
	    editButton = new JButton("KONFIRMATU");
	    editButton.setActionCommand("KONFIRMATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);

	    panel.add(botonAtrasPanel, BorderLayout.NORTH);
	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);

	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarPantallaEditarHuecos(Erabiltzailea erabiltzailea)
	{
		int i = 0;
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));
	    
	    for (i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        JButton button = new JButton("Hutsunea " + (i + 1));
	        button.addActionListener(kontrolatzailea);
	        botonesHuecosPanel.add(button);

	        String balio = erabiltzailea.getBalioZerrenda().get(i).toLowerCase();
	        irudiaJarri(balio, button);
	    }
	    if(i==1) 
	    {
	    	button2 = new JButton("Hueco " + 2);
		    button2.setActionCommand("HUECO2");
		    button2.addActionListener(kontrolatzailea);
		    botonesHuecosPanel.add(button2);
	    }
	    if(i<=2)
	    {
	    	button3 = new JButton("Hueco " + 3);
		    button3.setActionCommand("HUECO3");
		    button3.addActionListener(kontrolatzailea);
		    botonesHuecosPanel.add(button3);
	    }
	    
	    JPanel botonAtrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	    backButton = new JButton("ATZERA");
	    backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(80, 30)); 
	    botonAtrasPanel.add(backButton);

	    editButton = new JButton("EDITATU");
	    editButton.setActionCommand("EDITATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);
	    
	    editButton = new JButton("KONFIRMATU");
	    editButton.setActionCommand("KONFIRMATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);

	    panel.add(botonAtrasPanel, BorderLayout.NORTH);
	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);

	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarOpciones()
	{
		aukerenPANTAILA = new JFrame("AUKERAK");
        aukerenPANTAILA.setSize(300, 150);
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(4, 1));

        JButton pozoButton = new JButton("PUTZUA");
        pozoButton.setActionCommand("POZO");
        pozoButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(pozoButton);

        JButton ventiladorButton = new JButton("HAIZEGAILUA");
        ventiladorButton.setActionCommand("VENTILADOR");
        ventiladorButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(ventiladorButton);

        JButton fugaButton = new JButton("BEROGAILUA");
        fugaButton.setActionCommand("ESTUFA");
        fugaButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(fugaButton);

        aukerenPANTAILA.getContentPane().add(opcionesPanel);

        aukerenPANTAILA.setLocationRelativeTo(ordenagailuarenPANTAILA);
        aukerenPANTAILA.setVisible(true);
	}
	
	private void argazkiaSartu(String argazkia, int lekua) 
	{
	    String nombre = izenaField.getText();
	    Erabiltzailea usuario = getErabiltzailea(nombre);

	    if (usuario != null) 
	    {
	        switch (lekua) 
	        {
	            case 1:	      
	                irudiaJarri(argazkia, button);
	                usuario.setPosizioZerrendaAtIndex(usuario.getPosizioZerrenda().size(), 1);
	                System.out.println(nombre + " posizioa: " + usuario.getPosizioZerrenda());
	                break;

	            case 2:
	                irudiaJarri(argazkia, button2);
	                usuario.setPosizioZerrendaAtIndex(usuario.getPosizioZerrenda().size(), 2);
	                System.out.println(nombre + " posizioa: " + usuario.getPosizioZerrenda());
	                break;

	            case 3:
	                irudiaJarri(argazkia, button3);
	                usuario.setPosizioZerrendaAtIndex(usuario.getPosizioZerrenda().size(), 3);
	                System.out.println(nombre + " posizioa: " + usuario.getPosizioZerrenda());
	                break;
	        }
	    }
	}
	
	void irudiaJarri(String argazkia, JButton botoia)
	{
		int hostelWidth = 200;
        int hostelHeight = 200;

        ImageIcon icon = new ImageIcon("ikonoak/" + argazkia + ".png");
        Image scaledImage = icon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH);

        botoia.setIcon(new ImageIcon(scaledImage));
        botoia.setText(String.valueOf(mqtt.valueTemperature)/*argazkia.toUpperCase()*/);
		botoia.setHorizontalTextPosition(SwingConstants.CENTER);
		botoia.setVerticalTextPosition(SwingConstants.BOTTOM);

        aukerenPANTAILA.dispose();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) 
	{
	    String propiedad = e.getPropertyName();
	    int lekua = (int) e.getNewValue();

	    String nombre = izenaField.getText();

	    Erabiltzailea erabiltzailea = getErabiltzailea(nombre);

	    if (erabiltzailea != null) 
	    {
	        switch (propiedad) 
	        {
	            case Modeloa.PROPIETATEA:
	                argazkiaSartu(Modeloa.PROPIETATEA, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(erabiltzailea.getBalioZerrenda().size(), "pozo");
	                System.out.println(nombre + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;

	            case Modeloa.PROPIETATEA2:
	                argazkiaSartu(Modeloa.PROPIETATEA2, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(erabiltzailea.getBalioZerrenda().size(), "ventilador");
	                System.out.println(nombre + " balioa " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;

	            case Modeloa.PROPIETATEA3:
	                argazkiaSartu(Modeloa.PROPIETATEA3, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(erabiltzailea.getBalioZerrenda().size(), "estufa");
	                System.out.println(nombre + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;
	                
	            case Modeloa.PROPIETATEA4:
	                System.out.println("Recibida actualización de PROPIETATEA4");
	                actualizarPantalla();
	                break;
	        }
	        modeloa.start();
	    }
	}
	
	public void autenticarUsuario() 
	{
		String nombre = izenaField.getText();
        String contrasena = new String(pasahitzaField.getPassword());        
        
		if (modeloa.autenticar(nombre, contrasena)) 
		{
			loginPANTAILA.dispose(); //Login pantaila itxi

            if ("admin".equals(nombre)) 
            {
                //mostrarPantallaAdmin();
            } 
            else if ((isUsuarioRegistrado(nombre)) && (isUsuarioContraseña(nombre,contrasena)))
            {           	
            	Erabiltzailea erabiltzailea = getErabiltzailea(nombre);   
            	if(erabiltzailea.behin==0)
            	{          		                
            		mostrarPantallaHuecos();
            	}
            	else
            	{
            		mostrarPantallaHuecosCompletado(erabiltzailea);
            	}
            }         
            else if ("Ludok".equals(nombre) && "dj".equals(contrasena)) 
            {
                ludokLovesU();
            }
            else 
            {
                JOptionPane.showMessageDialog(null, "Erabiltzaile hau ez da existitzen.");
            }
        } 
		else
        {
            JOptionPane.showMessageDialog(null, "Akatz bat gertatu da. Saiatu berriz.");
        }
	}
	
	public void mostrarPantallaHuecosCompletado(Erabiltzailea erabiltzailea) 
	{
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));

	    for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        JButton button = new JButton("Hutsunea " + (i + 1));
	        button.addActionListener(kontrolatzailea);
	        botonesHuecosPanel.add(button);

	        String balio = erabiltzailea.getBalioZerrenda().get(i).toLowerCase();
	        irudiaJarri(balio, button);
	    }

	    JPanel botonAtrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	    backButton = new JButton("ATZERA");
	    backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(80, 30)); 
	    botonAtrasPanel.add(backButton);
	    
	    editButton = new JButton("EDITATU");
	    editButton.setActionCommand("EDITATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);
	    
	    editButton = new JButton("KONFIRMATU");
	    editButton.setActionCommand("KONFIRMATU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30)); 
	    botonAtrasPanel.add(editButton);

	    panel.add(botonAtrasPanel, BorderLayout.NORTH);
	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);

	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	void ludokLovesU() //EASTER EGG
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Pantaila tamaina hartu 
		//(Argazkiaren tamaina baino haundiagoa baldin bada ez da gehiago haundituko)

        ImageIcon ludok = new ImageIcon("ludok/arkitektura.jpg"); //ARGAZKIA
        JLabel lludok = new JLabel(ludok); //ARGAZKIA JARTZEKO LABEL-A
        lludok.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()); 
        JLayeredPane ludokdPane = new JLayeredPane();
        ludokdPane.add(lludok);
        JFrame ludokFrame = new JFrame("DJ LUDOK");
		 ludokFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 ludokFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		 ludokFrame.setUndecorated(true);
		 ludokFrame.getContentPane().add(ludokdPane);
		 ludokFrame.pack();
		 ludokFrame.setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		Modeloa modeloa = new Modeloa (); //MODELOA
		Kontrolatzailea kontrolatzailea = new Kontrolatzailea(modeloa); //KONTROLATZAILEA
		//
		//try {
            // Initialization
            Mqtt mqtt1 = null;
			try {
				mqtt1 = new Mqtt(BROKER,CLENT_ID);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
          
        //} catch (Exception ex) {
        //    throw new RuntimeException(ex);
        //}
		//
		Printzipala main = new Printzipala(modeloa,kontrolatzailea, mqtt1); //MAIN		
		main.loginPantailaErakutzi(); //HASIERAKO FUNTZIOA
	}
}