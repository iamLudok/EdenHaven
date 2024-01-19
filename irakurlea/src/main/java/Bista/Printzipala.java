package Bista;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;
import Modeloa.Erabiltzailea;
import Modeloa.Hizkuntza;
import Modeloa.Modeloa;
import irakurlea.Mqtt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.eclipse.paho.client.mqttv3.MqttException;

import Kontrolatzailea.Kontrolatzailea;
import Kontrolatzailea.NireAkzioa;

public class Printzipala implements PropertyChangeListener
{
	//MODELOA
	private Modeloa modeloa; 
	
	//KONTROLATZAILEA
	private Kontrolatzailea kontrolatzailea; 
	
	//PANTAILAK
	private JFrame ordenagailuarenPANTAILA; //REBISATU
	public JFrame mainPANTAILA;
	public JFrame mainPANTAILA2;
	public JFrame adminFrame;
	public JFrame loginPANTAILA;		
	private JFrame aukerenPANTAILA;
		
	//JFIELD
	//public JTextField izenaField;
	public TextFieldUsername izenaField;
	//public JPasswordField pasahitzaField;
	public TextFieldPassword pasahitzaField;
	
	//JLABEL
	public JLabel argeliaBABESETXE;
	public JLabel groenlandiaBABESETXE;
	JLabel erabiltzaileIzena;
	
	boolean verde = true;
	
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
    public String zer;
    /*MAKINA VIRTUALAREN BROKER: 172.20.10.4 / ORDENAGAILUKO BROKER: localhost*/
    private static final String BROKER = "tcp://localhost:1883";
    private static final String CLENT_ID = "TemperatureSimulator";
    
    //INT
    private int posizioa=0;
    public int adminMapa = 0;
    private static int spaceWidth = 50;
    private static int spaceWidth2 = 15;
    private int anchoPantalla = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    public String hizkuntza = "euskera"; 
    
    //MQTT
    private Mqtt mqtt;
    
