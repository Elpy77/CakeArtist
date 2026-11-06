package entity;

import java.util.List;

public class EntityTorta {
    private int idTorta;
    private String foto;
    private String descrizione;
    private List<String> farciture;
    private float prezzoAlKg;

    public EntityTorta(int idTorta, String foto, String descrizione, List<String> farciture, float prezzoAlKg) {
        this.idTorta = idTorta;
        this.foto = foto;
        this.descrizione = descrizione;
        this.farciture = farciture;
        this.prezzoAlKg = prezzoAlKg;
    }

    public int getIdTorta() { return idTorta; }
    public void setIdTorta(int idTorta) { this.idTorta = idTorta; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public List<String> getFarciture() { return farciture; }
    public void setFarciture(List<String> farciture) { this.farciture = farciture; }
    public float getPrezzoAlKg() { return prezzoAlKg; }
    public void setPrezzoAlKg(float prezzoAlKg) { this.prezzoAlKg = prezzoAlKg; }
}