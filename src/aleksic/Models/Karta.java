package aleksic.Models;

import java.io.Serializable;

public class Karta implements Serializable {
    Integer id = null;
    Integer napad = null;
    Integer odbrana = null;
    Integer cena = null;
    TipKarte tip = null;
    boolean iskoriscena = false;

    public Karta(Integer id) {
        this.id = id;
    }

    public Karta(Integer id, TipKarte tip) {
    }

    public Karta(Integer id, Integer napad, Integer odbrana, Integer cena, TipKarte tip, boolean iskoriscena) {
        this.id = id;
        this.napad = napad;
        this.odbrana = odbrana;
        this.cena = cena;
        this.tip = tip;
        this.iskoriscena = iskoriscena;
    }

    public Karta(TipKarte tip) {
        this.tip = tip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipKarte vratiTipKarte () {
        return this.tip;
    }

    public boolean isIskoriscena() {
        return iskoriscena;
    }

    public void setIskoriscena(boolean iskoriscena) {
        this.iskoriscena = iskoriscena;
    }

    public Integer getNapad() {
        return napad;
    }

    public void setNapad(Integer napad) {
        this.napad = napad;
    }

    public Integer getOdbrana() {
        return odbrana;
    }

    public void setOdbrana(Integer odbrana) {
        this.odbrana = odbrana;
    }

    public Integer getCena() {
        return cena;
    }

    public void setCena(Integer cena) {
        this.cena = cena;
    }

    @Override
    public String toString() {
        return "Karta{" +
                "id=" + id +
                ", napad=" + napad +
                ", odbrana=" + odbrana +
                ", cena=" + cena +
                ", tip=" + tip +
                ", da li je iskoriscena " + iskoriscena +
                '}';
    }
}
