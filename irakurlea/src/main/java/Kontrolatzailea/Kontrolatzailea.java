package Kontrolatzailea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.UIManager;

import Bista.Printzipala;
import Modeloa.Modeloa;

public class Kontrolatzailea extends MouseAdapter implements ActionListener, KeyListener 
{
	//MVC
	Printzipala irudia; //Bista
	Modeloa modeloa; //Modeloa
	
	//INT
	int pos; //Posizioa
	
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
		this.irudia = irudia;
	}
	
	@Override
	//ACTION PERFORMED
	public void actionPerformed(ActionEvent e) 
	{	
	    if ("SAIOAHASI".equals(e.getActionCommand()))
	    {
	    	modeloa.autenticarUsuario(); //Saioa hasi botoia sakatzean erabiltzailea ea ondo jarri duzun analisatuko du
	    } 
	    else if (("HUECO".equals(e.getActionCommand())) || (("HUECO2".equals(e.getActionCommand()))) || (("HUECO3".equals(e.getActionCommand()))))
	    {
	    	switch(e.getActionCommand()) 
	    	{
		    	case "HUECO":
		    	{
		    		pos = 1; //Ezkerreko botoia aukeratu da
		    		break;
		    	}
		    	case "HUECO2":
		    	{
		    		pos = 2; //Erdiko botoia aukeratu da
		    		break;
		    	}
		    	case "HUECO3":
		    	{
		    		pos = 3; //Eskuineko botoia aukeratu da
		    		break;
		    	}
	    	}
	    	irudia.mostrarOpciones(irudia.hizkuntza); //Argazkien aukerak erakuzten dira
	    } 
	    /*else if ("IRTEN".equals(e.getActionCommand())) 
	    {
	    	System.exit(0); //
	    }*/
	    else if ("ATRAS".equals(e.getActionCommand())) 
	    {
	    	
	    	if(irudia.adminMapa==1)
	    	{	    		
	    		irudia.mostrarPantallaAdmin();
	    		irudia.mainPANTAILA.dispose();
	    	}
	    	else if(irudia.adminMapa==2)
	    	{	    		
	    		irudia.loginPantailaErakutzi();
	    		irudia.adminFrame.dispose();
	    	}
	    	else
	    	{	    		
	    		irudia.loginPantailaErakutzi();
	    		irudia.mainPANTAILA.dispose();
	    	}	        
	    }
	    else if ("GEHITU".equals(e.getActionCommand())) 
	    {
	    	modeloa.timer3.setRepeats(false);
	    	modeloa.timer3.start();
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea);
	    	//SwingUtilities.invokeLater(() -> {
	    	//irudia.mainPANTAILA.dispose();    	
	        //irudia.mostrarPantallaEditarHuecos(irudia.erabiltzailea);	        
	        //irudia.mainPANTAILA2.dispose();
	    	//});
	    	irudia.zer = "gehitu";
	    }
	    else if ("KONFIRMATU".equals(e.getActionCommand())) 
	    {
	    	//irudia.mainPANTAILA.dispose();
	    	modeloa.timer2.stop();
	    	modeloa.timer3.setRepeats(false);
	    	modeloa.timer3.start();
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea);
		    //irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);	
	    	irudia.zer = "konfirmatu";
	    }
	    else if ("EZABATU".equals(e.getActionCommand())) 
	    {
	    	irudia.mostrarOpcionesEliminar();
	    	//irudia.deleteButton.setVisible(false);
	    	irudia.deleteButton.setBackground(new Color(120, 120, 120));
	    	//irudia.editButton.setVisible(false);
	    	irudia.editButton.setBackground(new Color(120, 120, 120));
	    	//irudia.backButton.setVisible(false);
	    	irudia.backButton.setBackground(new Color(120, 120, 120));
	    	//irudia.confirmButton.setVisible(true);
	    	irudia.confirmButton.setBackground(UIManager.getColor("Button.background"));
	    }
	    else if ("PUTZUA".equals(e.getActionCommand())) 
	    {	
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("putzua", pos);	    	
	    } 
	    else if ("HAIZEGAILUA".equals(e.getActionCommand())) 
	    {
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("haizegailua", pos);
	    } 
	    else if ("BEROGAILUA".equals(e.getActionCommand())) 
	    {
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("berogailua", pos);
	    }
	    else if ("EZKERREKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.timer2.start();
	    	modeloa.bota("ezk", pos);	    
	    } 
	    else if ("ERDIKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.timer2.start();
	    	modeloa.bota("erd", pos);
	    } 
	    else if ("ESKUINEKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.timer2.start();
	    	modeloa.bota("esk", pos);
	    }
	}

	@Override
	//KEY PRESSED
	public void keyPressed(KeyEvent e) 
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
        if (irudia.argeliaBABESETXE.getBounds().contains(e.getPoint()))
        {
        	modeloa.argeliaBABESETXEclick();
        }
        else if (irudia.groenlandiaBABESETXE.getBounds().contains(e.getPoint())) 
        {
        	modeloa.groenlandiaBABESETXEclick();
        }
    }
}