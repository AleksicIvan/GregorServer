package aleksic.SO;

import aleksic.BrokerBazePodataka.BrokerBazePodataka;
import aleksic.BrokerBazePodataka.BrokerBazePodataka1;
import aleksic.DomenskiObjekat.GeneralDObject;



/**
 *
 * @author Ivan Aleksic
 */
public abstract class OpsteIzvrsenjeSO {
    static public BrokerBazePodataka bbp = new BrokerBazePodataka1();
    int recordsNumber;
    int currentRecord = -1;
    GeneralDObject gdo;
    
    synchronized public boolean opsteIzvrsenjeSO() {
        bbp.makeConnection();
        boolean signal = izvrsiSO();
        if (signal==true) bbp.commitTransation();
        else
            bbp.rollbackTransation();
            bbp.closeConnection();
            return signal;
      }    
       
     
    abstract public boolean izvrsiSO();
}
