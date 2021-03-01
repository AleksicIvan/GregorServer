package aleksic.Server;

import aleksic.Models.*;
import aleksic.Servis.Faza;
import aleksic.Servis.Igra;
import aleksic.TransferObjekat.TransferObjekatIgrac;

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

    private boolean isPrviIgracNaPotezu (TransferObjekatIgrac toi) {
        return toi.igracNaPotezu.vratiKorisnickoIme().equals(toi.prviIgrac.vratiKorisnickoIme());
    }

    private void incrementBrojPoteza (TransferObjekatIgrac toi) {
        toi.brojPoteza++;
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

                if (toi.nazivOperacije.equals("init")) {
                    System.out.println("Sistemska operacija je init");
                    toi.igra = Igra.getInstance();
//                    if (toi.igra.getIgraci().size() == 1) {
//                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
//                    }
//                    toi.brojigraca = toi.igra.getIgraci().size() == 1 ? 1 : 0;
                    System.out.println("Igra je dodata do toi");
//                    out.writeObject(toi);
                }

                if (toi.nazivOperacije.equals("kreirajIgraca")) {
//                    l.kreirajIgraca(toi);
                    Igra.getInstance().dodajIgraca(toi.igr);

//                    if (Igra.getInstance().getIgraci().size() == 0) {
//                        System.out.println("Sistemska operacija je kreirajIgraca - nema jos igraca");
//                        toi.igra = Igra.getInstance();
//                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
                        toi.brojigraca = 1;
//                        out.writeObject(toi);
//                        obavestiProtivnika(toi);
//                    }

                    if (Igra.getInstance().getIgraci().size() == 1) {
                        System.out.println("Sistemska operacija je kreirajIgraca - ima jedan igrac");
                        toi.igra = Igra.getInstance();
//                        toi.prviIgrac = Igra.getInstance().getIgraci().get(0);
//                        toi.brojigraca = 1;
                        toi.poruka = "Prvi igrac je registrovan. Sistem ceka drugog igraca!!!";
//                        out.writeObject(toi);
//                        obavestiProtivnika(toi, "Prvi igrac je registrovan. Sistem ceka drugog igraca!!!");
                    }

                    if (Igra.getInstance().getIgraci().size() == 2) {
                        System.out.println("Sistemska operacija je kreirajIgraca - oba igraca su registrovana");
//
////                        igra.dodajIgraca(toi.igr);
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
                        toi.brojPoteza = 0;
                        incrementBrojPoteza(toi);
                        toi.fazaPoteza = Faza.IZBACI_ZLATNIK;
                        if (isPrviIgracNaPotezu(toi)) {
                            // prvi na potezu
                            if (toi.rukaPrvogIgraca.stream().filter(k -> k.vratiTipKarte().equals(TipKarte.ZLATNIK)).count() > 0) {
                                toi.prviPotez = true;
                            } else {
                                toi.prviPotez = false;
                                toi.igracNaPotezu = toi.drugiIgrac;
                            }
                        } else {
                            // drugi na potezu
                            if (toi.rukaDrugogIgraca.stream().filter(k -> k.vratiTipKarte().equals(TipKarte.ZLATNIK)).count() > 0) {
                                toi.prviPotez = true;
                            } else {
                                toi.prviPotez = false;
                                toi.igracNaPotezu = toi.prviIgrac;
                            }
                        }
                        toi.poruka = "Sistem je pronasao protivnika.Obavestavam zadnjeg ulogovanog!";
//                        out.writeObject(toi);
//                        obavestiProtivnika(toi, "Sistem je pronasao protivnika. Obavestavam zadnjeg ulogovanog!");
                    }

                }

                if (toi.nazivOperacije.equals("dodeliKartu")) {
                    System.out.println("Sistemska operacija je dodeliKartu.");
                    if (isPrviIgracNaPotezu(toi)) {
                        dodeliKartuIzSpila(toi.spilPrvogIgraca, toi.rukaPrvogIgraca);
                    } else {
                        dodeliKartuIzSpila(toi.spilDrugogIgraca, toi.rukaDrugogIgraca);
                    }
                    izracunajSledecuFazu(toi);
                }

                if (toi.nazivOperacije.equals("odigrajZlatnik")) {
                    // odigrava se uvek jedan zlatnik
                    System.out.println("Sistemska operacija je odigrajZlatnik.");
                    if (toi.fazaPoteza.equals(Faza.IZBACI_ZLATNIK)) {
                        if (isPrviIgracNaPotezu(toi)) {
                            toi.rukaPrvogIgraca.remove(toi.odigranaKarta);
                            toi.talonPrvogIgraca.dodajURedZlatnika(toi.odigranaKarta);
                        } else {
                            toi.rukaDrugogIgraca.remove(toi.odigranaKarta);
                            toi.talonDrugogIgraca.dodajURedZlatnika(toi.odigranaKarta);
                        }
                        izracunajSledecuFazu(toi);
                    }
                }
//
                if (toi.nazivOperacije.equals("plati")) {
                    // TODO omoguci placanje sa vise zlatnika ukoliko ih ima u redu zlatnika
                    System.out.println("Sistemska operacija je plati.");
                    izracunajSledecuFazu(toi);
                }

                if (toi.nazivOperacije.equals("izbaciViteza")) {
                    // TODO omoguci izbacivanje vise viteza koliko ih ima u ruci i ako je placeno za njih
                    System.out.println("Sistemska operacija je izbaci Viteza.");
                    System.out.println("prvi potez >> " + toi.prviPotez);
                    if (toi.fazaPoteza == Faza.IZBACI_VITEZA) {
                        if (isPrviIgracNaPotezu(toi)) {
                            setRedVitezova(toi.rukaPrvogIgraca, toi.kliknutiVItezovi, toi.talonPrvogIgraca);
                            if (toi.prviPotez == true) {
                                toi.prviPotez = false;
                                toi.igracNaPotezu = toi.drugiIgrac;
                                toi.fazaPoteza = Faza.DODELI_KARTU;
                            } else {
                                izracunajSledecuFazu(toi);
                            }
                        } else {
                            setRedVitezova(toi.rukaDrugogIgraca, toi.kliknutiVItezovi, toi.talonDrugogIgraca);
                            if (toi.prviPotez == true) {
                                toi.prviPotez = false;
                                toi.igracNaPotezu = toi.prviIgrac;
                                toi.fazaPoteza = Faza.DODELI_KARTU;
                            } else {
                                izracunajSledecuFazu(toi);
                            }
                        }
                    }
                    toi.kliknutiVItezovi.clear();
                }

                if (toi.nazivOperacije.equals("napad")) {
                    // TODO omoguci napad sa vise viteza koliko ih ima u redu vitezova
                    System.out.println("Sistemska operacija je NAPAD.");
                    if (toi.fazaPoteza == Faza.NAPAD) {
                        if (isPrviIgracNaPotezu(toi)) {
//                            toi.talonPrvogIgraca.getRedVitezova().remove(toi.odigranaKarta);
//                            toi.talonPrvogIgraca.dodajURedNapad(toi.odigranaKarta);
                            setRedNapadIOdbrana(toi.kliknutiVItezovi, toi.talonPrvogIgraca);
                            izracunajSledecuFazu(toi);
                            toi.igracNaPotezu = toi.drugiIgrac;
//                            if (toi.talonDrugogIgraca.getRedVitezova().stream().filter(k -> k.vratiTipKarte().equals(TipKarte.VITEZ)).count() > 0) {
//                                toi.fazaPoteza = Faza.ODBRANA;
//                                toi.igracNaPotezu = toi.drugiIgrac;
//                            } else {
//                                toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
//                                // izracunaj i postavi zivot
//                            }
                        } else {
//                            toi.talonDrugogIgraca.getRedVitezova().remove(toi.odigranaKarta);
//                            toi.talonDrugogIgraca.dodajURedNapad(toi.odigranaKarta);
                            setRedNapadIOdbrana(toi.kliknutiVItezovi, toi.talonDrugogIgraca);
                            izracunajSledecuFazu(toi);
                            toi.igracNaPotezu = toi.prviIgrac;
//                            if (toi.talonPrvogIgraca.getRedVitezova().stream().filter(k -> k.vratiTipKarte().equals(TipKarte.VITEZ)).count() > 0) {
//                                toi.fazaPoteza = Faza.ODBRANA;
//                                toi.igracNaPotezu = toi.prviIgrac;
//                            } else {
//                                toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
//                                // izracunaj i postavi zivot
//                            }
                        }

                    }
                    toi.kliknutiVItezovi.clear();
                }
//
                if (toi.nazivOperacije.equals("odbrana")) {
                    // TODO omoguci odbranu sa vise viteza koliko ih ima u redu vitezova
                    System.out.println("Sistemska operacija je Odbrana.");
//                    if (toi.fazaPoteza == Faza.ODBRANA) {
                    if (isPrviIgracNaPotezu(toi)) {
                        setRedNapadIOdbrana(toi.kliknutiVItezovi, toi.talonPrvogIgraca);
                        toi.igracNaPotezu = toi.drugiIgrac;
                        toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
                    } else {
                        setRedNapadIOdbrana(toi.kliknutiVItezovi, toi.talonDrugogIgraca);
                        toi.igracNaPotezu = toi.prviIgrac;
                        toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
                    }
                    toi.kliknutiVItezovi.clear();
//                    izracunajSledecuFazu(toi);
//                    }
                }

                if (toi.nazivOperacije.equals("izracunajIshod")) {
                    // TODO omoguci odbranu sa vise viteza koliko ih ima u redu vitezova
                    System.out.println("Sistemska operacija je izracunajIshod.");
                    izracunajIshod(toi);
                }

                if (toi.nazivOperacije.equals("preskociFazu")) {
                    System.out.println("Sistemska operacija je preskociFazu " + toi.fazaPoteza);
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
                    izracunajSledecuFazu(toi);
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
//                out.reset();
                if (Igra.getInstance().getIgraci().size() <= 1) {
                    out.writeObject(toi);
                } else {
                    obavestiSve(toi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private void dodeliKartuIzSpila (List<Karta> spil, List<Karta> igracevaRuka) {
        Karta prvaKarta = spil.get(0);
        spil.remove(prvaKarta);
        igracevaRuka.add(prvaKarta);
    }

    private void izracunajSledecuFazu(TransferObjekatIgrac toi) {
        System.out.println("toi faza poteza je " + toi.fazaPoteza.toString());
        switch (toi.fazaPoteza) {
            case DODELI_KARTU:
                System.out.println("DODELI_KARTU");
                toi.fazaPoteza = Faza.IZBACI_ZLATNIK;
                break;
            case IZBACI_ZLATNIK:
                System.out.println("IZBACI_ZLATNIK");
                toi.fazaPoteza = Faza.PLATI;
                break;
            case PLATI:
                System.out.println("PLATI");
                toi.fazaPoteza = Faza.IZBACI_VITEZA;
                break;
            case IZBACI_VITEZA:
                System.out.println("IZBACI_VITEZA");
                toi.fazaPoteza = Faza.NAPAD;
                break;
            case NAPAD:
                System.out.println("NAPAD");
                toi.fazaPoteza = Faza.ODBRANA;
                break;
            case ODBRANA:
                System.out.println("ODBRANA");
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

    private void setRedVitezova(List<Karta> ruka, List<Karta> odabraniVitezovi, Talon igracevTalon) {
        for (int i = 0; i < odabraniVitezovi.size(); i++) {
            for (int j = 0; j < ruka.size(); j++) {
                if (odabraniVitezovi.get(i).getId().equals(ruka.get(j).getId())) {
                    ruka.remove(j);
                    igracevTalon.dodajURedVitezova(odabraniVitezovi.get(i));
                }
            }
        }
    }

    private void setRedNapadIOdbrana(List<Karta> odabraniVitezovi, Talon igracevTalon) {
        for (int i = 0; i < odabraniVitezovi.size(); i++) {
            for (int j = 0; j < igracevTalon.getRedVitezova().size(); j++) {
                if (odabraniVitezovi.get(i).getId().equals(igracevTalon.getRedVitezova().get(j).getId())) {
                    igracevTalon.getRedVitezova().remove(j);
                    igracevTalon.dodajURedNapad(odabraniVitezovi.get(i));
                    igracevTalon.dodajURedOdbrana(odabraniVitezovi.get(i));
                }
            }
        }
    }

    private void izracunajIshod (TransferObjekatIgrac toi) {
        System.out.println("igrac na potezu : " + toi.igracNaPotezu.vratiKorisnickoIme());
        if (isPrviIgracNaPotezu(toi)) {
//                            toi.talonPrvogIgraca.getRedVitezova().remove(toi.odigranaKarta);
//                            toi.talonPrvogIgraca.dodajURedNapad(toi.odigranaKarta);
//                            toi.talonPrvogIgraca.dodajURedOdbrana(toi.odigranaKarta);
            setRedNapadIOdbrana(toi.talonPrvogIgraca.getRedVitezova(), toi.talonPrvogIgraca);
//            toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
            int napad = 0;
            int odbrana = 0;

            for (int i = 0; i < toi.talonDrugogIgraca.getRedNapad().size(); i++) {
                napad = toi.talonDrugogIgraca.getRedNapad().get(i).getNapad() + napad;
            }
            for (int i = 0; i < toi.talonPrvogIgraca.getRedOdbrana().size(); i++) {
                odbrana = toi.talonPrvogIgraca.getRedOdbrana().get(i).getOdbrana() + odbrana;
            }
            System.out.println("size of toi.talonDrugogIgraca.getRedNapad(): " + toi.talonDrugogIgraca.getRedNapad().size());
            System.out.println("napad: " + napad);
            System.out.println("size of toi.talonPrvogIgraca.getRedOdbrana(): " +toi.talonPrvogIgraca.getRedOdbrana());
            System.out.println("odbrana: " + odbrana);
            if (odbrana == 1) {
                toi.talonDrugogIgraca.setRedNapad(new ArrayList<>());
            } else {
                for(int x = toi.talonDrugogIgraca.getRedNapad().size() - 1; x >= odbrana; x--) {
                    toi.talonDrugogIgraca.getRedNapad().remove(x);
                }
            }

            if (napad == 1) {
                toi.talonPrvogIgraca.setRedNapad(new ArrayList<>());
                toi.talonPrvogIgraca.setRedOdbrana(new ArrayList<>());
            } else {
                for(int x = toi.talonPrvogIgraca.getRedNapad().size() - 1; x >= napad; x--) {
                    toi.talonPrvogIgraca.getRedNapad().remove(x);
                }
                for(int x = toi.talonPrvogIgraca.getRedOdbrana().size() - 1; x >= napad; x--) {
                    toi.talonPrvogIgraca.getRedOdbrana().remove(x);
                }
            }
            System.out.println("nakon napada: " + toi.talonDrugogIgraca.getRedNapad().size());
            System.out.println("nakon odbrane: " + toi.talonPrvogIgraca.getRedOdbrana().size());

            toi.igracNaPotezu = toi.drugiIgrac;
        } else {
//                            toi.talonDrugogIgraca.getRedVitezova().remove(toi.odigranaKarta);
////                            toi.talonDrugogIgraca.dodajURedNapad(toi.odigranaKarta);
////                            toi.talonDrugogIgraca.dodajURedOdbrana(toi.odigranaKarta);
            setRedNapadIOdbrana(toi.talonDrugogIgraca.getRedVitezova(), toi.talonDrugogIgraca);
//            toi.fazaPoteza = Faza.IZRACUNAJ_ISHOD;
            int napad = 0;
            for (int i = 0; i < toi.talonPrvogIgraca.getRedNapad().size(); i++) {
                napad = toi.talonPrvogIgraca.getRedNapad().get(i).getNapad() + napad;
            }
            int odbrana = 0;
            for (int i = 0; i < toi.talonDrugogIgraca.getRedOdbrana().size(); i++) {
                odbrana = toi.talonDrugogIgraca.getRedOdbrana().get(i).getOdbrana() + odbrana;
            }
            System.out.println("size of toi.talonPrvogIgraca.getRedNapad(): " + toi.talonPrvogIgraca.getRedNapad().size());
            System.out.println("napad: " + napad);
            System.out.println("size of toi.talonDrugogIgraca.getRedOdbrana(): " +toi.talonDrugogIgraca.getRedOdbrana());
            System.out.println("odbrana: " + odbrana);
            if (odbrana == 1) {
                toi.talonPrvogIgraca.setRedNapad(new ArrayList<>());
            } else {
                for(int x = toi.talonPrvogIgraca.getRedNapad().size() - 1; x >= odbrana; x--) {
                    toi.talonPrvogIgraca.getRedNapad().remove(x);
                }
            }

            if (napad == 1) {
                toi.talonDrugogIgraca.setRedNapad(new ArrayList<>());
                toi.talonDrugogIgraca.setRedOdbrana(new ArrayList<>());
            } else {
                for(int x = toi.talonPrvogIgraca.getRedNapad().size() - 1; x >= napad; x--) {
                    toi.talonDrugogIgraca.getRedNapad().remove(x);
                }
                for(int x = toi.talonPrvogIgraca.getRedOdbrana().size() - 1; x >= napad; x--) {
                    toi.talonDrugogIgraca.getRedOdbrana().remove(x);
                }
            }
            System.out.println("nakon napada: " + toi.talonPrvogIgraca.getRedNapad().size());
            System.out.println("nakon odbrane: " + toi.talonDrugogIgraca.getRedOdbrana().size());
            toi.igracNaPotezu = toi.prviIgrac;
        }
        toi.fazaPoteza = Faza.DODELI_KARTU;
//        if (isPrviIgracNaPotezu(toi)) {
//            dodeliKartuIzSpila(toi.spilPrvogIgraca, toi.rukaPrvogIgraca);
//        } else {
//            dodeliKartuIzSpila(toi.spilDrugogIgraca, toi.rukaDrugogIgraca);
//        }
    }

    private void obavestiSve(TransferObjekatIgrac toi) throws IOException {
        for (Klijent k : KontrolerServer.lkl) {
            System.out.println("obavestavam klijenta " + k.iKlijent);
            k.out.writeObject(toi);
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
