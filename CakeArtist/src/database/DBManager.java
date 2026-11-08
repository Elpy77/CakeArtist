package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private static Connection conn = null;
    private static final String URL = "jdbc:h2:./cakeArtists";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private DBManager() {}

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conn;
    }

    public static boolean closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            conn = null;
            return true;
        }
        return false;
    }

    public static void initDB() throws SQLException {
        Connection c = getConnection();
        try (Statement stmt = c.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Consegna");
            stmt.execute("DROP TABLE IF EXISTS Ordine");
            stmt.execute("DROP TABLE IF EXISTS FarcituraTorta");
            stmt.execute("DROP TABLE IF EXISTS Torta");
            stmt.execute("DROP TABLE IF EXISTS ClienteRegistrato");
            stmt.execute("DROP TABLE IF EXISTS CakeArtist");
            stmt.execute("DROP TABLE IF EXISTS Rider");

            stmt.execute("CREATE TABLE Torta ("
                    + "ID_Torta INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Foto VARCHAR(255) NOT NULL, "
                    + "Descrizione VARCHAR(50) NOT NULL, "
                    + "PrezzoAlKg DECIMAL(6, 2) NOT NULL)");

            stmt.execute("CREATE TABLE FarcituraTorta ("
                    + "Farcitura VARCHAR(50) NOT NULL, "
                    + "TortaID_Torta INT NOT NULL, "
                    + "PRIMARY KEY (Farcitura, TortaID_Torta), "
                    + "FOREIGN KEY (TortaID_Torta) REFERENCES Torta(ID_Torta) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE ClienteRegistrato ("
                    + "Username VARCHAR(30) PRIMARY KEY, "
                    + "Password VARCHAR(30) NOT NULL, "
                    + "Cognome VARCHAR(30) NOT NULL, "
                    + "Nome VARCHAR(50) NOT NULL, "
                    + "NumeroDiTelefono VARCHAR(15) NOT NULL, "
                    + "Email VARCHAR(100) NOT NULL, "
                    + "Indirizzo VARCHAR(100) NOT NULL)");

            stmt.execute("CREATE TABLE CakeArtist ("
                    + "ID_CakeArtist INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Indirizzo VARCHAR(100) NOT NULL, "
                    + "Email VARCHAR(100) NOT NULL)");

            stmt.execute("CREATE TABLE Rider ("
                    + "ID_Rider INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Email VARCHAR(100) NOT NULL)");

            stmt.execute("CREATE TABLE Ordine ("
                    + "ID_Ordine INT AUTO_INCREMENT PRIMARY KEY, "
                    + "DataPrenotazione DATE NOT NULL, "
                    + "Peso INT NOT NULL, "
                    + "Farcitura VARCHAR(50) NOT NULL, "
                    + "Scritta VARCHAR(100) NULL, "
                    + "GiornoDiConsegna DATE NOT NULL, "
                    + "Stato VARCHAR(50) NOT NULL, "
                    + "PrezzoTotale DECIMAL(6, 2) NOT NULL, "
                    + "ClienteRegistratoUsername VARCHAR(30) NOT NULL, "
                    + "TortaID_Torta INT NOT NULL, "
                    + "CakeArtistID_CakeArtist INT NULL, "
                    + "FOREIGN KEY (ClienteRegistratoUsername) REFERENCES ClienteRegistrato(Username), "
                    + "FOREIGN KEY (TortaID_Torta) REFERENCES Torta(ID_Torta), "
                    + "FOREIGN KEY (CakeArtistID_CakeArtist) REFERENCES CakeArtist(ID_CakeArtist))");

            stmt.execute("CREATE TABLE Consegna ("
                    + "ID_Consegna INT AUTO_INCREMENT PRIMARY KEY, "
                    + "LuogoDiRitiro VARCHAR(100) NOT NULL, "
                    + "DataDiRitiro DATE NOT NULL, "
                    + "OraDiRitiro TIME NOT NULL, "
                    + "OraPrevistaConsegna TIME NULL, "
                    + "OrdineID_Ordine INT NOT NULL UNIQUE, "
                    + "RiderID_Rider INT NULL, "
                    + "FOREIGN KEY (OrdineID_Ordine) REFERENCES Ordine(ID_Ordine), "
                    + "FOREIGN KEY (RiderID_Rider) REFERENCES Rider(ID_Rider))");

            stmt.execute("INSERT INTO Torta (ID_Torta, Foto, Descrizione, PrezzoAlKg) VALUES "
                    + "(1, 'img/millefoglie.jpg', 'Millefoglie', 30.00), "
                    + "(2, 'img/caprese.jpg', 'Caprese', 25.00), "
                    + "(3, 'img/sacher.jpg', 'Sacher', 28.00)");

            stmt.execute("INSERT INTO FarcituraTorta (Farcitura, TortaID_Torta) VALUES "
                    + "('cioccolato', 1), "
                    + "('crema chantilly', 1), "
                    + "('crema pasticciera', 1), "
                    + "('cioccolato', 2), "
                    + "('cioccolato e amarena', 2), "
                    + "('cioccolato e panna', 3), "
                    + "('crema al pistacchio', 3)");

            stmt.execute("INSERT INTO ClienteRegistrato VALUES "
                    + "('Elpidio', 'pass123', 'Tessitore', 'Elpidio', '3331234567', 'etessitore@gmail.com', 'Corso sicilia 66, Succivo'), "
                    + "('lbianchi', 'pass456', 'Bianchi', 'Laura', '3409876543', 'lbianchi@email.it', 'Corso Umberto I 45, Napoli')");

            stmt.execute("INSERT INTO CakeArtist (ID_CakeArtist, Indirizzo, Email) VALUES "
                    + "(1, 'Via Chiaia 22, Napoli', 'artist1@cakeartists.it'), "
                    + "(2, 'Via Scarlatti 80, Napoli', 'artist2@cakeartists.it')");

            stmt.execute("INSERT INTO Rider (ID_Rider, Email) VALUES "
                    + "(1, 'rider1@cakeartists.it'), "
                    + "(2, 'rider2@cakeartists.it')");
        }
    }
}