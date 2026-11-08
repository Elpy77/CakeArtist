package boundary;

import control.GestioneCakeArtistsController;
import database.DBManager;
import exception.OperationException;

import javax.swing.JOptionPane;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BoundaryClienteRegistrato extends BoundaryCliente {

    public static void main(String[] args) {
        try {
            DBManager.initDB();
            String username = "Elpidio";
            GestioneCakeArtistsController ctrl = GestioneCakeArtistsController.getInstance();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            while (true) {
                String torta = null;
                while (torta == null) {
                    String s = JOptionPane.showInputDialog("Seleziona la torta dal catalogo (es. Millefoglie):");
                    if (s == null) return;
                    if (!s.trim().isEmpty() && s.trim().length() <= 50) {
                        torta = s.trim();
                    } else {
                        JOptionPane.showMessageDialog(null, "ERRORE: Torta non valida", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Integer peso = null;
                while (peso == null) {
                    String s = JOptionPane.showInputDialog("Inserisci il peso (multipli interi del kg):");
                    if (s == null) return;
                    try {
                        int p = Integer.parseInt(s.trim());
                        if (p > 0) peso = p;
                        else JOptionPane.showMessageDialog(null, "ERRORE: Peso non valido", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ERRORE: Peso non valido", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                String farcitura = null;
                while (farcitura == null) {
                    String s = JOptionPane.showInputDialog("Inserisci la farcitura:");
                    if (s == null) return;
                    if (!s.trim().isEmpty() && s.trim().length() <= 50) {
                        farcitura = s.trim();
                    } else {
                        JOptionPane.showMessageDialog(null, "ERRORE: Farcitura non valida", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                String scritta = null;
                while (scritta == null) {
                    String s = JOptionPane.showInputDialog("Scritta sulla torta (opzionale; vuoto = nessuna):");
                    if (s == null) {
                        scritta = "";
                    } else if (s.length() <= 100) {
                        scritta = s;
                    } else {
                        JOptionPane.showMessageDialog(null, "ERRORE: Scritta troppo lunga", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                Date giorno = null;
                while (giorno == null) {
                    String s = JOptionPane.showInputDialog("Giorno di consegna (gg/mm/aaaa):");
                    if (s == null) return;
                    try {
                        giorno = Date.valueOf(LocalDate.parse(s.trim(), dateTimeFormatter));
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(null, "ERRORE: Giorno di consegna non valido", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
                try {
                    float prezzo = ctrl.ordinaTorta(torta, peso, farcitura, scritta, giorno, username);
                    JOptionPane.showMessageDialog(null, "Ordine creato con successo!\nPrezzo totale: " + prezzo + " €\nConsegna entro le ore 18:00 del giorno indicato", "Conferma Ordine", JOptionPane.INFORMATION_MESSAGE);
                    break;
                } catch (OperationException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Errore Ordine", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore interno del sistema.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}