package Modeloa;

import java.util.ArrayList;

//ABSTRACT METODOA PLANTILLA BEZALA ERABILTZEKO
public abstract class DatuakGordePlantilla 
{
	//ARRAYLIST
	protected ArrayList<String> balioZerrenda; 
	
	//STRING
	private String izena; 
	
	//KONSTRUKTOREA
	public DatuakGordePlantilla() 
	{
		balioZerrenda = new ArrayList<>(); //Balio zerrenda hasieratu
	}
	
	//GET BALIO ZERRENDA
	public ArrayList<String> getBalioZerrenda() 
    {
        return balioZerrenda;
    }
	
	//GET IZENA
	public String getIzena()
	{
		return izena;
	}
	
	//SET BALIO ZERRENDA
	public void setBalioZerrendaAtIndex(int index, String value)
    {
    	balioZerrenda.set(index,value); 
    }
	
	//SET IZENA
	public void setIzena(String izena)
	{
		this.izena = izena;
	}	
}