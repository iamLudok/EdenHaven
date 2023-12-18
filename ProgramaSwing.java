package proba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProgramaSwing {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Ventana de inicio de sesión
            JFrame loginFrame = new JFrame("Inicio de Sesión");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            loginFrame.setUndecorated(true);

            // Panel de inicio de sesión con GridBagLayout
            JPanel loginPanel = new JPanel(new GridBagLayout());

            // Configuración de GridBagConstraints para centrar y ajustar el tamaño
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);

            loginFrame.setLayout(new BorderLayout());
            loginFrame.add(loginPanel, BorderLayout.CENTER);

            // Campos de texto para nombre y contraseña
            JTextField nombreField = new JTextField(20);
            JPasswordField contrasenaField = new JPasswordField(20);

            loginPanel.add(new JLabel("Nombre:"), gbc);

            gbc.gridy++;
            loginPanel.add(nombreField, gbc);

            gbc.gridy++;
            loginPanel.add(new JLabel("Contraseña:"), gbc);

            gbc.gridy++;
            loginPanel.add(contrasenaField, gbc);

            JButton loginButton = new JButton("Iniciar Sesión");

            // Configuración de GridBagConstraints para centrar el botón
            gbc.gridy++;
            gbc.gridwidth = 2;
            loginPanel.add(loginButton, gbc);

            // Acción del botón de inicio de sesión
            loginButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                String contrasena = new String(contrasenaField.getPassword());

                if (autenticar(nombre, contrasena)) {
                    // Cerrar la ventana de inicio de sesión y abrir la pantalla correspondiente
                    loginFrame.dispose();

                    if ("admin".equals(nombre)) {
                        mostrarPantallaAdmin();
                    } else if ("user".equals(nombre)) {
                        mostrarPantallaHuecos();
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario no reconocido");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Autenticación fallida. Inténtalo de nuevo.");
                }
            });

            loginFrame.getContentPane().add(loginPanel);
            loginFrame.setVisible(true);
        });
    }

    // Método de autenticación simple (puedes implementar uno más seguro)
    private static boolean autenticar(String nombre, String contrasena) {
        // Aquí puedes agregar lógica de autenticación más segura
        // En este ejemplo, simplemente verificamos si ambos campos no están vacíos
        return nombre != null && !nombre.isEmpty() && contrasena != null && !contrasena.isEmpty();
    }

    private static void mostrarPantallaAdmin() {
        JFrame adminFrame = new JFrame("Pantalla de Admin");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        adminFrame.setUndecorated(true);

        // Agregar aquí la lógica y componentes para la pantalla de admin con mapa y albergues

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(adminFrame.getWidth(), adminFrame.getHeight()));

        try {      
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            ImageIcon worldMapIcon2 = new ImageIcon("ikonoak/mapa.jpg");
            JLabel worldMapLabel2 = new JLabel(worldMapIcon2);
            worldMapLabel2.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
            layeredPane.add(worldMapLabel2, JLayeredPane.DEFAULT_LAYER);
            
            // Escalar el tamaño de las imágenes de los albergues
            int hostelWidth = 50;
            int hostelHeight = 50;

            // Agregar imágenes de albergues en posiciones exactas con coordenadas
            ImageIcon africaIcon = new ImageIcon("ikonoak/babesetxe.png");
            africaIcon = new ImageIcon(africaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel africaLabel = new JLabel(africaIcon);
            africaLabel.setBounds(300, 200, hostelWidth, hostelHeight);
            layeredPane.add(africaLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon swedenIcon = new ImageIcon("ikonoak/babesetxe.png");
            swedenIcon = new ImageIcon(swedenIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel swedenLabel = new JLabel(swedenIcon);
            swedenLabel.setBounds(500, 750, hostelWidth, hostelHeight);
            layeredPane.add(swedenLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon chinaIcon = new ImageIcon("ikonoak/babesetxe.png");
            chinaIcon = new ImageIcon(chinaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel chinaLabel = new JLabel(chinaIcon);
            chinaLabel.setBounds(1650, 750, hostelWidth, hostelHeight);
            layeredPane.add(chinaLabel, JLayeredPane.PALETTE_LAYER);

            ImageIcon oceaniaIcon = new ImageIcon("ikonoak/babesetxe.png");
            oceaniaIcon = new ImageIcon(oceaniaIcon.getImage().getScaledInstance(hostelWidth, hostelHeight, Image.SCALE_SMOOTH));
            JLabel oceaniaLabel = new JLabel(oceaniaIcon);
            oceaniaLabel.setBounds(900, 400, hostelWidth, hostelHeight);
            layeredPane.add(oceaniaLabel, JLayeredPane.PALETTE_LAYER);

            // Configuración del administrador del mouse para manejar clics en las imágenes
            layeredPane.addMouseListener(new AdminClickListener(adminFrame, africaLabel, swedenLabel, chinaLabel, oceaniaLabel));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la imagen del mapa o albergues: " + ex.getMessage());
        }

        adminFrame.getContentPane().add(layeredPane);
        adminFrame.pack();
        adminFrame.setVisible(true);
    }


    private static void mostrarPantallaHuecos() {
        JFrame frame = new JFrame("Programa Swing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        for (int i = 0; i < 3; i++) {
            JButton button = new JButton("Hueco " + (i + 1));
            button.addActionListener(new HuecoClickListener(frame, button));
            panel.add(button);
        }

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    static class AdminClickListener extends MouseAdapter {

        private JFrame adminFrame;
        private JLabel africaLabel;
        private JLabel swedenLabel;
        private JLabel chinaLabel;
        private JLabel oceaniaLabel;

        public AdminClickListener(JFrame adminFrame, JLabel africaLabel, JLabel swedenLabel, JLabel chinaLabel, JLabel oceaniaLabel) {
            this.adminFrame = adminFrame;
            this.africaLabel = africaLabel;
            this.swedenLabel = swedenLabel;
            this.chinaLabel = chinaLabel;
            this.oceaniaLabel = oceaniaLabel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Verificar si el clic ocurrió dentro de las coordenadas de alguno de los albergues
            if (africaLabel.getBounds().contains(e.getPoint()) ||
                    swedenLabel.getBounds().contains(e.getPoint()) ||
                    chinaLabel.getBounds().contains(e.getPoint()) ||
                    oceaniaLabel.getBounds().contains(e.getPoint())) {
                // Si el clic ocurrió en uno de los albergues, abrir la pantalla de huecos
                adminFrame.dispose();
                mostrarPantallaHuecos();
            }
        }
    }

    static class HuecoClickListener implements ActionListener {

        private JFrame mainFrame;
        private JButton button;

        public HuecoClickListener(JFrame mainFrame, JButton button) {
            this.mainFrame = mainFrame;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame opcionesFrame = new JFrame("Opciones");
            opcionesFrame.setSize(300, 150);
            opcionesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel opcionesPanel = new JPanel();
            opcionesPanel.setLayout(new GridLayout(4, 1));

            JButton pozoButton = new JButton("POZO");
            pozoButton.addActionListener(new OpcionClickListener("POZO", opcionesFrame, button));
            opcionesPanel.add(pozoButton);

            JButton ventiladorButton = new JButton("VENTILADOR");
            ventiladorButton.addActionListener(new OpcionClickListener("VENTILADOR", opcionesFrame, button));
            opcionesPanel.add(ventiladorButton);

            JButton fugaButton = new JButton("ESTUFA");
            fugaButton.addActionListener(new OpcionClickListener("ESTUFA", opcionesFrame, button));
            opcionesPanel.add(fugaButton);

            opcionesFrame.getContentPane().add(opcionesPanel);

            // Centra la ventana de opciones en el centro de la pantalla principal
            opcionesFrame.setLocationRelativeTo(mainFrame);
            opcionesFrame.setVisible(true);
        }
    }

    static class OpcionClickListener implements ActionListener {

        private String opcion;
        private JFrame opcionesFrame;
        private JButton originalButton;

        public OpcionClickListener(String opcion, JFrame opcionesFrame, JButton originalButton) {
            this.opcion = opcion;
            this.opcionesFrame = opcionesFrame;
            this.originalButton = originalButton;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if ("ESTUFA".equals(opcion)) {
                // Cambia el contenido del JLabel para mostrar la imagen de la estufa
                ImageIcon estufaIcon = new ImageIcon("ikonoak/estufa.png");
                originalButton.setIcon(estufaIcon);
                originalButton.setText("<html>ESTUFA<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
                originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
                originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            }
            if ("VENTILADOR".equals(opcion)) {
                // Cambia el contenido del JLabel para mostrar la imagen del ventilador
                ImageIcon ventiladorIcon = new ImageIcon("ikonoak/ventilador.png");
                originalButton.setIcon(ventiladorIcon);
                originalButton.setText("<html>VENTILADOR<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
                originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
                originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            }
            if ("POZO".equals(opcion)) {
                // Cambia el contenido del JLabel para mostrar la imagen del pozo
                ImageIcon pozoIcon = new ImageIcon("ikonoak/pozo.png");
                originalButton.setIcon(pozoIcon);
                originalButton.setText("<html>POZO<br><br>Grados: 25<br>Nivel: Medio<br>Alerta: Baja</html>");
                originalButton.setHorizontalTextPosition(SwingConstants.CENTER);
                originalButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            }

            JOptionPane.showMessageDialog(null, "Seleccionaste: " + opcion);
            opcionesFrame.dispose();  // Cierra la ventana de opciones
        }
    }
}
