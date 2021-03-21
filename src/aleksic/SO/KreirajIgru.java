package aleksic.SO;

import aleksic.DomenskiObjekat.Igra;
import aleksic.TransferObjekat.TransferObjekatIgrac;



/**
 *
 * @author Sinisa
 */
public class KreirajIgru extends OpsteIzvrsenjeSO {
    TransferObjekatIgrac toi;


    public void kreirajIgru(TransferObjekatIgrac toi) {
        this.toi = toi;
        opsteIzvrsenjeSO();
    }

    @Override
    public boolean izvrsiSO() {
        toi.signal = false;
        Igra igra = (Igra) bbp.findRecord(toi.igra);
        if (igra == null) {
            if (bbp.insertRecord(toi.igra)) {
                toi.poruka = "Igra je kreirana u bazi";
                toi.signal = true;
            }
            else {
                toi.poruka = "Nije mogla da bude kreirana igra.";
            }
        }
        else {
            toi.poruka = "Igra vec postoji";
        }

        return toi.signal;
    }
}
