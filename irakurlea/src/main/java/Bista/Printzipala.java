package Bista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.beans.*;

import org.eclipse.paho.client.mqttv3.MqttException;

import Modeloa.Erabiltzailea;
import Modeloa.Hizkuntza;
import Modeloa.Modeloa;
import irakurlea.Mqtt;
import Kontrolatzailea.Kontrolatzailea;
import Kontrolatzailea.NireAkzioa;

public class Printzipala implements PropertyChangeListener
{
	//MODELOA
	private Modeloa modeloa; 
	
	//KONTROLATZAILEA
	private Kontrolatzailea kontrolatzailea; 
	
	//PANTAILAK
	private JFrame ordenagailuarenPANTAILA; 
	public JFrame mainPANTAILA;
	public JFrame mainPANTAILA2;
	public JFrame adminFrame;
	public JFrame loginPANTAILA;
	public JFrame loginPANTAILA2;
	private JFrame aukerenPANTAILA;
		
	//JFIELD
	public JTextField izenaField;
	public JPasswordField pasahitzaField;
	
	//JLABEL
	public JLabel argeliaBABESETXE;
	public JLabel sueciaBABESETXE;
	public JLabel libiaBABESETXE;
	JLabel erabiltzaileIzena;
	
	boolean verde = true;
	
	//BOTOIAK
	public JButton button; //EZKERREKO BOTOIA
	public JButton button2; //ERDIKO BOTOIA
	public JButton button3; //ESKUINEKO BOTOIA
	public JButton backButton; //ATZERA JOATEKO BOTOIA
	public JButton editButton;
	public JButton confirmButton;
	private JButton irtenButton;
	private JButton adminEspacio;
	public JButton deleteButton;
	public JButton loginButton;
	private JButton putzuBOTOIA;
	private JButton putzuBOTOIA2;
	private JButton putzuBOTOIA3;
    
    //ERABILTZAILEA
    public Erabiltzailea erabiltzailea;
    
    //JTOOLBAR
    private JToolBar toolBar;
    
    //STRING
    public String izena;
    public String zer, zer2;
    public String pant;
    
    /*MAKINA VIRTUALAREN BROKER: 172.20.10.4 / ORDENAGAILUKO BROKER: localhost*/
    private static final String BROKER = "tcp://172.20.10.4:1883";
    private static final String CLENT_ID = "TemperatureSimulator";
    
    //INT
    private int posizioa=0;
    public int adminMapa = 0;
    private static int spaceWidth = 50;
    private static int spaceWidth2 = 15;
    private int anchoPantalla = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int altoPantalla = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    public String hizkuntza = "euskera"; 
    public int maxPantaila = 0;
    
    //MQTT
    private Mqtt mqtt;
    
