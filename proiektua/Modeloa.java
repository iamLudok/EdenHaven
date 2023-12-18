package proiektua;

public class Modeloa 
{
	public Modeloa() 
	{
		
	}
	
    boolean autenticar(String nombre, String contrasena) 
    {
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
    }
}
