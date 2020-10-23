import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

abstract class Rute extends Rectangle{

    private Rute start;
    protected boolean utvei;
    private int rad;
    private int kolonne;
    private Labyrint labyrint;
    private Rute nord, øst, sør, vest = null;
    private ArrayList<Rute> naboListe = new ArrayList<Rute>();
    private static ArrayList<Rute> brukteRuter = new ArrayList<Rute>();

    public Rute(int rad, int kolonne) {
        this.rad = rad;
        this.kolonne = kolonne;
    }

    protected void gaa(String rute) {
        if(sjekkUtvei() && this != start) {
            labyrint.utveier.leggTil(rute);
            // labyrint.utveier.leggTil(rute + " UTVEI");
            return;
        }
        brukteRuter.add(this);
        if(this.tilTegn() == '.') {
            // naboListe.add(this);
            for(Rute r : naboListe) {
                if(!brukteRuter.contains(r)) {
                    r.gaa(rute + " --> (" + r.rad + ", " + r.kolonne + ")");
                }
            }
        }
    }


    protected void finnUtvei() {
        Rute start = this;
        brukteRuter.add(start);
        start.gaa("(" + start.rad + ", " + start.kolonne + ")");
        // start.gaa("START: (" + start.rad + ", " + start.kolonne + ")");
        brukteRuter.clear();
    }

    protected void skrivBrukteRuter() {
        int teller = 0;
        for(Rute r : brukteRuter) {
            teller++;
        }
        System.out.println("Brukte ruter: " + teller);
    }

    protected void settLabyrint(Labyrint l) {
        labyrint = l;
    }

    protected void settNord(Rute r) {
        nord = r;
        naboListe.add(nord);
    }

    protected void settØst(Rute r) {
        øst = r;
        naboListe.add(øst);
    }

    protected void settSør(Rute r) {
        sør = r;
        naboListe.add(sør);
    }

    protected void settVest(Rute r) {
        vest = r;
        naboListe.add(vest);
    }

    protected Labyrint hentRef() {
        return labyrint;
    }

    protected boolean sjekkUtvei() {
        return false;
    }

    abstract char tilTegn();





}
