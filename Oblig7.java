import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;


public class Oblig7 extends Application {

    private SortRute r;
    private HvitRute h;
    private Labyrint l;
    private GridPane rootPane;
    private VBox meny;
    private HBox vindu;
    private Button b;
    private ArrayList<Text> utveiListe = new ArrayList<Text>();

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) {

        FileChooser fileChooser = new FileChooser();
        File valgtFil = fileChooser.showOpenDialog(stage);
        if (valgtFil == null) {
            System.out.println("Ingen fil valgt");
            System.exit(1);
        }
        try {
            l = Labyrint.lesFraFil(valgtFil);
        } catch (FileNotFoundException e) {
            System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", valgtFil);
            System.exit(1);
        }

        rootPane = new GridPane();
        meny = new VBox();
        vindu = new HBox();

        b = new Button("Avslutt");
        b.setOnMouseClicked(e-> System.exit(1));
        meny.getChildren().add(b);

        vindu.getChildren().addAll(rootPane, meny);
        vindu.setSpacing(10);

        for (int i = 0; i < l.rader; i++) {
            for(int j = 0; j < l.kolonner; j++) {
                int rad = i;
                int kol = j;

                if(l.rutenett[rad][kol] instanceof SortRute) {
                    r = new SortRute(rad, kol);
                    r.setFill(Color.BLACK);
                    r.setStroke(Color.GREY);
                    r.setHeight(25);
                    r.setWidth(25);
                    if(l.rader>50){
                        r.setHeight(12);
                        r.setWidth(12);
                    }
                    rootPane.add(r, kol, rad);
                }

                if(l.rutenett[rad][kol] instanceof HvitRute) {
                    h = new HvitRute(rad, kol);
                    h.setFill(Color.WHITE);
                    h.setStroke(Color.GREY);
                    h.setHeight(25);
                    h.setWidth(25);
                    if(l.rader>50){
                        h.setHeight(12);
                        h.setWidth(12);
                    }
                    h.setOnMouseClicked(e -> testMetode(rad, kol, rootPane));
                    rootPane.add(h, kol, rad);
                }
            }
        }

        Scene scene = new Scene(vindu, 1000, 1000);
        stage.setTitle("Labyrint");
        stage.setScene(scene);
        stage.show();
    }

    private void testMetode(int rad, int kol, GridPane pane) {
        Liste<String> utveier;
        for(Node rute : pane.getChildren()) {
            Rute ruteobj = (Rute) rute;
            if(ruteobj.getFill().equals(Color.RED)) {
                ruteobj.setFill(Color.WHITE);
            }
        }
        boolean[][] losning;
        if(!utveiListe.isEmpty()) {
            meny.getChildren().removeAll(utveiListe);
            utveiListe.clear();
        }
        utveier = l.finnUtveiFra(rad, kol);

        Text antallUtveier = new Text("Antall utveier: " + utveier.stoerrelse());
        utveiListe.add(antallUtveier);
        meny.getChildren().add(antallUtveier);

        if(utveier.stoerrelse() == 0) {
            System.out.println("Ingen utveier!");
            return;
        }

        System.out.println("Finner utvei fra (" + rad + ", " + kol + ")");
        String kortesteutvei = l.finnKortesteUtvei(rad, kol, utveier);
        System.out.println(kortesteutvei);
        losning = stringTilTabell(kortesteutvei, l.kolonner, l.rader);
        skrivLosning(losning, rootPane);
    }

    private static boolean[][] stringTilTabell(String losningString, int hoyde, int bredde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
        while (m.find()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            losning[y][x] = true;
        }
        return losning;
    }

    private void skrivLosning(boolean[][] tabell, GridPane pane) {
        for (int i = 0; i < l.rader; i++) {
            for (int j = 0; j < l.kolonner; j++) {
                int rad = i;
                int kol = j;
                if(tabell[kol][rad]) {
                    h = new HvitRute(rad, kol);
                    h.setFill(Color.RED);
                    h.setHeight(25);
                    h.setWidth(25);
                    if(l.rader>50){
                        h.setHeight(12);
                        h.setWidth(12);
                    }
                    h.setOnMouseClicked(e -> testMetode(rad, kol, pane));
                    pane.add(h, kol, rad);
                }
            }
        }

    }

}
