package aleksic.Server;

import aleksic.Servis.Igra;
import aleksic.TransferObjekat.TransferObjekatIgrac;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class KontrolerServer {
    static ServerSocket ss;
    static Klijent kl;

    public static void main(String[] args) throws Exception {
        ss = new ServerSocket(9000);
        int iKlijent = 0;
        while (true) {
            System.out.println("Podignut je serverski program:");
            // programski kod!!!
            Socket soketS = ss.accept();
            kl = new Klijent(soketS, ++iKlijent);
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
//        igra = new Igra(new Date());
//        komunikacijaSaKlijentom();
        start();
    }

    @Override
    public void run() {
        try {
            String signal = "";

            TransferObjekatIgrac toi;

            while (true) {
                out = new ObjectOutputStream(soketS.getOutputStream());
                in = new ObjectInputStream(soketS.getInputStream());
                toi = (TransferObjekatIgrac) in.readObject();
                System.out.println("Klijent " + this + " povezan");
                if (toi.nazivOperacije.equals("init")) {
                    System.out.println("Sistemska operacija je init");

                    if (toi.igra == null) {
                        igra = new Igra(new Date());
                        toi.igra = igra;
                        System.out.println("Igra je kreirana i dodata do toi");
                    } else {
                        System.out.println("Igra vec postoji i dodata je do toi");
                        toi.igra = igra;
                    }
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
                    System.out.println("Sistemska operacija je kreirajIgraca");

                    l.kreirajIgraca(toi);
                    if (igra.getIgraci().size() < 2) {
                        igra.dodajIgraca(toi.igr);
                        toi.igra = igra;
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

    public void komunikacijaSaKlijentom() {
        try {
            String signal = "";

            TransferObjekatIgrac toi;

            while (true) {
                out = new ObjectOutputStream(soketS.getOutputStream());
                in = new ObjectInputStream(soketS.getInputStream());
                toi = (TransferObjekatIgrac) in.readObject();
                System.out.println("Klijent " + this + " povezan");
                if (toi.nazivOperacije.equals("init")) {
                    if (toi.igra == null) {
                        igra = new Igra(new Date());
                        toi.igra = igra;
                    } else {
                        toi.igra = igra;
                    }
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
                    l.kreirajIgraca(toi);
                    if (igra.getIgraci().size() < 2) {
                        igra.dodajIgraca(toi.igr);
                        toi.igra = igra;
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
}
