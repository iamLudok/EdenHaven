package Modeloa;

import java.util.ArrayList;

public class Erabiltzailea 
{
    //ArrayList<Integer> posizioZerrenda; //Hutsuneetako aukeren posizioak gorde
    ArrayList<String> balioZerrenda; //Hutsuneetako argazkiak gorde
    
    private String izena; //Erabiltzailearen izena
    private String pasahitza; //Erabiltzailearen pasahitza
    public int behin = 0;
    
    public int buttonBEHIN = 0; //EZKERREKOA
    public int button2BEHIN = 0; //ERDIKOA
    public int button3BEHIN = 0; //ESKUINEKOA

    public Erabiltzailea()
    {
        //posizioZerrenda = new ArrayList<>();
        balioZerrenda = new ArrayList<>();
      balioZerrenda.add(null); 
      balioZerrenda.add(null); 
      balioZerrenda.add(null); 
    }

    //POSIZIOA ZERRENDAREN GET ETA SET
    //public ArrayList<Integer> getPosizioZerrenda() 
    //{
    //    return posizioZerrenda;
    //}

    /*public void setPosizioZerrendaAtIndex(int index, int value) 
    {
        posizioZerrenda.add(value); 
    }*/
    //
    
    //BALIO ZERRENDAREN GET ETA SET
    public ArrayList<String> getBalioZerrenda() 
    {
        return balioZerrenda;
    }

    public void setBalioZerrendaAtIndex(int index, String value) 
    {
    	//balioZerrenda.add(value); 
    	balioZerrenda.set(index,value); 
    }
    //
    
    //PASAHITZAREN GET ETA SET
    public String getPasahitza() 
    {
        return pasahitza;
    }

	public void setPasahitza(String pasahitza) 
	{
		this.pasahitza = pasahitza;
	}
	//

	//IZENAREN GET ETA SET
	public String getIzena()
	{
		return izena;
	}

	public void setIzena(String izena) 
	{
		this.izena = izena;
	}		
}