package proiektua;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Modeloa implements ActionListener
{
	public final static String PROPIETATEA = "pozo";
	public final static String PROPIETATEA2 = "ventilador";
	public final static String PROPIETATEA3 = "estufa";
	PropertyChangeSupport konektorea;
		
	public Modeloa() 
	{
		konektorea = new PropertyChangeSupport(this);
	}
	
    boolean autenticar(String nombre, String contrasena) 
    {
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
    }
    
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		konektorea.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		konektorea.removePropertyChangeListener(listener);
	}
	
	public void bota(String mota, int posizioa) {
		switch (mota) {
		case "pozo":
			System.out.println("Kaixo2");
			konektorea.firePropertyChange(PROPIETATEA, null, posizioa);
		break;
		case "ventilador":
			//System.out.println("Kaixo2");
			konektorea.firePropertyChange(PROPIETATEA2, null, posizioa);
		break;
		case "estufa":
			//System.out.println("Kaixo2");
			konektorea.firePropertyChange(PROPIETATEA3, null, posizioa);
		break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Actionchanged, brah");
		
	}
	
	/*@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		switch (command) {
		case "POZO":
			konektorea.firePropertyChange(PROPIETATEA, -1, 0);
		break;
		}
	}*/
}
