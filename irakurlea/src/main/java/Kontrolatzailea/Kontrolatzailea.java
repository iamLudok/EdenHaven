package Kontrolatzailea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Bista.Printzipala;
import Modeloa.Modeloa;

public class Kontrolatzailea extends MouseAdapter implements ActionListener, KeyListener 
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
	
	/*Timer timerEliminar2 = new Timer(1000, (ActionEvent evt) -> 
	{	        
        irudia.mainPANTAILA2.dispose();
	});*/
	//Timer timerEliminar = new Timer(1000, (ActionEvent evt) -> 
	//{
		/*timerEliminar2.setRepeats(false);
    	timerEliminar2.start();
		irudia.mainPANTAILA.dispose();  
		if(zer=="gehitu")
		{
			irudia.mostrarPantallaEditarHuecos(irudia.erabiltzailea);
		}
		else if(zer=="konfirmatu") 
		{
			irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);
		}*/
	//});
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{	
	    if ("SAIOAHASI".equals(e.getActionCommand())) 
	    {
	    	modeloa.autenticarUsuario();
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
	    	modeloa.timerEliminar.setRepeats(false);
	    	modeloa.timerEliminar.start();
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
	    	modeloa.berdeTimer.stop();
	    	modeloa.timerEliminar.setRepeats(false);
	    	modeloa.timerEliminar.start();
	    	irudia.mostrarPantallaWait(irudia.erabiltzailea);
		    //irudia.mostrarPantallaHuecosCompletado(irudia.erabiltzailea);	
	    	irudia.zer = "konfirmatu";
	    }
	    else if ("EZABATU".equals(e.getActionCommand())) 
	    {
	    	irudia.mostrarOpcionesEliminar();
	    	irudia.deleteButton.setVisible(false);
	    	irudia.editButton.setVisible(false);
	    	irudia.backButton.setVisible(false);
	    	irudia.confirmButton.setVisible(true);
	    }
	    else if ("POZO".equals(e.getActionCommand())) 
	    {	
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("pozo", pos);	    	
	    } 
	    else if ("VENTILADOR".equals(e.getActionCommand())) 
	    {
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("ventilador", pos);
	    } 
	    else if ("ESTUFA".equals(e.getActionCommand())) 
	    {
	    	if(modeloa.timer!=null)
	    	{
	    		modeloa.timer.start();
	    	}
	    	
	    	modeloa.bota("estufa", pos);
	    }
	    else if ("EZKERREKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.berdeTimer.start();
	    	modeloa.bota("ezk", pos);	    
	    } 
	    else if ("ERDIKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.berdeTimer.start();
	    	modeloa.bota("erd", pos);
	    } 
	    else if ("ESKUINEKOA".equals(e.getActionCommand())) 
	    {
	    	modeloa.berdeTimer.start();
	    	modeloa.bota("esk", pos);
	    }
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER) 
		{
			irudia.loginButton.doClick();
        }
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	@Override
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