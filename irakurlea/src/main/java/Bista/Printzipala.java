package Bista;

import Modeloa.Erabiltzailea;
import Modeloa.Modeloa;
import irakurlea.Mqtt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.eclipse.paho.client.mqttv3.MqttException;

import Kontrolatzailea.Kontrolatzailea;

public class Printzipala implements PropertyChangeListener
{
	//MODELOA
	private Modeloa modeloa; 
	
	//KONTROLATZAILEA
	private Kontrolatzailea kontrolatzailea; 
	
	//PANTAILAK
	private JFrame ordenagailuarenPANTAILA; //REBISATU
	public JFrame mainPANTAILA;
	public JFrame adminFrame;
	public JFrame loginPANTAILA;		
	private JFrame aukerenPANTAILA;
		
	//JFIELD
	public JTextField izenaField;
	public JPasswordField pasahitzaField;
	
	//JLABEL
	public JLabel argeliaBABESETXE;
	public JLabel groenlandiaBABESETXE;
	JLabel erabiltzaileIzena;
	
	//BOTOIAK
	private JButton button; //EZKERREKO BOTOIA
	private JButton button2; //ERDIKO BOTOIA
	private JButton button3; //ESKUINEKO BOTOIA
	public JButton backButton; //ATZERA JOATEKO BOTOIA
	public JButton editButton;
	public JButton confirmButton;
	private JButton irtenButton;
	public JButton deleteButton;
	public JButton loginButton;
	private JButton putzuBOTOIA;
	private JButton putzuBOTOIA2;
	private JButton putzuBOTOIA3;
	private JButton haizegailuBOTOIA;
	private JButton haizegailuBOTOIA2;
	private JButton haizegailuBOTOIA3;
	private JButton berogailuBOTOIA;
	private JButton berogailuBOTOIA2;
	private JButton berogailuBOTOIA3;
    
    //ERABILTZAILEA
    public Erabiltzailea erabiltzailea;
    
    //JTOOLBAR
    private JToolBar toolBar;
    
    //STRING
    public String izena;
    private static final String BROKER = "tcp://localhost:1883";
    private static final String CLENT_ID = "TemperatureIrakurle";
    
    //INT
    private int posizioa=0;
    public int adminMapa = 0;
    private static int spaceWidth = 50;
    private static int spaceWidth2 = 15;

    //MQTT
    private Mqtt mqtt;
      
