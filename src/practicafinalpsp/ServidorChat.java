/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicafinalpsp;

/**
 *
 * @author Adri
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServidorChat extends JFrame implements ActionListener {

    private static final long serialversionUID = 1L;
    static ServerSocket servidor;
    public static final int PUERTO = 44444;
    public static int CONEXIONES = 0;
    public static int ACTUALES = 0;
    public static int MAXIMO = 10;
    static JTextField mensaje = new JTextField("");
    static JTextField mensaje2 = new JTextField("");
    private JScrollPane scrollpanel;
    static JTextArea areaTexto;
    JButton salir = new JButton("SALIR");
    static Socket[] tabla = new Socket[10];

    public ServidorChat() {
        super("VENTANA DEL SERVIDOR DEL CHAT");
        setLayout(null);
        mensaje.setBounds(10, 10, 400, 30);
        mensaje.setEditable(false);
        add(mensaje);
        mensaje2.setBounds(10, 348, 400, 30);
        mensaje2.setEditable(false);
        add(mensaje2);
        areaTexto = new JTextArea();
        scrollpanel = new JScrollPane(areaTexto);
        scrollpanel.setBounds(10, 50, 400, 300);
        add(scrollpanel);
        salir.addActionListener(this);
        salir.setBounds(420, 10, 100, 30);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(salir);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salir) {
            try {
                servidor.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("SERVIDOR INICIADO");
            ServidorChat pantalla = new ServidorChat();
            pantalla.setBounds(0, 0, 540, 400);
            pantalla.setVisible(true);
            mensaje.setText("NUMERO DE CONEXIONES ACTUALES " + CONEXIONES);
// SE ADMITEN HASTA 10 CONEXIONES
            while (CONEXIONES < MAXIMO) {
                Socket s = new Socket();
                try {
                    s = servidor.accept();
                } catch (SocketException ns) {
                    break;
                }
                tabla[CONEXIONES] = s;
                CONEXIONES++;
                ACTUALES++;
                HiloServidor hilo = new HiloServidor(s);
                hilo.start();
            }
            if (!servidor.isClosed()) {
                mensaje2.setForeground(Color.red);
                mensaje2.setText("MAXIMO NUMERO DE CONEXIONES ESTABLECIDAD "
                        + CONEXIONES);
                servidor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
