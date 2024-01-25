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
	private Modeloa modeloa; //Modeloa
	
	//KONTROLATZAILEA
	private Kontrolatzailea kontrolatzailea; //Kontrolatzailea 
	
	//PANTAILAK
	private JFrame ordenagailuarenPANTAILA; //Ordenagailuaren Pantaila
	public JFrame mainPANTAILA; //Main pantaila
	public JFrame mainPANTAILA2; //Main pantaila wait
	public JFrame adminFrame; //Admin pantaila
	public JFrame loginPANTAILA; //Login pantaila
	private JFrame aukerenPANTAILA; //Aukeren pantaila
		
	//JFIELD
	public JTextField izenaField; //Izena
	public JPasswordField pasahitzaField; //Pasahitza
	
	//JLABEL
	public JLabel argeliaBABESETXE; //Argelia
	public JLabel sueciaBABESETXE; //Suecia
	public JLabel libiaBABESETXE; //Libia
	
	//BOOLEAN
	boolean verde = true;
	
	//BOTOIAK
	public JButton button; //Ezkerreko botoia
	public JButton button2; //Erdiko botoia
	public JButton button3; //Eskuineko botoia
	public JButton backButton; //Atzera botoia
	public JButton editButton; //Gehitu botoia
	public JButton confirmButton; //Konfirmatu botoia
	private JButton irtenButton; //Irten botoia
	private JButton adminEspacio; //Admin toolbar espazioa izateko
	public JButton deleteButton; //Ezabatu botoia
	public JButton loginButton; //Hasi Saioa botoia
	private JButton putzuBOTOIA; //Putzu botoia1
	private JButton putzuBOTOIA2; //Putzu botoia 2
	private JButton putzuBOTOIA3; //Putzu botoia 3
    
    //ERABILTZAILEA
    public Erabiltzailea erabiltzailea;
    
    //JTOOLBAR
    private JToolBar toolBar;
    
    //STRING
    public String izena;
    public String zer;
    public String pant;
    
    /*MAKINA VIRTUALAREN BROKER: 172.20.10.4 / ORDENAGAILUKO BROKER: localhost*/
    private static final String BROKER = "tcp://localhost:1883"; //Broker
    private static final String CLENT_ID = "PBL"; //Client_ID
    
    //INT
    private int posizioa=0; //Klikaren posizioa
    public int adminMapa = 0; //Admin mapa
    private int anchoPantalla = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(); //Nire pantailaren zabalera
    private int altoPantalla = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(); //Nire pantailaren altuera
    public String hizkuntza = "euskera"; //Hasierako hizkuntza euskera
    public int maxPantaila = 0; //Pantaila maximizatuta gelditzeko
    
    //MQTT
    private Mqtt mqtt; //Mqtt
    
    //NIREAKZIOA
    private NireAkzioa euskera, euskeraLogin, castellano, castellanoLogin, ingles, inglesLogin, hasierakoTamaina, minimizatu, maximizatu, irten;
         
    //KONSTRUKTOREA
	public Printzipala(Modeloa modeloa, Kontrolatzailea kontrolatzailea, Mqtt mqtt) 
	{
		this.modeloa = modeloa; //Modeloa
		this.kontrolatzailea = kontrolatzailea; //Kontrolatzailea
		this.mqtt = mqtt; //Mqtt
		this.modeloa.addPropertyChangeListener(this); //Property Change
						
		modeloa.erabiltzaileakIrakurri(); //Erabiltzaileen Fitxategia Irakurri Eta Erabiltzaileak Sortu		
		modeloa.hizkuntzakIrakurri(); //Hizkuntzak irakurri
		
		sortuAkzioak(); //Akzioak sortu
		
		toolbarBotoienKonfigurazioa(); //Toolbar Botoien Konfigurazioa
		
		kontrolatzailea.setIrudia(this); //SetIrudia Kontrolatzailea
        modeloa.setIrudia(this); //SetIrudia Modeloa
	}
	
	public void sortuAkzioak() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		euskera = new NireAkzioa ("Euskera",new ImageIcon("ikonoak/eus.png"),"euskera",new Integer(KeyEvent.VK_Q), this, modeloa);
		castellano = new NireAkzioa ("Castellano",new ImageIcon("ikonoak/esp.png"),"castellano",new Integer(KeyEvent.VK_W), this, modeloa);
		ingles = new NireAkzioa ("Ingles",new ImageIcon("ikonoak/ing.png"),"ingles",new Integer(KeyEvent.VK_R), this, modeloa);
		
		euskeraLogin = new NireAkzioa ("Euskera ",new ImageIcon("ikonoak/eus.png"),"euskera",new Integer(KeyEvent.VK_E), this, modeloa);
		castellanoLogin = new NireAkzioa ("Castellano ",new ImageIcon("ikonoak/esp.png"),"castellano",new Integer(KeyEvent.VK_T), this, modeloa);
		inglesLogin = new NireAkzioa ("Ingles ",new ImageIcon("ikonoak/ing.png"),"ingles",new Integer(KeyEvent.VK_Y), this, modeloa);
		
		minimizatu = new NireAkzioa (hizkuntza1.minimizatu,new ImageIcon("ikonoak/min.png"),hizkuntza1.minimizatu, null, this, modeloa);
		maximizatu = new NireAkzioa (hizkuntza1.maximizatu,new ImageIcon("ikonoak/max.png"),hizkuntza1.maximizatu, null, this, modeloa);
		hasierakoTamaina = new NireAkzioa (hizkuntza1.hasierakoTamaina,new ImageIcon("ikonoak/max.png"),hizkuntza1.hasierakoTamaina, null, this, modeloa);
		irten = new NireAkzioa (hizkuntza1.irten,new ImageIcon("ikonoak/exit.png"),hizkuntza1.irten, new Integer(KeyEvent.VK_S), this, modeloa);
	}
	
	//MENUKO BARRA SORTU
	private JMenuBar crearMenuBar() 
	{		
		JMenuBar barra = new JMenuBar(); //Menu barra
		barra.add (sortuMenuHizkuntzak()); //Hizkuntzak gehitu
		barra.add(Box.createHorizontalGlue()); //Eskuineko aldean jartzeko hemendik aurrera deklaratutako guztiak
		barra.add (crearMenuMinimizatu()); //Minimizatu gehitu
		barra.add (crearMenuMaximizatu()); //Maximizatu gehitu
		barra.add(crearMenuMHasierakoTamaina()); //Hasierako Tamaina gehitu
		barra.add (crearMenuSalir()); //Irten gehitu
		
		return barra; //Barra itzuli
	}
	
	//LOGIN PANTAILAREN MENUKO BARRA SORTU
	private JMenuBar crearMenuBarLogin() 
	{		
		JMenuBar barra = new JMenuBar(); //Menu barra
		barra.add (sortuMenuHizkuntzakLogin()); //Hizkuntzak gehitu
		barra.add(Box.createHorizontalGlue()); //Eskuineko aldean jartzeko hemendik aurrera deklaratutako guztiak
		barra.add (crearMenuMinimizatu()); //Minimizatu gehitu
		barra.add (crearMenuMaximizatu()); //Maximizatu gehitu
		barra.add(crearMenuMHasierakoTamaina()); //Hasierako Tamaina gehitu
		barra.add (crearMenuSalir()); //Irten gehitu
		
		return barra; //Barra itzuli
	}
	
	//HIZKUNTZAK JMENU
	private JMenu sortuMenuHizkuntzak() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuHizkuntzak = new JMenu (hizkuntza1.hizkuntzak); //JMenu-a sortu
		menuHizkuntzak.setIcon(new ImageIcon("ikonoak/hizkun.png")); //Argazkia jarri
		menuHizkuntzak.add(euskera); //Euskera gehitu
		menuHizkuntzak.add(castellano); //Castellano gehitu
		menuHizkuntzak.add(ingles); //Ingles gehitu
		
		return menuHizkuntzak; //JMenu-a itzuli
	}
	
	//LOGIN HIZKUNTZAK JMENU
	private JMenu sortuMenuHizkuntzakLogin() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuHizkuntzak = new JMenu (hizkuntza1.hizkuntzak); //JMenu-a sortu
		menuHizkuntzak.setIcon(new ImageIcon("ikonoak/hizkun.png")); //Argazkia jarri
		menuHizkuntzak.add(euskeraLogin); //Euskera gehitu
		menuHizkuntzak.add(castellanoLogin); //Castellano gehitu
		menuHizkuntzak.add(inglesLogin); //Ingles gehitu
		
		return menuHizkuntzak; //JMenu-a itzuli
	}
	
	//MINIMIZATU JMENU
	private JMenu crearMenuMinimizatu() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuMinimizatu = new JMenu (hizkuntza1.minimizatu); //JMenu-a sortu
		menuMinimizatu.setIcon(new ImageIcon("ikonoak/min.png")); //Argazkia jarri
		menuMinimizatu.add(minimizatu); //Akzioa gehitu
		
		return menuMinimizatu; //JMenu-a itzuli
	}
	
	//MAXIMIZATU JMENU
	private JMenu crearMenuMaximizatu() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuMaximizatu = new JMenu (hizkuntza1.maximizatu); //JMenu-a sortu
		menuMaximizatu.setIcon(new ImageIcon("ikonoak/max.png")); //Argazkia jarri
		menuMaximizatu.add(maximizatu); //Akzioa gehitu
		
		return menuMaximizatu; //JMenu-a itzuli
	}
	
	//HASIERAKO TAMAINA JMENU
	private JMenu crearMenuMHasierakoTamaina() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuMaximizatu = new JMenu (hizkuntza1.hasierakoTamaina); //JMenu-a sortu
		menuMaximizatu.setIcon(new ImageIcon("ikonoak/max.png")); //Argazkia jarri
		menuMaximizatu.add(hasierakoTamaina); //Akzioa gehitu
		
		return menuMaximizatu; //JMenu-a itzuli
	}
	
	//IRTEN JMENU
	private JMenu crearMenuSalir() 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		JMenu menuSalir = new JMenu (hizkuntza1.irten); //JMenu-a sortu
		menuSalir.setIcon(new ImageIcon("ikonoak/exit.png")); //Argazkia jarri
		menuSalir.setMnemonic(new Integer(KeyEvent.VK_S)); //Tekla jarri
		menuSalir.add(irten); //Akzioa gehitu
		
		return menuSalir; //JMenu-a itzuli
	}
	
	//BOTOIA SORTU
	private JButton botoiaSortu(JButton botoia, String titulua, String actionCommand, LineBorder lineBorder, int zabalera, int altuera, Color kolorea, boolean bordeakBistaratu)
	{		
		botoia = new JButton(titulua); //Titulua jarri
		botoia.setActionCommand(actionCommand); //Action Command-a jarri
		botoia.addActionListener(kontrolatzailea); //Action Listener-a kontrolatzailea
		botoia.setPreferredSize(new Dimension(zabalera, altuera)); //Preferred Size
		botoia.setMinimumSize(new Dimension(zabalera, altuera)); //Minimum Size
		botoia.setBorder(lineBorder); //Bordea jarri
		botoia.setBackground(kolorea); //Atzealdeko kolorea jarri
		botoia.setForeground(Koloreak.ZURIA); //Testuaren kolorea zuria jarri
		botoia.setBorderPainted(bordeakBistaratu); //Ea bordeak ikusiko diren
		return botoia;
	}
	
	public void toolbarBotoienKonfigurazioa()
	{	
        LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3);
        
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		
		backButton = botoiaSortu(backButton, hizkuntza1.backButtom, "ATRAS", lineBorder, 130, 60, Koloreak.URDINILUNA, true);	    
		editButton = botoiaSortu(editButton, hizkuntza1.editButtom, "GEHITU", lineBorder, 130, 60, Koloreak.URDINILUNA, true);	    
		deleteButton = botoiaSortu(deleteButton, hizkuntza1.deleteButtom, "EZABATU", lineBorder, 130, 60, Koloreak.URDINILUNA, true);
		confirmButton = botoiaSortu(confirmButton, hizkuntza1.confirmButtom, "KONFIRMATU", lineBorder, 130, 60, Koloreak.URDINILUNA, true);
        
		putzuBOTOIA = botoiaSortu(putzuBOTOIA, "P1", null, null, 110, 100, null, true);
		putzuBOTOIA2 = botoiaSortu(putzuBOTOIA2, "P2", null, null, 110, 100, null, true);
		putzuBOTOIA3 = botoiaSortu(putzuBOTOIA3, "P3", null, null, 110, 100, null, true);
        
		irtenButton = botoiaSortu(irtenButton, " ", null, null, 1, 150, null, false);       
		adminEspacio = botoiaSortu(adminEspacio, "P1", null, null, 1, 65, null, false);
	}
	
	//STRING BATEAN ZENBAKIAK SOILIK HARTZEN DITU
	private static String extraerValoresNumericos(String sarrera) 
	{
        String emaitza = sarrera.replaceAll("[^0-9.]", ""); //Soilik zenbakiak hartzen ditu

        return emaitza; //Emaitza itzuli
    }
	
	//PANTAILA AKTUALIZATU MQTT-REN BALIOAK ALDATZEKO
	private void actualizarPantalla() 
	{
	    if (mqtt != null) //Mqtt null ez balda
	    {
	        for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) //Balio zerrenda osoan begiratu
	        {
	            String balioa = erabiltzailea.getBalioZerrenda().get(i); //Balioa gorde
	            
	            if (balioa == null) //Balioa null baldin bada
	            {
	                System.out.println("balioa zero da " + i + " zenbakian"); //Balioa 0 baldin bada
	                continue; 
	            }

	            switch (balioa) 
	            {
	                case "urmaila": //Ur Maila baldin bada
	                    actualizarNivelDeAgua(i + 1); //Zenbagarren putzua den begiratu
	                    break;
	                case "tenperatura": //Tenperatura baldin bada
	                    actualizarTemperatura(i + 1); //Zenbagarren haizegailua den begiratu
	                    break;
	                case "gasa": //Gasa baldin bada
	                    actualizarGas(i + 1); //Zenbagarren berogailua den begiratu
	                    break;
	                default:
	                	System.out.println("Ezagutzen ez den aukera"); //Default-a
	                    break;
	            }
	        }
	    } 
	    else 
	    {
	        System.out.println("MQTT es null"); //Mqtt zero dela jartzen du consolan
	    }
	}

	//ADC-AREN KALKULUA EGIN
	private int adcKalkulua(String mqttADC, int zenb1, int zenb2)
	{
		String adcZenbakiGabe = extraerValoresNumericos(mqttADC); //Mqtt-k bidalitakotik soilik zenbakiak hartu
		
		int adc = 0; //Adc = 0
		
		adc = Integer.valueOf(adcZenbakiGabe); //Mqtt-k bidalitakoa int bihurtu
		adc = (int) (adc*zenb1/zenb2); //Guk nahi dugun balioa bistaratzeko egin beharreko kalkuluak
		
		return adc; //Adc itzuli
	}
	
	//PANTAILAKO 3 BOTOI PRINTZIPALEN TESTUAREN KONFIGURAZIOA
	private void mqttBotoienTestua(JButton botoia, int adc, String bestelakoak)
	{
		botoia.setText(String.valueOf(adc + bestelakoak)); //Testua jarri
		botoia.setForeground(Color.white); //Testuak kolore zuria
		if(adc>999) 
		{
			botoia.setFont(LetraMotak.MQTT2);
		}
		else
		{
			botoia.setFont(LetraMotak.MQTT); //Letra mota jarri
		}
	}
	
	//UR MAILAREN BOTOIEN KOLOREAK
	private void urmailarenBotoia(JButton botoia, int adc)
	{
		if(Integer.valueOf(adc) <= 33) //Adc-aren balioa 33 baino txikiagoa baldin bada
        {
			botoia.setBackground(Color.RED); //Kolore gorria
        }
        else if(33 <= Integer.valueOf(adc) && Integer.valueOf(adc) <= 66) //Adc-aren balioa 33 eta 66 artean baldin bada
        {
        	botoia.setBackground(Color.YELLOW); //Kolore horia
        }
        else //Adc-aren balioa 66 baino altuagoa baldin bada
        {
        	botoia.setBackground(Color.GREEN); //Kolore berdea
        }
		botoia.setVisible(true); //Botoia bistaratu
	}
	
	//UR MAILAK AKTUALIZATU
	private void actualizarNivelDeAgua(int index) 
	{		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = adcKalkulua(mqtt.adc1, 100, 4095);
		adc2 = adcKalkulua(mqtt.adc2, 100, 4095);
		adc3 = adcKalkulua(mqtt.adc3, 100, 4095);
		
	    switch (index) 
	    {
	        case 1:
	        	mqttBotoienTestua(button, adc1, " %");
	        	urmailarenBotoia(putzuBOTOIA, adc1);
	            break;
	        case 2:
	        	mqttBotoienTestua(button2, adc2, " %");
	        	urmailarenBotoia(putzuBOTOIA, adc2);
	            break;
	        case 3:
	        	mqttBotoienTestua(button3, adc3, " %");
	        	urmailarenBotoia(putzuBOTOIA, adc3);
	            break;
	        default:
	            break;
	    }
	}

	//HAIZEGAILUAK AKTUALIZATU
	private void actualizarTemperatura(int index) 
	{		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = adcKalkulua(mqtt.adc1, 330, 4095);
		adc2 = adcKalkulua(mqtt.adc2, 330, 4095);
		adc3 = adcKalkulua(mqtt.adc3, 330, 4095);
		
	    switch (index) 
	    {
	        case 1:
	        	mqttBotoienTestua(button, adc1, " ºC");
	            break;
	        case 2:
	        	mqttBotoienTestua(button2, adc2, " ºC");
	            break;
	        case 3:
	        	mqttBotoienTestua(button3, adc3, " ºC");
	            break;
	        default:
	            break;
	    }
	}

	//BEROGAILUAK AKTUALIZATU
	private void actualizarGas(int index) 
	{		
		int adc1 = 0, adc2 = 0, adc3 = 0;
		
		adc1 = adcKalkulua(mqtt.adc1, 1, 1);
		adc2 = adcKalkulua(mqtt.adc2, 1, 1);
		adc3 = adcKalkulua(mqtt.adc3, 1, 1);
		
	    switch (index) 
	    {
	        case 1:
	        	mqttBotoienTestua(button, adc1, " PPM");
	        	if (adc1 > 1000) 
	        	{
                    modeloa.reproducirSonido("soinuak/alarma.wav");
                    mostrarAlertaFugaGas(null);
                }
	            break;
	        case 2:
	        	mqttBotoienTestua(button2, adc2, " PPM");
	        	if (adc2 > 1000) 
	        	{
                    modeloa.reproducirSonido("soinuak/alarma.wav");
                    mostrarAlertaFugaGas(null);
                }
	            break;
	        case 3:
	        	mqttBotoienTestua(button3, adc3, " PPM");
	        	if (adc3 > 1000) 
	        	{
                    modeloa.reproducirSonido("soinuak/alarma.wav");
                    mostrarAlertaFugaGas(null);
                }
	            break;
	        default:
	            break;
	    }
	}
	
	private void mostrarAlertaFugaGas(Component parentComponent) 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
        Object[] option = {hizkuntza1.onartu}; //Onartu botoia

        int choice = JOptionPane.showOptionDialog(parentComponent,
                hizkuntza1.gasIsurketa,
                hizkuntza1.alerta,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                option,
                option[0]);

        if (choice == 0) //Onartu aukeratzean
        {
            modeloa.clip.stop(); //Soinua gelditu
        }
	}	
	
	//KONFIRMATU BOTOIAREN PARPADEATZEA
	private void actualizarPantalla2()
	{
		if (verde) //Verde baldin bada
		{
            confirmButton.setBackground(Koloreak.URDINILUNA); //Konfirmatu botoiaren atzealde kolorea urdina
        } 
		else 
		{
			confirmButton.setBackground(Color.GREEN); //Konfirmatu botoiaren atzealde kolorea berdea
        }
        verde = !verde; //Verde alderantziz gorde
	}	
	
	//ERABILTZAILEA ERREGISTRATZEKO PANTAILA
	public void loginPantailaErakutzi() 
	{
			pant = "login"; //Login pantailan dago
            loginPANTAILA = new JFrame("SAIOA HASI");
            loginPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            if(maxPantaila==1)
    		{
            	loginPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH);
    		}
    		else 
    		{
    			loginPANTAILA.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
    		    loginPANTAILA.setLocationRelativeTo(null);
    		}	
            loginPANTAILA.setUndecorated(true); //Goiko barra kendu
            
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
	
	//LOGIN PANTAILAREN TITULUA
	private void add3DTituluGridBag(JPanel panel1, GridBagConstraints gbc, String titulu)
	{
		@SuppressWarnings("serial")
		JLabel titleLabel = new JLabel() //Label berri bat sortu
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g); //Bere aitari deia
                Graphics2D g2d = (Graphics2D) g; //Graphics 2D jarri

                Color color1 = Koloreak.NARANJA; //Kolore naranja
                Color color2 = Koloreak.URDINARGIA; //Kolore urdina
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2); //Bi koloreak nahastuz gradiente bezala pintatu
                g2d.setPaint(gradient); //Kolore gradientearekin pintatu
                g2d.setFont(LetraMotak.LOGINTITULUA); //Letra tamaina jarri

                FontMetrics fontMetrics = g2d.getFontMetrics(LetraMotak.LOGINTITULUA); //Fontmetrics sortu
                int stringWidth = fontMetrics.stringWidth(titulu); //Zabalera kalkulatu
                int stringHeight = fontMetrics.getAscent(); //Altuera kalkulatu
                int x = (getWidth() - stringWidth) / 2; //Zabalera ken zabalera zati bi
                int y = (getHeight() - stringHeight) / 2 + stringHeight - 10; //Altuera ken altuera zati bi gehi altuera ken hamar     
                g2d.drawString(titulu, x, y); //Kolorezko testua pintatu
                g2d.setColor(Color.BLACK); //Kolore beltza jarri testuari
                g2d.drawString(titulu, x-4, y + 2); //Aurreko tituluaren aurrean pintatu, 3D bezalako efektu bat emateko
            }
        };
        titleLabel.setPreferredSize(new Dimension(700, 100)); //Tituluaren Label tamaina jarri
        panel1.add(titleLabel, gbc); //Titulua panelera gehitu
	}
		
	private void add3DTituluToolbar()
	{
		@SuppressWarnings("serial")
		JLabel titleLabel = new JLabel() //Label berri bat sortu 
		{
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g); //Bere aitari deia
                Graphics2D g2d = (Graphics2D) g; //Graphic 2D jarri
                g2d.setPaint(/*gradient*/Koloreak.URDINARGIA); //Testua urdinez pintatu
                g2d.setFont(LetraMotak.ERABILTZAILETITULUA); //Letra tamaina jarri

                FontMetrics fontMetrics = g2d.getFontMetrics(LetraMotak.ERABILTZAILETITULUA); //Fontmetrics sortu
                int stringWidth = fontMetrics.stringWidth(izena.toUpperCase()); //Zabalera kalkulatu
                int stringHeight = fontMetrics.getAscent(); //Altuera kalkulatu
                int x = (getWidth() - stringWidth) / 2; //Zabalera ken zabalera zati bi
                int y = (getHeight() - stringHeight) / 2 + stringHeight - 10; //Altuera ken altuera zati bi gehi altuera ken hamar     
               
                g2d.drawString(izena.toUpperCase(), x, y); //Urdinezko testua pintatu
                g2d.setColor(Color.WHITE); //Kolore zuria jarri testuari
                g2d.drawString(izena.toUpperCase(), x-4, y + 2); //Aurreko tituluaren aurrean pintatu, 3D bezalako efektu bat emateko
            }
        };
        
        titleLabel.setPreferredSize(new Dimension(300, 100)); //Tituluaren Label tamaina jarri
        toolBar.add(titleLabel); //Titulua panelera gehitu
	}
	
	//LOGIN PANTAILAN LOGOA GEHITU
	private void addLogo(JPanel panel1, GridBagConstraints gbc, String imagePath) 
	{	
		ImageIcon icon = new ImageIcon(imagePath); //Irudia sortu helbidearekin
        Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH); //Irudia txikiagotu
        ImageIcon scaledIcon = new ImageIcon(image); //Irudia icon bihurtu

        JPanel logo = new JPanel(new BorderLayout()); //Label berri bat BorderLayout dena
        logo.setPreferredSize(new Dimension(300, 300)); //Label-aren tamaina
        logo.add(new JLabel(scaledIcon)); //Label-era eskalaturik dagoen icon-a gehitu
        logo.setBackground(Koloreak.URDINILUNA); //Logoaren Label-aren atzealdea urdinez jarri
        panel1.add(logo, gbc); //Logoa panelera gehitu
	}
	
	//IZENA JARTZEKO KARRATUA
	private void addUsernameTextField(JPanel panel1, GridBagConstraints gbc) 
	{
		izenaField.setColumns(25); //Zutabeak jarri
        izenaField.setBounds(423, 109, 250, 44); //Margenak jarri
        izenaField.setMinimumSize(new Dimension(250,44)); //Tamaina minimoa jarri
        izenaField.setBackground(Koloreak.URDINILUNA);
        izenaField.setForeground(Color.white); //testua zuriz jarri
        izenaField.setMargin(new Insets(2, 10, 2, 2)); //Tamaina jarri
        izenaField.setFont(LetraMotak.IZENAetaPASAHITZA); //Letra mota jarri
        izenaField.addFocusListener(kontrolatzailea); //Focus Listener-a kontrolatzailea da

        panel1.add(izenaField, gbc); //TextField-a panelera gehitu
	}	
	
	//PASAHITZA JARTZEKO KARRATUA
	private void addPasswordTextField(JPanel panel1, GridBagConstraints gbc) 
	{
	    pasahitzaField.setColumns(25); //Zutabeak jarri
	    pasahitzaField.setBounds(423, 168, 250, 44); //Margenak jarri
	    pasahitzaField.setMinimumSize(new Dimension(250,44)); //Tamaina minimoa jarri
	    LineBorder lineBorder = new LineBorder(Koloreak.GRISARGIA, 4); //Bordea sortu kolore grisa
	    pasahitzaField.setBorder(lineBorder); //Borde grisa jarri
	    pasahitzaField.setBackground(Koloreak.URDINILUNA);
	    pasahitzaField.setForeground(Color.white); //Testua zuriz jarri
	    pasahitzaField.setFont(LetraMotak.IZENAetaPASAHITZA); //Letra mota jarri
	    pasahitzaField.addFocusListener(kontrolatzailea); //Focus Listener-a kontrolatzailea da

	    panel1.add(pasahitzaField, gbc); //TextField-a panelera gehitu
	}
		
	//ADMINISTRADOREEN PANTAILA
	public void mostrarPantallaAdmin() 
	{
		adminMapa = 2; //Admin mapa 2 da
        adminFrame = new JFrame("ADMINISTRATZAILEEN MAPA"); //JFrame berria sortu
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Itxi
        adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Panaila osoan zabaldu
        adminFrame.setUndecorated(true);

        JLayeredPane layeredPane = new JLayeredPane(); //Jlayered Pane berria sortu
        layeredPane.setPreferredSize(new Dimension(adminFrame.getWidth(), adminFrame.getHeight())); //Tamaina jarri

        try 
        {      
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Pantaila tamaina

            ImageIcon worldMapIcon2 = new ImageIcon("ikonoak/mapa.jpg"); //Icono berri bat sortu
            JLabel worldMapLabel2 = new JLabel(worldMapIcon2); //JLabel berri bat sortu
            worldMapLabel2.setBounds(0, 0, (int) screenSize.getWidth()/*anchoPantalla*/, (int) screenSize.getHeight()/*altoPantalla*/); //Pantaila osoko tamainan jarri
            layeredPane.add(worldMapLabel2, JLayeredPane.DEFAULT_LAYER); //JLayered Pane-an beheko planoan jarri            

            ImageIcon argeliaIcon = new ImageIcon("ikonoak/babesetxe.png"); //Icono berri bat sortu
            argeliaIcon = new ImageIcon(argeliaIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); //Irudia txikiagotu
            argeliaBABESETXE = new JLabel(argeliaIcon); //JLabel berria sortu
            argeliaBABESETXE.setBounds(900, 400, 50, 50); //Iconoari koordenadak jarri or kokatzeko
            layeredPane.add(argeliaBABESETXE, JLayeredPane.PALETTE_LAYER); //JLayered Pane-an goiko planoan jarri

            ImageIcon sueciaIcon = new ImageIcon("ikonoak/babesetxe.png"); //Icono berri bat sortu
            sueciaIcon = new ImageIcon(sueciaIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); //Irudia txikiagotu
            sueciaBABESETXE = new JLabel(sueciaIcon); //JLabel berria sortu
            sueciaBABESETXE.setBounds(950, 200, 50, 50); //Iconoari koordenadak jarri or kokatzeko
            layeredPane.add(sueciaBABESETXE, JLayeredPane.PALETTE_LAYER); //JLayered Pane-an goiko planoan jarri
            
            ImageIcon libiaIcon = new ImageIcon("ikonoak/babesetxe.png"); //Icono berri bat sortu
            libiaIcon = new ImageIcon(libiaIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)); //Irudia txikiagotu
            libiaBABESETXE = new JLabel(libiaIcon); //JLabel berria sortu
            libiaBABESETXE.setBounds(950, 450, 50, 50); //Iconoari koordenadak jarri or kokatzeko
            layeredPane.add(libiaBABESETXE, JLayeredPane.PALETTE_LAYER); //JLayered Pane-an goiko planoan jarri

            layeredPane.addMouseListener(kontrolatzailea); //Mouse Listener-a kontrolatzailea da       
            
            adminToolbarJarri(); //Toolbar-a jarri
            //toolBar.add(backButton);
            adminFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START); //Admin JFrame-an toolbar-a gehitu
        } 
        catch (Exception ex) //Exception-a
        {
            ex.printStackTrace(); //Exceptionak saltatzen badu zergaitik gertatu den jarriko du
            JOptionPane.showMessageDialog(null, "Errorea mapa edo babesetxeak kargatzean: " + ex.getMessage());
        }

        adminFrame.getContentPane().add(layeredPane);
        adminFrame.pack();
        adminFrame.setVisible(true);
    }		
        
	//BOTOIAK HUTSIK DAUDEN PANTAILA
	public void mostrarPantallaHuecos()
	{	
		pant = "main"; //Main pantailan dago
		
		if(adminMapa==2) //Admin mapa 2 baldin bada
		{
			adminMapa = 0; //Admin mapa 0ra jarri
		}
		
		mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA"); //Titulua jarri
		mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Itxi
		
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
		    
		JPanel panel = new JPanel(new BorderLayout()); //JPanel berri bat sortu

		JPanel botonesHuecosPanel = new JPanel(new GridBagLayout());
		
	    GridBagConstraints gbct = new GridBagConstraints();
	    
	    gbct.insets = new Insets(10, 10, 10, 10);
		gbct.weightx = 0.1; 
		gbct.weighty = 1.0;
		
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza);
		
		Color borderColor = Koloreak.BERDEARGIA;
	    LineBorder lineBorder = new LineBorder(borderColor, 6);

	    button = botoiaSortu(button, hizkuntza1.aukeratu, "HUECO", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
		
		gbct.gridx = 0;
	    gbct.gridy = 0;
		botonesHuecosPanel.add(button, gbct);

		button2 = botoiaSortu(button2, hizkuntza1.aukeratu, "HUECO2", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	    
		gbct.gridx = 1;
		botonesHuecosPanel.add(button2, gbct);

		button3 = botoiaSortu(button3, hizkuntza1.aukeratu, "HUECO3", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	    
		gbct.gridx = 2;
		botonesHuecosPanel.add(button3, gbct);	
		    
	    mainPANTAILA.setJMenuBar(crearMenuBar());        
	        
	    toolbarJarri();
	        
	    botonesHuecosPanel.setBackground(Koloreak.URDINILUNA);
		panel.add(botonesHuecosPanel, BorderLayout.CENTER);
	    mainPANTAILA.add(toolBar, BorderLayout.NORTH);
	        
		mainPANTAILA.getContentPane().add(panel);
		mainPANTAILA.setVisible(true);
	}
	
	//TOOLBAR-A JARRI
	private void toolbarJarri()
	{
		toolBar = new JToolBar(); //Toolbar berri bat sortu	        
	    toolBar.setPreferredSize(new Dimension(anchoPantalla, 150)); //Tamaina jarri
	    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); //Ezkerrean beti
	    toolBar.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina jarri
	        	
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3); //Borde berdea sortu
	    
	    deleteButton.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina jarri
	    deleteButton.setBorder(lineBorder); //Borde berdea jarri
	    erabiltzailea.deleteButtonBEHIN = 0; //Ezabatu botoia aktibatu
	    editButton.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina jarri
	    editButton.setBorder(lineBorder); //Borde berdea jarri
	    erabiltzailea.editButtonBEHIN = 0; //Gehitu botoia aktibatu
	    backButton.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina jarri
	    backButton.setBorder(lineBorder); //Borde berdea jarri
	    erabiltzailea.backButtonBEHIN = 0; //Atzera botoia aktibatu
	    
	    erabiltzailea.confirmButtonBEHIN = 1; //Konfirmatu botoia desaktibatu
	    confirmButton.setBackground(Koloreak.GRISARGIA); //Atzealde kolorea grisa jarri
	    LineBorder lineBorder2 = new LineBorder(Koloreak.GRISARGIA, 3); //Borde grisa sortu
	    confirmButton.setBorder(lineBorder2); //Borde grisa jarri
	    	
	    putzuBOTOIA.setVisible(false); //Putzu botoia 1 inbisible jarri
	    putzuBOTOIA2.setVisible(false); //Putzu botoia 2 inbisible jarri
	    putzuBOTOIA3.setVisible(false); //Putzu botoia 3 inbisible jarri
	        
		toolBar.add(irtenButton); //Erdian zentratzeko botoi invisiblea
		toolBar.add(Box.createHorizontalStrut(15)); //Hutsunea jarri
	    toolBar.add(backButton); //Atzera botoia toolbarrean gehitu 
	    toolBar.add(Box.createHorizontalStrut(15)); //Hutsunea jarri
	    toolBar.add(editButton); //Gehitu botoia toolbarrean gehitu 
	    toolBar.add(Box.createHorizontalStrut(15)); //Hutsunea jarri
	    toolBar.add(deleteButton); //Ezabatu botoia toolbarrean gehitu 
	    toolBar.add(Box.createHorizontalStrut(15)); //Hutsunea jarri
	    toolBar.add(confirmButton); //Konfirmatu botoia toolbarrean gehitu 	       
	        
	    toolBar.add(Box.createHorizontalStrut(200)); //Hutsunea jarri	       

	    ImageIcon icon = new ImageIcon("ikonoak/" + izena + "BANDERA.png"); //Iconoa sortu helbidearekin
	    Image scaledImage = icon.getImage().getScaledInstance(130, 70, Image.SCALE_SMOOTH); //Iconoa txikiagotu
	    ImageIcon scaledIcon = new ImageIcon(scaledImage); //Scaled icon berria sortu
	    JLabel label1 = new JLabel(scaledIcon); //Label-ean scaled icon-a gehitu
	    toolBar.add(label1); //Label-a toolbarrean gehitu
	        
	    toolBar.add(Box.createHorizontalStrut(50)); //Hutsunea jarri
	        
	    add3DTituluToolbar(); //Erabiltzailea titulua 3D bezala jarri	        
	        
	    toolBar.add(Box.createHorizontalStrut(150)); //Hutsunea jarri      
	        
	    toolBar.add(putzuBOTOIA); //Putzu botoia 1 toolbarrean gehitu        
	    toolBar.add(putzuBOTOIA2); //Putzu botoia 2 toolbarrean gehitu 
	    toolBar.add(putzuBOTOIA3); //Putzu botoia 3 toolbarrean gehitu 
	}
	
	//ADMIN PANTAILAREN TOOLBAR-A
	private void adminToolbarJarri()
	{
		toolBar = new JToolBar(); //Toolbar berri bat sortu	        
	    toolBar.setPreferredSize(new Dimension(anchoPantalla, 65)); //Tamaina jarri
	    toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); //Ezkerrean beti
	    toolBar.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina
	        	
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 3); //Borde berdea sortu
	    
	    backButton.setBackground(Koloreak.URDINILUNA); //Atzealde kolorea urdina
	    backButton.setBorder(lineBorder); //Borde berdea jarri
	    erabiltzailea.backButtonBEHIN = 0; //Atzera botoia aktibatu egiten da
	   
	    toolBar.add(adminEspacio); //Erdian zentratzeko botoi invisiblea
		toolBar.add(Box.createHorizontalStrut(15)); //Hutsunea gehitu
	    toolBar.add(backButton); //Atzera botoia toolbarrean gehitu
	}
	
	public void mostrarPantallaEditarHuecos(Erabiltzailea erabiltzailea)
	{
		pant = "main"; //Main pantailan dago
		
		if(adminMapa==2) //Admin mapa 2 baldin bada
		{
			adminMapa = 0; //Admin mapa 0ra jarri
		}
		
		int i = 0, balixu = 0;
		
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA"); //Titulua jarri
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Itxi
	    
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

	    JPanel botonesHuecosPanel = new JPanel(new GridBagLayout());
		
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
	        		button = botoiaSortu(button, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);

		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button);
		            balixu++;
	        	}
	        	else if(i==1)
	        	{
	        		button2 = botoiaSortu(button2, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = String.valueOf(currentValue).toLowerCase();
		            irudiaJarri(balio, button2);
		            balixu++;
	        	}

	        	else if(i==2)
	        	{
	        		button3 = botoiaSortu(button3, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
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
	        		button2 = botoiaSortu(button2, hizkuntza1.aukeratu, "HUECO2", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	    		    
	    			gbct.gridx = 1;
	    			botonesHuecosPanel.add(button2, gbct);
	    	    }
	        	else if(i==2)
	    	    {
	        		button3 = botoiaSortu(button3, hizkuntza1.aukeratu, "HUECO3", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	    		    
	    			gbct.gridx = 2;
	    			botonesHuecosPanel.add(button3, gbct);
	    	    }
	        	else
	        	{	        		
	        		button = botoiaSortu(button, hizkuntza1.aukeratu, "HUECO", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        	}
	        }
	    } 
	    
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
		pant = "main"; //Main pantailan dago
		
		if(adminMapa==2) //Admin mapa 2 baldin bada
		{
			adminMapa = 0; //Admin mapa 0ra jarri
		}
		
	    mainPANTAILA = new JFrame("PANTAILA PRINTZIPALA"); //Titulua jarri
	    mainPANTAILA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Itxi
	    
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

	    JPanel botonesHuecosPanel = new JPanel(new GridBagLayout());
		
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
	        		button = botoiaSortu(button, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button);
	        	}
	        	else if(i==1)
	        	{
	        		button2 = botoiaSortu(button2, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button2);
	        	}

	        	else if(i==2)
	        	{
	        		button3 = botoiaSortu(button3, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button3);
	        	}
	        }
	    }
	    
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
	    mainPANTAILA2 = new JFrame("PANTAILA WAIT"); //Titulua jarri
	    mainPANTAILA2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Itxi
	    
	    if(maxPantaila==1)
		{
			mainPANTAILA2.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else 
		{
		    mainPANTAILA2.setSize(anchoPantalla-anchoPantalla/10, altoPantalla-altoPantalla/10);
		    mainPANTAILA2.setLocationRelativeTo(null);
		}	
	  	
	    mainPANTAILA2.setUndecorated(true);

	    JPanel panel = new JPanel(new BorderLayout());

	    JPanel botonesHuecosPanel = new JPanel(new GridBagLayout());
		
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
	        		button = botoiaSortu(button, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button);
	        	}
	        	else if(i==1)
	        	{
	        		button2 = botoiaSortu(button2, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button2);
	        	}

	        	else if(i==2)
	        	{
	        		button3 = botoiaSortu(button3, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, button3);
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
	
	/*public void mostrarPantallaWait(Erabiltzailea erabiltzailea) 
	{
	    mainPANTAILA2 = new JFrame("PANTAILA WAIT");
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
	    
	    /*JButton buton = new JButton(); //EZKERREKO BOTOIA
	    
		JButton buton2 = new JButton(); //ERDIKO BOTOIA
		
		JButton buton3 = new JButton(); //ESKUINEKO BOTOIA/

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
	        		button = botoiaSortu(button, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        		    
	        		gbct.gridx = 0;
	        	    gbct.gridy = 0;
	        		botonesHuecosPanel.add(button, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, /*butonbutton/);
	        	}
	        	else if(i==1)
	        	{
	        		button2= botoiaSortu(button2, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 1;
	        		botonesHuecosPanel.add(button2, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, /*buton2/button2);
	        	}

	        	else if(i==2)
	        	{
	        		button3 = botoiaSortu(button3, null, null, lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        	    
	        		gbct.gridx = 2;
	        		botonesHuecosPanel.add(button3, gbct);
	        		
		    	    balio = balio.toLowerCase();
		            irudiaJarri(balio, /*buton3/button3);
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
	}*/
	
	//EZABATZEKO AUKERA DESBERDINAK
	public void mostrarOpcionesEliminar()
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza gordetzen du
		aukerenPANTAILA = new JFrame(hizkuntza1.ezabatuEsaldia); //JFrame berri bat sortu
        aukerenPANTAILA.setSize(500, 300); //Tamaina jarri
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Itxi

        JPanel opcionesPanel = new JPanel(new GridLayout(1,3)); //JPanel berri bat sortu
        //opcionesPanel.setLayout(new GridLayout(1,3));  
        
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 6); //Borde berdea sortu
	    
        int kop=0; //Kopurua hasieratu
        
        for (int i = 0; i < erabiltzailea.getBalioZerrenda().size(); i++) //Erabiltzaile horren balio zerrenda osoa begiratzen du
        {
	        String balio = erabiltzailea.getBalioZerrenda().get(i); //Balioa gorde
	        
	        if(balio!=null) //Balioa 0 baldin bada
	        {
	        	kop++; //Kopurua gehi 1
	        }
        }
        
        if(kop==3) //Kopurua 3 baldin bada
        {
        	JButton ezkerra = null, erdia = null, eskuma = null; //Ezkerra, Erdia eta Eskuma botoiak hasieratu        	
        	ezkerra = botoiaSortu(ezkerra, hizkuntza1.ezkerra, "EZKERREKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        /*JButton ezkerra = new JButton(hizkuntza1.ezkerra);
	        ezkerra.setActionCommand("EZKERREKOA");
	        ezkerra.addActionListener(kontrolatzailea);
	        ezkerra.setBackground(Koloreak.URDINILUNA);
	        ezkerra.setBorder(lineBorder);
	        ezkerra.setForeground(Koloreak.ZURIA);*/
	        opcionesPanel.add(ezkerra); //Ezkerra botoia JPanel-era gehitu
	
	        erdia = botoiaSortu(erdia, hizkuntza1.erdia, "ERDIKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        /*JButton erdia = new JButton(hizkuntza1.erdia);
	        erdia.setActionCommand("ERDIKOA");
	        erdia.addActionListener(kontrolatzailea);
	        erdia.setBackground(Koloreak.URDINILUNA);
	        erdia.setBorder(lineBorder);
	        erdia.setForeground(Koloreak.ZURIA);*/
	        opcionesPanel.add(erdia); //Erdia botoia JPanel-era gehitu
	
	        eskuma = botoiaSortu(eskuma, hizkuntza1.eskuma, "ESKUINEKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
	        /*JButton eskuma = new JButton(hizkuntza1.eskuma);
	        eskuma.setActionCommand("ESKUINEKOA");
	        eskuma.addActionListener(kontrolatzailea);
	        eskuma.setBackground(Koloreak.URDINILUNA);
	        eskuma.setBorder(lineBorder);
	        eskuma.setForeground(Koloreak.ZURIA);*/
	        opcionesPanel.add(eskuma); //Eskuma botoia JPanel-era gehitu
        }
        else if(kop==2) //Kopurua 2 baldin bada
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)==null) //Eskuineko botoia null baldin bada
        	{
        		JButton ezkerra = null, eskuma = null; //Ezkerra eta Eskuma botoiak hasieratu        				
        		ezkerra = botoiaSortu(ezkerra, hizkuntza1.ezkerra, "EZKERREKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton ezkerra = new JButton(hizkuntza1.ezkerra);
    	        ezkerra.setActionCommand("EZKERREKOA");    
    	        ezkerra.addActionListener(kontrolatzailea);
    	        ezkerra.setBackground(Koloreak.URDINILUNA);
    	        ezkerra.setBorder(lineBorder);
    	        ezkerra.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(ezkerra); //Ezkerra botoia JPanel-era gehitu
    	
    	        eskuma = botoiaSortu(eskuma, hizkuntza1.eskuma, "ERDIKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
    	        /*JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ERDIKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(eskuma); //Eskuma botoia JPanel-era gehitu
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)==null) //Erdiko botoia null baldin bada
        	{
        		JButton ezkerra = null, eskuma = null; //Ezkerra eta Eskuma botoiak hasieratu       				
        		ezkerra = botoiaSortu(ezkerra, hizkuntza1.ezkerra, "EZKERREKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton ezkerra = new JButton(hizkuntza1.ezkerra);
        		ezkerra.setActionCommand("EZKERREKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(ezkerra); //Ezkerra botoia JPanel-era gehitu
    	
    	        eskuma = botoiaSortu(eskuma, hizkuntza1.eskuma, "ESKUINEKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
    	        /*JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ESKUINEKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(eskuma); //Eskuma botoia JPanel-era gehitu
        	}
        	else //Ezkerreko botoia null baldin bada
        	{
        		JButton ezkerra = null, eskuma = null; //Ezkerra eta Eskuma botoiak hasieratu         			
        		ezkerra = botoiaSortu(ezkerra, hizkuntza1.ezkerra, "ERDIKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton ezkerra = new JButton(hizkuntza1.ezkerra);
        		ezkerra.setActionCommand("ERDIKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(ezkerra); //Ezkerra botoia JPanel-era gehitu
    	
    	        eskuma = botoiaSortu(eskuma, hizkuntza1.eskuma, "ESKUINEKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
    	        /*JButton eskuma = new JButton(hizkuntza1.eskuma);
    	        eskuma.setActionCommand("ESKUINEKOA");
    	        eskuma.addActionListener(kontrolatzailea);
    	        eskuma.setBackground(Koloreak.URDINILUNA);
    	        eskuma.setBorder(lineBorder);
    	        eskuma.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(eskuma); //Eskuma botoia JPanel-era gehitu
        	}	        
        } 
        else if(kop==1) //Kopurua 1 baldin bada
        {
        	if(erabiltzailea.getBalioZerrenda().get(2)!=null) //Eskuineko botoia null baldin bada
        	{
        		JButton eskuma = null; //Eskuma botoia hasieratu       		
        		eskuma = botoiaSortu(eskuma, hizkuntza1.eskuma, "ESKUINEKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton eskuma = new JButton(hizkuntza1.deleteButtom);
        		eskuma.setActionCommand("ESKUINEKOA");
        		eskuma.addActionListener(kontrolatzailea);
        		eskuma.setBackground(Koloreak.URDINILUNA);
        		eskuma.setBorder(lineBorder);
        		eskuma.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(eskuma); //Eskuma botoia JPanel-era gehitu
        	}
        	else if(erabiltzailea.getBalioZerrenda().get(1)!=null) //Erdiko botoia null baldin bada
        	{
        		JButton erdia = null; //Erdia botoia hasieratu        		
        		erdia = botoiaSortu(erdia, hizkuntza1.deleteButtom, "ERDIKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton erdia = new JButton(hizkuntza1.deleteButtom);
        		erdia.setActionCommand("ERDIKOA");
        		erdia.addActionListener(kontrolatzailea);
        		erdia.setBackground(Koloreak.URDINILUNA);
        		erdia.setBorder(lineBorder);
        		erdia.setForeground(Koloreak.ZURIA);*/
     	        opcionesPanel.add(erdia); //Erdia botoia JPanel-era gehitu
        	}
        	else //Ezkerreko botoia null baldin bada
        	{
        		JButton ezkerra = null; //Ezkerra botoia hasieratu        		
        		ezkerra = botoiaSortu(ezkerra, hizkuntza1.ezkerra, "EZKERREKOA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        		/*JButton ezkerra = new JButton(hizkuntza1.deleteButtom);
        		ezkerra.setActionCommand("EZKERREKOA");    
        		ezkerra.addActionListener(kontrolatzailea);
        		ezkerra.setBackground(Koloreak.URDINILUNA);
        		ezkerra.setBorder(lineBorder);
        		ezkerra.setForeground(Koloreak.ZURIA);*/
    	        opcionesPanel.add(ezkerra); //Ezkerra botoia JPanel-era gehitu
        	}
        }

        aukerenPANTAILA.getContentPane().add(opcionesPanel); //Opciones JPanel-a, aukeren JFrame barruan jarri
        aukerenPANTAILA.setLocationRelativeTo(ordenagailuarenPANTAILA); //Pantailaren erdi erdian jarri
        aukerenPANTAILA.setVisible(true); //Bistaratu
	}
	
	//BOTOI PRINTZIPAL BAKOITZEAN JARRI AHAL DIREN AUKERAK
	public void mostrarOpciones()
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu
		aukerenPANTAILA = new JFrame(hizkuntza1.aukerak); //JFrame berria sortu
        aukerenPANTAILA.setSize(500, 300); //JFrame-aren tamaina
        aukerenPANTAILA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Itxi

        JPanel opcionesPanel = new JPanel(new GridLayout(1, 3)); //JPanel berria sortu
        //opcionesPanel.setLayout(new GridLayout(1, 3));
        
	    LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 6); //Borde berdea sortu

        JButton pozoButton = null; //Putzu botoia hasieratu
        pozoButton = botoiaSortu(pozoButton, hizkuntza1.urmaila, "URMAILA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        /*pozoButton = new JButton(hizkuntza1.putzua);
        pozoButton.setActionCommand("PUTZUA");
        pozoButton.addActionListener(kontrolatzailea);
        pozoButton.setBackground(Koloreak.URDINILUNA);
        pozoButton.setBorder(lineBorder);
        pozoButton.setForeground(Koloreak.ZURIA);*/
        opcionesPanel.add(pozoButton); //Putzu botoia aukeren panelera gehitu

        JButton ventiladorButton = null; //Haizegailu botoia hasieratu
        ventiladorButton = botoiaSortu(ventiladorButton, hizkuntza1.tenperatura, "TENPERATURA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        /*ventiladorButton = new JButton(hizkuntza1.haizegailua);
        ventiladorButton.setActionCommand("HAIZEGAILUA");
        ventiladorButton.addActionListener(kontrolatzailea);
        ventiladorButton.setBackground(Koloreak.URDINILUNA);
        ventiladorButton.setBorder(lineBorder);
        ventiladorButton.setForeground(Koloreak.ZURIA);*/
        opcionesPanel.add(ventiladorButton); //Haizegailu botoia aukeren panelera gehitu

        JButton fugaButton = null; //Berogailu botoia hasieratu
        fugaButton = botoiaSortu(fugaButton, hizkuntza1.gasa, "GASA", lineBorder, 300, 700, Koloreak.URDINILUNA, true);
        /*fugaButton = new JButton(hizkuntza1.berogailua);
        fugaButton.setActionCommand("BEROGAILUA");
        fugaButton.addActionListener(kontrolatzailea);
        fugaButton.setBackground(Koloreak.URDINILUNA);
        fugaButton.setBorder(lineBorder);
        fugaButton.setForeground(Koloreak.ZURIA);*/
        opcionesPanel.add(fugaButton); //Berogailu botoia aukeren panelera gehitu

        aukerenPANTAILA.getContentPane().add(opcionesPanel); //Opciones panel aukeren pantailan jartzen du
        aukerenPANTAILA.setLocationRelativeTo(ordenagailuarenPANTAILA); //Pantailaren erdi erdian jartzeko
        aukerenPANTAILA.setVisible(true); //Bisiblea jarri
	}
	
	//ARGAZKI DESBERDINAK SARTU
	private void argazkiaSartu(String argazkia, int lekua) 
	{
	    //erabiltzailea = modeloa.getErabiltzailea(izena);
	    if (erabiltzailea != null) //Erabiltzailea null ez den bitartean
	    {
	        switch (lekua) //Zein posiziotan dago
	        {
	            case 1: //Ezkerreko botoia	      
	                irudiaJarri(argazkia, button); //Irudia jarri
	                erabiltzailea.buttonBEHIN = 1; //Ezkerreko botoian irudia jarri da
	                posizioa=0; //Posizioa 0 da
	                break;

	            case 2: //Erdiko botoia	            	
	                irudiaJarri(argazkia, button2); //Irudia jarri
	                erabiltzailea.button2BEHIN = 1; //Erdiko botoian irudia jarri da
	                posizioa = 1; //Posizioa 1 da
	                break;

	            case 3: //Eskuineko botoia
	                irudiaJarri(argazkia, button3); //Irudia jarri
	                erabiltzailea.button3BEHIN = 1; //Eskuineko botoian irudia jarri da
	                posizioa=2; //Posizioa 2 da
	                break;
	        }
	    }
	}
	
	//IRUDI DESBERDINAK JARRi
	private void irudiaJarri(String argazkia, JButton botoia)
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(hizkuntza); //Momentuko hizkuntza hartu

        ImageIcon icon = new ImageIcon("ikonoak/" + argazkia + ".png"); //Iconoa sortu helbidearekin
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); //Irudia txikiagotu

        botoia.setIcon(new ImageIcon(scaledImage)); //Txikiagotutako irudia botoian jarri
        botoia.setText(hizkuntza1.kargatzen); //Testua jarri 
        botoia.setFont(LetraMotak.KARGATZEN); //Testu mota jarri
        botoia.setForeground(Koloreak.ZURIA); //Testua zuria jarri
		botoia.setHorizontalTextPosition(SwingConstants.CENTER); //Erdian kokatu
		botoia.setVerticalTextPosition(SwingConstants.BOTTOM); //Beheko aldean kokatu

        aukerenPANTAILA.dispose(); //Aukeren pantaila itxi
	}
	
	@Override
	//PROPERTY CHANGE
	public void propertyChange(PropertyChangeEvent e) 
	{
	    String propiedad = e.getPropertyName(); //Propietatea irakurri
	    int lekua = (int) e.getNewValue(); //Posizioa irakurri

	    //erabiltzailea = modeloa.getErabiltzailea(izena);
	    if (erabiltzailea != null) //Erabiltzailea null ez den bitartean
	    {
	        switch (propiedad) //Propietate desberdinak
	        {
	            case Modeloa.PROPIETATEA: //Ur Maila aukeratu da
	                argazkiaSartu(Modeloa.PROPIETATEA, lekua); //Argazkia sartu
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "urmaila"); //Posizioa kontuan izanda ur maila jarri erabiltzaile horren balio zerrendan
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1; //Erabiltzaileak sartu du daturen bat
	                break;

	            case Modeloa.PROPIETATEA2: //Tenperatura aukeratu da
	                argazkiaSartu(Modeloa.PROPIETATEA2, lekua); //Argazkia sartu
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "tenperatura"); //Posizioa kontuan izanda tenperatura jarri erabiltzaile horren balio zerrendan
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda()); 
	                erabiltzailea.behin = 1; //Erabiltzaileak sartu du daturen bat
	                break; 

	            case Modeloa.PROPIETATEA3: //Gasa aukeratu da
	                argazkiaSartu(Modeloa.PROPIETATEA3, lekua);
	                erabiltzailea.setBalioZerrendaAtIndex(posizioa, "gasa"); //Posizioa kontuan izanda gasa jarri erabiltzaile horren balio zerrendan
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                erabiltzailea.behin = 1; //Erabiltzaileak sartu du daturen bat  
	                break;
	                
	            case Modeloa.PROPIETATEA4: //Pantaila aktualizatzen da
	                System.out.println("PANTAILA AKTUALIZATZEN ARI DA");
	                actualizarPantalla(); //Pantaila aktualizatu
	                break;
	                
	            case Modeloa.PROPIETATEA5: //Ezkerreko botoia aukeratu da
	                erabiltzailea.setBalioZerrendaAtIndex(0, null); //0 posizioan null jarri
	                erabiltzailea.buttonBEHIN = 0; //Ezkerreko botoi ezabatu da
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose(); //Aukeren pantaila itxi
	                break;
	                
	            case Modeloa.PROPIETATEA6: //Erdiko botoia aukeratu da
	            	erabiltzailea.setBalioZerrendaAtIndex(1, null); //1 posizioan null jarri
	            	erabiltzailea.button2BEHIN = 0; //Erdiko botoia ezabatu da
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose(); //Aukeren pantaila itxi
	                break;
	                
	            case Modeloa.PROPIETATEA7: //Eskuineko botoia aukeratu da
	            	erabiltzailea.setBalioZerrendaAtIndex(2, null); //2 posizioan null jarri
	            	erabiltzailea.button3BEHIN = 0; //Eskuineko botoia ezabatu da
	                System.out.println(izena + " balioa: " + erabiltzailea.getBalioZerrenda());
	                aukerenPANTAILA.dispose(); //Aukeren pantaila itxi
	                break;
	                
	            case Modeloa.PROPIETATEA8: //Konfirmatu botoiak parpadeatzeko
	            	System.out.println("KONFIRMATU BOTOIA PARPADEATZEN ARI DA");
	            	confirmButton.setVisible(false); //Konfirmatu botoia ezkutatu
	                actualizarPantalla2(); //Konfirmatu botoiak parpadeatzea
	                confirmButton.setVisible(true); //Konfirmatu botoia bistaratu
	                break;
	            case Modeloa.PROPIETATEA9: //Hasi Saioa botoiari ematean, pantailen arteko aldaketak ez ikusteko
	            	System.out.println("Recibida actualización de PROPIETATEA9");
	            	loginPANTAILA.dispose(); //Login pantaila itxi
	                break; 
	            case Modeloa.PROPIETATEA10: //Gehitu botoiari ematean, pantailen arteko aldaketak ez ikusteko
	            	modeloa.timer4.setRepeats(false); //Timer4 behin bakarrik
	            	modeloa.timer4.start(); //Timer4 martxan hasi
	        		mainPANTAILA.dispose(); //Main pantaila itxi
	        		if(zer == "gehitu") //Gehitu botoiari eman zaio
	        		{
	        			mostrarPantallaEditarHuecos(erabiltzailea); //Hutsik dauden botoiak sortzen dira, gehitu botoiari eman zaiolako
	        		}
	        		else if(zer == "konfirmatu")  //Konfirmatu botoiari eman zaio
	        		{
	        			mostrarPantallaHuecosCompletado(erabiltzailea); //Erabiltzailearen datuekin pantaila bistaratu
	        		}
	        		else if(zer == "hutsik") //Hutsik
	        		{
	        			mostrarPantallaHuecos(); //Hutsik dagoen pantaila bistaratu
	        		}
	                break;
	            case Modeloa.PROPIETATEA11: //Gehitu botoiari ematean, wait pantailaren arteko aldaketak ez ikusteko
	            	mainPANTAILA2.dispose(); //Main pantaila 2 itxi egiten da    	
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
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        ImageIcon ludok = new ImageIcon("ludok/arkitektura.jpg"); //Irudia
        JLabel lludok = new JLabel(ludok); //Irudia jartzeko Label-a
        lludok.setBounds(0, 0, /*(int) screenSize.getWidth()*/anchoPantalla, /*(int) screenSize.getHeight()*/altoPantalla); 
        JLayeredPane ludokdPane = new JLayeredPane(); //Layered Pane bat sortu
        ludokdPane.add(lludok); //Label-ari irudia jarri
        JFrame ludokFrame = new JFrame("DJ LUDOK"); //JFrame barri bat sortu
		 ludokFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame-a itxi exit egitean
		 ludokFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Pantaila osoan jarri		 
		 //ludokFrame.setUndecorated(true);
		 ludokFrame.getContentPane().add(ludokdPane); //Jframe-an Jlayered Pane-a jarri
		 //ludokFrame.pack();
		 ludokFrame.setVisible(true); //Bisible izatea
	}
	/*
	 
	 L    U U  D    OOO  K K       L    U U  D    OOO  K K
	 L    U U  D D  O O  K         L    U U  D D  O O  K 
	 LLL  UUU  D    OOO  K K       LLL  UUU  D    OOO  K K
	 
	 */
	
	
	//MAIN-A
	public static void main(String[] args) 
	{
		//MODELOA
		Modeloa modeloa = new Modeloa (); //Modeloa
		
		//KONTROLATZAILEA
		Kontrolatzailea kontrolatzailea = new Kontrolatzailea(modeloa);  //Kontrolatzailea
		
		//MQTT
        Mqtt mqtt1 = null; //Mqtt hasieratu
        try 
        {
        	mqtt1 = new Mqtt(BROKER,CLENT_ID); //Broker-aren izena
		} 
        catch (MqttException e) //Exception MQTT
        {
			e.printStackTrace(); //Exception barruan sartzen bada, arrazoia printeatuko du
		}

        //MAIN	
		Printzipala main = new Printzipala(modeloa,kontrolatzailea, mqtt1);	//Bista
		
		//HASIERAKO FUNTZIOA
		main.loginPantailaErakutzi(); //Main
	}
}