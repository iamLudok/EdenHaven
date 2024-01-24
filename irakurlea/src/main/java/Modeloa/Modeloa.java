package Modeloa;

import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import Bista.Printzipala;

public class Modeloa implements ActionListener
{
	//MVC
	private Printzipala irudia; //Bista
	
	//PROPIETATEAK (STRING)
	public final static String PROPIETATEA = "putzua"; //Putzua aukeratu da
	public final static String PROPIETATEA2 = "haizegailua"; //Haizegailua aukeratu da
	public final static String PROPIETATEA3 = "berogailua"; //Berogailua aukeratu da
	public final static String PROPIETATEA4 = "denboraPasaDa"; //Pantaila aktualizatzen da
	public final static String PROPIETATEA5 = "ezk"; //Ezkerreko botoia aukeratu da
	public final static String PROPIETATEA6 = "erd"; //Erdiko botoia aukeratu da
	public final static String PROPIETATEA7 = "esk"; //Eskuineko botoia aukeratu da
	public final static String PROPIETATEA8 = "berdeaTimer"; //Konfirmatu botoiak parpadeatzeko
	public final static String PROPIETATEA9 = "eliminar3Timer";
	public final static String PROPIETATEA10 = "eliminarTimer";
	public final static String PROPIETATEA11 = "eliminar2Timer";
	public final static String PROPIETATEA12 = "eliminar4Timer"; //Login pantailan hizkuntzak aldatzean pantaila aldaketa ez ikusteko
	
	//MAPAK
	private Map<String, Erabiltzailea> erabiltzaileak; //Erabiltzaileen Mapa
	private Map<String, Hizkuntza> hizkuntzak; //Erabiltzaileen Mapa

	//TIMER-AK	
	public Timer timer=null; //Pantaila aktualizatzeko timer-a
	public Timer timer2 = null; //Konfirmatu Botoiaren parpadeo berdea
	public Timer timer3 = null; //
	public Timer timer4 = null; //
	public Timer timer5 = null; //
	
