package aleksic.Server;

import aleksic.Servis.Faza;
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
        System.out.println("Podignut je serverski program:");
        ss = new ServerSocket(9000);
        int iKlijent = 0;
        while (true) {
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

    public Klijent(Socket soketS1, int iKlijent) {
        soketS = soketS1;
        this.iKlijent = iKlijent;
        System.out.println("Klijent broj " + iKlijent + " je povezan!");
        l = new Lista();
        // programski kod!!!
        d = new Datoteka(l);
//        komunikacijaSaKlijentom();
        start();
    }

    @Override
    public void run() {
        try {
            String signal = "";
            out = new ObjectOutputStream(soketS.getOutputStream());
            in = new ObjectInputStream(soketS.getInputStream());
            TransferObjekatIgrac toi = null;


            while (true) {
                toi = (TransferObjekatIgrac) in.readObject();

                System.out.println("toi.indeks " + toi.indeks);

                if (toi.rukaPrvogIgraca != null) {
                    System.out.println("ruka prvog igraca posle citanja: " + toi.rukaPrvogIgraca.size());
                }

                if (toi.rukaDrugogIgraca != null) {
                    System.out.println("ruka drugog igraca posle citanja: " + toi.rukaDrugogIgraca.size());
                }




                System.out.println("procitao sam toi, majketiga");
                if (toi.nazivOperacije.equals("init")) {
                    System.out.println("Sistemska operacija je init");
                    toi.igra = Igra.getInstance();
                    if (toi.igra.getIgraci().size() == 1) {
                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
                    }
                    toi.brojigraca = toi.igra.getIgraci().size() == 1 ? 1 : 0;
                    System.out.println("Igra je dodata do toi");
                    out.writeObject(toi);
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
//                    l.kreirajIgraca(toi);
                    Igra.getInstance().dodajIgraca(toi.igr);

//                    if (Igra.getInstance().getIgraci().size() == 0) {
//                        System.out.println("Sistemska operacija je kreirajIgraca - nema jos igraca");
//                        toi.igra = Igra.getInstance();
//                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
//                        toi.brojigraca = 1;
//                        out.writeObject(toi);
//                        obavestiProtivnika(toi);
//                    }

                    if (Igra.getInstance().getIgraci().size() == 1) {
                        System.out.println("Sistemska operacija je kreirajIgraca - ima jedan igrac");
                        toi.igra = Igra.getInstance();
                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
                        toi.brojigraca = 1;
                        toi.poruka = "Prvi igrac je registrovan. Sistem ceka drugog igraca!!!";
                        out.writeObject(toi);
                        obavestiProtivnika(toi, "Prvi igrac je registrovan. Sistem ceka drugog igraca!!!");
                    }

                    if (Igra.getInstance().getIgraci().size() == 2) {
                        System.out.println("Sistemska operacija je kreirajIgraca - oba igraca su registrovana");

//                        igra.dodajIgraca(toi.igr);
                        Igra.getInstance().init();
                        toi.igra = Igra.getInstance();
                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
                        toi.spilPrvogIgraca = Igra.getInstance().getIgraci().get(0).vratiSpil();
                        toi.talonPrvogIgraca = Igra.getInstance().getIgraci().get(0).vratiTalon();
                        toi.rukaPrvogIgraca = Igra.getInstance().getIgraci().get(0).vratiRuku();
                        toi.drugiIgrac = Igra.getInstance().getIgraci().get(1);
                        toi.spilDrugogIgraca = Igra.getInstance().getIgraci().get(1).vratiSpil();
                        toi.talonDrugogIgraca = Igra.getInstance().getIgraci().get(1).vratiTalon();
                        toi.rukaDrugogIgraca = Igra.getInstance().getIgraci().get(1).vratiRuku();
                        toi.igracNaPotezu = Igra.getInstance().vratiIgracaNaPotezu();
                        toi.brojigraca = 2;
                        toi.fazaPoteza = Igra.getInstance().vratiFazuPoteza();
                        toi.poruka = "Sistem je pronasao protivnika.Obavestavam zadnjeg ulogovanog!";
                        out.writeObject(toi);
                        obavestiProtivnika(toi, "Sistem je pronasao protivnika. Obavestavam zadnjeg ulogovanog!");
                    }

                }

                if (toi.nazivOperacije.equals("odigrajZlatnik")) {
                    System.out.println("Sistemska operacija je odigrajZlatnik.");
                    obavestiProtivnika(toi, "Protivnik je odigrao zlatnik kartu. Obavestavam drugogo igraca!");
                }

                if (toi.nazivOperacije.equals("plati")) {
                    System.out.println("Sistemska operacija je plati.");
                    obavestiProtivnika(toi, "Protivnik je platio zlatnik kartom. Obavestavam drugogo igraca!");
                }

                if (toi.nazivOperacije.equals("promenaFaze")) {
                    System.out.println("Sistemska operacija je promena faze.");
                    obavestiProtivnika(toi, "Faza igre je promenjena. Obavestavam drugogo igraca!");
                }
//
                if (toi.nazivOperacije.equals("izbaciViteza")) {
                    System.out.println("Sistemska operacija je izbaci Viteza.");
                    obavestiProtivnika(toi, "Protivnik je izbacio viteza. Obavestavam drugogo igraca!");
                }
//
//                if (toi.nazivOperacije.equals("napadniVitezom")) {
//                    System.out.println("Sistemska operacija je napadni vitezom.");
//                    obavestiProtivnika(toi, "Protivnik je napao vitezom. Obavestavam drugogo igraca!");
//                }
//                    if (toi.nazivOperacije.equals("napuniDatotekuIzListe")) {
//                        d.napuniDatotekuIzListe(tok);
//                    }
                // programski kod!!!
//                System.out.println(toi);
//                out.writeObject(toi);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void obavestiProtivnika(TransferObjekatIgrac toi, String poruka) throws IOException {
        for (Klijent k : KontrolerServer.lkl) {
            System.out.println("iKlijent je " + iKlijent);
            System.out.println("k.iKlijent je " + k.iKlijent);
            if (k.iKlijent != iKlijent) {
                try {
                    System.out.println("Obavestavam klijenta " + k.iKlijent);
                    toi.poruka = poruka;
                    toi.igr = Igra.getInstance().getIgraci().get(0);
                    k.out.writeObject(toi);
//                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