    //private static final int QoS = 2;
           	
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea, Mqtt mqtt) //PRINTZIPALAREN KONSTRUKTOREA
	{
		this.modeloa = modeloa; //MODELOA
		this.kontrolatzailea = kontrolatzailea; //KONTROLATZAILEA
		this.mqtt = mqtt;
		this.modeloa.addPropertyChangeListener(this); //PROPERTY CHANGE
						
		modeloa.erabiltzaileakIrakurri(); //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
		
		toolbarBotoienKonfigurazioa();
		
		modeloa.start();
	}
	
	private void toolbarBotoienKonfigurazioa()
	{
		backButton = new JButton("ATZERA");
        backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(130, 30));
	    
	    editButton = new JButton("GEHITU");
        editButton.setActionCommand("GEHITU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 30));
	    
	    deleteButton = new JButton("EZABATU");
        deleteButton.setActionCommand("EZABATU");
	    deleteButton.addActionListener(kontrolatzailea);
	    deleteButton.setPreferredSize(new Dimension(130, 30));
	    
	    confirmButton = new JButton("KONFIRMATU");
        confirmButton.setActionCommand("KONFIRMATU");
        confirmButton.addActionListener(kontrolatzailea);
        confirmButton.setPreferredSize(new Dimension(130, 30)); 
        confirmButton.setBackground(Color.GREEN);
        
        putzuBOTOIA = new JButton("P1");
        putzuBOTOIA.setPreferredSize(new Dimension(110, 30)); 
        putzuBOTOIA2 = new JButton("P2");
        putzuBOTOIA2.setPreferredSize(new Dimension(110, 30)); 
        putzuBOTOIA3 = new JButton("P3");
        putzuBOTOIA3.setPreferredSize(new Dimension(110, 30)); 
        
        haizegailuBOTOIA = new JButton("H1");
        haizegailuBOTOIA.setPreferredSize(new Dimension(110, 30)); 
        haizegailuBOTOIA2 = new JButton("H2");
        haizegailuBOTOIA2.setPreferredSize(new Dimension(110, 30)); 
        haizegailuBOTOIA3 = new JButton("H3");
        haizegailuBOTOIA3.setPreferredSize(new Dimension(110, 30)); 
        
        berogailuBOTOIA = new JButton("B1");
        berogailuBOTOIA.setPreferredSize(new Dimension(110, 30)); 
        berogailuBOTOIA2 = new JButton("B2");
        berogailuBOTOIA2.setPreferredSize(new Dimension(110, 30)); 
        berogailuBOTOIA3 = new JButton("B3");
        berogailuBOTOIA3.setPreferredSize(new Dimension(110, 30)); 
        
        irtenButton = new JButton("IRTEN");
        irtenButton.setActionCommand("IRTEN");
        irtenButton.addActionListener(kontrolatzailea);
        irtenButton.setPreferredSize(new Dimension(130, 30)); 
	}
	
	private void actualizarPantalla() 
	{
		if (mqtt != null) 
		{
			String balioa;
			int PozoKop = 0, VentilaKop = 0, EstufaKop = 0;
			
			balioa = erabiltzailea.getBalioZerrenda().get(0);
			if(balioa=="pozo")
			{
				if(PozoKop == 1)
				{
					button.setText(String.valueOf(mqtt.valueWater2));
					putzuBOTOIA2.setBackground(Color.GREEN);
					putzuBOTOIA2.setVisible(true);
					PozoKop++;
				}
				else if(PozoKop == 2)
				{
					button.setText(String.valueOf(mqtt.valueWater3));
					putzuBOTOIA3.setBackground(Color.GREEN);
					putzuBOTOIA3.setVisible(true);
					PozoKop++;
				}
				else 
				{
					button.setText(String.valueOf(mqtt.valueWater));
					putzuBOTOIA.setBackground(Color.GREEN);
					putzuBOTOIA.setVisible(true);
					PozoKop++;
				}
			}
			if(balioa=="ventilador")
			{
				if(VentilaKop == 1)
				{
					button.setText(String.valueOf(mqtt.valueTemperature2));
					haizegailuBOTOIA2.setBackground(Color.GREEN);
					haizegailuBOTOIA2.setVisible(true);
					VentilaKop++;
				}
				else if(VentilaKop == 2)
				{
					button.setText(String.valueOf(mqtt.valueTemperature3));
					haizegailuBOTOIA3.setBackground(Color.GREEN);
					haizegailuBOTOIA3.setVisible(true);
					VentilaKop++;
				}
				else 
				{
					button.setText(String.valueOf(mqtt.valueTemperature));
					haizegailuBOTOIA.setBackground(Color.GREEN);
					haizegailuBOTOIA.setVisible(true);
					VentilaKop++;
				}
			}
			if(balioa=="estufa")
			{
				if(EstufaKop == 1)
				{
					button.setText(String.valueOf(mqtt.valueGas2));
					berogailuBOTOIA2.setBackground(Color.GREEN);
					berogailuBOTOIA2.setVisible(true);
					EstufaKop++;
				}
				else if(EstufaKop == 2)
				{
					button.setText(String.valueOf(mqtt.valueGas3));
					berogailuBOTOIA3.setBackground(Color.GREEN);
					berogailuBOTOIA3.setVisible(true);
					EstufaKop++;
				}
				else 
				{
					button.setText(String.valueOf(mqtt.valueGas));
					berogailuBOTOIA.setBackground(Color.GREEN);
					berogailuBOTOIA.setVisible(true);
					EstufaKop++;
				}
			}
			
			balioa = erabiltzailea.getBalioZerrenda().get(1);
			if(balioa=="pozo")
			{
				if(PozoKop == 1)
				{
					button2.setText(String.valueOf(mqtt.valueWater2));
					putzuBOTOIA2.setBackground(Color.GREEN);
					putzuBOTOIA2.setVisible(true);
					PozoKop++;
				}
				else if(PozoKop == 2)
				{
					button2.setText(String.valueOf(mqtt.valueWater3));
					putzuBOTOIA3.setBackground(Color.GREEN);
					putzuBOTOIA3.setVisible(true);
					PozoKop++;
				}
				else 
				{
					button2.setText(String.valueOf(mqtt.valueWater));
					putzuBOTOIA.setBackground(Color.GREEN);
					putzuBOTOIA.setVisible(true);
					PozoKop++;
				}
				
			}
			if(balioa=="ventilador")
			{
				if(VentilaKop == 1)
				{
					button2.setText(String.valueOf(mqtt.valueTemperature2));
					haizegailuBOTOIA2.setBackground(Color.GREEN);
					haizegailuBOTOIA2.setVisible(true);
					VentilaKop++;
				}
				else if(VentilaKop == 2)
				{
					button2.setText(String.valueOf(mqtt.valueTemperature3));
					haizegailuBOTOIA3.setBackground(Color.GREEN);
					haizegailuBOTOIA3.setVisible(true);
					VentilaKop++;
				}
				else 
				{
					button2.setText(String.valueOf(mqtt.valueTemperature));
					haizegailuBOTOIA.setBackground(Color.GREEN);
					haizegailuBOTOIA.setVisible(true);
					VentilaKop++;
				}
			}
			if(balioa=="estufa")
			{
				if(EstufaKop == 1)
				{
					button2.setText(String.valueOf(mqtt.valueGas2));
					berogailuBOTOIA2.setBackground(Color.GREEN);
					berogailuBOTOIA2.setVisible(true);
					EstufaKop++;
				}
				else if(EstufaKop == 2)
				{
					button2.setText(String.valueOf(mqtt.valueGas3));
					berogailuBOTOIA3.setBackground(Color.GREEN);
					berogailuBOTOIA3.setVisible(true);
					EstufaKop++;
				}
				else 
				{
					button2.setText(String.valueOf(mqtt.valueGas));
					berogailuBOTOIA.setBackground(Color.GREEN);
					berogailuBOTOIA.setVisible(true);
					EstufaKop++;
				}
			}
			
			balioa = erabiltzailea.getBalioZerrenda().get(2);
			if(balioa=="pozo")
			{
				if(PozoKop == 1)
				{
					button3.setText(String.valueOf(mqtt.valueWater2));
					putzuBOTOIA3.setBackground(Color.GREEN);
					putzuBOTOIA3.setVisible(true);
					PozoKop++;
				}
				else if(PozoKop == 2)
				{
					button3.setText(String.valueOf(mqtt.valueWater3));
					putzuBOTOIA2.setBackground(Color.GREEN);
					putzuBOTOIA2.setVisible(true);
					PozoKop++;
				}
				else 
				{
					button3.setText(String.valueOf(mqtt.valueWater));
					putzuBOTOIA.setBackground(Color.GREEN);
					putzuBOTOIA.setVisible(true);
					PozoKop++;
				}
			}
			if(balioa=="ventilador")
			{
				if(VentilaKop == 1)
				{
					button3.setText(String.valueOf(mqtt.valueTemperature2));
					haizegailuBOTOIA2.setBackground(Color.GREEN);
					haizegailuBOTOIA2.setVisible(true);
					VentilaKop++;
				}
				else if(VentilaKop == 2)
				{
					button3.setText(String.valueOf(mqtt.valueTemperature3));
					haizegailuBOTOIA3.setBackground(Color.GREEN);
					haizegailuBOTOIA3.setVisible(true);
					VentilaKop++;
				}
				else 
				{
					button3.setText(String.valueOf(mqtt.valueTemperature));
					haizegailuBOTOIA.setBackground(Color.GREEN);
					haizegailuBOTOIA.setVisible(true);
					VentilaKop++;
				}
			}
			if(balioa=="estufa")
			{
				if(EstufaKop == 1)
				{
					button3.setText(String.valueOf(mqtt.valueGas2));
					berogailuBOTOIA2.setBackground(Color.GREEN);
					berogailuBOTOIA2.setVisible(true);
					EstufaKop++;
				}
				else if(EstufaKop == 2)
				{
					button3.setText(String.valueOf(mqtt.valueGas3));
					berogailuBOTOIA3.setBackground(Color.GREEN);
					berogailuBOTOIA3.setVisible(true);
					EstufaKop++;
				}
				else 
				{
					button3.setText(String.valueOf(mqtt.valueGas));
					berogailuBOTOIA.setBackground(Color.GREEN);
					berogailuBOTOIA.setVisible(true);
					EstufaKop++;
				}
			}			
		} 
		else 
		{
		    System.out.println("MQTT es null");
		}	
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
            modeloa.setIrudia(this);
            
            JPanel loginPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);

            loginPANTAILA.setLayout(new BorderLayout());
            loginPANTAILA.add(loginPanel, BorderLayout.CENTER);

            //IZENA
            izenaField = new JTextField(20);
            izenaField.addKeyListener(kontrolatzailea);
            
            //PASAHITZA
            pasahitzaField = new JPasswordField(20);
            pasahitzaField.addKeyListener(kontrolatzailea);

            loginPanel.add(new JLabel("Izena:"), gbc);

            gbc.gridy++;
            loginPanel.add(izenaField, gbc);

            gbc.gridy++;
            loginPanel.add(new JLabel("Pasahitza:"), gbc);

            gbc.gridy++;
            loginPanel.add(pasahitzaField, gbc);

            loginButton = new JButton("HASI SAIOA");
            loginButton.setActionCommand("SAIOAHASI");
            loginButton.addActionListener(kontrolatzailea);                      
            
            gbc.gridy++;
            gbc.gridwidth = 2;
            loginPanel.add(loginButton, gbc);
            loginPANTAILA.getContentPane().add(loginPanel);
            loginPANTAILA.setVisible(true);
		});
	}
		
	public void mostrarPantallaAdmin() 
	{
		adminMapa = 2;
        adminFrame = new JFrame("Pantalla de Admin");
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
            argeliaBABESETXE = new JLabel(africaIcon);
            argeliaBABESETXE.setBounds(900, 400, hostelWidth, hostelHeight);
            layeredPane.add(argeliaBABESETXE, JLayeredPane.PALETTE_LAYER);

            ImageIcon swedenIcon = new ImageIcon("ikonoak/babesetxe.png");
            swedenIcon = new ImageIcon(swedenIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            groenlandiaBABESETXE = new JLabel(swedenIcon);
            groenlandiaBABESETXE.setBounds(700, 100, hostelWidth, hostelHeight);
            layeredPane.add(groenlandiaBABESETXE, JLayeredPane.PALETTE_LAYER);

            //Arratoia konfiguratu
            layeredPane.addMouseListener(kontrolatzailea);

            toolBar = new JToolBar();

            toolBar.add(backButton);
            adminFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
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
        
	public void mostrarPantallaHuecos()
	{
		adminMapa = 0;
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
	    
	    JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("EDEN HAVEN BABESETXEAK");
        menuBar.add(fileMenu);
        mainPANTAILA.setJMenuBar(menuBar);
        
        toolBar = new JToolBar();
        
        deleteButton.setVisible(true);
    	editButton.setVisible(true);
    	backButton.setVisible(true);
    	confirmButton.setVisible(false);
    	
    	putzuBOTOIA.setVisible(false);
    	putzuBOTOIA2.setVisible(false);
    	putzuBOTOIA3.setVisible(false);
    	haizegailuBOTOIA.setVisible(false);
    	haizegailuBOTOIA2.setVisible(false);
    	haizegailuBOTOIA3.setVisible(false);
    	berogailuBOTOIA.setVisible(false);
    	berogailuBOTOIA2.setVisible(false);
    	berogailuBOTOIA3.setVisible(false);
        
        erabiltzaileIzena = new JLabel(izena.toUpperCase());  
		erabiltzaileIzena.setFont(new Font("SansSerif", Font.BOLD, 30));
        
        toolBar.add(backButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(confirmButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(putzuBOTOIA);
        toolBar.add(putzuBOTOIA2);
        toolBar.add(putzuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(haizegailuBOTOIA);
        toolBar.add(haizegailuBOTOIA2);
        toolBar.add(haizegailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(berogailuBOTOIA);
        toolBar.add(berogailuBOTOIA2);
        toolBar.add(berogailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(erabiltzaileIzena);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(irtenButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        

	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
        
	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarPantallaEditarHuecos(Erabiltzailea erabiltzailea)
	{
		adminMapa = 0;
		int i = 0, balixu = 0;
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));
	    
	    for (i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        String currentValue = erabiltzailea.getBalioZerrenda().get(i);

	        if (currentValue != null) 
	        {
	        	String balio;

	        	if(i==0)
	        	{
		    	    botonesHuecosPanel.add(button);
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button);
		            balixu++;
	        	}
	        	else if(i==1)
	        	{
		    	    botonesHuecosPanel.add(button2);
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button2);
		            balixu++;
	        	}

	        	else if(i==2)
	        	{
		    	    botonesHuecosPanel.add(button3);
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button3);
		            balixu++;
	        	}
	        } 
	        else 
	        {
	        	if(i==1) 
	    	    {
	    	    	button2 = new JButton("Hueco " + 2);
	    		    button2.setActionCommand("HUECO2");
	    		    button2.addActionListener(kontrolatzailea);
	    		    botonesHuecosPanel.add(button2);
	    	    }
	        	else if(i==2)
	    	    {
	    	    	button3 = new JButton("Hueco " + 3);
	    		    button3.setActionCommand("HUECO3");
	    		    button3.addActionListener(kontrolatzailea);
	    		    botonesHuecosPanel.add(button3);
	    	    }
	        	else
	        	{
	        		System.out.println("Error: El valor en la posición " + i + " es null.");
	        		button = new JButton("Hueco " + 1);
	        		button.setActionCommand("HUECO");
	        		button.addActionListener(kontrolatzailea);
	        		botonesHuecosPanel.add(button);
	        	}
	        }
	    } 
	    if(balixu==3)
	    {
	    	String mensaje = "3 ELEMENTU GEHITU AHAL DITUZU GEHIENEZ!";
	        JOptionPane.showMessageDialog(null, mensaje);
	        deleteButton.setVisible(true);
	    	editButton.setVisible(true);
	    	backButton.setVisible(true);
	    	confirmButton.setVisible(false);	        
	    }
	    
	    JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("EDEN HAVEN BABESETXEAK");
        menuBar.add(fileMenu);
        mainPANTAILA.setJMenuBar(menuBar);
        
        toolBar = new JToolBar();
        
        deleteButton.setVisible(true);
    	editButton.setVisible(true);
    	backButton.setVisible(true);
    	confirmButton.setVisible(false);
        
    	putzuBOTOIA.setVisible(false);
    	putzuBOTOIA2.setVisible(false);
    	putzuBOTOIA3.setVisible(false);
    	haizegailuBOTOIA.setVisible(false);
    	haizegailuBOTOIA2.setVisible(false);
    	haizegailuBOTOIA3.setVisible(false);
    	berogailuBOTOIA.setVisible(false);
    	berogailuBOTOIA2.setVisible(false);
    	berogailuBOTOIA3.setVisible(false);
        
        erabiltzaileIzena = new JLabel(izena.toUpperCase());  
		erabiltzaileIzena.setFont(new Font("SansSerif", Font.BOLD, 30));
        
        toolBar.add(backButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(confirmButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(putzuBOTOIA);
        toolBar.add(putzuBOTOIA2);
        toolBar.add(putzuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(haizegailuBOTOIA);
        toolBar.add(haizegailuBOTOIA2);
        toolBar.add(haizegailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(berogailuBOTOIA);
        toolBar.add(berogailuBOTOIA2);
        toolBar.add(berogailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(erabiltzaileIzena);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(irtenButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));

	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);

	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarPantallaHuecosCompletado(Erabiltzailea erabiltzailea) 
	{
		adminMapa = 0;
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));

	    for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        String balio = erabiltzailea.getBalioZerrenda().get(i);
	        
	        if (balio != null) 
	        {
	        	if(i==0)
	        	{
		    	    botonesHuecosPanel.add(button);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button);
	        	}
	        	else if(i==1)
	        	{
		    	    botonesHuecosPanel.add(button2);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button2);
	        	}

	        	else if(i==2)
	        	{
		    	    botonesHuecosPanel.add(button3);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button3);
	        	}
	        }
	    }

	    JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("EDEN HAVEN BABESETXEAK");
        menuBar.add(fileMenu);
        mainPANTAILA.setJMenuBar(menuBar);
        
        toolBar = new JToolBar();
        
        deleteButton.setVisible(true);
    	editButton.setVisible(true);
    	backButton.setVisible(true);
    	confirmButton.setVisible(false);
        
    	putzuBOTOIA.setVisible(false);
    	putzuBOTOIA2.setVisible(false);
    	putzuBOTOIA3.setVisible(false);
    	haizegailuBOTOIA.setVisible(false);
    	haizegailuBOTOIA2.setVisible(false);
    	haizegailuBOTOIA3.setVisible(false);
    	berogailuBOTOIA.setVisible(false);
    	berogailuBOTOIA2.setVisible(false);
    	berogailuBOTOIA3.setVisible(false);
        
        erabiltzaileIzena = new JLabel(izena.toUpperCase());  
		erabiltzaileIzena.setFont(new Font("SansSerif", Font.BOLD, 30));
        
        toolBar.add(backButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(confirmButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(putzuBOTOIA);
        toolBar.add(putzuBOTOIA2);
        toolBar.add(putzuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(haizegailuBOTOIA);
        toolBar.add(haizegailuBOTOIA2);
        toolBar.add(haizegailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(berogailuBOTOIA);
        toolBar.add(berogailuBOTOIA2);
        toolBar.add(berogailuBOTOIA3);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(erabiltzaileIzena);
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(irtenButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));

	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);

	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarOpcionesEliminar()
	{
		aukerenPANTAILA = new JFrame("ZEIN NAHI DUZU EZABATU");
        aukerenPANTAILA.setSize(500, 150);
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(4, 1));      
        int kop=0;
        for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) 
        {
	        String balio = erabiltzailea.getBalioZerrenda().get(i);
	        if(balio!=null)
	        {
	        	kop++;
	        }
        }
        
        if(kop==3)
        {
	        JButton pozoButton = new JButton("EZKERREKOA");
	        pozoButton.setActionCommand("EZKERREKOA");
	        pozoButton.addActionListener(kontrolatzailea);
	        opcionesPanel.add(pozoButton);
	
	        JButton ventiladorButton = new JButton("ERDIKOA");
	        ventiladorButton.setActionCommand("ERDIKOA");
	        ventiladorButton.addActionListener(kontrolatzailea);
	        opcionesPanel.add(ventiladorButton);
	
	        JButton fugaButton = new JButton("ESKUINEKOA");
	        fugaButton.setActionCommand("ESKUINEKOA");
	        fugaButton.addActionListener(kontrolatzailea);
	        opcionesPanel.add(fugaButton);
        }
        else if(kop==2)
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)==null)
        	{
        		JButton pozoButton = new JButton("EZKERREKOA");
    	        pozoButton.setActionCommand("EZKERREKOA");    
    	        pozoButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(pozoButton);
    	
    	        JButton fugaButton = new JButton("ESKUINEKOA");
    	        fugaButton.setActionCommand("ERDIKOA");
    	        fugaButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(fugaButton);
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)==null)
        	{
        		JButton pozoButton = new JButton("EZKERREKOA");
        		pozoButton.setActionCommand("EZKERREKOA");    
    	        pozoButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(pozoButton);
    	
    	        JButton fugaButton = new JButton("ESKUINEKOA");
    	        fugaButton.setActionCommand("ESKUINEKOA");
    	        fugaButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(fugaButton);
        	}
        	else
        	{
        		JButton pozoButton = new JButton("EZKERREKOA");
        		pozoButton.setActionCommand("ERDIKOA");    
    	        pozoButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(pozoButton);
    	
    	        JButton fugaButton = new JButton("ESKUINEKOA");
    	        fugaButton.setActionCommand("ESKUINEKOA");
    	        fugaButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(fugaButton);
        	}	        
        } 
        else if(kop==1)
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)!=null)
        	{
        		JButton fugaButton = new JButton("EZABATU");
    	        fugaButton.setActionCommand("ESKUINEKOA");
    	        fugaButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(fugaButton);
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)!=null)
        	{
        		JButton ventiladorButton = new JButton("EZABATU");
     	        ventiladorButton.setActionCommand("ERDIKOA");
     	        ventiladorButton.addActionListener(kontrolatzailea);
     	        opcionesPanel.add(ventiladorButton);
        	}
        	else
        	{
        		JButton pozoButton = new JButton("EZABATU");
        		pozoButton.setActionCommand("EZKERREKOA");    
    	        pozoButton.addActionListener(kontrolatzailea);
    	        opcionesPanel.add(pozoButton);
        	}
        }

        aukerenPANTAILA.getContentPane().add(opcionesPanel);

        aukerenPANTAILA.setLocationRelativeTo(ordenagailuarenPANTAILA);
        aukerenPANTAILA.setVisible(true);
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
	    erabiltzailea = modeloa.getErabiltzailea(izena);

	    if (erabiltzailea != null) 
	    {
	        switch (lekua) 
	        {
	            case 1:	      
	                irudiaJarri(argazkia, button);
	                posizioa=0;
	                break;

	            case 2:
	                irudiaJarri(argazkia, button2);
	                posizioa=1;
	                break;

	            case 3:
	                irudiaJarri(argazkia, button3);
	                posizioa=2;
	                break;
	        }
	    }
	}
	
	private void irudiaJarri(String argazkia, JButton botoia)
	{
		int hostelWidth = 200;
        int hostelHeight = 200;

        ImageIcon icon = new ImageIcon("ikonoak/" + argazkia + ".png");
        Image scaledImage = icon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH);

        botoia.setIcon(new ImageIcon(scaledImage));
        botoia.setText("Kargatzen..."/*String.valueOf(mqtt.valueTemperature)*/);
		botoia.setHorizontalTextPosition(SwingConstants.CENTER);
		botoia.setVerticalTextPosition(SwingConstants.BOTTOM);

        aukerenPANTAILA.dispose();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) 
	{
	    String propiedad = e.getPropertyName();
	    int lekua = (int) e.getNewValue();	    

	    erabiltzailea = modeloa.getErabiltzailea(izena);

	    if (erabiltzailea != null) 
	    {
	        switch (propiedad) 
	        {
	            case Modeloa.PROPIETATEA:
	                argazkiaSartu(Modeloa.PROPIETATEA, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "pozo");
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;

	            case Modeloa.PROPIETATEA2:
	                argazkiaSartu(Modeloa.PROPIETATEA2, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "ventilador");
	                System.out.println(izena + " balioa " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break; 

	            case Modeloa.PROPIETATEA3:
	                argazkiaSartu(Modeloa.PROPIETATEA3, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "estufa");
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;
	                
	            case Modeloa.PROPIETATEA4:
	                System.out.println("Recibida actualización de PROPIETATEA4");
	                actualizarPantalla();
	                break;
	                
	            case Modeloa.PROPIETATEA5:
	                erabiltzailea.setBalioZerrendaAtIndex(0, null);
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose();
	                //erabiltzailea.behin = 0;  
	                break;
	                
	            case Modeloa.PROPIETATEA6:
	            	erabiltzailea.setBalioZerrendaAtIndex(1, null);
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose();
	                break;
	                
	            case Modeloa.PROPIETATEA7:
	            	erabiltzailea.setBalioZerrendaAtIndex(2, null);
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose();
	                break;
	        }
	    }
	}
	
	//EASTER EGG
	public void ludokLovesU() 
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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
		//MODELOA
		Modeloa modeloa = new Modeloa (); 
		
		//KONTROLATZAILEA
		Kontrolatzailea kontrolatzailea = new Kontrolatzailea(modeloa); 
		
		//MQTT
        Mqtt mqtt1 = null;
        try 
        {
        	mqtt1 = new Mqtt(BROKER,CLENT_ID);
		} 
        catch (MqttException e) 
        {
			e.printStackTrace();
		}

        //MAIN	
		Printzipala main = new Printzipala(modeloa,kontrolatzailea, mqtt1);
		
		//HASIERAKO FUNTZIOA
		main.loginPantailaErakutzi(); 
	}
}