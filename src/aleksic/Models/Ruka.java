package aleksic.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ruka implements Serializable {
    private Igrac igrac;
    private List<Karta> ruka = new ArrayList<>();

    public Ruka(Igrac igrac, List<Karta> ruka) {
        this.igrac = igrac;
        this.ruka = ruka;
    }

    public Igrac getIgrac() {
        return igrac;
    }

    public void setIgrac(Igrac igrac) {
        this.igrac = igrac;
    }

    public List<Karta> getRuka() {
        return ruka;
    }

    public void setRuka(List<Karta> ruka) {
        this.ruka = ruka;
    }

    @Override
    public String toString() {
        return "Ruka{" +
                "ruka=" + ruka +
                '}';
    }
}
