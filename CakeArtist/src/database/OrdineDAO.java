package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import entity.EntityOrdine;
import exception.DAOException;
import exception.DBConnectionException;

public class OrdineDAO {

    public static void createOrdine(EntityOrdine eO) throws DAOException, DBConnectionException {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione al database");
        }
        String query = "INSERT INTO Ordine (DataPrenotazione, Peso, Farcitura, Scritta, "+"GiornoDiConsegna, Stato, PrezzoTotale, ClienteRegistratoUsername, "+"TortaID_Torta, CakeArtistID_CakeArtist) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, eO.getDataPrenotazione());
            stmt.setInt(2, eO.getPeso());
            stmt.setString(3, eO.getFarcitura());
            stmt.setString(4, eO.getScritta());
            stmt.setDate(5, eO.getGiornoDiConsegna());
            stmt.setString(6, eO.getStato());
            stmt.setFloat(7, eO.getPrezzoTotale());
            stmt.setString(8, eO.getUsernameCliente());
            stmt.setInt(9, eO.getIdTorta());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore durante il salvataggio dell'ordine nel database");
        }
    }

    public static int countOrdini() throws DAOException, DBConnectionException {
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione al database");
        }
        String query = "SELECT COUNT(*) FROM Ordine";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DAOException("Errore nel conteggio degli ordini");
        }
    }
}