package Modeloa;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Timer;

public class Modeloa implements ActionListener
{
	public final static String PROPIETATEA = "pozo";
	public final static String PROPIETATEA2 = "ventilador";
	public final static String PROPIETATEA3 = "estufa";
	public final static String PROPIETATEA4 = "denboraPasaDa";
	public final static String PROPIETATEA5 = "ezk";
	public final static String PROPIETATEA6 = "erd";
	public final static String PROPIETATEA7 = "esk";

	PropertyChangeSupport konektorea;
	private Timer timer=null;
		
	public Modeloa() 
	{
		konektorea = new PropertyChangeSupport(this);
		timer = new Timer(5000, this);
	    timer.setActionCommand("Denbora");
	}
	
    public boolean autenticar(String nombre, String contrasena) 
    {
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
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

	public void start() {
	    if (timer != null) {
	        timer.start();
	    }
	}
	
	public void stop() {
		if(timer != null) {
			timer.stop();
			timer = null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		switch(command) {
        case "Denbora":
            konektorea.firePropertyChange(PROPIETATEA4, 0, 1);
            break;
    }
	}
}