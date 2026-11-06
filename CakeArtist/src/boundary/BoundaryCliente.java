package boundary;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BoundaryCliente extends JFrame {
    private static final long serialVersionUID = 1L;

    public void registrazione() {
        // Non richiesta dalla traccia
    }

    public void visualizzaCatalogoTorte() {
        String catalogo = "CATALOGO TORTE DISPONIBILI:\n\n"
                + "1. Millefoglie (30,00 €/kg)\n"
                + "   Farciture: cioccolato, crema chantilly, crema pasticciera\n\n"
                + "2. Caprese (25,00 €/kg)\n"
                + "   Farciture: cioccolato, cioccolato e amarena\n\n"
                + "3. Sacher (28,00 €/kg)\n"
                + "   Farciture: cioccolato e panna, crema al pistacchio";
        JOptionPane.showMessageDialog(this, catalogo, "Catalogo Torte - CakeArtists", JOptionPane.INFORMATION_MESSAGE);
    }
}