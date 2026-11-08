package control;

import java.sql.Date;
import java.time.LocalDate;
import database.TortaDAO;
import entity.EntityOrdine;
import entity.EntityTorta;
import exception.DAOException;
import exception.DBConnectionException;
import exception.OperationException;

public class GestioneCakeArtistsController {
    private static GestioneCakeArtistsController instance = null;

    protected GestioneCakeArtistsController() {}

    public static GestioneCakeArtistsController getInstance() {
        if (instance == null) {
            instance = new GestioneCakeArtistsController();
        }
        return instance;
    }

    public float ordinaTorta(String torta, int peso, String farcitura, String scritta, Date giornoDiConsegna, String username) throws OperationException {
        EntityTorta eT = null;
        float prezzoTotale = 0;
        try {
            eT = TortaDAO.readTorta(torta);
            if (eT == null) {
                throw new OperationException("ERRORE: Torta o farcitura non valida");
            }
            if (!eT.getFarciture().contains(farcitura.toLowerCase())) {
                throw new OperationException("ERRORE: Torta o farcitura non valida");
            }
            if (!verificaPeso(peso)) {
                throw new OperationException("ERRORE: Peso non valido");
            }
            Date dataPrenotazione = Date.valueOf(LocalDate.now());
            if (!verificaGiornoDiConsegna(giornoDiConsegna, dataPrenotazione)) {
                throw new OperationException("ERRORE: Giorno di consegna non valido");
            }
            float prezzoAlKg = eT.getPrezzoAlKg();
            prezzoTotale = calcolaPrezzoTotale(prezzoAlKg, peso);
            EntityOrdine eO = new EntityOrdine(dataPrenotazione, username, eT.getIdTorta(), peso, farcitura, scritta, giornoDiConsegna, "in attesa", prezzoTotale);
            eO.saveOrdine();

        } catch (DBConnectionException dbEx) {
            throw new OperationException("Riscontrato problema interno di connessione al DB!");
        } catch (DAOException ex) {
            throw new OperationException("Errore durante l'accesso ai dati dell'ordine!");
        }
        return prezzoTotale;
    }

    private boolean verificaPeso(int peso) {
        return peso > 0;
    }

    private boolean verificaGiornoDiConsegna(Date giornoDiConsegna, Date dataPrenotazione) {
        LocalDate consegna = giornoDiConsegna.toLocalDate();
        LocalDate minima = dataPrenotazione.toLocalDate().plusDays(2);
        return !consegna.isBefore(minima);
    }

    private float calcolaPrezzoTotale(float prezzoAlKg, int peso) {
        return prezzoAlKg * peso;
    }
}