package natalia.koc.sklepZoologiczny.baza;

import natalia.koc.sklepZoologiczny.domain.Dostawa;
import natalia.koc.sklepZoologiczny.domain.Produkt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DatabaseDumps {

    public static List<Produkt> produkty;
    public static List<Dostawa> dostawy;
    public static Integer lastId;

    public DatabaseDumps() {
        produkty = new ArrayList<>();
        dostawy = new ArrayList<>();
        lastId = 0;

        dostawy.add(new Dostawa(0,"DPD", "24 godziny", 12.00f));
        dostawy.add(new Dostawa(1,"Inpost", "24 godziny", 15.00f));
        dostawy.add(new Dostawa(2,"Poczta Polska", "3 dni", 10.00f));

        var p = new Produkt(++lastId, "Smycz dla psa", "5m, kolor zielony, max 25kg",
                 69.99f, true);
        produkty.add(p);
        p = new Produkt(++lastId, "Obroża dla psa", "10-30cm, różowa",
                 24.68f, true);
        produkty.add(p);
        p = new Produkt(++lastId, "Miska dla psa", "30cm, duża, metalowa",
                12.99f, false);
        produkty.add(p);
        p = new Produkt(++lastId, "Poidełko dla gryzoni", "300ml, plastikowe, uchwyt",
                32.50f, true);
        produkty.add(p);
        p = new Produkt(++lastId, "Karma dla kota", "5kg, Whiskas, jagnięcina z ryżem",
                45.00f, true);
        produkty.add(p);
        p = new Produkt(++lastId, "Szczotka dla psa", "niebieska, do długiej sierści",
                15.98f, false);
        produkty.add(p);
    }

    public List<Dostawa> getDostawy() {
        return dostawy;
    }

    public List<Produkt> getProdukty() {
        return produkty;
    }

    public void setProdukty(List<Produkt> produkty) {
        this.produkty = produkty;
    }
}
