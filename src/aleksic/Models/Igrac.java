package aleksic.Models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Igrac extends GeneralDObject implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int id;
    private String korisnickoIme;
    private String korisnickaSifra;
    private List<Karta> spil = new ArrayList<>();
    private List<Karta> ruka = new ArrayList<>();
    private Talon talon = new Talon(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
    private int zivot = 10;

    public Igrac(int id, String korisnickoIme) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
    }

    public Igrac(String korisnickoIme, String korisnickaSifra) {
        this.korisnickoIme = korisnickoIme;
        this.korisnickaSifra = korisnickaSifra;
    }

    public Igrac(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public int getZivot() {
        return zivot;
    }

    public void setZivot(int zivot) {
        this.zivot = zivot;
    }

    public List<Karta> postaviSpil (List<Karta> noviSpil) {
        this.spil = noviSpil;
        return this.spil;
    }

    public List<Karta> postaviRuku (List<Karta> novaRuka) {
        this.ruka = novaRuka;
        return this.ruka;
    }

    public Talon postaviTalon (Talon noviTalon) {
        this.talon = noviTalon;
        return this.talon;
    }

    public Talon vratiTalon () {
        return this.talon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String vratiKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }


    public List<Karta> vratiSpil() {
        return spil;
    }

    public List<Karta> vratiRuku() {
        return ruka;
    }

    public void setRuka(List<Karta> ruka) {
        this.ruka = ruka;
    }


    @Override
    public String toString() {
        return "Igrac{" +
                "id=" + id +
                ", korisnickoIme='" + korisnickoIme + '\'' +
                ", spil=" + spil.toString() +
                ", ruka=" + ruka.toString() +
                '}';
    }

    public void setIDIgraca(int idKorisnika) {
        this.id = idKorisnika;
    }

    public int getIdIgraca() {
        return this.id;
    }

    @Override
    public String getAtrValue() {
        return null;
    }

    @Override
    public String setAtrValue() {
        return null;
    }

    @Override
    public String getClassName() {
        return "user_accounts";
    }

    @Override
    public String getWhereCondition() {
        return "username = '" +  korisnickoIme + "' AND password = '" + korisnickaSifra + "'";
    }

    @Override
    public String getNameByColumn(int column) {
        return null;
    }

    @Override
    public GeneralDObject getNewRecord(ResultSet rs) throws SQLException {
        return new Igrac(rs.getInt("id"), rs.getString("username"));
    }

    @Override
    public int getPrimaryKey() {
        return 0;
    }
}
