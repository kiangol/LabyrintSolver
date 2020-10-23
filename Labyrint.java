import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Labyrint {
    Rute[][] rutenett;
    int rader;
    int kolonner;
    Lenkeliste<String> utveier = new Lenkeliste<String>();

    private Labyrint(Rute[][] rutenett, int rader, int kolonner) {
        this.rutenett = rutenett;
        this.rader = rader;
        this.kolonner = kolonner;
    }

    public static Labyrint lesFraFil(File file) throws FileNotFoundException {
        Scanner fil = new Scanner(file);
        String[] str = fil.nextLine().split(" ");
        int rader = Integer.parseInt(str[0]);
        int kolonner = Integer.parseInt(str[1]);
        Rute[][] rutenett = new Rute[rader][kolonner];
        for(int r = 0; r < rader; r++) {
            String linje = fil.nextLine();
            for(int k = 0; k < kolonner; k++) {
                if(linje.charAt(k) == '#') {
                    SortRute sortRute = new SortRute(r,k);
                    rutenett[r][k] = sortRute;
                }
                else if(linje.charAt(k) == '.') {
                    if(r == 0 || r == rader-1 || k == 0 || k == kolonner-1) {
                        Aapning aapning = new Aapning(r,k);
                        rutenett[r][k] = aapning;
                    } else {
                        HvitRute hvitRute = new HvitRute(r,k);
                        rutenett[r][k] = hvitRute;
                    }

                }
            }
        }
        Labyrint l = new Labyrint(rutenett, rader, kolonner);

        for(int r = 0; r < rader; r++) {
            for(int k = 0; k < kolonner; k++) {
                if(r-1 >= 0) {
                    rutenett[r][k].settNord(rutenett[r-1][k]);
                }
                if(k+1 < kolonner) {
                    rutenett[r][k].settØst(rutenett[r][k+1]);
                }
                if(r+1 < rader) {
                    rutenett[r][k].settSør(rutenett[r+1][k]);
                }
                if(k-1 >= 0) {
                    rutenett[r][k].settVest(rutenett[r][k-1]);
                }
                rutenett[r][k].settLabyrint(l);
            }
        }

        return l;
    }

    protected Liste<String> finnUtveiFra(int rad, int kol) {
        if(utveier.stoerrelse() == 0) {
            this.rutenett[rad][kol].finnUtvei();
        } else {
            while(utveier.stoerrelse() != 0) {
                utveier.fjern();
            }
            this.rutenett[rad][kol].finnUtvei();
        }

        return utveier;
    }

    protected String finnKortesteUtvei(int rad, int kol, Liste<String> l) {
       // Liste<String> korteste = new Liste<String>();

        String kortesteUtvei = l.hent(0);
        for (String s : l) {
            if (s.length() < kortesteUtvei.length()) {
                kortesteUtvei = s;
            }
        }
        return kortesteUtvei;
    }

    public void skrivUt() {
        System.out.print("Rader: " + rader + " Kolonner: " + kolonner);
        System.out.println();
        for(int i = 0; i < rader; i++) {
            if(i<10) {
                System.out.print(i + "  ");
            } else {
                System.out.print(i+ " ");
            }
            for(int j = 0; j < kolonner; j++) {
                System.out.print(rutenett[i][j].tilTegn() + " ");
            }
            System.out.println();
        }
    }

    public Rute[][] hentRutenett() {
        return rutenett;
    }

    public void settRef(Labyrint l) {
        for(int i = 0; i < rader; i++) {
            for(int j = 0; j < kolonner; j++) {
                rutenett[i][j].settLabyrint(l);
            }
        }
    }


    @Override
    public String toString() {
        for(int i = 0; i < rader; i++) {
            for(int j = 0; j < kolonner; j++) {
                System.out.print(rutenett[i][j].tilTegn());
                System.out.print(rutenett[i][j]);
            }
            System.out.println();
        }
        return null;
    }
}
