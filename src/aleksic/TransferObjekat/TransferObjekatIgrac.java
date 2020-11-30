package aleksic.TransferObjekat;

import aleksic.Models.Igrac;
import aleksic.Servis.Igra;

import java.io.Serializable;

public class TransferObjekatIgrac implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public Igrac igr;
    public Igra igra;
    public Igrac pronadjeniIgrac;
    public String poruka;
    public boolean signal; // signal o uspesnosti izvrsenja operacije.
    public int indeks = -1;
    public String nazivOperacije;
}
