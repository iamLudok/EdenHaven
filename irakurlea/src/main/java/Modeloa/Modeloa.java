package Modeloa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import Bista.Printzipala;
import Kontrolatzailea.NireAkzioa;

public class Modeloa implements ActionListener
{
	//PROPIETATEAK
	public final static String PROPIETATEA = "putzua";
	public final static String PROPIETATEA2 = "haizegailua";
	public final static String PROPIETATEA3 = "berogailua";
	public final static String PROPIETATEA4 = "denboraPasaDa";
	public final static String PROPIETATEA5 = "ezk";
	public final static String PROPIETATEA6 = "erd";
	public final static String PROPIETATEA7 = "esk";
	public final static String PROPIETATEA8 = "berdeaTimer";
	public final static String PROPIETATEA9 = "eliminar3Timer";
	public final static String PROPIETATEA10 = "eliminarTimer";
	public final static String PROPIETATEA11 = "eliminar2Timer";
	
	//COLLECTIONS
	private Map<String, Erabiltzailea> erabiltzaileak; //Erabiltzaileen Mapa
	private Map<String, Hizkuntza> hizkuntzak; //Erabiltzaileen Mapa

	//TIMER-AK	
	public Timer timer=null; //Pantaila aktualizatzeko timer-a
	public Timer timer2 = null; //Konfirmatu Botoiaren parpadeo berdea
	public Timer timer3 = null; //Pantaila batetik bestera dagoen denbora luzatzeko
	public Timer timer4 = null; //Pantaila batetik bestera dagoen denbora luzatzeko
	public Timer timer5 = null; //Pantaila batetik bestera dagoen denbora luzatzeko
	
	int maxHiz = 0;
	
	//MVC
	Printzipala irudia; //Bista
	
	//PROPERTY CHANGE
	PropertyChangeSupport konektorea; //Konektorea
		
	//KONSTRUKTOREA
	public Modeloa() 
	{
	    irudia = null; //Bista
		konektorea = new PropertyChangeSupport(this); //Konektorea
		
		timer = new Timer(1000, this); //Pantaila aktualizatzeko timer-a
	    timer.setActionCommand("Denbora"); //Pantaila aktualizatzeko timer-a
	    timer2 = new Timer(500, this); //Konfirmatu Botoiaren parpadeo berdea
	    timer2.setActionCommand("Denbora2"); //Konfirmatu Botoiaren parpadeo berdea
	    timer3 = new Timer(1000, this);
	    timer3.setActionCommand("Denbora4");
	    timer4 = new Timer(1000, this);
	    timer4.setActionCommand("Denbora5");
	    timer5 = new Timer(500, this);
	    timer5.setActionCommand("Denbora3");
	    
	    erabiltzaileak = new HashMap<>(); //Erabiltzaileen Mapa
	    hizkuntzak = new HashMap<>();
	}
	
