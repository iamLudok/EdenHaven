package Kontrolatzailea;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import Bista.Koloreak;
import Bista.Printzipala;
import Modeloa.Modeloa;

public class Kontrolatzailea extends MouseAdapter implements ActionListener, KeyListener, FocusListener
{
	//MVC
	private Printzipala irudia; //Bista
	private Modeloa modeloa; //Modeloa
	
	//INT
	private int pos; //Posizioa
	
	//KONSTRUKTOREA
	public Kontrolatzailea(Modeloa modeloa) 
	{
		irudia = null; ///Bista
		this.modeloa = modeloa; //Modeloa
		pos = 0; //Posizioa
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
	    if ("SAIOAHASI".equals(e.getActionCommand())) //Login pantailako saioa hasi botoia
	    {
	    	modeloa.autenticarUsuario(); //Saioa hasi botoia sakatzean erabiltzailea ea ondo jarri duzun analisatuko du
	    } 
	    else if (("HUECO".equals(e.getActionCommand())) || (("HUECO2".equals(e.getActionCommand()))) || (("HUECO3".equals(e.getActionCommand())))) //Pantailan sortzen diren hutsuneak zuk klikatu eta zer jarri nahi duzun aukeratu ahal duzu
	    {
	    	switch(e.getActionCommand()) 
	    	{
		    	case "HUECO":
		    	{
		    		if(irudia.erabiltzailea.buttonBEHIN == 0) //Ezkerreko botoia aktibatuta baldin badago
		    		{
		    			pos = 1; //Ezkerreko botoia aukeratu da
		    			irudia.mostrarOpciones(); //Argazkien aukerak erakuzten dira
		    		}		    		
		    		break;
		    	}
		    	case "HUECO2":
		    	{
		    		if(irudia.erabiltzailea.button2BEHIN == 0) //Erdiko botoia aktibatuta baldin badago
		    		{
		    			pos = 2; //Erdiko botoia aukeratu da
		    			irudia.mostrarOpciones(); //Argazkien aukerak erakuzten dira
		    		}
		    		break;
		    	}
		    	case "HUECO3":
		    	{
		    		if(irudia.erabiltzailea.button3BEHIN == 0) //Eskuineko botoia aktibatuta baldin badago
		    		{
		    			pos = 3; //Eskuineko botoia aukeratu da
		    			irudia.mostrarOpciones(); //Argazkien aukerak erakuzten dira
		    		}
		    		break;
		    	}
	    	}    	
	    } 
	    else if ("ATRAS".equals(e.getActionCommand()))//Atzera botoia
	    {
	    	if(irudia.erabiltzailea.backButtonBEHIN == 0) //Atzera botoia aktibatuta baldin badago
	    	{
		    	if(irudia.adminMapa==1) //Admin-eko mapako babesetxe batetik etorri eskero
		    	{	    		
		    		irudia.mostrarPantallaAdmin(); //Admin mapa zabaldu
		    		irudia.mainPANTAILA.dispose(); //Main pantaila itxi
		    	}
		    	else if(irudia.adminMapa==2) //Admin-eko mapatik etorri eskero
		    	{	    		
		    		irudia.loginPantailaErakutzi(); //Login pantaila zabaldu
		    		irudia.adminFrame.dispose(); //Admin pantaila itxi
		    	}
		    	else //Babesetxe batetik etorri eskero
		    	{	    		
		    		irudia.loginPantailaErakutzi(); //Login pantaila zabaldu
		    		irudia.mainPANTAILA.dispose(); //Main pantaila itxi
		    	}	        
	    	}
	    }
	    else if ("GEHITU".equals(e.getActionCommand())) //Gehitu botoia
	    {
	    	if(irudia.erabiltzailea.editButtonBEHIN == 0) //Gehitu botoia aktibatuta baldin badago
	    	{
		    	modeloa.timer3.setRepeats(false); //Timer3 soilik behin
		    	modeloa.timer3.start(); //Timer3 hasi
		    	
		    	irudia.mostrarPantallaWait(irudia.erabiltzailea);
		    	irudia.zer = "gehitu";
	    	}
	    }
	    else if ("KONFIRMATU".equals(e.getActionCommand())) 
	    {
	    	if(irudia.erabiltzailea.confirmButtonBEHIN == 0)
	    	{
		    	modeloa.timer2.stop(); //Timer2 gelditu
		    	
		    	modeloa.timer3.setRepeats(false); //Timer3 soilik behin
		    	modeloa.timer3.start(); //Timer3 hasi
		    	
		    	irudia.mostrarPantallaWait(irudia.erabiltzailea); //Wait pantaila erakutzi
		    	irudia.zer = "konfirmatu"; //Aukeratu den botoia konfirmatu da
	    	}
	    }
	    else if ("EZABATU".equals(e.getActionCommand())) //Ezabatu botoia
	    {
	    	if(irudia.erabiltzailea.deleteButtonBEHIN == 0) //Ezabatu botoia aktibatuta baldin badago
	    	{
		    	irudia.mostrarOpcionesEliminar(); //Ezabatzeko aukerak erakutzi
		    	
		    	LineBorder lineBorder = new LineBorder(Koloreak.GRISARGIA, 3); //Borde grisa sortu
		    	LineBorder lineBorder2 = new LineBorder(Koloreak.BERDEARGIA, 3); //Borde berdea sortu
		    	
		    	irudia.erabiltzailea.deleteButtonBEHIN = 1; //Ezabatu botoia desaktibatu
		    	irudia.deleteButton.setBackground(Koloreak.GRISARGIA); //Ezabatu botoiaren kolorea grisa
		    	irudia.deleteButton.setBorder(lineBorder); //Ezabatu botoiari borde grisa jarri
		    	
		    	irudia.erabiltzailea.editButtonBEHIN = 1; //Gehitu botoia desaktibatu
		    	irudia.editButton.setBackground(Koloreak.GRISARGIA); //Gehitu botoiaren kolorea grisa
		    	irudia.editButton.setBorder(lineBorder); //Gehitu botoiari borde grisa jarri
		    	
		    	irudia.erabiltzailea.backButtonBEHIN = 1; //Atzera botoia desaktibatu
		    	irudia.backButton.setBackground(Koloreak.GRISARGIA); //Atzera botoiaren kolorea grisa
		    	irudia.backButton.setBorder(lineBorder); //Atzera botoiari borde grisa jarri
		    	
		    	irudia.erabiltzailea.confirmButtonBEHIN = 0; //Konfirmatu botoia aktibatu
		    	irudia.confirmButton.setBackground(Koloreak.URDINILUNA); //Konfirmatu botoiaren kolorea urdin iluna
		    	irudia.confirmButton.setBorder(lineBorder2); //Konfirmatu botoiari borde berdea jarri
	    	}
	    }
	    else if ("URMAILA".equals(e.getActionCommand())) //Ur Maila aukeratu da
	    {	
	    	if(modeloa.timer!=null) //Timer ez baldin bada null barrura sartu
	    	{
	    		modeloa.timer.start(); //Timer martxan hasi
	    	}
	    	
	    	modeloa.bota("urmaila", pos); //Modelora bidali    	
	    } 
	    else if ("TENPERATURA".equals(e.getActionCommand())) //Tenperatura aukeratu da
	    {
	    	if(modeloa.timer!=null) //Timer ez baldin bada null barrura sartu
	    	{
	    		modeloa.timer.start(); //Timer martxan hasi
	    	}
	    	
	    	modeloa.bota("tenperatura", pos); //Modelora bidali    
	    } 
	    else if ("GASA".equals(e.getActionCommand())) //Gasa aukeratu da
	    {
	    	if(modeloa.timer!=null) //Timer ez baldin bada null barrura sartu
	    	{
	    		modeloa.timer.start(); //Timer martxan hasi
	    	}
	    	
	    	modeloa.bota("gasa", pos); //Modelora bidali    
	    }
	    else if ("EZKERREKOA".equals(e.getActionCommand())) //Ezabatzerako orduan ezkerrekoa aukeratu da
	    {
	    	LineBorder lineBorder = new LineBorder(Color.RED, 3); //Borde gorria sortu    	
		    irudia.button.setBorder(lineBorder); //Borde gorria jarri
	    	
	    	modeloa.timer2.start(); //Timer2 martxan jarri
	    	modeloa.bota("ezk", pos); //Modelora bidali    
	    } 
	    else if ("ERDIKOA".equals(e.getActionCommand())) //Ezabatzerako orduan erdikoa aukeratu da
	    {
	    	LineBorder lineBorder = new LineBorder(Color.RED, 3); //Borde gorria sortu
		    irudia.button2.setBorder(lineBorder); //Borde gorria jarri
	    	
	    	modeloa.timer2.start(); //Timer2 martxan jarri
	    	modeloa.bota("erd", pos); //Modelora bidali 
	    } 
	    else if ("ESKUINEKOA".equals(e.getActionCommand())) //Ezabatzerako orduan eskuinekoa aukeratu da
	    {
	    	LineBorder lineBorder = new LineBorder(Color.RED, 3); //Borde gorria sortu
		    irudia.button3.setBorder(lineBorder); //Borde gorria jarri
	    	
	    	modeloa.timer2.start(); //Timer2 martxan jarri
	    	modeloa.bota("esk", pos); //Modelora bidali 
	    }
	}

