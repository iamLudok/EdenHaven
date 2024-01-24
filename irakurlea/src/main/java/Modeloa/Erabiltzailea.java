package Modeloa;

//PROGRAMA ERABILIKO DUTEN ERABILTZAILEAK
public class Erabiltzailea extends DatuakGordePlantilla
{
	//STRING
    private String pasahitza; //Erabiltzailearen pasahitza
    
    //INT
    public int behin = 0; //Lehenengo aldia erabiltzailea sartzen duena   
    public int buttonBEHIN = 0; //Ezkerreko botoia behin
    public int button2BEHIN = 0; //Erdiko botoia behin
    public int button3BEHIN = 0; //Eskuineko botoia behin
    public int backButtonBEHIN = 0; //Atzera botoia aktibatu
    public int editButtonBEHIN = 0; //Gehitu botoia aktibatu
    public int deleteButtonBEHIN = 0; //Ezabatu botoia aktibatu
    public int confirmButtonBEHIN = 0; //Konfirmatu botoia aktibatu

    //KONSTRUKTOREA
    public Erabiltzailea()
    {
      balioZerrenda.add(null); //Balio zerrendari 0 posizioan null bat jarri
      balioZerrenda.add(null); //Balio zerrendari 1 posizioan null bat jarri
      balioZerrenda.add(null); //Balio zerrendari 2 posizioan null bat jarri 
    }
    
    //GET PASAHITZA  
    public String getPasahitza()
    {
        return pasahitza;
    }

    //SET PASAHITZA
	public void setPasahitza(String pasahitza) 
	{
		this.pasahitza = pasahitza;
	}
}