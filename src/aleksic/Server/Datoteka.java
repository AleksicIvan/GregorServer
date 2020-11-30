package aleksic.Server;

import aleksic.Models.Igrac;
import aleksic.TransferObjekat.TransferObjekatIgrac;

import java.io.*;
import java.util.List;

public class Datoteka {
    Lista ls;

    public Datoteka(Lista ls) {
        this.ls = ls;
    }


    public void ucitajListuIzDatoteke(TransferObjekatIgrac tok) throws IOException, FileNotFoundException, ClassNotFoundException, FileNotFoundException {
        String filepath = "DatIgraca.txt";
        FileInputStream fileIn = null;
        boolean signal = new File("DatKorisnika.txt").isFile();
        if (signal) {
            fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            ls.lkor = (List<Igrac>) objectIn.readObject();
        }
    }

    public void napuniDatotekuIzListe(TransferObjekatIgrac tok) throws FileNotFoundException, IOException {
        String filepath = "DatIgraca.txt";
        FileOutputStream fileOut = null;

        if (ls.lkor != null) {
            fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(ls.lkor);
        }
    }

}