	@Override
	//KEY PRESSED
	public void keyPressed(KeyEvent e) //Botoia sakatzean
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER) //ENTER BOTOIA SAKATU
		{
			irudia.loginButton.doClick(); //Login botoia klikatearen berdina egiten du
        }
	}

	@Override
	//HUTSIK
	public void keyReleased(KeyEvent e) 
	{
		
	}

	@Override
	//HUTSIK
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	@Override
	//MOUSE CLICKED EA ARGAZKIA KLIKATU DUZUN EDO EZ BEGIRATZEN DU
    public void mouseClicked(MouseEvent e) 
    {
        if (irudia.argeliaBABESETXE.getBounds().contains(e.getPoint())) //Argeliako babes etxean klik egitean barrura
        {
        	modeloa.argeliaBABESETXEclick(); //Modeloko funtziora
        }
        else if (irudia.sueciaBABESETXE.getBounds().contains(e.getPoint())) //Sueciako babes etxean klik egitean barrura
        {
        	modeloa.sueciaBABESETXEclick(); //Modeloko funtziora
        }
        else if (irudia.libiaBABESETXE.getBounds().contains(e.getPoint())) //Libiako babes etxean klik egitean barrura
        {
        	modeloa.libiaBABESETXEclick(); //Modeloko funtziora
        }
    }

	@Override
	//FOCUS GAINED IZENAFIELD ETA PASAHITZAFIELD
	public void focusGained(FocusEvent e) 
	{
	    if (e.getSource() instanceof JTextField) //JTextField-a baldin bada
	    {
	        JTextField textField = (JTextField) e.getSource(); //Ea zein ari garen klikatzen detektatu

	        if (textField == irudia.izenaField) //Klikatzen duguna izenaField baldin bada
	        {
	            if (textField.getText().equals("izena")) //Izena jarrita baldin badago
	            {
	                textField.setText(""); //"" idazten du
	            }
	            textField.setForeground(Koloreak.ZURIA); //Testua zuria jarri
	            LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 4); //Borde berdea sortu
	            textField.setBorder(lineBorder); //Borde berdea jarri
	        } 
	        else if (textField == irudia.pasahitzaField) //Klikatzen duguna pasahitzaField baldin bada
	        {
	            textField.setForeground(Koloreak.ZURIA); //Testua zuria jarri
	            LineBorder lineBorder = new LineBorder(Koloreak.BERDEARGIA, 4); //Borde berdea sortu
	            textField.setBorder(lineBorder); //Borde berdea jarri
	        }
	    }
	}

	@Override
	//FOCUS LOST IZENAFIELD ETA PASAHITZAFIELD
	public void focusLost(FocusEvent e) 
	{
	    if (e.getSource() instanceof JTextField) //JTextField-a baldin bada
	    {
	        JTextField textField = (JTextField) e.getSource(); //Ea zein ari garen klikatzen detektatu

	        if (textField == irudia.izenaField) //Klikatzen duguna izenaField baldin bada 
	        {
	            if (textField.getText().isEmpty()) //Hutsik baldin badago
	            {
	                textField.setText("izena"); //"izena" idazten du
	            }
	            textField.setForeground(Koloreak.GRISARGIA); //Testua grisa jarri
	            LineBorder lineBorder = new LineBorder(Koloreak.GRISARGIA, 4); //Borde grisa sortu
	            textField.setBorder(lineBorder); //Borde grisa jarri
	        } 
	        else if (textField == irudia.pasahitzaField) //Klikatzen duguna pasahitzaField baldin bada 
	        {
	            textField.setForeground(Koloreak.GRISARGIA); //Testua grisa jarri
	            LineBorder lineBorder = new LineBorder(Koloreak.GRISARGIA, 4); //Borde grisa sortu
	            textField.setBorder(lineBorder); //Borde grisa jarri
	        }
	    }
	}
}