    NireAkzioa euskera, euskeraLogin, castellano, castellanoLogin, ingles, inglesLogin, hasierakoTamaina, minimizatu, maximizatu, irten;
           	
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea, Mqtt mqtt) //PRINTZIPALAREN KONSTRUKTOREA
	{
		this.modeloa = modeloa; //MODELOA
		this.kontrolatzailea = kontrolatzailea; //KONTROLATZAILEA
		this.mqtt = mqtt;
		this.modeloa.addPropertyChangeListener(this); //PROPERTY CHANGE
						
		modeloa.erabiltzaileakIrakurri(); //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
		
		modeloa.hizkuntzakIrakurri();
		
		sortuAkzioak();
		
		toolbarBotoienKonfigurazioa();		
		
		kontrolatzailea.setIrudia(this);
        modeloa.setIrudia(this);
	}
	
	public void sortuAkzioak() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		euskera = new NireAkzioa ("Euskera",new ImageIcon("ikonoak/eus.png"),"euskera",new Integer(KeyEvent.VK_E), this, modeloa);
		castellano = new NireAkzioa ("Castellano",new ImageIcon("ikonoak/esp.png"),"castellano",new Integer(KeyEvent.VK_E), this, modeloa);
		ingles = new NireAkzioa ("Ingles",new ImageIcon("ikonoak/ing.png"),"ingles",new Integer(KeyEvent.VK_E), this, modeloa);
		
		euskeraLogin = new NireAkzioa ("Euskera ",new ImageIcon("ikonoak/eus.png"),"euskera",new Integer(KeyEvent.VK_E), this, modeloa);
		castellanoLogin = new NireAkzioa ("Castellano ",new ImageIcon("ikonoak/esp.png"),"castellano",new Integer(KeyEvent.VK_E), this, modeloa);
		inglesLogin = new NireAkzioa ("Ingles ",new ImageIcon("ikonoak/ing.png"),"ingles",new Integer(KeyEvent.VK_E), this, modeloa);
		
		minimizatu = new NireAkzioa (hizkuntza1.minimizatu,new ImageIcon("ikonoak/min.png"),hizkuntza1.minimizatu, null, this, modeloa);
		maximizatu = new NireAkzioa (hizkuntza1.maximizatu,new ImageIcon("ikonoak/max.png"),hizkuntza1.maximizatu, null, this, modeloa);
		hasierakoTamaina = new NireAkzioa (hizkuntza1.hasierakoTamaina,new ImageIcon("ikonoak/max.png"),hizkuntza1.hasierakoTamaina, null, this, modeloa);
		irten = new NireAkzioa (hizkuntza1.irten,new ImageIcon("ikonoak/exit.png"),hizkuntza1.irten, new Integer(KeyEvent.VK_S), this, modeloa);
	}
	
	private JMenuBar crearMenuBar() 
	{		
		JMenuBar barra = new JMenuBar();
		barra.add (sortuMenuHizkuntzak());
		barra.add(Box.createHorizontalGlue());
		barra.add (crearMenuMinimizatu());
		barra.add (crearMenuMaximizatu());
		barra.add(crearMenuMHasierakoTamaina());
		barra.add (crearMenuSalir());
		
		return barra;
	}
	
	private JMenuBar crearMenuBarLogin() 
	{		
		JMenuBar barra = new JMenuBar();
		barra.add (sortuMenuHizkuntzakLogin());
		barra.add(Box.createHorizontalGlue());
		barra.add (crearMenuMinimizatu());
		barra.add (crearMenuMaximizatu());
		barra.add(crearMenuMHasierakoTamaina());
		barra.add (crearMenuSalir());
		
		return barra;
	}
	
	private JMenu sortuMenuHizkuntzak() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuHizkuntzak = new JMenu (hizkuntza1.hizkuntzak);
		menuHizkuntzak.setIcon(new ImageIcon("ikonoak/hizkun.png"));
		menuHizkuntzak.setMnemonic(new Integer(KeyEvent.VK_H));
		menuHizkuntzak.add(euskera);
		menuHizkuntzak.add(castellano);
		menuHizkuntzak.add(ingles);
		
		return menuHizkuntzak;
	}
	
	private JMenu sortuMenuHizkuntzakLogin() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuHizkuntzak = new JMenu (hizkuntza1.hizkuntzak);
		menuHizkuntzak.setIcon(new ImageIcon("ikonoak/hizkun.png"));
		menuHizkuntzak.setMnemonic(new Integer(KeyEvent.VK_H));
		menuHizkuntzak.add(euskeraLogin);
		menuHizkuntzak.add(castellanoLogin);
		menuHizkuntzak.add(inglesLogin);
		
		return menuHizkuntzak;
	}
	
	private JMenu crearMenuMinimizatu() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuMinimizatu = new JMenu (hizkuntza1.minimizatu);
		menuMinimizatu.setIcon(new ImageIcon("ikonoak/min.png"));
		menuMinimizatu.add(minimizatu);
		
		return menuMinimizatu;
	}
	
	private JMenu crearMenuMaximizatu() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuMaximizatu = new JMenu (hizkuntza1.maximizatu);
		menuMaximizatu.setIcon(new ImageIcon("ikonoak/max.png"));
		menuMaximizatu.add(maximizatu);
		
		return menuMaximizatu;
	}
	
	private JMenu crearMenuMHasierakoTamaina() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuMaximizatu = new JMenu (hizkuntza1.hasierakoTamaina);
		menuMaximizatu.setIcon(new ImageIcon("ikonoak/max.png"));
		menuMaximizatu.add(hasierakoTamaina);
		
		return menuMaximizatu;
	}
	
	private JMenu crearMenuSalir() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		JMenu menuSalir = new JMenu (hizkuntza1.irten);
		menuSalir.setIcon(new ImageIcon("ikonoak/exit.png"));
		menuSalir.setMnemonic(new Integer(KeyEvent.VK_S));
		menuSalir.add(irten);
		
		return menuSalir;
	}
	
	public void toolbarBotoienKonfigurazioa()
	{	
        LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3);
        
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		backButton = new JButton(hizkuntza1.backButtom);
        backButton.setActionCommand("ATRAS");
	    backButton.addActionListener(kontrolatzailea);
	    backButton.setPreferredSize(new Dimension(130, 60));
	    backButton.setBorder(lineBorder);
	    backButton.setForeground(Color.white);
	    
	    editButton = new JButton(hizkuntza1.editButtom);
        editButton.setActionCommand("GEHITU");
	    editButton.addActionListener(kontrolatzailea);
	    editButton.setPreferredSize(new Dimension(130, 60));
	    editButton.setBorder(lineBorder);
	    editButton.setForeground(Color.white);
	    
	    deleteButton = new JButton(hizkuntza1.deleteButtom);
        deleteButton.setActionCommand("EZABATU");
	    deleteButton.addActionListener(kontrolatzailea);
	    deleteButton.setPreferredSize(new Dimension(130, 60));
	    deleteButton.setBorder(lineBorder);
	    deleteButton.setForeground(Color.white);

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
        
        irtenButton = new JButton(" ");
        irtenButton.setBorderPainted(false);
        irtenButton.setPreferredSize(new Dimension(1, 150)); 
        
        adminEspacio = new JButton(" ");
        adminEspacio.setBorderPainted(false);
        adminEspacio.setPreferredSize(new Dimension(1, 65)); 
	}
	
	private static String extraerValoresNumericos(String input) 
	{
        String result = input.replaceAll("[^0-9.]", "");

        return result;
    }
	
	private void actualizarPantalla() 
	{
	    if (mqtt != null) 
	    {
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
	    } 
	    else 
	    {
	        System.out.println("MQTT es null");
	    }
	}
	
	private void actualizarPozo(int index) 
	{
		String adc1ZenbakiGabe = extraerValoresNumericos(mqtt.adc1);
		String adc2ZenbakiGabe = extraerValoresNumericos(mqtt.adc2);
		String adc3ZenbakiGabe = extraerValoresNumericos(mqtt.adc3);
		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = Integer.valueOf(adc1ZenbakiGabe);
		adc1 = adc1*330/4095;
		
		adc2 = Integer.valueOf(adc2ZenbakiGabe);
		adc2 = adc2*330/4095;
		
		adc3 = Integer.valueOf(adc3ZenbakiGabe);
		adc3 = adc3*330/4095;
		
	    switch (index) 
	    {
	        case 1:
	            button.setText(String.valueOf(adc1 + " L"));	
	            button.setForeground(Color.white);
	            button.setFont(LetraMotak.MQTT);
	            if(Integer.valueOf(adc1) <= 33)
	            {
	            	putzuBOTOIA.setBackground(Color.RED);
	            }
	            else if(33 <= Integer.valueOf(adc1) && Integer.valueOf(adc1) <= 66)
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
	            button2.setText(String.valueOf(adc2 + " L"));
	            button2.setForeground(Color.white);
	            button2.setFont(LetraMotak.MQTT);
	            if(Integer.valueOf(adc2) <= 33)
	            {
	            	putzuBOTOIA2.setBackground(Color.RED);
	            }
	            else if(33 <= Integer.valueOf(adc2) && Integer.valueOf(adc2) <= 66)
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
	            button3.setText(String.valueOf(adc3 + " L"));
	            button3.setForeground(Color.white);
	            button3.setFont(LetraMotak.MQTT);
	            if(Integer.valueOf(adc3) <= 33)
	            {
	            	putzuBOTOIA3.setBackground(Color.RED);
	            }
	            else if(33 <= Integer.valueOf(adc3) && Integer.valueOf(adc3) <= 66)
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

	private void actualizarVentilador(int index) 
	{
		String adc1ZenbakiGabe = extraerValoresNumericos(mqtt.adc1);
		String adc2ZenbakiGabe = extraerValoresNumericos(mqtt.adc2);
		String adc3ZenbakiGabe = extraerValoresNumericos(mqtt.adc3);
		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = Integer.valueOf(adc1ZenbakiGabe);
		adc1 = adc1*330/4095;
		
		adc2 = Integer.valueOf(adc2ZenbakiGabe);
		adc2 = adc2*330/4095;
		
		adc3 = Integer.valueOf(adc3ZenbakiGabe);
		adc3 = adc3*330/4095;
		
	    switch (index) 
	    {
	        case 1:
	            button.setText(String.valueOf(adc1 + " ºC"));
	        	button.setForeground(Color.white);
	            button.setFont(LetraMotak.MQTT);
	            break;
	        case 2:
	            button2.setText(String.valueOf(adc2 + " ºC"));
	            button2.setForeground(Color.white);
	            button2.setFont(LetraMotak.MQTT);
	            break;
	        case 3:
	            button3.setText(String.valueOf(adc3 + " ºC"));
	            button3.setForeground(Color.white);
	            button3.setFont(LetraMotak.MQTT);
	            break;
	        default:
	            break;
	    }
	}
	
	private void actualizarEstufa(int index) 
	{
		String adc1ZenbakiGabe = extraerValoresNumericos(mqtt.adc1);
		String adc2ZenbakiGabe = extraerValoresNumericos(mqtt.adc2);
		String adc3ZenbakiGabe = extraerValoresNumericos(mqtt.adc3);
		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = Integer.valueOf(adc1ZenbakiGabe);
		adc1 = adc1*330/4095;
		
		adc2 = Integer.valueOf(adc2ZenbakiGabe);
		adc2 = adc2*330/4095;
		
		adc3 = Integer.valueOf(adc3ZenbakiGabe);
		adc3 = adc3*330/4095;
		
	    switch (index) 
	    {
	        case 1:
	            button.setText(String.valueOf(adc1));
	            button.setForeground(Color.white);
	            button.setFont(LetraMotak.MQTT);
	            break;
	        case 2:
	            button2.setText(String.valueOf(adc2));
	            button2.setForeground(Color.white);
	            button2.setFont(LetraMotak.MQTT);
	            break;
	        case 3:
	            button3.setText(String.valueOf(adc3));
	            button3.setForeground(Color.white);
	            button3.setFont(LetraMotak.MQTT);
	            break;
	        default:
	            break;
	    }
	}
	
	private void actualizarPantalla2()
	{
		if (verde) 
		{
            confirmButton.setBackground(Koloreak.URDINILUNA);
        } 
		else 
		{
			confirmButton.setBackground(Color.GREEN);
        }
        verde = !verde;
	}	
	
	public void loginPantailaErakutzi() 
	{
			pant = "login";
            loginPANTAILA = new JFrame("SAIOA HASI");
            loginPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //loginPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
            if(maxPantaila==1)
    		{
            	loginPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
    		}
    		else 
    		{
    			loginPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
    		    loginPANTAILA.setLocationRelativeTo(null);
    		}	
            loginPANTAILA.setUndecorated(true);
            
            //kontrolatzailea.setIrudia(this);
            //modeloa.setIrudia(this);
            
            JPanel loginPanel = new JPanel(new GridBagLayout());
            
            Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);

            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);

            loginPANTAILA.setLayout(new BorderLayout());
            loginPANTAILA.add(loginPanel, BorderLayout.NORTH);

            //IZENA
            izenaField = new JTextField(20);
            izenaField.addKeyListener(kontrolatzailea);
            
            //PASAHITZA
            pasahitzaField = new JPasswordField(20);
            pasahitzaField.addKeyListener(kontrolatzailea);
            
            loginPANTAILA.setJMenuBar(crearMenuBarLogin()); 

            add3DTituluGridBag(loginPanel, gbc, "EDEN HAVEN");           

            gbc.gridy++;
            
            addLogo(loginPanel, gbc, "ikonoak/logo.png"); 
            
            gbc.gridy++;
            
            JLabel ize = new JLabel(hizkuntza1.izena + ":");
            ize.setForeground(Color.white);
            ize.setFont(LetraMotak.LOGINLETRAK);
            loginPanel.add(ize, gbc);

            gbc.gridy++;            

            addUsernameTextField(loginPanel, gbc);
            
            gbc.gridy++;
            JLabel pas = new JLabel(hizkuntza1.pasahitza + ":");
            pas.setForeground(Color.white);
            pas.setFont(LetraMotak.LOGINLETRAK);
            loginPanel.add(pas, gbc);

            gbc.gridy++;
            
            addPasswordTextField(loginPanel, gbc);

            loginButton = new JButton(hizkuntza1.hasiSaioa);
            loginButton.setFont(LetraMotak.HASISAIOA);
            loginButton.setActionCommand("SAIOAHASI");
            loginButton.addActionListener(kontrolatzailea); 
            loginButton.setBackground(Koloreak.BERDEARGIA);
            loginButton.setForeground(Color.white);
            loginButton.setPreferredSize(new Dimension(230, 60));
            
            gbc.gridy++;
            gbc.gridwidth = 2;
            
            loginPanel.add(loginButton, gbc);            
            loginPanel.setBackground(Koloreak.URDINILUNA);
            
            loginPANTAILA.getContentPane().add(loginPanel);
            loginPANTAILA.setVisible(true);
	}
	
	private void add3DTituluGridBag(JPanel panel1, GridBagConstraints gbc, String titulu)
	{
		@SuppressWarnings("serial")
		JLabel titleLabel = new JLabel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define el degradado de colores
                Color color1 = Koloreak.NARANJA; // Verde oscuro
                Color color2 = Koloreak.URDINARGIA; // Verde claro
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);

                // Establece la fuente y tamaño
                Font font = LetraMotak.LOGINTITULUA; // Ajusta el tamaño según tus necesidades
                g2d.setFont(font);

                FontMetrics fontMetrics = g2d.getFontMetrics(font);
                int stringWidth = fontMetrics.stringWidth(titulu);
                int stringHeight = fontMetrics.getAscent();
                int x = (getWidth() - stringWidth) / 2;
                int y = (getHeight() - stringHeight) / 2 + stringHeight - 10; // Ajusta según tus necesidades

                
                // Dibuja el texto principal
                g2d.drawString(titulu, x, y);
             // Dibuja la sombra del texto
                g2d.setColor(Color.BLACK);
                g2d.drawString(titulu, x-4, y + 2);
            }
        };
        titleLabel.setPreferredSize(new Dimension(700, 100));
        panel1.add(titleLabel, gbc);
	}
		
	private void add3DTituluToolbar()
	{
		@SuppressWarnings("serial")
		JLabel titleLabel = new JLabel() 
		{
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Define el degradado de colores
                Color color2 = Koloreak.URDINARGIA; // Verde claro
                GradientPaint gradient = new GradientPaint(0, 0, color2, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);

                // Establece la fuente y tamaño
                Font font = LetraMotak.ERABILTZAILETITULUA; // Ajusta el tamaño según tus necesidades
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
        
    titleLabel.setPreferredSize(new Dimension(300, 100));
    toolBar.add(titleLabel);
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
        logo.setBackground(Koloreak.URDINILUNA);
        panel1.add(logo, gbc);
	}
	
	private void addUsernameTextField(JPanel panel1, GridBagConstraints gbc) 
	{
		izenaField.setColumns(25);
        izenaField.setBounds(423, 109, 250, 44);
        izenaField.setMinimumSize(new Dimension(250,44));
        izenaField.setOpaque(false);
        izenaField.setBackground(Koloreak.URDINILUNA);
        izenaField.setForeground(Color.white);
        izenaField.setMargin(new Insets(2, 10, 2, 2));
        izenaField.setHorizontalAlignment(SwingConstants.LEFT);
        izenaField.setFont(LetraMotak.IZENAetaPASAHITZA);
        izenaField.addFocusListener(kontrolatzailea);

        panel1.add(izenaField, gbc);
	}
	
	private void addPasswordTextField(JPanel panel1, GridBagConstraints gbc) 
	{
	    pasahitzaField.setColumns(25);
	    pasahitzaField.setBounds(423, 168, 250, 44);
	    pasahitzaField.setMinimumSize(new Dimension(250,44));
	    Color borderColor = Koloreak.GRISARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 4);
	    pasahitzaField.setBorder(lineBorder);

	    pasahitzaField.setBackground(Koloreak.URDINILUNA);
	    pasahitzaField.setForeground(Color.white);
	    pasahitzaField.setFont(LetraMotak.IZENAetaPASAHITZA);
	    pasahitzaField.addFocusListener(kontrolatzailea);

	    panel1.add(pasahitzaField, gbc);
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

            ImageIcon argeliaIcon = new ImageIcon("ikonoak/babesetxe.png");
            argeliaIcon = new ImageIcon(argeliaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            argeliaBABESETXE = new JLabel(argeliaIcon);
            argeliaBABESETXE.setBounds(900, 400, hostelWidth, hostelHeight);
            layeredPane.add(argeliaBABESETXE, JLayeredPane.PALETTE_LAYER);

            ImageIcon sueciaIcon = new ImageIcon("ikonoak/babesetxe.png");
            sueciaIcon = new ImageIcon(sueciaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            sueciaBABESETXE = new JLabel(sueciaIcon);
            sueciaBABESETXE.setBounds(950, 200, hostelWidth, hostelHeight);
            layeredPane.add(sueciaBABESETXE, JLayeredPane.PALETTE_LAYER);
            
            ImageIcon libiaIcon = new ImageIcon("ikonoak/babesetxe.png");
            libiaIcon = new ImageIcon(libiaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            libiaBABESETXE = new JLabel(libiaIcon);
            libiaBABESETXE.setBounds(950, 450, hostelWidth, hostelHeight);
            layeredPane.add(libiaBABESETXE, JLayeredPane.PALETTE_LAYER);

            layeredPane.addMouseListener(kontrolatzailea);        
            
            adminToolbarJarri();

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
		pant = "main";
		if(adminMapa==2)
		{
			adminMapa = 0;
		}
		
		mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
		mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(maxPantaila==1)
		{
			mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else 
		{
		    mainPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
		    mainPANTAILA.setLocationRelativeTo(null);
		}	    
	    
		mainPANTAILA.setUndecorated(true);	    
		    
		JPanel panel = new JPanel(new BorderLayout());

		JPanel botonesHuecosPanel = new JPanel();
		botonesHuecosPanel.setLayout(new GridBagLayout());
		
	    GridBagConstraints gbct = new GridBagConstraints();
	    
	    gbct.insets = new Insets(10, 10, 10, 10);
		gbct.weightx = 0.1; 
		gbct.weighty = 1.0;
		
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);

		button = new JButton(hizkuntza1.aukeratu);
		button.setActionCommand("HUECO");
		button.addActionListener(kontrolatzailea);
		button.setForeground(Koloreak.ZURIA);
		button.setPreferredSize(new Dimension(300, 700));	
		button.setMinimumSize(new Dimension(300, 700));
	    button.setBorder(lineBorder);
		
		gbct.gridx = 0;
	    gbct.gridy = 0;
		botonesHuecosPanel.add(button, gbct);

		button2 = new JButton(hizkuntza1.aukeratu);
		button2.setActionCommand("HUECO2");
		button2.addActionListener(kontrolatzailea);
		button2.setForeground(Koloreak.ZURIA);
		button2.setPreferredSize(new Dimension(300, 700));
		button2.setMinimumSize(new Dimension(300, 700));
	    button2.setBorder(lineBorder);
	    
		gbct.gridx = 1;
		botonesHuecosPanel.add(button2, gbct);

		button3 = new JButton(hizkuntza1.aukeratu);
		button3.setActionCommand("HUECO3");
		button3.addActionListener(kontrolatzailea);
		button3.setForeground(Koloreak.ZURIA);
		button3.setPreferredSize(new Dimension(300, 700));
		button3.setMinimumSize(new Dimension(300, 700));
	    button3.setBorder(lineBorder);
	    
		gbct.gridx = 2;
		botonesHuecosPanel.add(button3, gbct);		   
		    
		button.setBackground(Koloreak.URDINILUNA);
		button2.setBackground(Koloreak.URDINILUNA);
		button3.setBackground(Koloreak.URDINILUNA);
		    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
	        
	    toolbarJarri();
	        
	    botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		panel.add(botonesHuecosPanel, BorderLayout.CENTER);
	    mainPANTAILA.add(toolBar, BorderLayout.NORTH);
	        
		mainPANTAILA.getContentPane().add(panel);
		mainPANTAILA.setVisible(true);
	}
	
	private void toolbarJarri()
	{
		toolBar = new JToolBar();	        
	    toolBar.setPreferredSize(new Dimension(anchoPantalla, 150));
	    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    toolBar.setBackground(Koloreak.URDINILUNA);
	        	
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3);
	    
	    deleteButton.setBackground(Koloreak.URDINILUNA);
	    deleteButton.setBorder(lineBorder);
	    erabiltzailea.deleteButtonBEHIN = 0;
	    editButton.setBackground(Koloreak.URDINILUNA);
	    editButton.setBorder(lineBorder);
	    erabiltzailea.editButtonBEHIN = 0;
	    backButton.setBackground(Koloreak.URDINILUNA);
	    backButton.setBorder(lineBorder);
	    erabiltzailea.backButtonBEHIN = 0;
	    
	    erabiltzailea.confirmButtonBEHIN = 1;
	    confirmButton.setBackground(Koloreak.GRISARGIA);
	    LineBorder lineBorder2 = new LineBorder(Koloreak.GRISARGIA, 3);
	    confirmButton.setBorder(lineBorder2);
	    	
	    putzuBOTOIA.setVisible(false);
	    putzuBOTOIA2.setVisible(false);
	    putzuBOTOIA3.setVisible(false);
	        
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
	        
	    add3DTituluToolbar();	        
	        
	    toolBar.add(Box.createHorizontalStrut(spaceWidth));
	    toolBar.add(Box.createHorizontalStrut(spaceWidth));
	    toolBar.add(Box.createHorizontalStrut(spaceWidth));       
	        
	    toolBar.add(putzuBOTOIA);	        
	    toolBar.add(putzuBOTOIA2);
	    toolBar.add(putzuBOTOIA3);
	}
	
	private void adminToolbarJarri()
	{
		toolBar = new JToolBar();	        
	    toolBar.setPreferredSize(new Dimension(anchoPantalla, 65));
	    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	    toolBar.setBackground(Koloreak.URDINILUNA);
	        	
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3);
	    
	    backButton.setBackground(Koloreak.URDINILUNA);
	    backButton.setBorder(lineBorder);
	    erabiltzailea.backButtonBEHIN = 0;
	   
	    toolBar.add(adminEspacio);
		toolBar.add(Box.createHorizontalStrut(spaceWidth2));
	    toolBar.add(backButton);
	}
	
	public void mostrarPantallaEditarHuecos(Erabiltzailea erabiltzailea)
	{
		pant = "main";
		if(adminMapa==2)
		{
			adminMapa = 0;
		}
		int i = 0, balixu = 0;
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    if(maxPantaila==1)
		{
			mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else 
		{
		    mainPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
		    mainPANTAILA.setLocationRelativeTo(null);
		}	
	  	
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());
	    
	    Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);

	    JPanel botonesHuecosPanel = new JPanel();
		botonesHuecosPanel.setLayout(new GridBagLayout());
		
	    GridBagConstraints gbct = new GridBagConstraints();
	    
	    gbct.insets = new Insets(10, 10, 10, 10);
		gbct.weightx = 0.1; 
		gbct.weighty = 1.0;
		
		Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);
	    
	    for (i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        String currentValue = erabiltzailea.getBalioZerrenda().get(i);

	        if (currentValue != null) 
	        {
	        	String balio;

	        	if(i==0)
	        	{
	        		button.setPreferredSize(new Dimension(300, 700));
	        		button.setMinimumSize(new Dimension(300, 700));
	        	    button.setBorder(lineBorder);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);

		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button);
		            balixu++;
	        	}
	        	else if(i==1)
	        	{
	        		button2.setPreferredSize(new Dimension(300, 700));
	        		button2.setMinimumSize(new Dimension(300, 700));
	        	    button2.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button2);
		            balixu++;
	        	}

	        	else if(i==2)
	        	{
	        		button3.setPreferredSize(new Dimension(300, 700));
	        		button3.setMinimumSize(new Dimension(300, 700));
	        	    button3.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button3);
		            balixu++;
	        	}
	        } 
	        else 
	        {
	        	if(i==1) 
	    	    {
	    	    	button2 = new JButton(hizkuntza1.aukeratu);
	    		    button2.setActionCommand("HUECO2");
	    		    button2.addActionListener(kontrolatzailea);
	    		    button2.setForeground(Koloreak.ZURIA);
	    		    button2.setPreferredSize(new Dimension(300, 700));
	    		    button2.setMinimumSize(new Dimension(300, 700));
	    		    button2.setBorder(lineBorder);
	    		    
	    			gbct.gridx = 1;
	    			botonesHuecosPanel.add(button2, gbct);
	    	    }
	        	else if(i==2)
	    	    {
	    	    	button3 = new JButton(hizkuntza1.aukeratu);
	    		    button3.setActionCommand("HUECO3");
	    		    button3.addActionListener(kontrolatzailea);
	    		    button3.setForeground(Koloreak.ZURIA);
	    		    button3.setPreferredSize(new Dimension(300, 700));
	    		    button3.setMinimumSize(new Dimension(300, 700));
	    		    button3.setBorder(lineBorder);
	    		    
	    			gbct.gridx = 2;
	    			botonesHuecosPanel.add(button3, gbct);
	    	    }
	        	else
	        	{	        		
	        		button = new JButton(hizkuntza1.aukeratu);
	        		button.setActionCommand("HUECO");
	        		button.addActionListener(kontrolatzailea);
	        		button.setForeground(Koloreak.ZURIA);
	        		button.setPreferredSize(new Dimension(300, 700));	
	        		button.setMinimumSize(new Dimension(300, 700));
	        	    button.setBorder(lineBorder);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        	}
	        }
	    } 
	    	    
	    button.setBackground(Koloreak.URDINILUNA);
		button2.setBackground(Koloreak.URDINILUNA);
		button3.setBackground(Koloreak.URDINILUNA);
	    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
        
	    toolbarJarri();
       
	    botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
        
	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	    
	    if(balixu==3)
	    {
	    	String mensaje = hizkuntza1.hiruGehienez;
	        JOptionPane.showMessageDialog(null, mensaje);    
	    }
	}
	
	//ERABILTZAILEAREN DATUEKIN PANTAILA ERAKUTZI
	public void mostrarPantallaHuecosCompletado(Erabiltzailea erabiltzailea) 
	{
		pant = "main";
		if(adminMapa==2)
		{
			adminMapa = 0;
		}
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    if(maxPantaila==1)
		{
			mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else 
		{
		    mainPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
		    mainPANTAILA.setLocationRelativeTo(null);
		}	
	  	
	    mainPANTAILA.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel();
		botonesHuecosPanel.setLayout(new GridBagLayout());
		
	    GridBagConstraints gbct = new GridBagConstraints();
	    
	    gbct.insets = new Insets(10, 10, 10, 10);
		gbct.weightx = 0.1; 
		gbct.weighty = 1.0;
		
		Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);

	    for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) {
	        String balio = erabiltzailea.getBalioZerrenda().get(i);
	        
	        if (balio != null) 
	        {
	        	if(i==0)
	        	{
	        		button.setPreferredSize(new Dimension(300, 700));	
	        		button.setMinimumSize(new Dimension(300, 700));
	        	    button.setBorder(lineBorder);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button);
	        	}
	        	else if(i==1)
	        	{
	        		button2.setPreferredSize(new Dimension(300, 700));
	        		button2.setMinimumSize(new Dimension(300, 700));
	        	    button2.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button2);
	        	}

	        	else if(i==2)
	        	{
	        		button3.setPreferredSize(new Dimension(300, 700));
	        		button3.setMinimumSize(new Dimension(300, 700));
	        	    button3.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button3);
	        	}
	        }
	    }
	    
	    button.setBackground(Koloreak.URDINILUNA);
		button2.setBackground(Koloreak.URDINILUNA);
		button3.setBackground(Koloreak.URDINILUNA);
	    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
        
	    toolbarJarri();
       
	    botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA.add(toolBar, BorderLayout.NORTH);
        
	    mainPANTAILA.getContentPane().add(panel);
	    mainPANTAILA.setVisible(true);
	}
	
	public void mostrarPantallaWait(Erabiltzailea erabiltzailea) 
	{
	    mainPANTAILA2 = new JFrame("PANTAILA PRINTZIPALA");
	    mainPANTAILA2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    if(maxPantaila==1)
		{
			mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else 
		{
		    mainPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
		    mainPANTAILA.setLocationRelativeTo(null);
		}	
	  	
	    mainPANTAILA2.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());
	    
	    JButton buton = new JButton(); //EZKERREKO BOTOIA
	    
		JButton buton2 = new JButton(); //ERDIKO BOTOIA
		
		JButton buton3 = new JButton(); //ESKUINEKO BOTOIA

		JPanel botonesHuecosPanel = new JPanel();
		botonesHuecosPanel.setLayout(new GridBagLayout());
		
	    GridBagConstraints gbct = new GridBagConstraints();
	    
	    gbct.insets = new Insets(10, 10, 10, 10);
		gbct.weightx = 0.1; 
		gbct.weighty = 1.0;
		
		Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);

	    for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) 
	    {
	        String balio = erabiltzailea.getBalioZerrenda().get(i);
	        
	        if (balio != null) 
	        {
	        	if(i==0)
	        	{
	        		button.setPreferredSize(new Dimension(300, 700));	
	        		button.setMinimumSize(new Dimension(300, 700));
	        	    button.setBorder(lineBorder);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton);
	        	}
	        	else if(i==1)
	        	{
	        		button2.setPreferredSize(new Dimension(300, 700));
	        		button2.setMinimumSize(new Dimension(300, 700));
	        	    button2.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton2);
	        	}

	        	else if(i==2)
	        	{
	        		button3.setPreferredSize(new Dimension(300, 700));
	        		button3.setMinimumSize(new Dimension(300, 700));
	        	    button3.setBorder(lineBorder);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, buton3);
	        	}
	        }
	    }	    
	    
	    mainPANTAILA2.setJMenuBar(crearMenuBar());        
        
	    toolbarJarri();       
       
	    botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		panel.add(botonesHuecosPanel, BorderLayout.CENTER);
        mainPANTAILA2.add(toolBar, BorderLayout.NORTH);

	    mainPANTAILA2.getContentPane().add(panel);
	    mainPANTAILA2.setVisible(true);
	}
	
	public void mostrarOpcionesEliminar()
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		aukerenPANTAILA = new JFrame(hizkuntza1.ezabatuEsaldia);
        aukerenPANTAILA.setSize(500, 300);
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(1,3));  
        
        Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);
	    
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
	        JButton ezkerra = new JButton(hizkuntza1.ezkerra);
	        ezkerra.setActionCommand("EZKERREKOA");
	        ezkerra.addActionListener(kontrolatzailea);
	        ezkerra.setBackground(Koloreak.URDINILUNA);
	        ezkerra.setBorder(lineBorder);
	        ezkerra.setForeground(Koloreak.ZURIA);
	        opcionesPanel.add(ezkerra);
	
	        JButton erdia = new JButton(hizkuntza1.erdia);
	        erdia.setActionCommand("ERDIKOA");
	        erdia.addActionListener(kontrolatzailea);
	        erdia.setBackground(Koloreak.URDINILUNA);
	        erdia.setBorder(lineBorder);
	        erdia.setForeground(Koloreak.ZURIA);
	        opcionesPanel.add(erdia);
	
	        JButton eskuma = new JButton(hizkuntza1.eskuma);
	        eskuma.setActionCommand("ESKUINEKOA");
	        eskuma.addActionListener(kontrolatzailea);
	        eskuma.setBackground(Koloreak.URDINILUNA);
	        eskuma.setBorder(lineBorder);
	        eskuma.setForeground(Koloreak.ZURIA);
	        opcionesPanel.add(eskuma);
        }
        else if(kop==2)
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)==null)
        	{
        		JButton ezkerra = new JButton(hizkuntza1.ezkerra);
    	        ezkerra.setActionCommand("EZKERREKOA");    
    	        ezkerra.addActionListener(kontrolatzailea);
    	        ezkerra.setBackground(Koloreak.URDINILUNA);
    	        ezkerra.setBorder(lineBorder);
    	        ezkerra.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(ezkerra);
    	
    	        JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ERDIKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(eskuma);
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)==null)
        	{
        		JButton ezkerra = new JButton(hizkuntza1.ezkerra);
        		ezkerra.setActionCommand("EZKERREKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(ezkerra);
    	
    	        JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ESKUINEKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(eskuma);
        	}
        	else
        	{
        		JButton ezkerra = new JButton(hizkuntza1.ezkerra);
        		ezkerra.setActionCommand("ERDIKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(ezkerra);
    	
    	        JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ESKUINEKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(eskuma);
        	}	        
        } 
        else if(kop==1)
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)!=null)
        	{
        		JButton eskuma = new JButton(hizkuntza1.deleteButtom);
        		eskuma.setActionCommand("ESKUINEKOA");
        		eskuma.addActionListener(kontrolatzailea);
        		eskuma.setBackground(Koloreak.URDINILUNA);
        		eskuma.setBorder(lineBorder);
        		eskuma.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(eskuma);
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)!=null)
        	{
        		JButton erdia = new JButton(hizkuntza1.deleteButtom);
        		erdia.setActionCommand("ERDIKOA");
        		erdia.addActionListener(kontrolatzailea);
        		erdia.setBackground(Koloreak.URDINILUNA);
        		erdia.setBorder(lineBorder);
        		erdia.setForeground(Koloreak.ZURIA);
     	        opcionesPanel.add(erdia);
        	}
        	else
        	{
        		JButton ezkerra = new JButton(hizkuntza1.deleteButtom);
        		ezkerra.setActionCommand("EZKERREKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);
    	        opcionesPanel.add(ezkerra);
        	}
        }

        aukerenPANTAILA.getContentPane().add(opcionesPanel);

        aukerenPANTAILA.setLocationRelativeTo(ordenagailuarenPANTAILA);
        aukerenPANTAILA.setVisible(true);
	}
	
	public void mostrarOpciones()
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		aukerenPANTAILA = new JFrame(hizkuntza1.aukerak);
        aukerenPANTAILA.setSize(500, 300);
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.setLayout(new GridLayout(1, 3));
        
        Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);

        JButton pozoButton;
        pozoButton = new JButton(hizkuntza1.putzua);
        pozoButton.setActionCommand("PUTZUA");
        pozoButton.addActionListener(kontrolatzailea);
        pozoButton.setBackground(Koloreak.URDINILUNA);
        pozoButton.setBorder(lineBorder);
        pozoButton.setForeground(Koloreak.ZURIA);
        opcionesPanel.add(pozoButton);

        JButton ventiladorButton;
        ventiladorButton = new JButton(hizkuntza1.haizegailua);
        ventiladorButton.setActionCommand("HAIZEGAILUA");
        ventiladorButton.addActionListener(kontrolatzailea);
        ventiladorButton.setBackground(Koloreak.URDINILUNA);
        ventiladorButton.setBorder(lineBorder);
        ventiladorButton.setForeground(Koloreak.ZURIA);
        opcionesPanel.add(ventiladorButton);

        JButton fugaButton;
        fugaButton = new JButton(hizkuntza1.berogailua);
        fugaButton.setActionCommand("BEROGAILUA");
        fugaButton.addActionListener(kontrolatzailea);
        fugaButton.setBackground(Koloreak.URDINILUNA);
        fugaButton.setBorder(lineBorder);
        fugaButton.setForeground(Koloreak.ZURIA);
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
	                erabiltzailea.buttonBEHIN = 1;
	                posizioa=0;
	                break;

	            case 2:	            	
	                irudiaJarri(argazkia, button2);
	                erabiltzailea.button2BEHIN = 1;
	                posizioa = 1;
	                break;

	            case 3:
	                irudiaJarri(argazkia, button3);
	                erabiltzailea.button3BEHIN = 1;
	                posizioa=2;
	                break;
	        }
	    }
	}
	
	private void irudiaJarri(String argazkia, JButton botoia)
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		int hostelWidth = 200;
        int hostelHeight = 200;

        ImageIcon icon = new ImageIcon("ikonoak/" + argazkia + ".png");
        Image scaledImage = icon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH);

        botoia.setIcon(new ImageIcon(scaledImage));
        botoia.setText(hizkuntza1.kargatzen);
        botoia.setFont(LetraMotak.KARGATZEN);
        botoia.setForeground(Koloreak.ZURIA);
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
	                erabiltzailea.buttonBEHIN = 0;
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose();
	                //erabiltzailea.behin = 0;  
	                break;
	                
	            case Modeloa.PROPIETATEA6:
	            	erabiltzailea.setBalioZerrendaAtIndex(1, null);
	            	erabiltzailea.button2BEHIN = 0;
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose();
	                break;
	                
	            case Modeloa.PROPIETATEA7:
	            	erabiltzailea.setBalioZerrendaAtIndex(2, null);
	            	erabiltzailea.button3BEHIN = 0;
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
	            	System.out.println("Recibida actualización de PROPIETATEA9");
	            	loginPANTAILA.dispose();
	                break; 
	            case Modeloa.PROPIETATEA10:
	            	modeloa.timer4.setRepeats(false);
	            	modeloa.timer4.start();
	            	zer2 = "prop10";
	        		mainPANTAILA.dispose();  
	        		if(zer == "gehitu")
	        		{
	        			mostrarPantallaEditarHuecos(erabiltzailea);
	        		}
	        		else if(zer == "konfirmatu") 
	        		{
	        			mostrarPantallaHuecosCompletado(erabiltzailea);
	        		}
	        		else if(zer == "hutsik")
	        		{
	        			mostrarPantallaHuecos();
	        		}
	                break;
	            case Modeloa.PROPIETATEA11:
	            	if(zer2 == "prop10")
	        		{
	            		mainPANTAILA2.dispose();
	        		}
	            	else if(zer2 == "prop12")
	        		{
	            		loginPANTAILA2.dispose();
	        		}
	            	
	                break;
	            case Modeloa.PROPIETATEA12:
	            	zer2 = "prop12";    
	            	System.out.println("zer2: " + zer2);
	        		loginPANTAILA.dispose();
	            	modeloa.timer4.setRepeats(false);
	            	modeloa.timer4.start();	  
	        		//loginPantailaErakutzi();
	                break;
	        }
	    }
	}
	
	/*
	 	 
	 L    U U  D    OOO  K K       L    U U  D    OOO  K K
	 L    U U  D D  O O  K         L    U U  D D  O O  K
	 LLL  UUU  D    OOO  K K       LLL  UUU  D    OOO  K K
	 
	 */
	public void ludokLovesU() //Easter egg
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
	/*
	 
	 L    U U  D    OOO  K K       L    U U  D    OOO  K K
	 L    U U  D D  O O  K         L    U U  D D  O O  K 
	 LLL  UUU  D    OOO  K K       LLL  UUU  D    OOO  K K
	 
	 */
	
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