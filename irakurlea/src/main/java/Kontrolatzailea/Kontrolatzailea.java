package Kontrolatzailea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Bista.Printzipala;
import Modeloa.Erabiltzailea;
import Modeloa.Modeloa;

public class Kontrolatzailea implements ActionListener
{
	Printzipala irudia;
	Modeloa modeloa;
	int pos;
	
	public Kontrolatzailea(Modeloa modeloa) 
	{
		irudia = null;
		this.modeloa = modeloa;
		pos = 0;
	}
	
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
	    if ("SAIOAHASI".equals(e.getActionCommand())) 
	    {
	    	irudia.autenticarUsuario();
	    } 
	    else if (("HUECO".equals(e.getActionCommand())) || (("HUECO2".equals(e.getActionCommand()))) || (("HUECO3".equals(e.getActionCommand()))))
	    {
	    	switch(e.getActionCommand()) 
	    	{
		    	case "HUECO":
		    	{
		    		pos = 1;
		    		break;
		    	}
		    	case "HUECO2":
		    	{
		    		pos = 2;
		    		break;
		    	}
		    	case "HUECO3":
		    	{
		    		pos = 3;
		    		break;
		    	}
	    	}
	    	irudia.mostrarOpciones();
	    } 
	    else if ("ATRAS".equals(e.getActionCommand())) 
	    {
	    	irudia.mainPANTAILA.dispose();
	        irudia.loginPantailaErakutzi();
	    }
	    else if ("EDITATU".equals(e.getActionCommand())) 
	    {
	    	String nombre = irudia.izenaField.getText();
	    	Erabiltzailea erabiltzailea = irudia.getErabiltzailea(nombre);
	    	irudia.mainPANTAILA.dispose();
	        irudia.mostrarPantallaEditarHuecos(erabiltzailea);
	    }
	    else if ("KONFIRMATU".equals(e.getActionCommand())) 
	    {
	    	String nombre = irudia.izenaField.getText();
	    	Erabiltzailea erabiltzailea = irudia.getErabiltzailea(nombre);  
	    	irudia.mainPANTAILA.dispose();
	        irudia.mostrarPantallaHuecosCompletado(erabiltzailea);
	    }
	    else if ("EZABATU".equals(e.getActionCommand())) 
	    {
	    	irudia.mostrarOpcionesEliminar();
	    }
	    else if ("POZO".equals(e.getActionCommand())) 
	    {	    	
	    	modeloa.bota("pozo", pos);	    	
	    } 
	    else if ("VENTILADOR".equals(e.getActionCommand())) 
	    {
	    	modeloa.bota("ventilador", pos);
	    } 
	    else if ("ESTUFA".equals(e.getActionCommand())) 
	    {
	    	modeloa.bota("estufa", pos);
	    }
	    else if ("EZKERREKOA".equals(e.getActionCommand())) 
	    {	    	
	    	modeloa.bota("ezk", pos);	    	
	    } 
	    else if ("ERDIKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.bota("erd", pos);
	    } 
	    else if ("ESKUINEKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.bota("esk", pos);
	    }
	}
}