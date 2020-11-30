package aleksic.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Talon implements Serializable {
    private List<Karta> redZlatnika = new ArrayList<>();
    private List<Karta> redVitezova = new ArrayList<>();
    private List<Karta> redNapad = new ArrayList<>();
    private List<Karta> redOdbrana = new ArrayList<>();
    private List<Karta> groblje = new ArrayList<>();

    public Talon(List<Karta> redZlatnika, List<Karta> redVitezova, List<Karta> redNapad, List<Karta> redOdbrana, List<Karta> groblje) {
        this.redZlatnika = redZlatnika;
        this.redVitezova = redVitezova;
        this.redNapad = redNapad;
        this.redOdbrana = redOdbrana;
        this.groblje = groblje;
    }

    public void izbaciIzRedaZlatnika (Karta odigraniZlatnik) {
        redZlatnika.remove(odigraniZlatnik);
    }

    public void dodajURedZlatnika (Karta odigraniZlatnik) {
        redZlatnika.add(odigraniZlatnik);
    }

    public void dodajURedVitezova (Karta odigraniVitez) {
        redVitezova.add(odigraniVitez);
    }

    public void dodajURedNapad (Karta odigraniVitez) {
        // ovde ce arg biti lista izabranih napadaca
        redNapad.add(odigraniVitez);
    }

    public void dodajURedOdbrana (Karta odigraniVitez) {
        redOdbrana.add(odigraniVitez);
    }

    public void dodajUGroblje (Karta odigraniVitez) {
        groblje.add(odigraniVitez);
        redVitezova.remove(odigraniVitez);
    }

    public List<Karta> vratiGroblje () {
        return groblje;
    }

    public List<Karta> getRedZlatnika() {
        return redZlatnika;
    }

    public void setRedZlatnika(List<Karta> redZlatnika) {
        this.redZlatnika = redZlatnika;
    }

    public List<Karta> getRedVitezova() {
        return redVitezova;
    }

    public void setRedVitezova(List<Karta> redVitezova) {
        this.redVitezova = redVitezova;
    }

    public List<Karta> getRedNapad() {
        return redNapad;
    }

    public void setRedNapad(List<Karta> redNapad) {
        this.redNapad = redNapad;
    }

    public List<Karta> getRedOdbrana() {
        return redOdbrana;
    }

    public void setRedOdbrana(List<Karta> redOdbrana) {
        this.redOdbrana = redOdbrana;
    }

    @Override
    public String toString() {
        return "Talon{" +
                "redZlatnika=" + redZlatnika +
                ", redVitezova=" + redVitezova +
                ", redNapad=" + redNapad +
                '}';
    }
}
