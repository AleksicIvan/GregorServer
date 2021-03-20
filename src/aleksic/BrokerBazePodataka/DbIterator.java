package aleksic.BrokerBazePodataka;

import aleksic.DomenskiObjekat.GeneralDObject;

import java.io.Serializable;


public class DbIterator implements Serializable {
       
    BrokerBazePodataka bbp;
    int recordsNumber;
    int currentRecord = -1;
    GeneralDObject gdo;
    
    public DbIterator(){}
    public DbIterator(GeneralDObject gdo1) {this.gdo=gdo1;currentRecord = -1;}
    
        
    public void createDB() { bbp = new BrokerBazePodataka1();}
    
    
    public Object first() { 
        Object ob = getRecordsNumber();
        
        if (ob == null) return ob;
        
        currentRecord = 0; 
        ob = bbp.getRecord(gdo,currentRecord);
        
        bbp.closeConnection();
        
        return ob;
         }

    
    public Object next() {
       // Uneti programski kod
       Object ob = getRecordsNumber();
       if (ob == null) return ob;
       if (recordsNumber <= (currentRecord + 1))
           {}
       else
           { currentRecord++; }
           ob = bbp.getRecord(gdo, currentRecord);
           bbp.closeConnection();
           return ob;
    }

    
    public Object previous() {
        Object ob = getRecordsNumber();
        if (ob == null) return ob;
        
        if (currentRecord-1 < 0) 
          {}
        else 
          { currentRecord--;
          }
        ob = bbp.getRecord(gdo,currentRecord);
        bbp.closeConnection();
        return ob;
         }

    
    public Object last() {
        Object ob = getRecordsNumber();
        if (ob == null) return ob;
        
        
        currentRecord = recordsNumber-1;
        ob = bbp.getRecord(gdo,currentRecord);
        bbp.closeConnection();
        return ob;
        }
    
    
     public Object getRecordsNumber()
      { createDB();
        Object ob = new Object();
        
        bbp.makeConnection();
        recordsNumber = bbp.getRecordsNumber(gdo);
        if (recordsNumber == 0 ) {currentRecord = -1;bbp.closeConnection(); System.out.println("Prosao 24"); return null;}
        
        return ob;
      }
    
     public int getCurrentRecord()
      {return currentRecord;}  
     
     public void setCurrentRecord(int currentRecord)
      {this.currentRecord = currentRecord;}     
     
}
