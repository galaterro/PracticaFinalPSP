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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClienteChat extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    Socket conector = null;
    DataInputStream fentrada;
    DataOutputStream fsalida;
    String nombre;
    static JTextField mensaje = new JTextField();
    private JScrollPane scrollpane;
    static JTextArea areatexto;
    JButton boton = new JButton("ENVIAR");
    JButton desconectar = new JButton("SALIR");
    Boolean repetir = true;

    public ClienteChat(Socket s, String nombre) {
        super("CONEXION DEL CLIENTE DEL CHAT: " + nombre);
        setLayout(null);
        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);
        areatexto = new JTextArea();
        scrollpane = new JScrollPane(areatexto);
        scrollpane.setBounds(10, 50, 400, 300);
        add(scrollpane);
        boton.setBounds(420, 10, 100, 30);
        boton.addActionListener(this);
        add(boton);
        desconectar.setBounds(420, 50, 100, 30);
        desconectar.addActionListener(this);
        add(desconectar);
        areatexto.setEditable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        conector = s;
        this.nombre = nombre;
        try {
            fentrada = new DataInputStream(conector.getInputStream());
            fsalida = new DataOutputStream(conector.getOutputStream());
            String texto = "SE ENTRA EN EL CHAT ----";
            fsalida.writeUTF(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boton) {
            String texto = nombre + " >> " + mensaje.getText();
            mensaje.setText(" ");
            try {
                fsalida.writeUTF(texto);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (e.getSource() == desconectar) {
            String texto = " <<< ABANDONA EL CHAT " + nombre;
            try {
                fsalida.writeUTF(texto);
                fsalida.writeUTF("*");
                repetir = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void ejecutar() {
        String texto = "";
        while (repetir) {
            try {
                texto = fentrada.readUTF();
                areatexto.setText(texto);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR\n"
                        + e.getMessage(), "<< mensaje de error >>", JOptionPane.ERROR_MESSAGE);
                repetir = false;
                e.printStackTrace();
            }
        }
        try {
            conector.close();
            System.exit(0);
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int puerto = 44444;
        String nombre = JOptionPane.showInputDialog("INTRODUZCA TU NOMBRE O NICK");
        Socket s = null;
        try {
            s = new Socket("localhost", puerto);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IMPOSIBLE CONECTAR CON EL SERVIDOR"
                    + e.getMessage(), "<< MENSAJE DE ERROR >>", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            e.printStackTrace();
        }
        if (!nombre.trim().equals("")) {
            ClienteChat cliente = new ClienteChat(s, nombre);
            cliente.setBounds(0, 0, 540, 400);
            cliente.setVisible(true);
            cliente.ejecutar();
        } else {
            System.out.println("EL NOMBRE ESTA VACIO");
        }
    }
}
