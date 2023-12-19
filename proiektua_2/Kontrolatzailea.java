package proiektua_2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class Kontrolatzailea /*extends MouseAdapter*/ implements ActionListener
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
	    	switch(e.getActionCommand()) {
	    	case "HUECO":
	    		pos = 1;
	    		break;
	    	case "HUECO2":
	    		pos = 2;
	    		break;
	    	case "HUECO3":
	    		pos = 3;
	    		break;
	    	}
	    	irudia.mostrarOpciones();
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
	}
}