    NireAkzioa euskera, castellano, ingles, irten;
           	
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea, Mqtt mqtt) //PRINTZIPALAREN KONSTRUKTOREA
	{
		this.modeloa = modeloa; //MODELOA
		this.kontrolatzailea = kontrolatzailea; //KONTROLATZAILEA
		this.mqtt = mqtt;
		this.modeloa.addPropertyChangeListener(this); //PROPERTY CHANGE
						
		modeloa.erabiltzaileakIrakurri(); //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
		
		modeloa.hizkuntzakIrakurri();
		//modeloa.hizkuntzaBakoitzaIrakurri();
		
		sortuAkzioak();
		
		toolbarBotoienKonfigurazioa(hizkuntza);				
	}
	
	/*private void sortuHizkuntzaAkzioak(NireAkzioa izena) 
	{
		izena = new NireAkzioa (String.valueOf(izena),new ImageIcon("ikonoak/" + String.valueOf(izena) + ".png"),String.valueOf(izena),null, this, modeloa);	
	}*/
	
	private void sortuAkzioak() 
	{
		euskera = new NireAkzioa ("Euskera",new ImageIcon("ikonoak/exit.png"),"euskera",new Integer(KeyEvent.VK_E), this, modeloa);
		castellano = new NireAkzioa ("Castellano",new ImageIcon("ikonoak/exit.png"),"castellano",new Integer(KeyEvent.VK_E), this, modeloa);
		ingles = new NireAkzioa ("Ingles",new ImageIcon("ikonoak/exit.png"),"ingles",new Integer(KeyEvent.VK_E), this, modeloa);
		irten = new NireAkzioa ("Irten",new ImageIcon("ikonoak/exit.png"),"Aplikazioa itxi",
				new Integer(KeyEvent.VK_S), this, modeloa);
	}
	
	private JMenuBar crearMenuBar() 
	{		
		JMenuBar barra = new JMenuBar();
		barra.add (sortuMenuHizkuntzak());
		barra.add(Box.createHorizontalGlue());
		barra.add (crearMenuSalir());
		
		return barra;
	}
	
	private JMenu sortuMenuHizkuntzak() 
	{
		JMenu menuHizkuntzak = new JMenu ("Hizkuntzak");
		menuHizkuntzak.setMnemonic(new Integer(KeyEvent.VK_H));
		menuHizkuntzak.add(euskera);
		menuHizkuntzak.add(castellano);
		menuHizkuntzak.add(ingles);
		
		return menuHizkuntzak;
	}
	
	private JMenu crearMenuSalir() 
	{
		JMenu menuSalir = new JMenu ("Irten");
		menuSalir.setMnemonic(new Integer(KeyEvent.VK_S));
		menuSalir.add(irten);
		
		return menuSalir;
	}
	
	public void toolbarBotoienKonfigurazioa(String hizkuntzAukera)
	{	
		Color borderColor = Koloreak.AKUA;
        LineBorder lineBorder = new LineBorder(borderColor, 3);
        
		/*if (hizkuntza == 1) { backButton = new JButton("ATZERA"); }
		else if (hizkuntza == 2) { backButton = new JButton("ATRAS"); }
		else { backButton = new JButton("BACK"); }		*/
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntzAukera);
		backButton = new JButton(hizkuntza1.backButtom);
        backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(130, 60));
	    backButton.setBorder(lineBorder);
	    backButton.setForeground(Color.white);
	    
	    /*if (hizkuntza1 == 1) { editButton = new JButton("GEHITU"); }
		else if (hizkuntza1 == 2) { editButton = new JButton("AÑADIR"); }
		else { editButton = new JButton("ADD"); }*/
	    editButton = new JButton(hizkuntza1.editButtom);
        editButton.setActionCommand("GEHITU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 60));
	    editButton.setBorder(lineBorder);
	    editButton.setForeground(Color.white);
	    
	    /*if (hizkuntza1 == 1) { deleteButton = new JButton("EZABATU"); }
		else if (hizkuntza1 == 2) { deleteButton = new JButton("BORRAR"); }
		else { deleteButton = new JButton("ERASE"); }*/
	    deleteButton = new JButton(hizkuntza1.deleteButtom);
        deleteButton.setActionCommand("EZABATU");
	    deleteButton.addActionListener(kontrolatzailea);
	    deleteButton.setPreferredSize(new Dimension(130, 60));
	    deleteButton.setBorder(lineBorder);
	    deleteButton.setForeground(Color.white);
	    
	    /*if (hizkuntza1 == 1) { confirmButton = new JButton("KONFIRMATU"); }
		else if (hizkuntza1 == 2) { confirmButton = new JButton("CONFIRMAR"); }
		else { confirmButton = new JButton("CONFIRM"); }*/
	    confirmButton = new JButton(hizkuntza1.confirmButtom);
        confirmButton.setActionCommand("KONFIRMATU");
        confirmButton.addActionListener(kontrolatzailea);
        confirmButton.setPreferredSize(new Dimension(130, 60)); 
        confirmButton.setBackground(Color.GREEN);
        confirmButton.setBorder(lineBorder);
        confirmButton.setForeground(Color.white);
        
        putzuBOTOIA = new JButton("P1");
        putzuBOTOIA.setPreferredSize(new Dimension(110, 100)); 
        putzuBOTOIA2 = new JButton("P2");
        putzuBOTOIA2.setPreferredSize(new Dimension(110, 100)); 
        putzuBOTOIA3 = new JButton("P3");
        putzuBOTOIA3.setPreferredSize(new Dimension(110, 100)); 
        
        haizegailuBOTOIA = new JButton("H1");
        haizegailuBOTOIA.setPreferredSize(new Dimension(110, 100)); 
        haizegailuBOTOIA2 = new JButton("H2");
        haizegailuBOTOIA2.setPreferredSize(new Dimension(110, 100)); 
        haizegailuBOTOIA3 = new JButton("H3");
        haizegailuBOTOIA3.setPreferredSize(new Dimension(110, 100)); 
        
        berogailuBOTOIA = new JButton("B1");
        berogailuBOTOIA.setPreferredSize(new Dimension(110, 100)); 
        berogailuBOTOIA2 = new JButton("B2");
        berogailuBOTOIA2.setPreferredSize(new Dimension(110, 100)); 
        berogailuBOTOIA3 = new JButton("B3");
        berogailuBOTOIA3.setPreferredSize(new Dimension(110, 100)); 
        
        irtenButton = new JButton(" ");
        irtenButton.setBackground(new Color(184, 184, 184));
        irtenButton.setBorderPainted(false);
        irtenButton.setPreferredSize(new Dimension(1, 150)); 
	}
	
	private void actualizarPantalla() 
	{
	    if (mqtt != null) {
	        //int VentilaKop = 0, EstufaKop = 0;

	        for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) 
	        {
	            String balioa = erabiltzailea.getBalioZerrenda().get(i);
	            System.out.println("BALIOA: " + balioa);
	            
	            if (balioa == null) 
	            {
	                System.out.println("Advertencia: balioa es null en el índice " + i);
	                continue; 
	            }

	            switch (balioa) 
	            {
	                case "putzua":
	                    actualizarPozo(i + 1); 
	                    break;
	                case "haizegailua":
	                    actualizarVentilador(i + 1);
	                    break;
	                case "berogailua":
	                    actualizarEstufa(i + 1);
	                    break;
	                default:
	                	System.out.println("Advertencia: Opción desconocida en el índice " + i + ": " + balioa);
	                    break;
	            }
	        }
	    } else {
	        System.out.println("MQTT es null");
	    }
	}
	
	private void actualizarPozo(int index) 
	{
	    switch (index) 
	    {
	        case 1:
	            button.setText(String.valueOf(/*mqtt.valueWater*/mqtt.adc1 + " L"));	
	            button.setForeground(Color.white);
	            button.setFont(new Font("SansSerif", Font.BOLD, 70));
	            if(mqtt.valueWater<=33)
	            {
	            	putzuBOTOIA.setBackground(Color.RED);
	            }
	            else if(33 <= mqtt.valueWater && mqtt.valueWater <= 66)
	            {
	            	putzuBOTOIA.setBackground(Color.YELLOW);
	            }
	            else 
	            {
	            	putzuBOTOIA.setBackground(Color.GREEN);
	            }	            
	            putzuBOTOIA.setVisible(true);
	            break;
	        case 2:
	            button2.setText(String.valueOf(/*mqtt.valueWater2*/mqtt.adc2 + " L"));
	            button2.setForeground(Color.white);
	            button2.setFont(new Font("SansSerif", Font.BOLD, 70));
	            if(mqtt.valueWater2<=33)
	            {
	            	putzuBOTOIA2.setBackground(Color.RED);
	            }
	            else if(33 <= mqtt.valueWater2 && mqtt.valueWater2 <= 66)
	            {
	            	putzuBOTOIA2.setBackground(Color.YELLOW);
	            }
	            else 
	            {
	            	putzuBOTOIA2.setBackground(Color.GREEN);
	            }
	            putzuBOTOIA2.setVisible(true);
	            break;
	        case 3:
	            button3.setText(String.valueOf(/*mqtt.valueWater3*/mqtt.adc3 + " L"));
	            button3.setForeground(Color.white);
	            button3.setFont(new Font("SansSerif", Font.BOLD, 70));
	            if(mqtt.valueWater3<=33)
	            {
	            	putzuBOTOIA3.setBackground(Color.RED);
	            }
	            else if(33 <= mqtt.valueWater3 && mqtt.valueWater3 <= 66)
	            {
	            	putzuBOTOIA3.setBackground(Color.YELLOW);
	            }
	            else 
	            {
	            	putzuBOTOIA3.setBackground(Color.GREEN);
	            }
	            putzuBOTOIA3.setVisible(true);
	            break;
	        default:
	            break;
	    }
	}
	
	private static String extraerValoresNumericos(String input) 
	{
        String result = input.replaceAll("[^0-9.]", "");

        return result;
    }
	
	private void actualizarVentilador(int index) 
	{
		String esaldia = extraerValoresNumericos(/*mqtt.valueTemperature*/mqtt.adc1 + " ºC");		
		
		int balioa = 0;
		balioa = Integer.valueOf(esaldia);
		balioa = balioa*330/4095;
	    switch (index) {
	        case 1:
	            //button.setText(String.valueOf(mqtt.valueTemperature));
	        	button.setText("<html><center>" + balioa/*String.valueOf(mqtt.valueTemperature)*/ + "</center></html>");
	        	button.setForeground(Color.white);
	            button.setFont(new Font("SansSerif", Font.BOLD, 70));
	            haizegailuBOTOIA.setBackground(Color.GREEN);
	            haizegailuBOTOIA.setVisible(true);
	            break;
	        case 2:
	            button2.setText(String.valueOf(/*mqtt.valueTemperature2*/mqtt.adc2 + " ºC"));
	            button2.setForeground(Color.white);
	            button2.setFont(new Font("SansSerif", Font.BOLD, 70));
	            haizegailuBOTOIA2.setBackground(Color.GREEN);
	            haizegailuBOTOIA2.setVisible(true);
	            break;
	        case 3:
	            button3.setText(String.valueOf(/*mqtt.valueTemperature3*/mqtt.adc3 + " ºC"));
	            button3.setForeground(Color.white);
	            button3.setFont(new Font("SansSerif", Font.BOLD, 70));
	            haizegailuBOTOIA3.setBackground(Color.GREEN);
	            haizegailuBOTOIA3.setVisible(true);
	            break;
	        default:
	            break;
	    }
	}
	
	private void actualizarEstufa(int index) 
	{
	    switch (index) 
	    {
	        case 1:
	            button.setText(String.valueOf(/*mqtt.valueGas*/mqtt.adc1));
	            button.setForeground(Color.white);
	            button.setFont(new Font("SansSerif", Font.BOLD, 70));
	            berogailuBOTOIA.setBackground(Color.GREEN);
	            berogailuBOTOIA.setVisible(true);
	            break;
	        case 2:
	            button2.setText(String.valueOf(/*mqtt.valueGas2*/mqtt.adc2));
	            button2.setForeground(Color.white);
	            button2.setFont(new Font("SansSerif", Font.BOLD, 70));
	            berogailuBOTOIA2.setBackground(Color.GREEN);
	            berogailuBOTOIA2.setVisible(true);
	            break;
	        case 3:
	            button3.setText(String.valueOf(/*mqtt.valueGas3*/mqtt.adc3));
	            button3.setForeground(Color.white);
	            button3.setFont(new Font("SansSerif", Font.BOLD, 70));
	            berogailuBOTOIA3.setBackground(Color.GREEN);
	            berogailuBOTOIA3.setVisible(true);
	            break;
	        default:
	            break;
	    }
	}
	
	private void actualizarPantalla2()
	{
		if (verde) 
		{
            confirmButton.setBackground(UIManager.getColor("Button.background"));
        } 
		else 
		{
			confirmButton.setBackground(Color.GREEN);
        }
        verde = !verde;
	}	
	
	public void loginPantailaErakutzi() 
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
            loginPANTAILA.add(loginPanel, BorderLayout.NORTH);

            //IZENA
            //izenaField = new JTextField(20);
            izenaField = new TextFieldUsername();
            izenaField.addKeyListener(kontrolatzailea);
            
            //PASAHITZA
            //pasahitzaField = new JPasswordField(20);
            pasahitzaField = new TextFieldPassword();
            pasahitzaField.addKeyListener(kontrolatzailea);

            JLabel titleLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;

                    // Define el degradado de colores
                    Color color1 = new Color(  235, 166, 27  ); // Verde oscuro
                    Color color2 = new Color( 15, 180, 150 ); // Verde claro
                    GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                    g2d.setPaint(gradient);

                    // Establece la fuente y tamaño
                    Font font = new Font("Arial", Font.BOLD, 98); // Ajusta el tamaño según tus necesidades
                    g2d.setFont(font);

                    FontMetrics fontMetrics = g2d.getFontMetrics(font);
                    int stringWidth = fontMetrics.stringWidth("EDEN HAVEN");
                    int stringHeight = fontMetrics.getAscent();
                    int x = (getWidth() - stringWidth) / 2;
                    int y = (getHeight() - stringHeight) / 2 + stringHeight - 10; // Ajusta según tus necesidades

                    
                    // Dibuja el texto principal
                    g2d.drawString("EDEN HAVEN", x, y);
                 // Dibuja la sombra del texto
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("EDEN HAVEN", x-4, y + 2);
                }
            };

            titleLabel.setPreferredSize(new Dimension(700, 100)); // Ajusta el tamaño según tus necesidades
            //titleLabel.setBorder(BorderFactory.createEtchedBorder());
            loginPanel.add(titleLabel, gbc);

            gbc.gridy++;
            
            addLogo(loginPanel, gbc, "ikonoak/logo.png"); 
            gbc.gridy++;
            
            JLabel ize = new JLabel("IZENA:");
            ize.setForeground(Color.white);
            ize.setFont(new Font("Segoe UI", Font.BOLD, 40));
            loginPanel.add(ize, gbc);

            gbc.gridy++;
            //loginPanel.add(izenaField, gbc);
             //ooooo

            addUsernameTextField(loginPanel, gbc);
            //ooooo
            gbc.gridy++;
            JLabel pas = new JLabel("PASAHITZA:");
            pas.setForeground(Color.white);
            pas.setFont(new Font("Segoe UI", Font.BOLD, 40));
            loginPanel.add(pas, gbc);

            gbc.gridy++;
            //loginPanel.add(pasahitzaField, gbc);
            //ooo
            
            addPasswordTextField(loginPanel, gbc);
            //ooo
            //addLoginButton(loginPanel, gbc);
            loginButton = new JButton("HASI SAIOA");
            loginButton.setFont(new Font("Segoe UI", Font.BOLD, 30));
            loginButton.setActionCommand("SAIOAHASI");
            loginButton.addActionListener(kontrolatzailea); 
            loginButton.setBackground(new Color(108, 216, 158));
            loginButton.setForeground(Color.white);
            loginButton.setPreferredSize(new Dimension(230, 60));
            
            gbc.gridy++;
            gbc.gridwidth = 2;
            loginPanel.add(loginButton, gbc);
            
            loginPanel.setBackground(new Color(37, 51, 61));
            loginPANTAILA.getContentPane().add(loginPanel);
            loginPANTAILA.setVisible(true);
	}
	
	private void addLogo(JPanel panel1, GridBagConstraints gbc, String imagePath) 
	{
		int hostelWidth = 300;
        int hostelHeight = 300;

        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);

        JPanel logo = new JPanel(new BorderLayout());
        logo.setPreferredSize(new Dimension(hostelWidth, hostelHeight));
        logo.add(new JLabel(scaledIcon)); 
        logo.setBackground(new Color(37, 51, 61));
        panel1.add(logo, gbc);
	}
	
	private void addUsernameTextField(JPanel panel1, GridBagConstraints gbc) 
	{
		izenaField.setColumns(25);
        izenaField.setBounds(423, 109, 250, 44);
        izenaField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (izenaField.getText().equals("izena")) {
                	izenaField.setText("");
                }
                izenaField.setForeground(Color.white);
                izenaField.setBorderColor(Koloreak.AKUA);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (izenaField.getText().isEmpty()) {
                	izenaField.setText("izena");
                }
                izenaField.setForeground(new Color(103, 112, 120));
                izenaField.setBorderColor(new Color(103, 112, 120));
            }
        });

        panel1.add(izenaField, gbc);
	}
	
	private void addPasswordTextField(JPanel panel1, GridBagConstraints gbc) 
	{
		pasahitzaField.setColumns(25);           
        pasahitzaField.setBounds(423, 168, 250, 44);
        pasahitzaField.addFocusListener(new FocusListener() {
			@Override
            public void focusGained(FocusEvent e) {
            	pasahitzaField.setForeground(Color.white);
                pasahitzaField.setBorderColor(Koloreak.AKUA);
            }

			@Override
            public void focusLost(FocusEvent e) {
            	pasahitzaField.setForeground(new Color(103, 112, 120));
                pasahitzaField.setBorderColor(new Color(103, 112, 120));
            }
        });

        /*passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    //loginEventHandler();
            }
        });*/

        panel1.add(pasahitzaField,gbc);
	}
	
	private void addLoginButton(JPanel panel1, GridBagConstraints gbc) 
	{
        final Color[] loginButtonColors = {new Color(108, 216, 158), Color.white};

        JButton loginButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
            	Graphics2D g2 = (Graphics2D) g;
                g2.addRenderingHints(new HashMap<RenderingHints.Key, Object>() {{
                    put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                }});
                super.paintComponent(g2);

                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(loginButtonColors[0]);
                g2.fillRoundRect(insets.left, insets.top, w, h, 8, 8);

                FontMetrics metrics = g2.getFontMetrics(new Font("Segoe UI", Font.PLAIN, 20));
                int x2 = (getWidth() - metrics.stringWidth("HASI SAIOA")) / 2;
                int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                g2.setColor(loginButtonColors[1]);
                g2.drawString("HASI SAIOA", x2, y2);
            }
        };

        loginButton.addMouseListener(new MouseAdapter() 
        {

            /*@Override
            public void mousePressed(MouseEvent e) {
                loginEventHandler();
            }*/

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButtonColors[0] = new Color(87, 171, 127);
                loginButtonColors[1] = new Color(229, 229, 229);
                loginButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButtonColors[0] = new Color(108, 216, 158);
                loginButtonColors[1] = Color.white;
                loginButton.repaint();
            }
        });
        
        loginButton.setActionCommand("SAIOAHASI");
        loginButton.addActionListener(kontrolatzailea);
        loginButton.setBackground(new Color(37, 51, 61));
        loginButton.setBounds(423, 247, 250, 44);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.add(loginButton, gbc);
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
	        toolBar.setPreferredSize(new Dimension(anchoPantalla, 60));
	        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	        toolBar.setBackground(new Color(184, 184, 184));

	        backButton.setBackground(new Color(255, 255, 255));
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
			if(adminMapa==2)
			{
				adminMapa = 0;
			}
		    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
		    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
		    mainPANTAILA.setUndecorated(true);	    
		    
		    JPanel panel = new JPanel(new BorderLayout());

		    JPanel botonesHuecosPanel = new FondoPanel("xikonoak/fondoa.jpg");
		    botonesHuecosPanel.setLayout(new GridBagLayout());
	        GridBagConstraints gbct = new GridBagConstraints();
	        gbct.insets = new Insets(10, 10, 10, 10);
		    gbct.weightx = 0.1; 
		    gbct.weighty = 1.0;

		    button = new JButton("Hutsunea " + 1);
		    button.setActionCommand("HUECO");
		    button.addActionListener(kontrolatzailea);
		    button.setPreferredSize(new Dimension(300, 700));
		    button.setMinimumSize(new Dimension(300, 700));
		    
		    Color borderColor = Koloreak.AKUA;
	        LineBorder lineBorder = new LineBorder(borderColor, 6);
	        button.setBorder(lineBorder);
		    
		    gbct.gridx = 0;
	        gbct.gridy = 0;
		    botonesHuecosPanel.add(button, gbct);

		    button2 = new JButton("Hueco " + 2);
		    button2.setActionCommand("HUECO2");
		    button2.addActionListener(kontrolatzailea);
		    button2.setPreferredSize(new Dimension(300, 700));
		    button2.setMinimumSize(new Dimension(300, 700));
	        button2.setBorder(lineBorder);
		    gbct.gridx = 1;
		    botonesHuecosPanel.add(button2, gbct);

		    button3 = new JButton("Hueco " + 3);
		    button3.setActionCommand("HUECO3");
		    button3.addActionListener(kontrolatzailea);
		    button3.setPreferredSize(new Dimension(300, 700));
		    button3.setMinimumSize(new Dimension(300, 700));
	        button3.setBorder(lineBorder);
		    gbct.gridx = 2;
		    botonesHuecosPanel.add(button3, gbct);		   
		    
		    button.setBackground(Koloreak.URDINILUNA);
			button2.setBackground(Koloreak.URDINILUNA);
			button3.setBackground(Koloreak.URDINILUNA);
			/*button.setOpaque(false);
			button2.setOpaque(false);
			button3.setOpaque(false);*/
		    		    
		    /*button.setBackground(new Color(191, 132, 203));
			button2.setBackground(new Color(191, 132, 203));
			button3.setBackground(new Color(191, 132, 203));*/
			
		    //JMenuBar menuBar = new JMenuBar();
		    //menuBar.setBackground(new Color(184, 184, 184));
		    
		    mainPANTAILA.setJMenuBar(crearMenuBar());        
	        
	        toolBar = new JToolBar();	        
	        toolBar.setPreferredSize(new Dimension(anchoPantalla, 150));
	        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	        //toolBar.setBackground(new Color(184, 184, 184));
	        toolBar.setBackground(new Color(37, 51, 61));
	        	
	        deleteButton.setBackground(Koloreak.URDINILUNA);
	        editButton.setBackground(Koloreak.URDINILUNA);
	        backButton.setBackground(Koloreak.URDINILUNA);
	        /*deleteButton.setBackground(new Color(255, 255, 255));
	        editButton.setBackground(new Color(255, 255, 255));
	        backButton.setBackground(new Color(255, 255, 255));*/
	    	confirmButton.setBackground(new Color(120, 120, 120));
	    	
	    	putzuBOTOIA.setVisible(false);
	    	putzuBOTOIA2.setVisible(false);
	    	putzuBOTOIA3.setVisible(false);
	    	haizegailuBOTOIA.setVisible(false);
	    	haizegailuBOTOIA2.setVisible(false);
	    	haizegailuBOTOIA3.setVisible(false);
	    	berogailuBOTOIA.setVisible(false);
	    	berogailuBOTOIA2.setVisible(false);
	    	berogailuBOTOIA3.setVisible(false);
	        
	        /*erabiltzaileIzena = new JLabel(izena.toUpperCase());  
	        erabiltzaileIzena.setForeground(Color.white);
			erabiltzaileIzena.setFont(new Font("SansSerif", Font.BOLD, 30));*/
	        
			toolBar.add(irtenButton);
			toolBar.add(Box.createHorizontalStrut(spaceWidth2));
	        toolBar.add(backButton);
	        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
	        toolBar.add(editButton);
	        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
	        toolBar.add(deleteButton);
	        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
	        toolBar.add(confirmButton);	       
	        
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));	        	        
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));

	        ImageIcon icon = new ImageIcon("ikonoak/" + izena + "BANDERA.png");
	        Image scaledImage = icon.getImage().getScaledInstance(130, 70, Image.SCALE_SMOOTH);
	        ImageIcon scaledIcon = new ImageIcon(scaledImage);
	        JLabel worldMapLabel2 = new JLabel(scaledIcon); 
	        toolBar.add(worldMapLabel2);
	        
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        
	        //rrrrr
	        JLabel titleLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;

                    // Define el degradado de colores
                    Color color1 = new Color(  235, 166, 27  ); // Verde oscuro
                    Color color2 = new Color( 15, 180, 150 ); // Verde claro
                    GradientPaint gradient = new GradientPaint(0, 0, color2, getWidth(), getHeight(), color2);
                    g2d.setPaint(gradient);

                    // Establece la fuente y tamaño
                    Font font = new Font("Arial", Font.BOLD, 60); // Ajusta el tamaño según tus necesidades
                    g2d.setFont(font);

                    FontMetrics fontMetrics = g2d.getFontMetrics(font);
                    int stringWidth = fontMetrics.stringWidth(izena.toUpperCase());
                    int stringHeight = fontMetrics.getAscent();
                    int x = (getWidth() - stringWidth) / 2;
                    int y = (getHeight() - stringHeight) / 2 + stringHeight - 10; // Ajusta según tus necesidades

                    
                    // Dibuja el texto principal
                    g2d.drawString(izena.toUpperCase(), x, y);
                 // Dibuja la sombra del texto
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(izena.toUpperCase(), x-4, y + 2);
                }
            };
	        //rrrrr
	        
	        //toolBar.add(erabiltzaileIzena);
            titleLabel.setPreferredSize(new Dimension(300, 100));
            toolBar.add(titleLabel);
	        
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        
	        
	        toolBar.add(putzuBOTOIA);	        
	        toolBar.add(putzuBOTOIA2);
	        toolBar.add(putzuBOTOIA3);

	        /*toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        
	        toolBar.add(haizegailuBOTOIA);
	        toolBar.add(haizegailuBOTOIA2);
	        toolBar.add(haizegailuBOTOIA3);

	        toolBar.add(Box.createHorizontalStrut(spaceWidth));
	        
	        toolBar.add(berogailuBOTOIA);
	        toolBar.add(berogailuBOTOIA2);
	        toolBar.add(berogailuBOTOIA3);*/
	        
	        botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
	        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
	        
		    mainPANTAILA.getContentPane().add(panel);
		    mainPANTAILA.setVisible(true);
		}
	
	class FondoPanel extends JPanel {
	    private Image backgroundImage;

	    public FondoPanel(String imagePath) {
	        backgroundImage = new ImageIcon(imagePath).getImage();
	        //Dimension screenSize = getToolkit().getScreenSize();
	        //Dimension size = new Dimension(screenSize.width, screenSize.height);
	        //setPreferredSize(size);
	        //setMinimumSize(size);
	        //setMaximumSize(size);
	        //setSize(size);
	        setLayout(null);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	    }
	}
	
	public void mostrarPantallaEditarHuecos(Erabiltzailea erabiltzailea)
	{
		if(adminMapa==2)
		{
			adminMapa = 0;
		}
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
	    	    
	    button.setBackground(new Color(191, 132, 203));
		button2.setBackground(new Color(191, 132, 203));
		button3.setBackground(new Color(191, 132, 203));
	    
		JMenuBar menuBar = new JMenuBar();
	    menuBar.setBackground(new Color(184, 184, 184));
	    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
        
        toolBar = new JToolBar();
        toolBar.setPreferredSize(new Dimension(anchoPantalla, 150));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBar.setBackground(new Color(184, 184, 184));
        	        	        
        deleteButton.setBackground(new Color(255, 255, 255));
        editButton.setBackground(new Color(255, 255, 255));
        backButton.setBackground(new Color(255, 255, 255));
    	confirmButton.setBackground(new Color(120, 120, 120));
    	
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
        
		toolBar.add(irtenButton);
		toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(editButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(deleteButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(confirmButton);	       
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));	        	        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));

        ImageIcon icon = new ImageIcon("ikonoak/" + izena + "BANDERA.png");
        Image scaledImage = icon.getImage().getScaledInstance(130, 70, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel worldMapLabel2 = new JLabel(scaledIcon); 
        toolBar.add(worldMapLabel2);
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        
        toolBar.add(erabiltzaileIzena);
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        
        
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
       
	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
        
	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	    
	    if(balixu==3)
	    {
	    	String mensaje = "3 ELEMENTU GEHITU AHAL DITUZU GEHIENEZ!";
	        JOptionPane.showMessageDialog(null, mensaje);    
	    }
	}
	
	//ERABILTZAILEAREN DATUEKIN PANTAILA ERAKUTZI
	public void mostrarPantallaHuecosCompletado(Erabiltzailea erabiltzailea) 
	{
		if(adminMapa==2)
		{
			adminMapa = 0;
		}
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
	    
	    button.setBackground(new Color(191, 132, 203));
		button2.setBackground(new Color(191, 132, 203));
		button3.setBackground(new Color(191, 132, 203));
	    
	    JMenuBar menuBar = new JMenuBar();
	    menuBar.setBackground(new Color(184, 184, 184));
	    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
        
        toolBar = new JToolBar();
        toolBar.setPreferredSize(new Dimension(anchoPantalla, 150));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBar.setBackground(new Color(184, 184, 184));
        	        	        
        deleteButton.setBackground(new Color(255, 255, 255));
        editButton.setBackground(new Color(255, 255, 255));
        backButton.setBackground(new Color(255, 255, 255));
    	confirmButton.setBackground(new Color(120, 120, 120));
    	
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
        
		toolBar.add(irtenButton);
		toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(editButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(deleteButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(confirmButton);	       
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));	        	        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));

        ImageIcon icon = new ImageIcon("ikonoak/" + izena + "BANDERA.png");
        Image scaledImage = icon.getImage().getScaledInstance(130, 70, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel worldMapLabel2 = new JLabel(scaledIcon); 
        toolBar.add(worldMapLabel2);
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        
        toolBar.add(erabiltzaileIzena);
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));       
        
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
       
	    panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
        
	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarPantallaWait(Erabiltzailea erabiltzailea) 
	{
	    mainPANTAILA2 = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    mainPANTAILA2.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    mainPANTAILA2.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());
	    JButton buton = new JButton(); //EZKERREKO BOTOIA
	    buton.setFont(new Font("SansSerif", Font.BOLD, 100));
		buton.setBackground(new Color(191, 132, 203));
	    
		JButton buton2 = new JButton(); //ERDIKO BOTOIA
		buton2.setFont(new Font("SansSerif", Font.BOLD, 100));
		buton2.setBackground(new Color(191, 132, 203));
		
		JButton buton3 = new JButton(); //ESKUINEKO BOTOIA
		buton3.setFont(new Font("SansSerif", Font.BOLD, 100));
		buton3.setBackground(new Color(191, 132, 203));

	    JPanel botonesHuecosPanel = new JPanel(new GridLayout(1, 3));

	    for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) 
	    {
	        String balio = erabiltzailea.getBalioZerrenda().get(i);
	        
	        if (balio != null) 
	        {
	        	if(i==0)
	        	{
		    	    botonesHuecosPanel.add(buton);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton);
	        	}
	        	else if(i==1)
	        	{
		    	    botonesHuecosPanel.add(buton2);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton2);
	        	}

	        	else if(i==2)
	        	{
		    	    botonesHuecosPanel.add(buton3);
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton3);
	        	}
	        }
	    }	    
	    
	    JMenuBar menuBar = new JMenuBar();
	    menuBar.setBackground(new Color(184, 184, 184));
	    
	    mainPANTAILA2.setJMenuBar(crearMenuBar());        
        
        toolBar = new JToolBar();
        toolBar.setPreferredSize(new Dimension(anchoPantalla, 150));
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        toolBar.setBackground(new Color(184, 184, 184));
        	        	        
        deleteButton.setBackground(new Color(255, 255, 255));
        editButton.setBackground(new Color(255, 255, 255));
        backButton.setBackground(new Color(255, 255, 255));
    	confirmButton.setBackground(new Color(120, 120, 120));
    	
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
        
		toolBar.add(irtenButton);
		toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(backButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(editButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(deleteButton);
        toolBar.add(Box.createHorizontalStrut(spaceWidth2));
        toolBar.add(confirmButton);	       
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));	        	        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        toolBar.add(Box.createHorizontalStrut(spaceWidth));

        ImageIcon icon = new ImageIcon("ikonoak/" + izena + "BANDERA.png");
        Image scaledImage = icon.getImage().getScaledInstance(130, 70, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel worldMapLabel2 = new JLabel(scaledIcon); 
        toolBar.add(worldMapLabel2);
        
        toolBar.add(Box.createHorizontalStrut(spaceWidth));
        
        toolBar.add(erabiltzaileIzena);        
       
        panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA2.add(toolBar, BorderLayout.NORTH);

	    mainPANTAILA2.getContentPane().add(panel);
	    mainPANTAILA2.setVisible(true);
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
	
	public void mostrarOpciones(String hizkuntzAukera)
	{
		/*if (hizkuntza2 == 1) { aukerenPANTAILA = new JFrame("AUKERAK"); }
		else if (hizkuntza2 == 2) { aukerenPANTAILA = new JFrame("OPCIONES"); }
		else { aukerenPANTAILA = new JFrame("OPTIONS"); }	*/
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntzAukera);
		aukerenPANTAILA = new JFrame(hizkuntza1.aukerak);
        aukerenPANTAILA.setSize(300, 150);
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(4, 1));

        JButton pozoButton;
        /*if (hizkuntza2 == 1) { pozoButton = new JButton("PUTZUA"); }
		else if (hizkuntza2 == 2) { pozoButton = new JButton("POZO"); }
		else { pozoButton = new JButton("WELL"); }	*/
        pozoButton = new JButton(hizkuntza1.putzua);
        pozoButton.setActionCommand("PUTZUA");
        pozoButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(pozoButton);

        JButton ventiladorButton;
        /*if (hizkuntza2 == 1) { ventiladorButton = new JButton("HAIZEGAILUA"); }
		else if (hizkuntza2 == 2) { ventiladorButton = new JButton("VENTILADOR"); }
		else { ventiladorButton = new JButton("FAN"); }*/
        ventiladorButton = new JButton(hizkuntza1.haizegailua);
        ventiladorButton.setActionCommand("HAIZEGAILUA");
        ventiladorButton.addActionListener(kontrolatzailea);
        opcionesPanel.add(ventiladorButton);

        JButton fugaButton;
        /*if (hizkuntza2 == 1) { fugaButton = new JButton("BEROGAILUA"); }
		else if (hizkuntza2 == 2) { fugaButton = new JButton("ESTUFA"); }
		else { fugaButton = new JButton("STOVE"); }*/
        fugaButton = new JButton(hizkuntza1.berogailua);
        fugaButton.setActionCommand("BEROGAILUA");
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
	            	/*JLabel lineaSuperior = new JLabel(argazkia.toUpperCase());
	            	button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS));
	            	lineaSuperior.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	            	lineaSuperior.setFont(new Font("Arial", Font.BOLD, 50));
	                lineaSuperior.setBorder(new EmptyBorder(espacioSuperior, 0, 0, 0));
	            	button.add(lineaSuperior);*/
	            	
	                irudiaJarri(argazkia, button);
	                posizioa=0;
	                break;

	            case 2:
	            	JLabel lineaSuperior = new JLabel("ON");
	                button2.setLayout(new BoxLayout(button2, BoxLayout.Y_AXIS));
	                lineaSuperior.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	                lineaSuperior.setForeground(Color.white);
	                lineaSuperior.setFont(new Font("Arial", Font.BOLD, 50));
	                lineaSuperior.setBorder(new EmptyBorder(50, 0, 0, 0));
	                button2.add(lineaSuperior);

	                JPanel logoPanel = new JPanel();
	                logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));  // Crea un nuevo BoxLayout para logoPanel
	                logoPanel.setOpaque(false);  // Hace que el panel sea transparente

	                JLabel lineaSuperior2 = new JLabel();
	                lineaSuperior2.setText("Texto aquí");
	                //int hostelWidth = 200;
	                //int hostelHeight = 200;
	                //ImageIcon icon = new ImageIcon("ikonoak/tm.png");
	                //Image image = icon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH);
	                //ImageIcon scaledIcon = new ImageIcon(image);
	                //lineaSuperior2.setIcon(scaledIcon);
	                lineaSuperior2.setAlignmentX(JLabel.CENTER_ALIGNMENT);

	                //JPanel logo = new JPanel(new BorderLayout());
	                //logo.setPreferredSize(new Dimension(hostelWidth, hostelHeight));
	                //logo.add(new JLabel(scaledIcon));
	                //lineaSuperior2.add(logo);
	                lineaSuperior2.setBorder(new EmptyBorder(380, 50, 0, 0));
	                logoPanel.add(lineaSuperior2);

	                button2.add(logoPanel);  // Agrega logoPanel en lugar de lineaSuperior2 directamente

	                irudiaJarri(argazkia, button2);
	                posizioa = 1;
	                break;

	            case 3:
	            	/*JLabel lineaSuperior3 = new JLabel(argazkia.toUpperCase());
	            	button3.setLayout(new BoxLayout(button3, BoxLayout.Y_AXIS));
	            	lineaSuperior3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	            	lineaSuperior3.setFont(new Font("Arial", Font.BOLD, 50));
	                lineaSuperior3.setBorder(new EmptyBorder(espacioSuperior, 0, 0, 0));
	            	button3.add(lineaSuperior3);*/
	            	
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
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "putzua");
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break;

	            case Modeloa.PROPIETATEA2:
	                argazkiaSartu(Modeloa.PROPIETATEA2, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "haizegailua");
	                System.out.println(izena + " balioa " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1;  
	                break; 

	            case Modeloa.PROPIETATEA3:
	                argazkiaSartu(Modeloa.PROPIETATEA3, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "berogailua");
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
	                
	            case Modeloa.PROPIETATEA8:
	            	System.out.println("Recibida actualización de PROPIETATEA8");
	            	confirmButton.setVisible(false);
	                actualizarPantalla2();
	                confirmButton.setVisible(true);
	                break;
	            case Modeloa.PROPIETATEA9:
	            	//modeloa.timerEliminar3.stop();
	            	System.out.println("Recibida actualización de PROPIETATEA9");
	            	loginPANTAILA.dispose();
	                break; 
	            case Modeloa.PROPIETATEA10:
	            	modeloa.timer4.setRepeats(false);
	            	modeloa.timer4.start();
	        		mainPANTAILA.dispose();  
	        		if(zer=="gehitu")
	        		{
	        			mostrarPantallaEditarHuecos(erabiltzailea);
	        		}
	        		else if(zer=="konfirmatu") 
	        		{
	        			mostrarPantallaHuecosCompletado(erabiltzailea);
	        		}
	                break;
	            case Modeloa.PROPIETATEA11:
	            	mainPANTAILA2.dispose();
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