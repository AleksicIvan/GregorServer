package aleksic.SO;

import aleksic.DomenskiObjekat.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgra;


/**
 *
 * @author Ivan Aleksic
 */
public class PromeniIgraca extends OpsteIzvrsenjeSO {
    TransferObjekatIgra toi;


    public void promeniIgraca(TransferObjekatIgra toi) {
        this.toi = toi;
        opsteIzvrsenjeSO();
    }

    @Override
    public boolean izvrsiSO() {
        toi.signal = false;
        Igrac igrac = (Igrac) bbp.findRecord(toi.igr);
        if (igrac != null) {
            if (bbp.updateRecord(toi.igr)) {
                toi.poruka = "Igrac je promenjen.";
                toi.signal = true;
            } else {
                toi.poruka = "Nije mogao da se promeni igrac.";
            }
        } else {
            toi.poruka = "Ne moze se promeniti igrac jer ne postoji.";
        }

        return toi.signal;
    }
}