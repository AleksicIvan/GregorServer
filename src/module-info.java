module GregorServer {
    requires java.sql;
    opens aleksic.Servis;
    opens aleksic.Models;
    opens aleksic.Server;
    opens aleksic.TransferObjekat;
    opens aleksic.BrokerBazePodataka;
}