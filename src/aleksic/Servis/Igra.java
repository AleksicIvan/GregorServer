package aleksic.Servis;

import aleksic.Models.Igrac;
import aleksic.Models.Karta;
import aleksic.Models.Spil;
import aleksic.Models.TipKarte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Igra implements Serializable {
    private List<Igrac> igraci = new ArrayList<>();
    private Date datumIgre;
    private Igrac igracNaPotezu;
    private FazePoteza fazaPoteza;
    private boolean krajIgre = false;

    public Igra(Date datumIgre) {
        this.datumIgre = datumIgre;
    }

    public Igra(List<Igrac> igraci, Date datumIgre) {
        this.igraci = igraci;
        this.datumIgre = datumIgre;
        init(igraci);
    }

    public List<Igrac> getIgraci() {
        return igraci;
    }

    public void dodajIgraca (Igrac igrac) {
        igraci.add(igrac);
    }

    public void init(List<Igrac> igraci) {
        for (Integer i = 0; i < igraci.size(); i++) {
            Igrac trenutniIgrac = igraci.get(i);
            trenutniIgrac.setZivot(20);
            Spil spil = new Spil();
            List<Karta> promesaniSpil = spil.promesajSpil();
            List<Karta> podeljenaRuka = spil.podeliRuku(promesaniSpil);
            trenutniIgrac.postaviSpil(promesaniSpil);
            trenutniIgrac.postaviRuku(podeljenaRuka);
            System.out.println("Igrac: " + trenutniIgrac.vratiKorisnickoIme());
            System.out.println(trenutniIgrac.vratiKorisnickoIme()  + " zivot: " + trenutniIgrac.getZivot());
            System.out.println(trenutniIgrac.vratiKorisnickoIme()  + " spil: " + trenutniIgrac.vratiSpil());
            System.out.println(trenutniIgrac.vratiKorisnickoIme() + " ruka: " + trenutniIgrac.vratiRuku());
            System.out.println(trenutniIgrac.vratiKorisnickoIme() + " talon: " + trenutniIgrac.vratiTalon());
        }
        setIgracNaPotezu(odrediIgracaNaPrvomPotezu(igraci.get(0), igraci.get(1)));
        postaviFazuPoteza(Faza.IZBACI_ZLATNIK);
//        while (isKrajIgre() != true) {
//            odigrajPotez();
//        }
    }

    public Igrac odrediIgracaNaPrvomPotezu (Igrac igrac1, Igrac igrac2) {
        int dice = (int)(Math.random()*6+1);
        if (dice >= 3) {
            return igrac1;
        } else {
            return igrac2;
        }
    }

    public Igrac vratiIgracaNaPotezu() {
        return igracNaPotezu;
    }

    public FazePoteza vratiFazuPoteza() {
        return fazaPoteza;
    }

    public void postaviFazuPoteza(Faza faza) {
        fazaPoteza = new FazePoteza(faza);
        this.fazaPoteza = fazaPoteza;
    }

    public void setIgracNaPotezu(Igrac igracNaPotezu) {
        this.igracNaPotezu = igracNaPotezu;
    }

    public Igrac getIgracNaPotezu() {
        return igracNaPotezu;
    }

    public void odigrajPotez () {
        Scanner scanner = new Scanner(System.in);
        System.out.println("__________________");
        System.out.println(vratiIgracaNaPotezu().vratiKorisnickoIme() + " je na potezu. Faza igre je " + vratiFazuPoteza().faza.toString());
        Karta odigranaKarta = null;
        if (vratiFazuPoteza().faza.equals(Faza.IZBACI_ZLATNIK)) {
            var igracevaRuka = igracNaPotezu.vratiRuku();
            Predicate<Karta> poTipu = k -> k.vratiTipKarte().equals(TipKarte.ZLATNIK);
            odigranaKarta = igracevaRuka
                    .stream()
                    .filter(poTipu)
                    .findFirst()
                    .get();
            System.out.println("Karta iz ruke je: " + odigranaKarta.toString());
            if (odigranaKarta == null) {
                System.out.println("Nemate Zlatnik u ruci. Prelazimo na sledecu fazu igre");
                postaviFazuPoteza(Faza.ODIGRAJ_VITEZA);
                return;
            }
            System.out.println("Odigraj Zlatnik? y/n");
            var odgovorIgraca = scanner.nextLine();
            if (odgovorIgraca == "n") {
                System.out.println("Odabrali ste da ne odigrate Zlatnik. Prelazimo na sledecu fazu igre");
                postaviFazuPoteza(Faza.ODIGRAJ_VITEZA);
                return;
            }
            igracevaRuka.remove(odigranaKarta);
            var igracevTalon = igracNaPotezu.vratiTalon();
            var novaIgracevaRuka = igracNaPotezu.vratiRuku();
            igracevTalon.dodajURedZlatnika(odigranaKarta);
            var noviTalon = igracNaPotezu.vratiTalon();
            System.out.println("Nakon poteza IZBACI_ZLATNIK novi Talon igraca je " + noviTalon);
            System.out.println("Nakon poteza IZBACI_ZLATNIK novi Ruka igraca je " + novaIgracevaRuka);
            postaviFazuPoteza(Faza.ODIGRAJ_VITEZA);
            System.out.println("Faza poteza nakon IZBACI_ZLATNIK je: " + vratiFazuPoteza());
            if (igracNaPotezu.getZivot() == 0) {
                setKrajIgre(true);
            } else {
                setKrajIgre(false);
            }
        }

        if (vratiFazuPoteza().faza.equals(Faza.ODIGRAJ_VITEZA)) {
            var igracevaRuka = igracNaPotezu.vratiRuku();
            Predicate<Karta> poTipu = k -> k.vratiTipKarte().equals(TipKarte.VITEZ);
            // ponoviti dok god ima zlatnika u redu zlatnika i vitezova u ruci
            odigranaKarta = igracevaRuka
                    .stream()
                    .filter(poTipu)
                    .findFirst()
                    .get();
            System.out.println("Karta iz ruke je: " + odigranaKarta.toString());
            if (odigranaKarta == null) {
                System.out.println("Nemate Viteza u ruci. Prelazimo na sledecu fazu igre");
                postaviFazuPoteza(Faza.NAPAD);
                return;
            }
            System.out.println("Odigraj  Viteza? y/n");
            var odgovorIgraca = scanner.nextLine();
            if (odgovorIgraca == "n") {
                System.out.println("Odabrali ste da ne odigrate Viteza. Prelazimo na sledecu fazu igre");
                postaviFazuPoteza(Faza.NAPAD);
                return;
            }
            igracevaRuka.remove(odigranaKarta);
            var igracevTalon = igracNaPotezu.vratiTalon();
            var novaIgracevaRuka = igracNaPotezu.vratiRuku();
            igracevTalon.dodajURedVitezova(odigranaKarta);
            var noviTalon = igracNaPotezu.vratiTalon();
            System.out.println("Nakon poteza ODIGRAJ_VITEZA novi Talon igraca je " + noviTalon);
            System.out.println("Nakon poteza ODIGRAJ_VITEZA novi Ruka igraca je " + novaIgracevaRuka);
            postaviFazuPoteza(Faza.NAPAD);
            System.out.println("Faza poteza nakon ODIGRAJ_VITEZA je: " + vratiFazuPoteza());
            if (igracNaPotezu.getZivot() == 0) {
                setKrajIgre(true);
            } else {
                setKrajIgre(false);
            }
        }

        if (vratiFazuPoteza().faza.equals(Faza.NAPAD)) {
            System.out.println("dosli smo do faze NAPAD");
            var igracevTalon = igracNaPotezu.vratiTalon();
            if (igracevTalon.getRedVitezova().size() != 0) {
                System.out.println("igracevTalon red vitezova PRE je: " + igracevTalon.getRedVitezova());
                System.out.println("igracevTalon red napad PRE je: " + igracevTalon.getRedNapad());
                Karta vitezKarta = igracevTalon.getRedVitezova().get(0);
                igracevTalon.dodajURedNapad(vitezKarta);
                vitezKarta.setIskoriscena(true);
                System.out.println("vitezKarta iz reda vitezova je: " + vitezKarta.toString());
                System.out.println("igracevTalon red vitezova POSLE je: " + igracevTalon.getRedVitezova());
                System.out.println("igracevTalon red napad POSLE je: " + igracevTalon.getRedNapad());
                System.out.println("STARI IGRAC ZA ODBRANU je : " + igracNaPotezu.toString());
                setIgracNaPotezu(igraci.stream().filter(i -> i.getId() != igracNaPotezu.getId()).findFirst().get());
                System.out.println("NOVI IGRAC ZA ODBRANU je : " + igracNaPotezu.toString());
                postaviFazuPoteza(Faza.ODBRANA);
            } else {
                System.out.println("Nemate dovoljno Vitezova za fazu NAPAD : ");
                setIgracNaPotezu(igraci.stream().filter(i -> i.getId() != igracNaPotezu.getId()).findFirst().get());
                postaviFazuPoteza(Faza.IZBACI_ZLATNIK);
            }
        }
        if (vratiFazuPoteza().faza.equals(Faza.ODBRANA)) {
            System.out.println("dosli smo do faze ODBRANA");
            var igracevTalon = igracNaPotezu.vratiTalon();
            if (igracevTalon.getRedVitezova().size() != 0) {
                System.out.println("IGRAC U ODBRANI je : " + igracNaPotezu.toString());
                System.out.println("igracevTalon red vitezova PRE je: " + igracevTalon.getRedVitezova());
                System.out.println("igracevTalon red napad PRE je: " + igracevTalon.getRedNapad());
                Karta vitezKarta = igracevTalon.getRedVitezova().get(0);
                System.out.println("Da li hoces da iskoristis Viteza za odbranu? y/n");
                String odgovor = scanner.nextLine();
                if (odgovor == "y") {
                    igracevTalon.dodajURedOdbrana(vitezKarta);
                    vitezKarta.setIskoriscena(true);
                    var igracUOdbrani = igracNaPotezu;
                    setIgracNaPotezu(igraci.stream().filter(i -> i.getId() != igracUOdbrani.getId()).findFirst().get());
                    // b o r b a
                    for (int i = 0; i < igracNaPotezu.vratiTalon().getRedNapad().size(); i++) {
                        for (int j = 0; j < igracUOdbrani.vratiTalon().getRedOdbrana().size(); j++) {
                            if (igracNaPotezu
                                    .vratiTalon()
                                    .getRedNapad()
                                    .get(i)
                                    .getNapad()
                                    >= igracUOdbrani
                                    .vratiTalon()
                                    .getRedOdbrana()
                                    .get(j)
                                    .getOdbrana()
                            ) {
                                igracUOdbrani.vratiTalon().dodajUGroblje(igracUOdbrani.vratiTalon().getRedOdbrana().get(j));
                                System.out.println("Groblje karata Igraca u ddbrani: " + igracUOdbrani.vratiTalon().vratiGroblje());
                            } else {
                                igracNaPotezu.vratiTalon().dodajUGroblje(igracNaPotezu.vratiTalon().getRedOdbrana().get(j));
                                System.out.println("Groblje karata Igraca u napadu: " + igracNaPotezu.vratiTalon().vratiGroblje());
                            }
                        }
                    }
                    postaviFazuPoteza(Faza.IZBACI_ZLATNIK);
                } else {
                    igracNaPotezu.setZivot(getIgracNaPotezu().getZivot() - 1);
                    System.out.println("Zivot Igraca u odbrani: " + igracNaPotezu.getZivot());
                }

            } else {
                System.out.println("Nemate dovoljno Vitezova za fazu ODBRANA : ");
                igracNaPotezu.setZivot(igracNaPotezu.getZivot() - 1);
                System.out.println("Zivot Igraca u odbrani: " + igracNaPotezu.getZivot());
                setIgracNaPotezu(igraci.stream().filter(i -> i.getId() != igracNaPotezu.getId()).findFirst().get());
                postaviFazuPoteza(Faza.IZBACI_ZLATNIK);
            }
        }
        setKrajIgre(true);
    }

    public boolean isKrajIgre() {
        return krajIgre;
    }

    public void setKrajIgre(boolean krajIgre) {
        this.krajIgre = krajIgre;
    }
}
