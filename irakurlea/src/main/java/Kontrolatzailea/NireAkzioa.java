package Kontrolatzailea;

import java.awt.event.*;
import javax.swing.*;

import Bista.Printzipala;
import Modeloa.Hizkuntza;
import Modeloa.Modeloa;

@SuppressWarnings("serial")
//AKZIOAK
public class NireAkzioa extends AbstractAction
{
	//MVC
	private Printzipala irudia;
	private Modeloa modeloa;
	
	//STRING
	private String texto;	
	
	//KONSTRUKTOREA
	public NireAkzioa (String texto, Icon imagen, String descrip, Integer nemonic, Printzipala irudia, Modeloa modeloa)
	{
		super(texto,imagen); //Bere aitari deia
		
		this.irudia = irudia; //Bista
		this.modeloa = modeloa; //Modeloa
		this.texto = texto; //Testua
		this.putValue( Action.SHORT_DESCRIPTION ,descrip); //Deskripzioa jarri
		this.putValue(Action.MNEMONIC_KEY, nemonic); //Tekla jarri
	}
	
	//BISTA
	public void setIrudia (Printzipala irudia) 
	{
		this.irudia = irudia; //Bista
	}
	
	@Override
	//ACTION PERFORMED
	public void actionPerformed(ActionEvent e) 
	{
		Hizkuntza hizkuntza1 = modeloa.getHizkuntza(irudia.hizkuntza); //Momentuko hizkuntza hartu
		
		if (texto.equals("Euskera")) //Menuan Euskera aukeratu da
		{
			irudia.hizkuntza = "euskera"; //Hizkuntza euskera da
			irudia.toolbarBotoienKonfigurazioa(); //Toolbarra aktualizatu
			irudia.sortuAkzioak(); //Akzioak aktualizatu
			
			modeloa.timer3.setRepeats(false); //Timer3 bakarrik behin
	    	modeloa.timer3.start(); //Timer3 martxan jarri
	    	
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea); //Wait pantaila erakutzi

			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{          		 
				irudia.zer = "hutsik"; //zer hutsik da
        	}
        	else
        	{
        		irudia.zer = "konfirmatu"; //zer konfirmatu da
        	} 
			
		}
		if (texto.equals("Castellano")) //Menuan Castellano aukeratu da
		{
			irudia.hizkuntza = "castellano"; //Hizkuntza castellano da
			irudia.toolbarBotoienKonfigurazioa(); //Toolbarra aktualizatu
			irudia.sortuAkzioak(); //Akzioak aktualizatu
			
			modeloa.timer3.setRepeats(false); //Timer3 bakarrik behin
	    	modeloa.timer3.start(); //Timer3 martxan jarri
	    	
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea); //Wait pantaila erakutzi

			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{          		 
				irudia.zer = "hutsik"; //zer hutsik da
        	}
        	else
        	{
        		irudia.zer = "konfirmatu"; //zer konfirmatu da
        	} 
			
		}
		if (texto.equals("Ingles")) //Menuan Ingles aukeratu da
		{
			irudia.hizkuntza = "ingles"; //Hizkuntza ingles da
			irudia.toolbarBotoienKonfigurazioa(); //Toolbarra aktualizatu
			irudia.sortuAkzioak(); //Akzioak aktualizatu

			modeloa.timer3.setRepeats(false); //Timer3 bakarrik behin
	    	modeloa.timer3.start(); //Timer3 martxan jarri
	    	
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea); //Wait pantaila erakutzi

			if(irudia.erabiltzailea.behin==0) //Erabiltzailea jartzen den lehenengo aldia baldin bada
        	{          		 
				irudia.zer = "hutsik"; //zer hutsik da
        	}
        	else
        	{
        		irudia.zer = "konfirmatu"; //zer konfirmatu da
        	} 
			
		}
		if (texto.equals("Euskera ")) //Login Menuan Euskera aukeratu da
		{			
			irudia.loginPANTAILA.dispose(); //Login pantaila itxi
			irudia.hizkuntza = "euskera"; //Hizkuntza euskera da
			irudia.sortuAkzioak(); //Akzioak aktualizatu	
			irudia.loginPantailaErakutzi(); //Login pantaila erakutzi
		}
		if (texto.equals("Castellano ")) //Login Menuan Castellano aukeratu da
		{
			irudia.loginPANTAILA.dispose(); //Login pantaila itxi
			irudia.hizkuntza = "castellano"; //Hizkuntza castellano da
			irudia.sortuAkzioak(); //Akzioak aktualizatu
			irudia.loginPantailaErakutzi(); //Login pantaila erakutzi
		}
		if (texto.equals("Ingles ")) //Login Menuan Ingles aukeratu da
		{
			irudia.loginPANTAILA.dispose(); //Login pantaila itxi
			irudia.hizkuntza = "ingles"; //Hizkuntza ingles da
			irudia.sortuAkzioak(); //Akzioak aktualizatu
			irudia.loginPantailaErakutzi(); //Login pantaila erakutzi
		}
		if (texto.equals(hizkuntza1.minimizatu)) //Menuan Minimizatu aukeratu da
		{			
			if(irudia.pant == "main") //Pantaila main baldin bada
			{
				irudia.mainPANTAILA.setExtendedState(JFrame.ICONIFIED); //Programa minimizatu egiten da
			}
			else
			{
				irudia.loginPANTAILA.setExtendedState(JFrame.ICONIFIED); //Programa minimizatu egiten da
			}
		}
		if (texto.equals(hizkuntza1.maximizatu)) //Menuan Maximizatu aukeratu da
		{
			irudia.maxPantaila = 1; //Pantaila maximizatuta izatea aukeratu da
			
			if(irudia.pant == "main") //Pantaila main baldin bada
			{
				irudia.mainPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH); //Programa pantaila osoan jartzen da
			}
			else
			{
				irudia.loginPANTAILA.setExtendedState(JFrame.MAXIMIZED_BOTH); //Programa pantaila osoan jartzen da
			}
			
		}
		if (texto.equals(hizkuntza1.hasierakoTamaina)) //Menuan Hasierako Tamaina aukeratu da
		{
			irudia.maxPantaila = 0; //Pantaila maximizatuta izatea aukeratu da
			
			if(irudia.pant == "main") //Pantaila main baldin bada
			{
				irudia.mainPANTAILA.setExtendedState(JFrame.NORMAL); //Programa hasierako tamainan jartzen da
			}
			else
			{
				irudia.loginPANTAILA.setExtendedState(JFrame.NORMAL); //Programa hasierako tamainan jartzen da
			}
		}
		if (texto.equals(hizkuntza1.irten)) //Menuan Irten aukeratu da
		{
			System.exit(0); //Programa itxi egiten da
		}
	}
}