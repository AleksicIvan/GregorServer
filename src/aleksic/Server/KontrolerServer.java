package aleksic.Server;

import aleksic.BrokerBazePodataka.BrokerBazePodataka;
import aleksic.BrokerBazePodataka.BrokerBazePodataka1;
import aleksic.DomenskiObjekat.*;
import aleksic.DomenskiObjekat.Faza;
import aleksic.DomenskiObjekat.Igra;
import aleksic.SO.*;
import aleksic.TransferObjekat.TransferObjekatIgra;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KontrolerServer {
    static ServerSocket ss;
    static Klijent kl;
    static List<Klijent> lkl = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Podignut je serverski program:");
        ss = new ServerSocket(9001);
        int iKlijent = 0;
        while (true) {
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
    static public BrokerBazePodataka bbp = new BrokerBazePodataka1();

    public Klijent(Socket soketS1, int iKlijent) {
        soketS = soketS1;
        this.iKlijent = iKlijent;
        System.out.println("Klijent broj " + iKlijent + " je povezan!");
        start();
    }

    private boolean isPrviIgracNaPotezu (TransferObjekatIgra toi) {
        return toi.igracNaPotezu.vratiKorisnickoIme().equals(toi.prviIgrac.vratiKorisnickoIme());
    }

    private void incrementBrojPoteza (TransferObjekatIgra toi) {
        toi.brojPoteza++;
    }

    @Override
    public void run() {
        try {
            String signal = "";
            out = new ObjectOutputStream(soketS.getOutputStream());
            in = new ObjectInputStream(soketS.getInputStream());
            TransferObjekatIgra toi = null;


            while (true && !soketS.isClosed()) {
                toi = (TransferObjekatIgra) in.readObject();

                if (toi.nazivOperacije.equals("odustanak")) {
                    onOdustanak(toi);
                }

                if (toi.nazivOperacije.equals("novaIgra")) {
                    onNovaIgra(toi);
                }

                if (toi.nazivOperacije.equals("init")) {
                    onInit(toi, "Sistemska operacija je init", "Igra je dodata do toi");
                }

                if (toi.nazivOperacije.equals("registracija")) {
                    onRegistracija(toi);
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
                    onKreirajIgraca(toi);
                }

                if (toi.nazivOperacije.equals("dodeliKartu")) {
                    onDodeliKartu(toi);
                }

                if (toi.nazivOperacije.equals("odigrajZlatnik")) {
                    onOdigrajZlatnik(toi);
                }

                if (toi.nazivOperacije.equals("plati")) {
                    onPlati(toi);
                }

                if (toi.nazivOperacije.equals("izbaciViteza")) {
                    onIzbaciViteza(toi);
                }

                if (toi.nazivOperacije.equals("napad")) {
                    onNapad(toi);
                }

                if (toi.nazivOperacije.equals("odbrana")) {
                    onOdbrana(toi);
                }

                if (toi.nazivOperacije.equals("izracunajIshod")) {
                    izracunajIshod(toi);
                }

                if (toi.nazivOperacije.equals("preskociFazu")) {
                    onPreskociFazu(toi);
                }
                
                if (Igra.getInstance().getIgraci().size() <= 1) {
                    if (soketS.isClosed()) return;
                    out.writeObject(toi);
                } else {
                    if (toi.nazivOperacije.equals("odustanak")) {
                        return;
                    }
                    obavestiSve(toi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            try {
                soketS.close();
            } catch (IOException err) {
                e.printStackTrace(System.err);
            }
        }
    }

    private void onRegistracija(TransferObjekatIgra toi) {
        registrujIgraca(toi);
    }

    private void onPreskociFazu(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je preskociFazu " + toi.fazaPoteza);
        if (toi.fazaPoteza.equals(Faza.IZBACI_ZLATNIK)) {
            if (toi.prviPotez) {
                toi.igracNaPotezu = isPrviIgracNaPotezu(toi) ? toi.drugiIgrac : toi.prviIgrac;
                System.out.println("PRVI JE POTEZ i preskocio sam fazu IZBACI_ZLATNIK; igrac na potezu je: " + toi.igracNaPotezu.vratiKorisnickoIme());
                toi.fazaPoteza = Faza.DODELI_KARTU;
                toi.prviPotez = false;
                return;
            }
        }
        if (toi.fazaPoteza.equals(Faza.IZBACI_VITEZA)) {
            if (toi.prviPotez) {
                toi.igracNaPotezu = isPrviIgracNaPotezu(toi) ? toi.drugiIgrac : toi.prviIgrac;
                System.out.println("PRVI JE POTEZ i preskocio sam fazu IZBACI_VITEZA; igrac na potezu je: " + toi.igracNaPotezu.vratiKorisnickoIme());
                toi.fazaPoteza = Faza.DODELI_KARTU;
                toi.prviPotez = false;
                return;
            }
        }
        if (toi.fazaPoteza.equals(Faza.NAPAD)) {
            toi.igracNaPotezu = isPrviIgracNaPotezu(toi) ? toi.drugiIgrac : toi.prviIgrac;
            System.out.println("preskocio sam fazu NAPAD i igrac na potezu je: " + toi.igracNaPotezu.vratiKorisnickoIme());
        }
        if (toi.fazaPoteza.equals(Faza.ODBRANA)) {
            toi.igracNaPotezu = isPrviIgracNaPotezu(toi) ? toi.drugiIgrac : toi.prviIgrac;
            System.out.println("preskocio sam fazu ODBRANA i igrac na potezu je: " + toi.igracNaPotezu.vratiKorisnickoIme());
        }
        if (toi.fazaPoteza.equals(Faza.IZRACUNAJ_ISHOD)) {
            toi.igracNaPotezu = isPrviIgracNaPotezu(toi) ? toi.drugiIgrac : toi.prviIgrac;
            System.out.println("preskocio sam fazu IZRACUNAJ_ISHOD i igrac na potezu je: " + toi.igracNaPotezu.vratiKorisnickoIme());
        }
        if (!toi.prviPotez) {
            izracunajSledecuFazu(toi);
        }
    }

    private void onOdbrana(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je Odbrana.");
        if (isPrviIgracNaPotezu(toi)) {
            for (int i = toi.kliknutiVItezovi.size() - 1; i >= 0 ; i--) {
                toi.redVitezovaPrviIgrac.remove(i);
            }
            toi.redNapadPrviIgrac = new ArrayList<>(toi.kliknutiVItezovi);
            toi.redOdbranaPrviIgrac = new ArrayList<>(toi.kliknutiVItezovi);
            toi.igracNaPotezu = toi.drugiIgrac;
            toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
        } else {
            for (int i = toi.kliknutiVItezovi.size() - 1; i >= 0 ; i--) {
                toi.redRedVitezovaDrugiIgrac.remove(i);
            }
            toi.redNapadDrugiIgrac = new ArrayList<>(toi.kliknutiVItezovi);
            toi.redOdbranaDrugiIgrac = new ArrayList<>(toi.kliknutiVItezovi);
            toi.igracNaPotezu = toi.prviIgrac;
            toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
        }
        toi.kliknutiVItezovi.clear();
    }

    private void onNapad(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je NAPAD.");
        if (toi.fazaPoteza == Faza.NAPAD) {
            if (isPrviIgracNaPotezu(toi)) {
                for (int i = toi.kliknutiVItezovi.size() - 1; i >= 0 ; i--) {
                    toi.redVitezovaPrviIgrac.remove(i);
                }
                toi.redNapadPrviIgrac = new ArrayList<>(toi.kliknutiVItezovi);
                toi.redOdbranaPrviIgrac = new ArrayList<>(toi.kliknutiVItezovi);
                izracunajSledecuFazu(toi);
                toi.igracNaPotezu = toi.drugiIgrac;
            } else {
                for (int i = toi.kliknutiVItezovi.size() - 1; i >= 0 ; i--) {
                    toi.redRedVitezovaDrugiIgrac.remove(i);
                }
                toi.redNapadDrugiIgrac = new ArrayList<>(toi.kliknutiVItezovi);
                toi.redOdbranaDrugiIgrac = new ArrayList<>(toi.kliknutiVItezovi);
                izracunajSledecuFazu(toi);
                toi.igracNaPotezu = toi.prviIgrac;
            }
            toi.kliknutiVItezovi.clear();
        }
    }

    private void onIzbaciViteza(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je izbaci Viteza.");
        if (toi.prviPotez) {
            if (isPrviIgracNaPotezu(toi)) {
                for (int i = 0; i < toi.kliknutiVItezovi.size(); i++) {
                    System.out.println("PETLJA odabrani vitezovi set red vitezova 1 prvi potez");
                    toi.rukaPrvogIgraca.remove(toi.kliknutiVItezovi.get(i));
                }
                ArrayList<Karta> nk = new ArrayList<>();
                nk.addAll(toi.redVitezovaPrviIgrac);
                nk.addAll(new ArrayList<>(toi.kliknutiVItezovi));
                toi.redVitezovaPrviIgrac = nk;
                toi.sizeRedVitezovaPrviIgrac = toi.redVitezovaPrviIgrac.size();
                toi.igracNaPotezu = toi.drugiIgrac;
                toi.fazaPoteza = Faza.DODELI_KARTU;
            } else {
                toi.igracNaPotezu = toi.prviIgrac;
                for (int i = 0; i < toi.kliknutiVItezovi.size(); i++) {
                    System.out.println("PETLJA odabrani vitezovi set red vitezova 2 prvi potez");
                    toi.rukaDrugogIgraca.remove(toi.kliknutiVItezovi.get(i));
                }
                ArrayList<Karta> nk = new ArrayList<>();
                nk.addAll(toi.redRedVitezovaDrugiIgrac);
                nk.addAll(new ArrayList<>(toi.kliknutiVItezovi));
                toi.redRedVitezovaDrugiIgrac = nk;
                toi.sizeRedVitezovaDrugiIgrac = toi.redRedVitezovaDrugiIgrac.size();
                toi.fazaPoteza = Faza.DODELI_KARTU;
            }
            toi.prviPotez = false;
        } else {
            System.out.println("nije prvi potez podesavam red vitezova");
            if (isPrviIgracNaPotezu(toi)) {
                for (int i = 0; i < toi.kliknutiVItezovi.size(); i++) {
                    System.out.println("PETLJA odabrani vitezovi set red vitezova 1");
                    toi.rukaPrvogIgraca.remove(toi.kliknutiVItezovi.get(i));
                }
                ArrayList<Karta> nk = new ArrayList<>();
                nk.addAll(toi.redVitezovaPrviIgrac);
                nk.addAll(new ArrayList<>(toi.kliknutiVItezovi));
                toi.redVitezovaPrviIgrac = nk;
                toi.sizeRedVitezovaPrviIgrac = toi.redVitezovaPrviIgrac.size();

            } else {
                for (int i = 0; i < toi.kliknutiVItezovi.size(); i++) {
                    System.out.println("PETLJA odabrani vitezovi set red vitezova 2");
                    toi.rukaDrugogIgraca.remove(toi.kliknutiVItezovi.get(i));
                }
                ArrayList<Karta> nk = new ArrayList<>();
                nk.addAll(toi.redRedVitezovaDrugiIgrac);
                nk.addAll(new ArrayList<>(toi.kliknutiVItezovi));
                toi.redRedVitezovaDrugiIgrac = nk;
                toi.sizeRedVitezovaDrugiIgrac = toi.redRedVitezovaDrugiIgrac.size();

            }
            izracunajSledecuFazu(toi);
        }
        toi.kliknutiVItezovi.clear();
    }

    private void onPlati(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je plati.");
        izracunajSledecuFazu(toi);
    }

    private void onOdigrajZlatnik(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je odigrajZlatnik.");
        if (toi.fazaPoteza.equals(Faza.IZBACI_ZLATNIK)) {
            if (isPrviIgracNaPotezu(toi)) {
                toi.rukaPrvogIgraca.remove(toi.odigranaKarta);
                toi.talonPrvogIgraca.dodajURedZlatnika(toi.odigranaKarta);
                toi.redZlatnikaPrviIgrac.add(toi.odigranaKarta);
            } else {
                toi.rukaDrugogIgraca.remove(toi.odigranaKarta);
                toi.talonDrugogIgraca.dodajURedZlatnika(toi.odigranaKarta);
                toi.redZlatnikaDrugiIgrac.add(toi.odigranaKarta);
            }
            izracunajSledecuFazu(toi);
        }
    }

    private void onDodeliKartu(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je dodeliKartu.");
        if (isPrviIgracNaPotezu(toi)) {
            dodeliKartuIzSpila(toi.spilPrvogIgraca, toi.rukaPrvogIgraca);
        } else {
            dodeliKartuIzSpila(toi.spilDrugogIgraca, toi.rukaDrugogIgraca);
        }
        izracunajSledecuFazu(toi);
    }

    private void onKreirajIgraca(TransferObjekatIgra toi) {
        Igrac igrac = nadjiIgraca(toi).igr;
        if (igrac != null) {
            Igra.getInstance().dodajIgraca(igrac);
            toi.brojigraca = 1;

            if (Igra.getInstance().getIgraci().size() == 1) {
                onInit(toi, "Sistemska operacija je kreirajIgraca - ima jedan igrac", "Prvi igrac je registrovan. Sistem ceka drugog igraca!!!");
            }

            if (Igra.getInstance().getIgraci().size() == 2) {
                System.out.println("Sistemska operacija je kreirajIgraca - oba igraca su registrovana");
                Igra.getInstance().init();
                inicicijalizacijaToi(toi);
                toi.poruka = "Sistem je pronasao protivnika. Obavestavam zadnjeg ulogovanog!";
            }
        } else {
            toi.poruka = "Greska! Igrac sa unetim vrednostima ne postoji. Probajte ponovo";
        }
    }

    private void onNovaIgra(TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je novaIgra");
        Igra.reset();
        Igra.getInstance().dodajIgraca(toi.prviIgrac);
        Igra.getInstance().dodajIgraca(toi.drugiIgrac);
        Igra.getInstance().init();
        inicicijalizacijaToi(toi);
        toi.poruka = "Nova igra je dodata do toi";
    }

    private void onOdustanak(TransferObjekatIgra toi) {
        Igra.reset();
        onInit(toi, "Sistemska operacija je init", "Igra je dodata do toi");
        System.out.println("The server is shut down!");
    }

    private void onInit(TransferObjekatIgra toi, String s, String s2) {
        System.out.println(s);
        toi.igra = Igra.getInstance();
        toi.poruka = s2;
    }

    public TransferObjekatIgra nadjiIgraca(TransferObjekatIgra toi) {
        System.out.println("Usao u nadji igraca.");
        new NadjiIgraca().nadjiIgraca(toi);
        return toi;
    }

    public TransferObjekatIgra registrujIgraca(TransferObjekatIgra toi) {
        System.out.println("Usao u registruj igraca.");
        new RegistrujIgraca().registrujIgraca(toi);
        return toi;
    }

    public TransferObjekatIgra promeniIgraca(TransferObjekatIgra toi) {
        // Uneti programski kod
        new PromeniIgraca().promeniIgraca(toi);
        return toi;
    }

    public TransferObjekatIgra kreirajIgru(TransferObjekatIgra toi) {
        new KreirajIgru().kreirajIgru(toi);
        return toi;
    }

    private void inicicijalizacijaToi(TransferObjekatIgra toi) {
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
        toi.brojPoteza = 0;
        toi.odigranaKarta = null;
        incrementBrojPoteza(toi);
        toi.fazaPoteza = Faza.IZBACI_ZLATNIK;
        toi.prviPotez = true;
        toi.redVitezovaPrviIgrac = new ArrayList<Karta>();
        toi.redNapadPrviIgrac = new ArrayList<Karta>();
        toi.redOdbranaPrviIgrac = new ArrayList<Karta>();
        toi.redZlatnikaPrviIgrac = new ArrayList<Karta>();
        toi.redRedVitezovaDrugiIgrac = new ArrayList<Karta>();
        toi.redNapadDrugiIgrac = new ArrayList<Karta>();
        toi.redOdbranaDrugiIgrac = new ArrayList<Karta>();
        toi.redZlatnikaDrugiIgrac = new ArrayList<Karta>();
    }

    private void dodeliKartuIzSpila (List<Karta> spil, List<Karta> igracevaRuka) {
        if (spil.size() > 0) {
            Karta prvaKarta = spil.get(0);
            spil.remove(prvaKarta);
            igracevaRuka.add(prvaKarta);
        }
    }

    private void izracunajSledecuFazu(TransferObjekatIgra toi) {
        System.out.println("IZRACUNAJ SLEDECU FAZU metoda -- trenutna toi faza poteza je " + toi.fazaPoteza.toString());
        switch (toi.fazaPoteza) {
            case DODELI_KARTU:
                System.out.println("Trenutna faza je DODELI_KARTU, povecavam brojPoteza za 1");
                incrementBrojPoteza(toi);
                System.out.println("nova faza poteza je IZBACI_ZLATNIK");
                toi.fazaPoteza = Faza.IZBACI_ZLATNIK;
                break;
            case IZBACI_ZLATNIK:
                System.out.println("nova faza poteza je PLATI");
                toi.fazaPoteza = Faza.PLATI;
                break;
            case PLATI:
                System.out.println("nova faza poteza je IZBACI_VITEZA");
                toi.fazaPoteza = Faza.IZBACI_VITEZA;
                break;
            case IZBACI_VITEZA:
                System.out.println("nova faza poteza je NAPAD");
                toi.fazaPoteza = Faza.NAPAD;
                break;
            case NAPAD:
                System.out.println("nova faza poteza je ODBRANA");
                toi.fazaPoteza = Faza.ODBRANA;
                break;
            case ODBRANA:
                System.out.println("nova faza poteza je IZRACUNAJ_ISHOD");
                toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
                break;
            case IZRACUNAJ_ISHOD:
                toi.fazaPoteza = Faza.DODELI_KARTU;
                break;
            default:
                System.out.println("default default default default default DODELI_KARTU!!!!");
                toi.fazaPoteza = Faza.DODELI_KARTU;
        }
    }

    private void izracunajIshod (TransferObjekatIgra toi) {
        System.out.println("Sistemska operacija je izracunajIshod.");
        System.out.println("igrac na potezu : " + toi.igracNaPotezu.vratiKorisnickoIme());
        if (isPrviIgracNaPotezu(toi)) {
            System.out.println("Napadac je prvi igrac");
            int napad = toi.redNapadPrviIgrac.size();
            int odbrana = toi.redOdbranaDrugiIgrac.size();

            if (napad != 0 || odbrana != 0) {
                if (napad == odbrana) {
                    System.out.println("Napad == odbrana");

                    for (int i = napad - 1; i >= 0; i--) {
                        toi.redNapadPrviIgrac.remove(i);
                        toi.redOdbranaPrviIgrac.remove(i);
                        toi.redNapadDrugiIgrac.remove(i);
                        toi.redOdbranaDrugiIgrac.remove(i);
                    }
                    ArrayList<Karta> nk0 = new ArrayList<>();
                    nk0.addAll(toi.redVitezovaPrviIgrac);
                    nk0.addAll(new ArrayList<>(toi.redNapadPrviIgrac));
                    toi.redVitezovaPrviIgrac = nk0;

                    ArrayList<Karta> nk1 = new ArrayList<>();
                    nk1.addAll(toi.redRedVitezovaDrugiIgrac);
                    nk1.addAll(new ArrayList<>(toi.redOdbranaDrugiIgrac));
                    toi.redRedVitezovaDrugiIgrac = nk1;
                }

                if (napad > odbrana) {
                    System.out.println("Napad > odbrana");
                    if (odbrana == 0) {
                        System.out.println("Napad > odbrana, a odbrana = 0");
                        toi.redVitezovaPrviIgrac = new ArrayList<>(toi.redNapadPrviIgrac);
                        toi.redNapadPrviIgrac.clear();
                        toi.redOdbranaPrviIgrac.clear();
                    } else {
                        System.out.println("Napad > odbrana, a odbrana NIJE 0");
                        for (int i = odbrana - 1; i >= 0; i--) {
                            toi.redNapadPrviIgrac.remove(i);
                            toi.redOdbranaPrviIgrac.remove(i);
                            toi.redNapadDrugiIgrac.remove(i);
                            toi.redOdbranaDrugiIgrac.remove(i);
                        }
                        ArrayList<Karta> nk1 = new ArrayList<>();
                        nk1.addAll(toi.redVitezovaPrviIgrac);
                        nk1.addAll(new ArrayList<>(toi.redNapadPrviIgrac));
                        toi.redVitezovaPrviIgrac = nk1;

                        ArrayList<Karta> nk = new ArrayList<>();
                        nk.addAll(toi.redRedVitezovaDrugiIgrac);
                        nk.addAll(new ArrayList<>(toi.redOdbranaDrugiIgrac));
                        toi.redRedVitezovaDrugiIgrac = nk;
                    }
                    toi.drugiIgrac.setZivot(toi.drugiIgrac.getZivot() - (napad - odbrana));
                }

                if (odbrana > napad) {
                    System.out.println("Odbrana > napad");
                    if (napad == 0) {
                        System.out.println("Odbrana > napad, a napad = 0");
                        toi.redRedVitezovaDrugiIgrac = new ArrayList<>(toi.redNapadDrugiIgrac);
                        toi.redOdbranaDrugiIgrac.clear();
                        toi.redNapadDrugiIgrac.clear();
                    } else {
                        System.out.println("Napad > odbrana, a odbrana NIJE 0");
                        for (int i = napad - 1; i >= 0; i--) {
                            toi.redNapadPrviIgrac.remove(i);
                            toi.redOdbranaPrviIgrac.remove(i);
                            toi.redNapadDrugiIgrac.remove(i);
                            toi.redOdbranaDrugiIgrac.remove(i);
                        }
                        ArrayList<Karta> nk1 = new ArrayList<>();
                        nk1.addAll(toi.redVitezovaPrviIgrac);
                        nk1.addAll(new ArrayList<>(toi.redNapadPrviIgrac));
                        toi.redVitezovaPrviIgrac = nk1;

                        ArrayList<Karta> nk = new ArrayList<>();
                        nk.addAll(toi.redRedVitezovaDrugiIgrac);
                        nk.addAll(new ArrayList<>(toi.redOdbranaDrugiIgrac));
                        toi.redRedVitezovaDrugiIgrac = nk;
                    }
                }
            }

            toi.igracNaPotezu = toi.drugiIgrac;
        } else {
            System.out.println("Napadac je drugi igrac");
            int napad = toi.redNapadDrugiIgrac.size();
            int odbrana = toi.redOdbranaPrviIgrac.size();

            if (napad != 0 || odbrana != 0) {
                if (napad == odbrana) {
                    System.out.println("Napad == odbrana");
                    for (int i = napad - 1; i >= 0; i--) {
                        toi.redNapadPrviIgrac.remove(i);
                        toi.redOdbranaPrviIgrac.remove(i);
                        toi.redNapadDrugiIgrac.remove(i);
                        toi.redOdbranaDrugiIgrac.remove(i);
                    }
                    ArrayList<Karta> nk1 = new ArrayList<>();
                    nk1.addAll(toi.redVitezovaPrviIgrac);
                    nk1.addAll(new ArrayList<>(toi.redOdbranaPrviIgrac));
                    toi.redVitezovaPrviIgrac = nk1;

                    ArrayList<Karta> nk = new ArrayList<>();
                    nk.addAll(toi.redRedVitezovaDrugiIgrac);
                    nk.addAll(new ArrayList<>(toi.redNapadDrugiIgrac));
                    toi.redRedVitezovaDrugiIgrac = nk;
                }

                if (napad > odbrana) {
                    System.out.println("Napad > odbrana");

                    if (odbrana == 0) {
                        System.out.println("Napad > odbrana, a odbrana = 0");

                        toi.redRedVitezovaDrugiIgrac = new ArrayList<>(toi.redNapadDrugiIgrac);
                        toi.redOdbranaDrugiIgrac.clear();
                        toi.redNapadDrugiIgrac.clear();
                    } else {
                        System.out.println("Napad > odbrana, a odbrana NIJE 0");

                        for (int i = odbrana - 1; i >= 0; i--) {
                            toi.redNapadPrviIgrac.remove(i);
                            toi.redOdbranaPrviIgrac.remove(i);
                            toi.redNapadDrugiIgrac.remove(i);
                            toi.redOdbranaDrugiIgrac.remove(i);
                        }
                        ArrayList<Karta> nk1 = new ArrayList<>();
                        nk1.addAll(toi.redVitezovaPrviIgrac);
                        nk1.addAll(new ArrayList<>(toi.redOdbranaPrviIgrac));
                        toi.redVitezovaPrviIgrac = nk1;

                        ArrayList<Karta> nk = new ArrayList<>();
                        nk.addAll(toi.redRedVitezovaDrugiIgrac);
                        nk.addAll(new ArrayList<>(toi.redNapadDrugiIgrac));
                        toi.redRedVitezovaDrugiIgrac = nk;
                    }
                    toi.prviIgrac.setZivot(toi.prviIgrac.getZivot() - (napad - odbrana));
                }

                if (odbrana > napad) {
                    System.out.println("Odbrana > napad");

                    if (napad == 0) {
                        System.out.println("Odbrana > napad, a napad = 0");

                        toi.redVitezovaPrviIgrac = new ArrayList<>(toi.redNapadPrviIgrac);
                        toi.redOdbranaPrviIgrac.clear();
                        toi.redNapadPrviIgrac.clear();
                    } else {
                        System.out.println("Odbrana > napad, a napad NIJE 0");

                        for (int i = napad - 1; i >= napad; i--) {
                            toi.redNapadPrviIgrac.remove(i);
                            toi.redOdbranaPrviIgrac.remove(i);
                            toi.redNapadDrugiIgrac.remove(i);
                            toi.redOdbranaDrugiIgrac.remove(i);
                        }
                        ArrayList<Karta> nk1 = new ArrayList<>();
                        nk1.addAll(toi.redVitezovaPrviIgrac);
                        nk1.addAll(new ArrayList<>(toi.redOdbranaPrviIgrac));
                        toi.redVitezovaPrviIgrac = nk1;

                        ArrayList<Karta> nk = new ArrayList<>();
                        nk.addAll(toi.redRedVitezovaDrugiIgrac);
                        nk.addAll(new ArrayList<>(toi.redNapadDrugiIgrac));
                        toi.redRedVitezovaDrugiIgrac = nk;
                    }
                }
            }
            toi.igracNaPotezu = toi.prviIgrac;
        }

        toi.redNapadPrviIgrac.clear();
        toi.redOdbranaPrviIgrac.clear();
        toi.redNapadDrugiIgrac.clear();
        toi.redOdbranaDrugiIgrac.clear();
        toi.igra = Igra.getInstance();
        if (toi.prviIgrac.getZivot() <= 0 || toi.drugiIgrac.getZivot() <= 0) {
            Igra.getInstance().setKrajIgre(true);
            Igrac pobednik = toi.prviIgrac.getZivot() > 0
                    ? toi.prviIgrac
                    : toi.drugiIgrac;
            Igra.getInstance().setIdPobednika(pobednik.getId());
            Igra.getInstance().setBrojPoteza(toi.brojPoteza);
            kreirajIgru(toi);
            toi.poruka = "Kraj igre. Pobednik je " + pobednik.vratiKorisnickoIme() + "! Da li zelite novu igru?";
            // TODO: Pozovi update Igraca u bazi (update za broj odigranih paritija i broj pobeda)
        } else if (toi.spilPrvogIgraca.size() == 0) {
            Igra.getInstance().setKrajIgre(true);
            Igra.getInstance().setIdPobednika(toi.drugiIgrac.getId());
            Igra.getInstance().setBrojPoteza(toi.brojPoteza);
            kreirajIgru(toi);
            toi.poruka = "Kraj igre. Pobednik je " + toi.drugiIgrac.vratiKorisnickoIme() + "! Da li zelite novu igru?";
        } else if (toi.spilDrugogIgraca.size() == 0) {
            Igra.getInstance().setKrajIgre(true);
            Igra.getInstance().setIdPobednika(toi.prviIgrac.getId());
            Igra.getInstance().setBrojPoteza(toi.brojPoteza);
            kreirajIgru(toi);
            toi.poruka = "Kraj igre. Pobednik je " + toi.prviIgrac.vratiKorisnickoIme() + "! Da li zelite novu igru?";
        } else {
            izracunajSledecuFazu(toi);
        }
    }

    private void obavestiSve(TransferObjekatIgra toi) throws IOException {
        if (soketS.isClosed()) {
            return;
        }
        for (Klijent k : KontrolerServer.lkl) {
            System.out.println("obavestavam klijenta " + k.iKlijent);
            k.out.writeObject(toi);
        }
    }
}
