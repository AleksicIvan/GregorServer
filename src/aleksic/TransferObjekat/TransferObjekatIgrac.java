package aleksic.TransferObjekat;

import aleksic.Models.Igrac;
import aleksic.Models.Karta;
import aleksic.Models.Talon;
import aleksic.Servis.Faza;
import aleksic.Servis.Igra;

import java.io.Serializable;
import java.util.List;

public class TransferObjekatIgrac implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public Igrac igr;
    public Igra igra;
    public Igrac pronadjeniIgrac;
    public String poruka;
    public boolean signal; // signal o uspesnosti izvrsenja operacije.
    public int indeks = -1;
    public String nazivOperacije;
    public Igrac prviIgrac;
    public Igrac drugiIgrac;
    public Integer brojigraca;
    public List<Karta> rukaPrvogIgraca;
    public List<Karta> spilPrvogIgraca;
    public Talon talonPrvogIgraca;
    public List<Karta> rukaDrugogIgraca;
    public List<Karta> spilDrugogIgraca;
    public Talon talonDrugogIgraca;
    public Igrac igracNaPotezu;
    public Faza fazaPoteza;
}
