package aleksic.SO;

import aleksic.DomenskiObjekat.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgra;

public class RegistrujIgraca extends OpsteIzvrsenjeSO {
    TransferObjekatIgra toi;

    public void registrujIgraca(TransferObjekatIgra toi) {
        this.toi = toi;
        opsteIzvrsenjeSO();
    }

    @Override
    public boolean izvrsiSO() {
        toi.signal = false;
        Igrac igrac = (Igrac) bbp.findRecord(toi.igr);

        if (igrac == null) {
            if (bbp.insertRecord(toi.igr)) {
                toi.poruka = "Novi igrac je registrovan. Ulogujte se!";
                toi.signal = true;
            }
        } else {
            toi.poruka = "Greska!!! Igrac sa unetim vrenostima vec postoji u bazi. Ulogujte se!";
        }

        return toi.signal;
    }
}
