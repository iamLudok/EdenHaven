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
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Bista.Printzipala;

public class Modeloa implements ActionListener
{
	public final static String PROPIETATEA = "pozo";
	public final static String PROPIETATEA2 = "ventilador";
	public final static String PROPIETATEA3 = "estufa";
	public final static String PROPIETATEA4 = "denboraPasaDa";
	public final static String PROPIETATEA5 = "ezk";
	public final static String PROPIETATEA6 = "erd";
	public final static String PROPIETATEA7 = "esk";
	public final static String PROPIETATEA8 = "berdeaTimer";
	public final static String PROPIETATEA9 = "eliminar3Timer";
	public final static String PROPIETATEA10 = "eliminarTimer";
	public final static String PROPIETATEA11 = "eliminar2Timer";
	
	private Map<String, Erabiltzailea> erabiltzaileak; //ERABILTZAILEEN MAPA

	PropertyChangeSupport konektorea;
	public Timer timer=null;
	public Timer berdeTimer = null;
	public Timer timerEliminar = null;
	public Timer timerEliminar2 = null;
	public Timer timerEliminar3 = null;
	Printzipala irudia;
		
	public Modeloa() 
	{
		konektorea = new PropertyChangeSupport(this);
		timer = new Timer(5000, this);
	    timer.setActionCommand("Denbora");
	    berdeTimer = new Timer(500, this);
	    berdeTimer.setActionCommand("Denbora2");
	    timerEliminar = new Timer(1000, this);
	    timerEliminar.setActionCommand("Denbora4");
	    timerEliminar2 = new Timer(1000, this);
	    timerEliminar2.setActionCommand("Denbora5");
	    timerEliminar3 = new Timer(500, this);
	    timerEliminar3.setActionCommand("Denbora3");
	    irudia = null;
	    
	    erabiltzaileak = new HashMap<>(); //ERABILTZAILEEN HASH MAPA
	}
	
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia;
	}
	
	public void erabiltzaileakIrakurri() //ERABILTZAILEEN FITXATEGIA IRAKURRI ETA ERABILTZAILEAK SORTU
	{
	    try (BufferedReader br = new BufferedReader(new FileReader("erabiltzaileak.txt"))) 
	    {
	        String linea;
	        while ((linea = br.readLine()) != null) 
	        {
	            String[] partes = linea.split(":");
	            if (partes.length >= 2) 
	            {
	                String izena = partes[0].trim();
	                String contrasena = partes[1].trim();

	                Erabiltzailea erabiltzailea = new Erabiltzailea();
	                erabiltzailea.setPasahitza(contrasena);
	                erabiltzaileak.put(izena, erabiltzailea);
	            }
	        }
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	public boolean isUsuarioContraseña(String nombre, String contrasena) 
	{
		irudia.erabiltzailea = getErabiltzailea(nombre);

	    if (irudia.erabiltzailea != null) 
	    {
	        return contrasena.equals(irudia.erabiltzailea.getPasahitza());
	    }

	    return false;
	}
	
	public void argeliaBABESETXEclick()
	{		
		irudia.izena = "argelia";
		irudia.erabiltzailea = getErabiltzailea("argelia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
		irudia.adminFrame.dispose();
        irudia.adminMapa = 1;
	}
	
	public void groenlandiaBABESETXEclick()
	{		
		irudia.izena = "groenlandia";
		irudia.erabiltzailea = getErabiltzailea("groenlandia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
		irudia.adminFrame.dispose();
    	irudia.adminMapa = 1;
	}		
	
	public void autenticarUsuario() 
	{
		irudia.izena = irudia.izenaField.getText();
        String contrasena = new String(irudia.pasahitzaField.getPassword());        
        
		if (autenticar(irudia.izena, contrasena)) 
		{			

            if (("admin".equals(irudia.izena)) && ("admin".equals(contrasena))) 
            {       
            	//timerEliminar3.setRepeats(false);
            	//timerEliminar3.start(); 
                irudia.mostrarPantallaAdmin(); 
                irudia.loginPANTAILA.dispose();
            } 
            else if (isUsuarioContraseña(irudia.izena,contrasena))
            {            	
            	irudia.erabiltzailea = getErabiltzailea(irudia.izena);   
            	if(irudia.erabiltzailea.behin==0)
            	{          		                
            		irudia.mostrarPantallaHuecos();
            	}
            	else
            	{
            		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
            	}  
            	timerEliminar3.setRepeats(false);
    	    	timerEliminar3.start();
            }         
            else if ("Ludok".equals(irudia.izena) && "dj".equals(contrasena)) 
            {           	
                irudia.ludokLovesU();
                irudia.loginPANTAILA.dispose();
                //timerEliminar3.setRepeats(false);
    	    	//timerEliminar3.start();
            }
            else 
            {
                JOptionPane.showMessageDialog(null, "Pasahitza ez da zuzena");
            }
            //timerEliminar3.setRepeats(false);
	    	//timerEliminar3.start();
        } 
		else
        {
            JOptionPane.showMessageDialog(null, "Akatz bat gertatu da. Saiatu berriz.");
        }
	}
	
    public boolean autenticar(String nombre, String contrasena) 
    {
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
    }
    
    public Erabiltzailea getErabiltzailea(String nombre) 
	{
	    return erabiltzaileak.get(nombre);
	}
	
	public boolean isUsuarioRegistrado(String nombre) 
	{
	    return erabiltzaileak.containsKey(nombre);
	}
    
	public void addPropertyChangeListener(PropertyChangeListener listener) 
	{
		konektorea.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) 
	{
		konektorea.removePropertyChangeListener(listener);
	}
	
	public void bota(String mota, int posizioa)
	{
		switch (mota) 
		{
			case "pozo":
			{
				konektorea.firePropertyChange(PROPIETATEA, null, posizioa);
				break;
			}
			case "ventilador":
			{
				konektorea.firePropertyChange(PROPIETATEA2, null, posizioa);
				break;
			}
			case "estufa":
			{
				konektorea.firePropertyChange(PROPIETATEA3, null, posizioa);
				break;
			}
			case "ezk":
			{
				konektorea.firePropertyChange(PROPIETATEA5, null, posizioa);
				break;
			}
			case "erd":
			{
				konektorea.firePropertyChange(PROPIETATEA6, null, posizioa);
				break;
			}
			case "esk":
			{
				konektorea.firePropertyChange(PROPIETATEA7, null, posizioa);
				break;
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		switch(command) 
		{
	        case "Denbora":
	            konektorea.firePropertyChange(PROPIETATEA4, 0, 1);
	            break;      
	        case "Denbora2":
	            konektorea.firePropertyChange(PROPIETATEA8, 0, 1);
	            break;
	        case "Denbora3":
	            konektorea.firePropertyChange(PROPIETATEA9, 0, 1);
	            break;
	        case "Denbora4":
	            konektorea.firePropertyChange(PROPIETATEA10, 0, 1);
	            break;
	        case "Denbora5":
	            konektorea.firePropertyChange(PROPIETATEA11, 0, 1);
	            break;
		}
	}
}