package aleksic.TransferObjekat;

import aleksic.DomenskiObjekat.*;
import aleksic.DomenskiObjekat.Faza;
import aleksic.DomenskiObjekat.Igra;

import java.io.Serializable;
import java.util.List;

public class TransferObjekatIgrac implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public Igrac igr;
    public Igra igra;
    public String poruka;
    public boolean signal;
    public String nazivOperacije;
    public Karta odigranaKarta;
    public Igrac prviIgrac;
    public Igrac drugiIgrac;
    public int brojigraca;
    public int brojPoteza;
    public List<Zlatnik> kliknutiZlatnici;
    public List<Karta> kliknutiVItezovi;
    public List<Karta> rukaPrvogIgraca;
    public List<Karta> spilPrvogIgraca;
    public Talon talonPrvogIgraca;
    public List<Karta> rukaDrugogIgraca;
    public List<Karta> spilDrugogIgraca;
    public Talon talonDrugogIgraca;
    public Igrac igracNaPotezu;
    public Faza fazaPoteza;
    public boolean prviPotez;
    public int sizeRedVitezovaPrviIgrac;
    public int sizeRedVitezovaDrugiIgrac;
    public List<Karta> redZlatnikaPrviIgrac;
    public List<Karta> redVitezovaPrviIgrac;
    public List<Karta> redNapadPrviIgrac;
    public List<Karta> redOdbranaPrviIgrac;
    public List<Karta> redZlatnikaDrugiIgrac;
    public List<Karta> redRedVitezovaDrugiIgrac;
    public List<Karta> redNapadDrugiIgrac;
    public List<Karta> redOdbranaDrugiIgrac;
}