	//PROPERTY CHANGE
	private PropertyChangeSupport konektorea; //Konektorea
		
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
	    hizkuntzak = new HashMap<>(); //Hizkuntzen mapa
	}
	
	//BISTA
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia; //Bista
	}
	
	//ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
	public void erabiltzaileakIrakurri() 
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("erabiltzaileak.txt"))) //erabiltzaileak.txt fitxategia irakurtzen ahalegintzen da
	    {
	        String linea; //Lerro bakoitza
	        
	        while ((linea = br.readLine()) != null) //Lerroa null ez baldin bada
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
	    catch (IOException e)  //Exception
	    {
	        e.printStackTrace(); //Exceptionak saltatu eskero zer gertatu den esateko
	    }
	}
	
	//HIZKUNTZEN FITXATEGIA IRAKURRI ETA HIZKUNTZAK SORTU
	public void hizkuntzakIrakurri() 
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("hizkuntzak.txt"))) //hizkuntzak.txt fitxategia irakurtzen ahalegintzen da
	    {
	        String linea; //Lerro bakoitza
	        
	        while ((linea = br.readLine()) != null) //Lerroa null ez baldin bada
	        {
	            String hizkuntza1 = linea; //Lehenengo linean jartzen duena hizkuntza1-en gorde
	            Hizkuntza hizkuntza = new Hizkuntza(); //Hizkuntza berri bat sortu
	            hizkuntza.setIzena(hizkuntza1); //Sortutako hizkuntza berriari izena gehitu
	            hizkuntzak.put(hizkuntza1, hizkuntza); //Hizkuntzak mapan gorde sortutako hizkuntza berria
	            System.out.println(hizkuntza.getIzena()); //Sortutako hizkuntzak consolan ikusteko
	            
	            try (BufferedReader br2 = new BufferedReader(new FileReader(hizkuntza1 + ".txt"))) //hizkuntza1-ek duen balioa.txt fitxategia irakurtzen ahalegintzen da
			    {
			        String lineea = null; //Lerro bakoitza
			        
			        while ((lineea = br2.readLine()) != null) //Lerroa null ez baldin bada
			        {
			        	String[] partes = lineea.split(":"); //Hitz desberdinak banantzeko : beraien artean
			        	 
			            String datua = partes[0].trim(); //Datua irakurri
			            hizkuntza.backButtom = datua; //Datua gorde
			            
			            String datua2 = partes[1].trim(); //Datua irakurri
			            hizkuntza.editButtom = datua2; //Datua gorde	 
			            
			            String datua3 = partes[2].trim(); //Datua irakurri
			            hizkuntza.deleteButtom = datua3; //Datua gorde
			            
			            String datua4 = partes[3].trim(); //Datua irakurri
			            hizkuntza.confirmButtom = datua4; //Datua gorde
			            
			            String datua5 = partes[4].trim(); //Datua irakurri
			            hizkuntza.aukerak = datua5; //Datua gorde
			            
			            String datua6 = partes[5].trim(); //Datua irakurri
			            hizkuntza.putzua = datua6; //Datua gorde	 
			            
			            String datua7 = partes[6].trim(); //Datua irakurri
			            hizkuntza.haizegailua = datua7; //Datua gorde
			            
			            String datua8 = partes[7].trim(); //Datua irakurri
			            hizkuntza.berogailua = datua8; //Datua gorde
			            
			            String datua9 = partes[8].trim(); //Datua irakurri
			            hizkuntza.kargatzen = datua9; //Datua gorde
			            
			            String datua10 = partes[9].trim(); //Datua irakurri
			            hizkuntza.eskuma = datua10; //Datua gorde
			            
			            String datua11 = partes[10].trim(); //Datua irakurri
			            hizkuntza.erdia = datua11; //Datua gorde
			            
			            String datua12 = partes[11].trim(); //Datua irakurri
			            hizkuntza.ezkerra = datua12; //Datua gorde
			            
			            String datua13 = partes[12].trim(); //Datua irakurri
			            hizkuntza.ezabatuEsaldia = datua13; //Datua gorde
			            
			            String datua14 = partes[13].trim(); //Datua irakurri
			            hizkuntza.aukeratu = datua14; //Datua gorde
			            
			            String datua15 = partes[14].trim(); //Datua irakurri
			            hizkuntza.irten = datua15; //Datua gorde
			            
			            String datua16 = partes[15].trim(); //Datua irakurri
			            hizkuntza.minimizatu = datua16; //Datua gorde
			            
			            String datua17 = partes[16].trim(); //Datua irakurri
			            hizkuntza.maximizatu = datua17; //Datua gorde
			            
			            String datua18 = partes[17].trim(); //Datua irakurri
			            hizkuntza.hizkuntzak = datua18; //Datua gorde
			            
			            String datua19 = partes[18].trim(); //Datua irakurri
			            hizkuntza.hiruGehienez = datua19; //Datua gorde
			            
			            String datua20 = partes[19].trim(); //Datua irakurri
			            hizkuntza.izena = datua20; //Datua gorde
			            
			            String datua21 = partes[20].trim(); //Datua irakurri
			            hizkuntza.pasahitza = datua21; //Datua gorde
			            
			            String datua22 = partes[21].trim(); //Datua irakurri
			            hizkuntza.hasiSaioa = datua22; //Datua gorde
			            
			            String datua23 = partes[22].trim(); //Datua irakurri
			            hizkuntza.hasierakoTamaina = datua23; //Datua gorde
			        }
			    } 
			    catch (IOException e) //Exception
			    {
			        e.printStackTrace(); //Exceptionak saltatu eskero zer gertatu den esateko
			    }
	        }
	    } 
	    catch (IOException e) //Exception
	    {
	        e.printStackTrace(); //Exceptionak saltatu eskero zer gertatu den esateko
	    }
	}
	
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
		
		irudia.erabiltzailea = getErabiltzailea(irudia.izena); //Erabiltzailearen datuak hartzen ditu  
    	
    	if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
    	{          		                
    		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
    	}
    	else
    	{
    		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
    	}  
		
		irudia.adminFrame.dispose(); //Maparen pantaila itxi egiten du
        irudia.adminMapa = 1;
	}
	
	//SUECIAKO BABES ETXEAN CLICK EGITEAN...
	public void sueciaBABESETXEclick()
	{		
		irudia.izena = "suecia"; //Erabiltzailearen izena aldatu
		irudia.erabiltzailea = getErabiltzailea("suecia");
		if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
    	{          		                
    		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
    	}
    	else
    	{
    		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
    	}  
		irudia.adminFrame.dispose(); //Maparen pantaila itxi egiten du
    	irudia.adminMapa = 1;
	}		
	
	//LIBIAKO BABES ETXEAN CLICK EGITEAN...
		public void libiaBABESETXEclick()
		{		
			irudia.izena = "libia"; //Erabiltzailearen izena aldatu
			irudia.erabiltzailea = getErabiltzailea("libia");
			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
	    	{          		                
	    		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
	    	}
	    	else
	    	{
	    		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
	    	}  
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
            	irudia.erabiltzailea = getErabiltzailea(irudia.izena);
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
	
	//PROPIETATEAK BISTARA BIDALTZEN DITU FIRE PROPERTY CHANGE ERABILIZ
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