	//BISTA
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia;
	}
	
	//ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
	public void erabiltzaileakIrakurri() 
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("erabiltzaileak.txt"))) 
	    {
	        String linea; //Lerro bakoitza
	        
	        while ((linea = br.readLine()) != null) 
	        {
	            String[] partes = linea.split(":"); //Izena eta pasahitza desberdintzeko bi puntu erdian
	            if (partes.length >= 2) //Zenbat zati dituen fila bakoitzak
	            {
	                String izena = partes[0].trim(); //Izena bezala gorde
	                String contrasena = partes[1].trim(); //Pasahitz bezala gorde

	                Erabiltzailea erabiltzailea = new Erabiltzailea(); //Erabiltzaile berria sortu
	                erabiltzailea.setPasahitza(contrasena); //Erabiltzaileari pasahitza jarri
	                erabiltzaileak.put(izena, erabiltzailea); //Erabiltzaileak Mapan jarri
	            }
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public void hizkuntzakIrakurri() 
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("hizkuntzak.txt"))) 
	    {
	        String linea;
	        while ((linea = br.readLine()) != null) 
	        {
	            String hizkuntza1 = linea;
	            Hizkuntza hizkuntza = new Hizkuntza();
	            hizkuntza.setIzena(hizkuntza1);
	            hizkuntzak.put(hizkuntza1, hizkuntza);
	            System.out.println(hizkuntza.getIzena());
	            maxHiz++;
	            
	            try (BufferedReader br2 = new BufferedReader(new FileReader(hizkuntza1 + ".txt"))) 
			    {
			        String lineea = null;
			        while ((lineea = br2.readLine()) != null) 
			        {
			        	 String[] partes = lineea.split(":");
			            String datua = partes[0].trim();
			            hizkuntza.backButtom = datua;
			            System.out.println(hizkuntza.backButtom);
			            
			            String datua2 = partes[1].trim();
			            hizkuntza.editButtom = datua2;	 
			            System.out.println(hizkuntza.editButtom);
			            
			            String datua3 = partes[2].trim();
			            hizkuntza.deleteButtom = datua3;
			            System.out.println(hizkuntza.deleteButtom);
			            
			            String datua4 = partes[3].trim();
			            hizkuntza.confirmButtom = datua4;
			            System.out.println(hizkuntza.confirmButtom);
			            
			            String datua5 = partes[4].trim();
			            hizkuntza.aukerak = datua5;
			            
			            String datua6 = partes[5].trim();
			            hizkuntza.putzua = datua6;	 
			            
			            String datua7 = partes[6].trim();
			            hizkuntza.haizegailua = datua7;
			            
			            String datua8 = partes[7].trim();
			            hizkuntza.berogailua = datua8;
			        }
			    } 
			    catch (IOException e) 
			    {
			        e.printStackTrace();
			    }
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	/*public void hizkuntzaBakoitzaIrakurri() 
	{
		Hizkuntza izena;
		for(int i=0;i<maxHiz;i++) 
		{
			izena = hizkuntzak.get(i);
		    try (BufferedReader br = new BufferedReader(new FileReader(String.valueOf(izena) + ".txt"))) 
		    {
		        String linea;
		        while ((linea = br.readLine()) != null) 
		        {
		        	 String[] partes = linea.split(":");
		            String datua = partes[0].trim();
		            izena.backButtom = datua;	
		            String datua2 = partes[1].trim();
		            izena.editButtom = datua2;	 
		            String datua3 = partes[2].trim();
		            izena.deleteButtom = datua3;	 
		            String datua4 = partes[3].trim();
		            izena.confirmButtom = datua4;	 
		        }
		    } 
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		}
	}*/
	
	//ERABILTZAILE HORREN PASAHITZA HORI DEN EDO EZ ESATEN DU
	public boolean isUsuarioContraseña(String nombre, String contrasena) 
	{
		irudia.erabiltzailea = getErabiltzailea(nombre);

	    if (irudia.erabiltzailea != null) 
	    {
	        return contrasena.equals(irudia.erabiltzailea.getPasahitza());
	    }

	    return false;
	}
	
	//ARGELIAKO BABES ETXEAN CLICK EGITEAN...
	public void argeliaBABESETXEclick()
	{		
		irudia.izena = "argelia"; //Erabiltzailearen izena aldatu
		irudia.erabiltzailea = getErabiltzailea("argelia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzaile horren pantaila aurretik gordetako elementuekin erakusten du
		irudia.adminFrame.dispose(); //Maparen pantaila itxi egiten du
        irudia.adminMapa = 1;
	}
	
	//GROENLANDIAKO BABES ETXEAN CLICK EGITEAN...
	public void groenlandiaBABESETXEclick()
	{		
		irudia.izena = "groenlandia"; //Erabiltzailearen izena aldatu
		irudia.erabiltzailea = getErabiltzailea("groenlandia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzaile horren pantaila aurretik gordetako elementuekin erakusten du
		irudia.adminFrame.dispose(); //Maparen pantaila itxi egiten du
    	irudia.adminMapa = 1;
	}		
	
	//LOGIN PANTAILAN IZENAREN TOKIAN JARRITAKOAREN ARABERA...
	public void autenticarUsuario() 
	{
		irudia.izena = irudia.izenaField.getText(); //Izena hartzen du
        String contrasena = new String(irudia.pasahitzaField.getPassword()); //Pasahitza hartzen du      
        
		if (autenticar(irudia.izena, contrasena)) //Izena eta pasahitza hutsuneak beteta daueden edo ez esaten du
		{			
            if (("admin".equals(irudia.izena)) && ("admin".equals(contrasena))) //Admin Admin jartzean maparen pantaila zabaltzen du
            {       
            	//timerEliminar3.setRepeats(false);
            	//timerEliminar3.start(); 
                irudia.mostrarPantallaAdmin(); //Admin pantaila irekitzen da, mapa ageri dena
                irudia.loginPANTAILA.dispose(); //Login pantaila itxi egiten du
            } 
            else if (isUsuarioContraseña(irudia.izena,contrasena)) //Txt barnean dagoen erabiltzaileren bat jartzean...
            {            	
            	irudia.erabiltzailea = getErabiltzailea(irudia.izena); //Erabiltzailearen datuak hartzen ditu  
            	
            	if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
            	{          		                
            		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
            	}
            	else
            	{
            		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
            	}  
            	timer5.setRepeats(false); //Ez errepikatzea timer-a
    	    	timer5.start(); //Timer-a hasi
            }         
            else if ("Ludok".equals(irudia.izena) && "luken".equals(contrasena)) //Ludok luken jartzean EASTER EGG-a AURKITZEN DA
            {           	
                irudia.ludokLovesU(); //Easter egg
                irudia.loginPANTAILA.dispose(); //Login pantaila itxi egiten du
                //timerEliminar3.setRepeats(false);
    	    	//timerEliminar3.start();
            }
            else 
            {
                JOptionPane.showMessageDialog(null, "Pasahitza ez da zuzena"); //Pasahitza ez baldin baduzu asmatzen
            }
            //timerEliminar3.setRepeats(false);
	    	//timerEliminar3.start();
        } 
		else
        {
            JOptionPane.showMessageDialog(null, "Akatz bat gertatu da. Saiatu berriz."); //Izena jarri behar den lekuan ezer ez baldin baduzu jartzen
        }
	}
	
	//IZENA ETA PASAHITZA KASILLAK EA BETETA DAUDEN BEGIRATZEN DU
    public boolean autenticar(String nombre, String contrasena) 
    {
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
    }
    
    //GET-AK
    public Erabiltzailea getErabiltzailea(String nombre) 
	{
	    return erabiltzaileak.get(nombre);
	}
    
    public Hizkuntza getHizkuntza(String nombre) 
	{
	    return hizkuntzak.get(nombre);
	}
	
    //EA ERABILTZAILEA EXISTITZEN DEN ESATEN DU
	public boolean isUsuarioRegistrado(String nombre) 
	{
	    return erabiltzaileak.containsKey(nombre);
	}
    
	//ADD PROPERTY CHANGE LISTENER
	public void addPropertyChangeListener(PropertyChangeListener listener) 
	{
		konektorea.addPropertyChangeListener(listener);
	}

	//REMOVE PROPERTY CHANGE LISTENER
	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
		konektorea.removePropertyChangeListener(listener);
	}
	
	//PRIPIETATEAK BISTARA BIDALTZEN DITU FIRE PROPERTY CHANGE ERABILIZ
	public void bota(String mota, int posizioa)
	{
		switch (mota) 
		{
			case "putzua": //Putzua aukera aukeratzen baldin bada
			{
				konektorea.firePropertyChange(PROPIETATEA, null, posizioa);
				break;
			}
			case "haizegailua": //Haizegailu aukera aukeratzen baldin bada
			{
				konektorea.firePropertyChange(PROPIETATEA2, null, posizioa);
				break;
			}
			case "berogailua": //Berogailu aukera aukeratzen baldin bada
			{
				konektorea.firePropertyChange(PROPIETATEA3, null, posizioa);
				break;
			}
			case "ezk": //Ezkerreko botoia ezabatu
			{
				konektorea.firePropertyChange(PROPIETATEA5, null, posizioa);
				break;
			}
			case "erd": //Erdiko botoia ezabatu 
			{
				konektorea.firePropertyChange(PROPIETATEA6, null, posizioa);
				break;
			}
			case "esk": //Eskuineko botoia ezabatu 
			{
				konektorea.firePropertyChange(PROPIETATEA7, null, posizioa);
				break;
			}
		}
	}
	
	@Override
	//ACTION PERFORMED
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand(); //Komandoa irakurri
		
		switch(command) 
		{
	        case "Denbora": //Pantaila aktualizatzeko timer-a
	            konektorea.firePropertyChange(PROPIETATEA4, 0, 1);
	            break;      
	        case "Denbora2": //Konfirmatu Botoiaren parpadeo berdea
	            konektorea.firePropertyChange(PROPIETATEA8, 0, 1);
	            break;
	        case "Denbora3": //Pantaila batetik bestera dagoen denbora luzatzeko
	            konektorea.firePropertyChange(PROPIETATEA9, 0, 1);
	            break;
	        case "Denbora4": //Pantaila batetik bestera dagoen denbora luzatzeko
	            konektorea.firePropertyChange(PROPIETATEA10, 0, 1);
	            break;
	        case "Denbora5": //Pantaila batetik bestera dagoen denbora luzatzeko
	            konektorea.firePropertyChange(PROPIETATEA11, 0, 1);
	            break;
		}
	}
}