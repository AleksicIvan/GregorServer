package aleksic.Server;

import aleksic.Models.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgrac;



/**
 *
 * @author Sinisa
 */
public class KNadjiIgraca extends OpsteIzvrsenjeSO {
    TransferObjekatIgrac toi;
    
    
    public void nadjiIgraca(TransferObjekatIgrac toi) {
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