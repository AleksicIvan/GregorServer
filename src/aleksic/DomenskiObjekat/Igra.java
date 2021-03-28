package aleksic.DomenskiObjekat;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Igra extends GeneralDObject implements Serializable {
    private int id;
    private static Igra instance;
    private List<Igrac> igraci = new ArrayList<>();
    private Date datumIgre;
    private Igrac igracNaPotezu;
    private Faza fazaPoteza;
    private int brojPoteza = 0;
    private int idPobednika;
    private boolean krajIgre = false;

    public Igra(Date datumIgre) {
        this.datumIgre = datumIgre;
    }

    public Igra(int id) {
        this.id = id;
    }

    public Igra(List<Igrac> igraci, Date datumIgre) {
        this.igraci = igraci;
        this.datumIgre = datumIgre;
        init();
    }

    public static synchronized Igra getInstance() {
        if (instance == null) {
            instance = new Igra(new Date());
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }



    public void setBrojPoteza(int brojPoteza) {
        this.brojPoteza = brojPoteza;
    }

    public List<Igrac> getIgraci() {
        return igraci;
    }

    public void dodajIgraca (Igrac igrac) {
        igraci.add(igrac);
    }

    public void init() {
        for (Integer i = 0; i < igraci.size(); i++) {
            Igrac trenutniIgrac = igraci.get(i);
            trenutniIgrac.setZivot(10);
            Spil spil = new Spil();
            List<Karta> promesaniSpil = spil.promesajSpil();
            List<Karta> podeljenaRuka = spil.podeliRuku(promesaniSpil);
            trenutniIgrac.postaviSpil(promesaniSpil);
            trenutniIgrac.postaviRuku(podeljenaRuka);
            System.out.println("Igrac: " + trenutniIgrac.vratiKorisnickoIme());
            System.out.println(trenutniIgrac.vratiKorisnickoIme()  + " zivot: " + trenutniIgrac.getZivot());
            System.out.println(trenutniIgrac.vratiKorisnickoIme()  + " spil: " + trenutniIgrac.vratiSpil().size());
            System.out.println(trenutniIgrac.vratiKorisnickoIme() + " ruka: " + trenutniIgrac.vratiRuku());
            System.out.println(trenutniIgrac.vratiKorisnickoIme() + " talon: " + trenutniIgrac.vratiTalon());
        }
        setIgracNaPotezu(odrediIgracaNaPrvomPotezu(igraci.get(0), igraci.get(1)));
        postaviFazuPoteza(Faza.IZBACI_ZLATNIK);
    }

    public void setIdPobednika (int id) {
        idPobednika = id;
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

    public void postaviFazuPoteza(Faza faza) {
        this.fazaPoteza = faza;
    }

    public void setIgracNaPotezu(Igrac igracNaPotezu) {
        this.igracNaPotezu = igracNaPotezu;
    }

    public void setKrajIgre(boolean krajIgre) {
        this.krajIgre = krajIgre;
    }

    @Override
    public String getAtrValue() {
        return brojPoteza + ", " + idPobednika;
    }

    @Override
    public String setAtrValue() {
        return "brojPoteza = '" +  brojPoteza + "', pobednik = '" + idPobednika + "'";
    }

    @Override
    public String getClassName() {
        return "game";
    }

    @Override
    public String getWhereCondition() {
        return "id = '" +  id + "'";
    }

    @Override
    public String getNameByColumn(int column) {
        return null;
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws SQLException {
        return new Igra(rs.getInt("id"));
    }

    @Override
    public int getPrimaryKey() {
        return 0;
    }

    @Override
    public String getInsertAtributes() {
        return "brojPoteza, pobednik";
    }
}
