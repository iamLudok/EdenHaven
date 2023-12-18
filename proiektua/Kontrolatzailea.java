package proiektua;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class Kontrolatzailea /*extends MouseAdapter*/ implements ActionListener
{
	Printzipala irudia;
	Modeloa modeloa;
	
	public Kontrolatzailea(Modeloa modeloa) 
	{
		irudia = null;
		this.modeloa = modeloa;
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
	    	String nombre = irudia.nombreField.getText();
            String contrasena = new String(irudia.contrasenaField.getPassword());
			if (modeloa.autenticar(nombre, contrasena)) 
			{
                //Login pantaila itxi
				irudia.loginFrame.dispose();

                if ("admin".equals(nombre)) {
                    irudia.mostrarPantallaAdmin();
                } else if ("user".equals(nombre)) {
                    irudia.mostrarPantallaHuecos();
                } else {
                    //JOptionPane.showMessageDialog(null, "Usuario no reconocido");
                }
            } else
            {
                //JOptionPane.showMessageDialog(null, "Autenticación fallida. Inténtalo de nuevo.");
            }
	    } 
	    else if ("HUECO".equals(e.getActionCommand())) 
	    {
	    	irudia.mostrarOpciones();
	    } 
	    else if ("POZO".equals(e.getActionCommand())) 
	    {
            ImageIcon pozoIcon = new ImageIcon("ikonoak/pozo.png");
            irudia.originalButton.setIcon(pozoIcon);
            irudia.originalButton.setText("<html>POZO<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
            irudia.originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
            irudia.originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            
            irudia.opcionesFrame.dispose();  
	    } 
	    else if ("VENTILADOR".equals(e.getActionCommand())) 
	    {
            ImageIcon ventiladorIcon = new ImageIcon("ikonoak/ventilador.png");
            irudia.originalButton.setIcon(ventiladorIcon);
            irudia.originalButton.setText("<html>VENTILADOR<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
            irudia.originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
            irudia.originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);

            irudia.opcionesFrame.dispose();
	    } 
	    else if ("ESTUFA".equals(e.getActionCommand())) 
	    {
            ImageIcon estufaIcon = new ImageIcon("ikonoak/estufa.png");
            irudia.originalButton.setIcon(estufaIcon);
            irudia.originalButton.setText("<html>ESTUFA<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
            irudia.originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
            irudia.originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);

            irudia.opcionesFrame.dispose();
	    }
	}
}