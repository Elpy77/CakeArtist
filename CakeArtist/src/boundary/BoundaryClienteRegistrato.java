package boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import control.GestioneCakeArtistsController;
import database.DBManager;
import database.OrdineDAO;
import exception.OperationException;

public class BoundaryClienteRegistrato extends BoundaryCliente {
    private static final long serialVersionUID = 1L;

    private JTextField txtTorta;
    private JTextField txtPeso;
    private JTextField txtFarcitura;
    private JTextField txtScritta;
    private JTextField txtGiornoConsegna;
    private JLabel lblEsito;
    private JLabel lblStatoDB;
    private String usernameLoggato = "Elpidio Tessitore";

    public BoundaryClienteRegistrato() {
        setTitle("CakeArtists - Area Cliente Registrato (" + usernameLoggato + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 620, 430);
        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        JLabel lblTitolo = new JLabel("ORDINA TORTA PERSONALIZZATA");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(lblTitolo, BorderLayout.WEST);

        JButton btnCatalogo = new JButton("Visualizza Catalogo Torte");
        btnCatalogo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                visualizzaCatalogoTorte();
            }
        });
        topPanel.add(btnCatalogo, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(new JLabel("1. Torta desiderata (es. Millefoglie):"));
        txtTorta = new JTextField();
        formPanel.add(txtTorta);

        formPanel.add(new JLabel("2. Peso in kg (multiplo intero, es. 2):"));
        txtPeso = new JTextField();
        formPanel.add(txtPeso);

        formPanel.add(new JLabel("3. Farcitura scelta (es. cioccolato):"));
        txtFarcitura = new JTextField();
        formPanel.add(txtFarcitura);

        formPanel.add(new JLabel("4. Eventuale scritta (opzionale, max 100 car.):"));
        txtScritta = new JTextField();
        formPanel.add(txtScritta);

        formPanel.add(new JLabel("5. Giorno di consegna (gg/mm/aaaa):"));
        txtGiornoConsegna = new JTextField();
        formPanel.add(txtGiornoConsegna);

        getContentPane().add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        JButton btnOrdina = new JButton("Conferma e Invia Ordine");
        btnOrdina.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(btnOrdina);

        lblEsito = new JLabel("Compila i campi e clicca su 'Conferma e Invia Ordine'", SwingConstants.CENTER);
        lblEsito.setFont(new Font("SansSerif", Font.BOLD, 12));
        bottomPanel.add(lblEsito);

        lblStatoDB = new JLabel("Ordini registrati nel DB: 0", SwingConstants.CENTER);
        lblStatoDB.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bottomPanel.add(lblStatoDB);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        btnOrdina.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inviaOrdineDaGUI();
            }
        });
    }

    private void inviaOrdineDaGUI() {
        String torta = txtTorta.getText();
        String pesoStr = txtPeso.getText();
        String farcitura = txtFarcitura.getText();
        String scritta = txtScritta.getText();
        String giornoStr = txtGiornoConsegna.getText();

        if (torta == null || torta.trim().isEmpty() || torta.length() > 50) {
            mostraMessaggio("ERRORE: Torta non valida", true);
            return;
        }

        int peso;
        try {
            peso = Integer.parseInt(pesoStr.trim());
        } catch (NumberFormatException ex) {
            mostraMessaggio("ERRORE: Peso non valido", true);
            return;
        }

        if (farcitura == null || farcitura.trim().isEmpty() || farcitura.length() > 50) {
            mostraMessaggio("ERRORE: Farcitura non valida", true);
            return;
        }

        if (scritta != null && scritta.length() > 100) {
            mostraMessaggio("ERRORE: Scritta troppo lunga", true);
            return;
        }

        Date giornoDiConsegna;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate parsedDate = LocalDate.parse(giornoStr.trim(), formatter);
            giornoDiConsegna = Date.valueOf(parsedDate);
        } catch (DateTimeParseException ex) {
            mostraMessaggio("ERRORE: Giorno di consegna non valido", true);
            return;
        }

        ordinaTorta(torta.trim(), peso, farcitura.trim(),
                    scritta == null ? "" : scritta.trim(), giornoDiConsegna, usernameLoggato);
    }

    public float ordinaTorta(String torta, int peso, String farcitura,
                             String scritta, Date giornoDiConsegna, String username) {
        try {
            float prezzoTotale = GestioneCakeArtistsController.getInstance()
                    .ordinaTorta(torta, peso, farcitura, scritta, giornoDiConsegna, username);
            String msg = String.format(Locale.ITALY,
                    "Conferma ordine (\"Consegna entro le ore 18:00 del giorno indicato\") con prezzo totale %.2f €",
                    prezzoTotale);
            mostraMessaggio(msg, false);
            return prezzoTotale;
        } catch (OperationException ex) {
            mostraMessaggio(ex.getMessage(), true);
            return -1;
        }
    }

    private void mostraMessaggio(String testo, boolean isErrore) {
        lblEsito.setText(testo);
        lblEsito.setForeground(isErrore ? Color.RED : new Color(0, 128, 0));
        try {
            lblStatoDB.setText("Ordini registrati nel DB: " + OrdineDAO.countOrdini());
        } catch (Exception e) {
            lblStatoDB.setText("Errore lettura conteggio DB");
        }
    }

    public static void main(String[] args) {
        try {
            DBManager.initDB();
        } catch (Exception e) {
            System.err.println("Errore inizializzazione DB: " + e.getMessage());
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BoundaryClienteRegistrato frame = new BoundaryClienteRegistrato();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}