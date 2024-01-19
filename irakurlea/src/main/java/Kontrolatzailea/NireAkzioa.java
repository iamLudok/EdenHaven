package Kontrolatzailea;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import Bista.Printzipala;
import Modeloa.Modeloa;

public class NireAkzioa extends AbstractAction
{
	String texto;
	Printzipala irudia;
	Modeloa modeloa;
	
	public NireAkzioa (String texto, Icon imagen, String descrip, Integer nemonic, Printzipala irudia, Modeloa modeloa)
	{
		super(texto,imagen);
		
		this.irudia = irudia;
		this.modeloa = modeloa;
		this.texto = texto;
		this.putValue( Action.SHORT_DESCRIPTION ,descrip);
		this.putValue(Action.MNEMONIC_KEY, nemonic);
	}
	
	//BISTA
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (texto.equals("Euskera"))
		{
			irudia.mainPANTAILA.dispose();
			irudia.hizkuntza = "euskera";
			irudia.toolbarBotoienKonfigurazioa(irudia.hizkuntza);

			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{          		                
        		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
        	}
        	else
        	{
        		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
        	} 
			
		}
		if (texto.equals("Castellano"))
		{
			irudia.mainPANTAILA.dispose();
			irudia.hizkuntza = "castellano";
			irudia.toolbarBotoienKonfigurazioa(irudia.hizkuntza);
			
			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{          		                
        		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
        	}
        	else
        	{
        		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
        	}  
			
		}
		if (texto.equals("Ingles"))
		{
			irudia.mainPANTAILA.dispose();
			irudia.hizkuntza = "ingles";
			irudia.toolbarBotoienKonfigurazioa(irudia.hizkuntza);

			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{				
        		irudia.mostrarPantallaHuecos(); //Hutsuneen pantaila jarri
        	}
        	else
        	{
        		irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea); //Erabiltzailearen datuekin pantaila erakutzi
        	} 
			
		}
		if (texto.equals("Irten"))
		{
			System.exit(0); //Jokoa itxi egiten da
		}
	}
}