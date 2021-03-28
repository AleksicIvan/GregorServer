package aleksic.SO;

import aleksic.DomenskiObjekat.Igra;
import aleksic.TransferObjekat.TransferObjekatIgra;


/**
 *
 * @author Ivan Aleksic
 */
public class PromeniIgru extends OpsteIzvrsenjeSO {
    TransferObjekatIgra toi;


    public void promeniIgru(TransferObjekatIgra toi) {
        this.toi = toi;
        opsteIzvrsenjeSO();
    }

    @Override
    public boolean izvrsiSO() {
        toi.signal = false;
        Igra igra = (Igra) bbp.findRecord(toi.igra);
        if (igra != null) {
            if (bbp.updateRecord(toi.igra)) {
                System.out.println("Igra je azurirana i sacuvana.");
                toi.poruka = "Igra je azurirana i sacuvana.";
                toi.signal = true;
            } else {
                System.out.println("Nije bilo moguce azurirati igru.");
                toi.poruka = "Nije bilo moguce azurirati igru.";
            }
        } else {
            System.out.println("Ne moze se azurirati igra jer ne postoji.");
            toi.poruka = "Ne moze se azurirati igra jer ne postoji.";
        }

        return toi.signal;
    }
}