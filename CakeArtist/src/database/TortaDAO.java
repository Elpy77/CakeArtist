package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entity.EntityTorta;
import exception.DAOException;
import exception.DBConnectionException;

public class TortaDAO {

    public static EntityTorta readTorta(String descrizione) throws DAOException, DBConnectionException {
        EntityTorta eT = null;
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione al database");
        }

        String query = "SELECT * FROM Torta WHERE LOWER(Descrizione) = LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, descrizione);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idTorta = rs.getInt("ID_Torta");
                    String foto = rs.getString("Foto");
                    String desc = rs.getString("Descrizione");
                    float prezzoAlKg = rs.getFloat("PrezzoAlKg");
                    List<String> farciture = readFarcitureTorta(idTorta);
                    eT = new EntityTorta(idTorta, foto, desc, farciture, prezzoAlKg);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la lettura della torta");
        }
        return eT;
    }

    public static List<String> readFarcitureTorta(int idTorta) throws DAOException, DBConnectionException {
        List<String> farciture = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBManager.getConnection();
        } catch (SQLException e) {
            throw new DBConnectionException("Errore di connessione al database");
        }

        String query = "SELECT Farcitura FROM FarcituraTorta WHERE TortaID_Torta = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idTorta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    farciture.add(rs.getString("Farcitura").toLowerCase());
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la lettura delle farciture");
        }
        return farciture;
    }
}
