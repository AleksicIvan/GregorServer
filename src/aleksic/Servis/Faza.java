package aleksic.Servis;

import java.io.Serializable;

public enum Faza implements Serializable {
    IZBACI_ZLATNIK, PLATI, IZBACI_VITEZA, NAPAD, ODBRANA, IZRACUNAJ_ISHOD, DODELI_KARTU;
}
