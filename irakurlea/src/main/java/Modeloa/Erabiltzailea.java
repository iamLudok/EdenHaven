package Modeloa;

import java.util.ArrayList;

public class Erabiltzailea 
{
	//ZEIN ARGAZKI GORDETZEN DUGUN BOTOI BAKOITZEAN
    ArrayList<String> balioZerrenda;
    
    //STRING
    private String izena; //Erabiltzailearen izena
    private String pasahitza; //Erabiltzailearen pasahitza
    
    //INT
    public int behin = 0; //Lehenengo aldia erabiltzailea sartzen duena   
    public int buttonBEHIN = 0; //Ezkerreko botoia behin
    public int button2BEHIN = 0; //Erdiko botoia behin
    public int button3BEHIN = 0; //Esluineko botoia behin

    //KONSTRUKTOREA
    public Erabiltzailea()
    {
      balioZerrenda = new ArrayList<>();
      balioZerrenda.add(null); 
      balioZerrenda.add(null); 
      balioZerrenda.add(null); 
    }
    
    //GET-AK
    public ArrayList<String> getBalioZerrenda() //Balio Zerrenda
    {
        return balioZerrenda;
    }
    
    public String getPasahitza() //Pasahitza
    {
        return pasahitza;
    }
    
    public String getIzena() //Izena
	{
		return izena;
	}

    //SET-AK
    public void setBalioZerrendaAtIndex(int index, String value) //Balio Zerrenda
    {
    	balioZerrenda.set(index,value); 
    }

	public void setPasahitza(String pasahitza) //Pasahitza
	{
		this.pasahitza = pasahitza;
	}

	public void setIzena(String izena) //Izena
	{
		this.izena = izena;
	}		
}