package aleksic.Servis;

public class FazePoteza {
    Faza faza = null;

    public FazePoteza(Faza faza) {
        this.faza = faza;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "FazePoteza{" +
                "faza=" + faza.toString() +
                '}';
    }
}
