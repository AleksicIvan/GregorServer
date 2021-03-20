package aleksic.DomenskiObjekat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Spil implements Serializable {
    private List<Karta> tekuciSpil = new ArrayList<>();
    private List<Karta> tekucaRuka = new ArrayList<>();

    public Spil() {
        for (int i = 0; i <= 7; i++) {
            this.tekuciSpil.add(new Zlatnik(i, null, null, 1, TipKarte.ZLATNIK, false));
        }

        for (int i = 12; i <= 24; i++) {
            this.tekuciSpil.add(new Vitez(i, 1, 1, 1, TipKarte.VITEZ, false));
        }
    }

    public List<Karta> promesajSpil () {
        Collections.shuffle(this.tekuciSpil);
        return this.tekuciSpil;
    }

    public List<Karta> podeliRuku (List<Karta> promesaniSpil) {
        for (int i = 0; i <= 4; i++) {
            Karta trenutnaKarta = this.tekuciSpil.get(i);
            this.tekucaRuka.add(trenutnaKarta);
            promesaniSpil.remove(this.tekuciSpil.indexOf(trenutnaKarta));
        }

        return this.tekucaRuka;
    }

    public List<Karta> getTekuciSpil() {
        return tekuciSpil;
    }

    public void setTekuciSpil(List<Karta> tekuciSpil) {
        this.tekuciSpil = tekuciSpil;
    }


    @Override
    public String toString() {
        return "Spil{" +
                " tekuciSpil=" + tekuciSpil.toString() +
                '}';
    }

}


