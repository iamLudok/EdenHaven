package Modeloa;

import java.util.ArrayList;

public class Hizkuntza 
{
	//ZEIN ARGAZKI GORDETZEN DUGUN BOTOI BAKOITZEAN
    ArrayList<String> balioZerrenda;
    
    //STRING
    private String izena; //Erabiltzailearen izena
    private String pasahitza; //Erabiltzailearen pasahitza
    
    //INT
    public String backButtom;
    public String editButtom;
    public String deleteButtom;
    public String confirmButtom;
    public String aukerak;
    public String putzua;
    public String haizegailua;
    public String berogailua;

    //KONSTRUKTOREA
    public Hizkuntza()
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