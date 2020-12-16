package aleksic.Servis;

import java.io.Serializable;

public class FazePoteza implements Serializable {
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
