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

public class Modeloa implements ActionListener
{
	public final static String PROPIETATEA = "pozo";
	public final static String PROPIETATEA2 = "ventilador";
	public final static String PROPIETATEA3 = "estufa";
	public final static String PROPIETATEA4 = "denboraPasaDa";
	public final static String PROPIETATEA5 = "ezk";
	public final static String PROPIETATEA6 = "erd";
	public final static String PROPIETATEA7 = "esk";
	
	private Map<String, Erabiltzailea> erabiltzaileak; //ERABILTZAILEEN MAPA

	PropertyChangeSupport konektorea;
	private Timer timer=null;
	Printzipala irudia;
		
	public Modeloa() 
	{
		konektorea = new PropertyChangeSupport(this);
		timer = new Timer(5000, this);
	    timer.setActionCommand("Denbora");
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
		irudia.adminFrame.dispose();
		irudia.izena = "argelia";
		irudia.erabiltzailea = getErabiltzailea("argelia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
        irudia.adminMapa = 1;
	}
	
	public void groenlandiaBABESETXEclick()
	{
		irudia.adminFrame.dispose();
		irudia.izena = "groenlandia";
		irudia.erabiltzailea = getErabiltzailea("groenlandia");
		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
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
            	irudia.loginPANTAILA.dispose(); 
                irudia.mostrarPantallaAdmin();
            } 
            else if ((isUsuarioRegistrado(irudia.izena)) && (isUsuarioContraseña(irudia.izena,contrasena)))
            {
            	irudia.loginPANTAILA.dispose(); 
            	irudia.erabiltzailea = getErabiltzailea(irudia.izena);   
            	if(irudia.erabiltzailea.behin==0)
            	{          		                
            		irudia.mostrarPantallaHuecos();
            	}
            	else
            	{
            		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
            	}
            }         
            else if ("Ludok".equals(irudia.izena) && "dj".equals(contrasena)) 
            {
            	irudia.loginPANTAILA.dispose(); 
                irudia.ludokLovesU();
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

	public void start() 
	{
	    if (timer != null)
	    {
	        timer.start();
	    }
	}
	
	public void stop() 
	{
		if(timer != null) 
		{
			timer.stop();
			timer = null;
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
		}
	}
}