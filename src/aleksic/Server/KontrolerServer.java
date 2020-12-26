package aleksic.Server;

import aleksic.Servis.Igra;
import aleksic.TransferObjekat.TransferObjekatIgrac;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KontrolerServer {
    static ServerSocket ss;
    static Klijent kl;
    static List<Klijent> lkl = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ss = new ServerSocket(9000);
        int iKlijent = 0;
        while (true) {
            System.out.println("Podignut je serverski program:");
            // programski kod!!!
            Socket soketS = ss.accept();
            kl = new Klijent(soketS, ++iKlijent);
            lkl.add(kl);
        }
    }
}

class Klijent extends Thread {
    private
    Socket soketS;
    ObjectOutputStream out;
    ObjectInputStream in;
    int iKlijent;
    Lista l;
    Datoteka d;
    Igra igra;

    public Klijent(Socket soketS1, int iKlijent) {
        soketS = soketS1;
        this.iKlijent = iKlijent;
        System.out.println("Klijent broj " + iKlijent + " je povezan!");
        l = new Lista();
        // programski kod!!!
        d = new Datoteka(l);
        igra = Igra.getInstance();
//        komunikacijaSaKlijentom();
        start();
    }

    @Override
    public void run() {
        try {
            String signal = "";

            TransferObjekatIgrac toi = null;


            while (true) {
                out = new ObjectOutputStream(soketS.getOutputStream());
                in = new ObjectInputStream(soketS.getInputStream());
                toi = (TransferObjekatIgrac) in.readObject();
                System.out.println("Klijent " + iKlijent + " povezan");
                if (toi.nazivOperacije.equals("init")) {
                    System.out.println("Sistemska operacija je init");
                    toi.igra = igra;
                    System.out.println("Igra je dodata do toi");
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
                    System.out.println("Sistemska operacija je kreirajIgraca");

                    l.kreirajIgraca(toi);
                    if (igra.getIgraci().size() < 2) {
                        igra.dodajIgraca(toi.igr);
                        toi.igra = igra;
                        obavestiProtivnika(toi);
                    }
                }
//                    if (toi.nazivOperacije.equals("napuniDatotekuIzListe")) {
//                        d.napuniDatotekuIzListe(tok);
//                    }
                // programski kod!!!
                out.writeObject(toi);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void obavestiProtivnika(TransferObjekatIgrac toi) throws IOException {
        for (Klijent k : KontrolerServer.lkl) {
            System.out.println("iKlijent je " + iKlijent);
            System.out.println("k.iKlijent je " + k.iKlijent);
            if (k.iKlijent != iKlijent) {
                try {
                    System.out.println("Obavestavam klijenta " + k.iKlijent);
                    toi.poruka = "Sistem je pronasao protivnika.";
                    k.out.writeObject(toi);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
