package aleksic.Server;

import aleksic.Models.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgrac;



/**
 *
 * @author Sinisa
 */
public class KNadjiKorisnika extends OpsteIzvrsenjeSO {
    TransferObjekatIgrac toi;
    
    
    public void nadjiKorisnika(TransferObjekatIgrac tkor)
     { this.toi = tkor;
       opsteIzvrsenjeSO();    
     }
    
    @Override
    public boolean izvrsiSO()
      {  toi.signal = false;
         toi.igr = (Igrac) bbp.findRecord(toi.igr);
         
         
//        if (toi.igr!=null)
//         { toi.signal = true;
//            toi.pozicija = bbp.findRecordPosition(toi.kor);
//            System.out.println("Pozicija sloga:" + toi.pozicija);
//         }
//        else
//         { toi.pozicija = -1;
//         }
       return toi.signal;
      }     
}