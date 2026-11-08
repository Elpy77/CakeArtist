package entity;

import java.sql.Date;
import database.OrdineDAO;
import exception.DAOException;
import exception.DBConnectionException;

public class EntityOrdine {
    private int idOrdine;
    private Date dataPrenotazione;
    private String usernameCliente;
    private int idTorta;
    private int peso;
    private String farcitura;
    private String scritta;
    private Date giornoDiConsegna;
    private String stato;
    private float prezzoTotale;

    public EntityOrdine(Date dataPrenotazione, String usernameCliente, int idTorta, int peso, String farcitura, String scritta, Date giornoDiConsegna, String stato, float prezzoTotale) {
        this.dataPrenotazione = dataPrenotazione;
        this.usernameCliente = usernameCliente;
        this.idTorta = idTorta;
        this.peso = peso;
        this.farcitura = farcitura;
        this.scritta = scritta;
        this.giornoDiConsegna = giornoDiConsegna;
        this.stato = stato;
        this.prezzoTotale = prezzoTotale;
    }

    public void saveOrdine() throws DAOException, DBConnectionException {
        OrdineDAO.createOrdine(this);
    }

    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }
    public Date getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(Date dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
    public String getUsernameCliente() { return usernameCliente; }
    public void setUsernameCliente(String usernameCliente) { this.usernameCliente = usernameCliente; }
    public int getIdTorta() { return idTorta; }
    public void setIdTorta(int idTorta) { this.idTorta = idTorta; }
    public int getPeso() { return peso; }
    public void setPeso(int peso) { this.peso = peso; }
    public String getFarcitura() { return farcitura; }
    public void setFarcitura(String farcitura) { this.farcitura = farcitura; }
    public String getScritta() { return scritta; }
    public void setScritta(String scritta) { this.scritta = scritta; }
    public Date getGiornoDiConsegna() { return giornoDiConsegna; }
    public void setGiornoDiConsegna(Date giornoDiConsegna) { this.giornoDiConsegna = giornoDiConsegna; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public float getPrezzoTotale() { return prezzoTotale; }
    public void setPrezzoTotale(float prezzoTotale) { this.prezzoTotale = prezzoTotale; }
}