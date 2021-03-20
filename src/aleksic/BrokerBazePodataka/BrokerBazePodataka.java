package aleksic.BrokerBazePodataka;


import aleksic.DomenskiObjekat.GeneralDObject;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.List;


public abstract class BrokerBazePodataka 
{   public abstract boolean makeConnection();
    public abstract boolean insertRecord(GeneralDObject odo);
    public abstract boolean updateRecord(GeneralDObject odo,GeneralDObject odoold);
    public abstract boolean updateRecord(GeneralDObject odo); 
    public abstract boolean deleteRecord(GeneralDObject odo);
    public abstract boolean deleteRecords(GeneralDObject odo,String where); 
    public abstract GeneralDObject findRecord(GeneralDObject odo);
    public abstract List<GeneralDObject> findRecord(GeneralDObject odo,String where);
    public abstract boolean commitTransation();
    public abstract boolean rollbackTransation();
    public abstract boolean getCounter(GeneralDObject odo,AtomicInteger counter); 
    public abstract boolean increaseCounter(GeneralDObject odo,AtomicInteger counter); 
    public abstract void closeConnection();
    public abstract GeneralDObject getRecord(GeneralDObject odo,int index);
    public abstract int getRecordsNumber(GeneralDObject odo); 
    public abstract int findRecordPosition(GeneralDObject odo); 
}
