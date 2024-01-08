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
	    else if ("IRTEN".equals(e.getActionCommand())) 
	    {
	    	System.exit(0);
	    }
	    else if ("ATRAS".equals(e.getActionCommand())) 
	    {
	    	
	    	if(irudia.adminMapa==1)
	    	{
	    		irudia.mainPANTAILA.dispose();
	    		irudia.mostrarPantallaAdmin();
	    	}
	    	else if(irudia.adminMapa==2)
	    	{
	    		irudia.adminFrame.dispose();
	    		irudia.loginPantailaErakutzi();
	    	}
	    	else
	    	{
	    		irudia.mainPANTAILA.dispose();
	    		irudia.loginPantailaErakutzi();
	    	}	        
	    }
	    else if ("GEHITU".equals(e.getActionCommand())) 
	    {
	    	irudia.confirmButton.setVisible(true);
	    	irudia.mainPANTAILA.dispose();
	        irudia.mostrarPantallaEditarHuecos(irudia.erabiltzailea);	        
	    }
	    else if ("KONFIRMATU".equals(e.getActionCommand())) 
	    {
	    	irudia.mainPANTAILA.dispose();
	        irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
	    }
	    else if ("EZABATU".equals(e.getActionCommand())) 
	    {
	    	irudia.mostrarOpcionesEliminar();
	    	irudia.confirmButton.setVisible(true);
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