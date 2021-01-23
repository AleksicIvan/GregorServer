package aleksic.Server;

import aleksic.Models.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgrac;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lista {
    List<Igrac> lkor = new ArrayList<>();
    int poslednjiIDIgraca = 0;

    int kreirajIDIgraca() {
        poslednjiIDIgraca++;
        return poslednjiIDIgraca;
    }

    public void kreirajIgraca(TransferObjekatIgrac tok) {
        int idKorisnika = kreirajIDIgraca();
        tok.igr.setIDIgraca(idKorisnika);
        lkor.add(tok.igr);
        tok.poruka = "Igrac je kreiran!!!";
        tok.signal = true;
    }

//    public Igrac nadjiKorisnika(TransferObjekatIgrac tok) {
//        tok.pronadjeniIgrac = null;
//        try {
//            int idIgraca = tok.igr.getIdIgraca();
//            Iterator<Igrac> it = lkor.iterator();
//            while (it.hasNext()) {
//                Igrac igr = it.next();
//                if (igr.equals(idIgraca)) {
//                    tok.pronadjeniIgrac = igr;
//                    tok.indeks = lkor.indexOf(igr);
//                    return igr;
//                }
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Pogresan unos vrednosti u polje id igraca!!!");
//        }
//        return null;
//    }
}
