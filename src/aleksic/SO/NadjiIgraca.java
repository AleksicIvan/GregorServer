package aleksic.SO;

import aleksic.DomenskiObjekat.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgra;



/**
 *
 * @author Ivan Aleksic
 */
public class NadjiIgraca extends OpsteIzvrsenjeSO {
    TransferObjekatIgra toi;
    
    
    public void nadjiIgraca(TransferObjekatIgra toi) {
        this.toi = toi;
        opsteIzvrsenjeSO();
     }
    
    @Override
    public boolean izvrsiSO() {
        toi.signal = false;
        toi.igr = (Igrac) bbp.findRecord(toi.igr);

        if (toi.igr!=null) {
            toi.signal = true;
         }
       return toi.signal;
      }
}