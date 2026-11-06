package boundary;

import control.GestioneCakeArtistsController;
import database.DBManager;
import exception.OperationException;

import javax.swing.JOptionPane;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class BoundaryClienteRegistrato {

    // gg/mm/aaaa con risoluzione STRICT: rifiuta anche 31/02/2026, non solo 32/13/2026
    private static final DateTimeFormatter FORMATO_GIORNO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static void main(String[] args) {
        try {
            DBManager.initDB();                                   // popolamento (DB_init.txt)
            String username = "mrossi";                           // pre-condizione V02: login effettuato
            GestioneCakeArtistsController ctrl = GestioneCakeArtistsController.getInstance();

            while (true) {                                        // retry sui fallimenti del controller
                String torta = leggiTorta();                      // test 3
                if (torta == null) { annulla(); return; }
                Integer peso = leggiPeso();                       // test 5
                if (peso == null) { annulla(); return; }
                String farcitura = leggiFarcitura();              // test 6
                if (farcitura == null) { annulla(); return; }
                String scritta = leggiScritta();                  // test 7 (null = assente, valida)
                Date giorno = leggiGiorno();                      // test 8
                if (giorno == null) { annulla(); return; }

                try {
                    float prezzo = ctrl.ordinaTorta(torta, peso, farcitura, scritta, giorno, username);
                    JOptionPane.showMessageDialog(null,
                            "Ordine creato con successo!\nPrezzo totale: " + prezzo
                                    + " €\nConsegna entro le ore 18:00 del giorno indicato",
                            "Conferma Ordine", JOptionPane.INFORMATION_MESSAGE);
                    break;                                        // test 1 e 2: flusso felice
                } catch (OperationException e) {
                    // test 4, 9, 10, 11: errori di business del controller
                    JOptionPane.showMessageDialog(null, e.getMessage(),
                            "Errore Ordine", JOptionPane.ERROR_MESSAGE);
                    // il ciclo riparte: il sistema richiede nuovamente i dati dell'ordine
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore interno del sistema.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void annulla() {
        JOptionPane.showMessageDialog(null, "Operazione annullata dall'utente.",
                "Ordine annullato", JOptionPane.INFORMATION_MESSAGE);
    }

    // TEST 3: selezione vuota o > 50 caratteri -> errore di boundary
    private static String leggiTorta() {
        while (true) {
            String s = JOptionPane.showInputDialog("Seleziona la torta dal catalogo (es. Millefoglie):");
            if (s == null) return null;
            s = s.trim();
            if (!s.isEmpty() && s.length() <= 50) return s;
            errore("ERRORE: Torta non valida");
        }
    }

    // TEST 5: parsing intero qui (2.5 -> NumberFormatException); il <= 0 lo valuta il controller (test 4)
    private static Integer leggiPeso() {
        while (true) {
            String s = JOptionPane.showInputDialog("Inserisci il peso (multipli interi del kg):");
            if (s == null) return null;
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                errore("ERRORE: Peso non valido");
            }
        }
    }

    // TEST 6: farcitura vuota o > 50 caratteri -> errore di boundary
    private static String leggiFarcitura() {
        while (true) {
            String s = JOptionPane.showInputDialog("Inserisci la farcitura:");
            if (s == null) return null;
            s = s.trim();
            if (!s.isEmpty() && s.length() <= 50) return s;
            errore("ERRORE: Farcitura non valida");
        }
    }

    // TEST 7: scritta > 100 -> errore; vuota/annullata = scelta valida (campo opzionale)
    private static String leggiScritta() {
        while (true) {
            String s = JOptionPane.showInputDialog("Scritta sulla torta (opzionale; vuoto = nessuna):");
            if (s == null) return "";
            if (s.length() <= 100) return s;
            errore("ERRORE: Scritta troppo lunga");
        }
    }

    // TEST 8: formato gg/mm/aaaa o data inesistente -> errore di boundary
    private static Date leggiGiorno() {
        while (true) {
            String s = JOptionPane.showInputDialog("Giorno di consegna (gg/mm/aaaa):");
            if (s == null) return null;
            try {
                return Date.valueOf(LocalDate.parse(s.trim(), FORMATO_GIORNO));
            } catch (DateTimeParseException e) {
                errore("ERRORE: Giorno di consegna non valido");
            }
        }
    }

    private static void errore(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }
}