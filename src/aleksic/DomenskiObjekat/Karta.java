package aleksic.DomenskiObjekat;

import java.io.Serializable;
import java.util.Objects;

public class Karta implements Serializable {
    Integer id = null;
    Integer napad = null;
    Integer odbrana = null;
    Integer cena = null;
    TipKarte tip = null;
    boolean iskoriscena = false;
    boolean pokaziKartu = true;
    boolean isDisabled = true;

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

    public boolean isPokaziKartu() {
        return pokaziKartu;
    }

    public void setPokaziKartu(boolean value) {
        this.pokaziKartu = value;
    }

    public boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean disabled) {
        isDisabled = disabled;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Karta karta = (Karta) o;
        return id.equals(karta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
                ", da li je treba pokazati " + pokaziKartu +
                ", da li je disabled " + isDisabled +
                '}';
    }
}
