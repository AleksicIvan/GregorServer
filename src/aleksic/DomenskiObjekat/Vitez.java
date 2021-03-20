package aleksic.DomenskiObjekat;

public class Vitez extends Karta {
    public Vitez(Integer id, Integer napad, Integer odbrana, Integer cena, TipKarte tip, boolean iskoriscena) {
        super(id, napad, odbrana, cena, tip, iskoriscena);
    }

    public Vitez(Integer id) {
        super(id);
    }
}
