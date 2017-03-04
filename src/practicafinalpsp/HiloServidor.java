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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HiloServidor extends Thread {

    DataInputStream fentrada;
    Socket conector = null;

    public HiloServidor(Socket s) {
        conector = s;
        try {
            fentrada = new DataInputStream(conector.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        ServidorChat.mensaje.setText("NUMERO DE CONEXIONES ACTUALES"
                + ServidorChat.ACTUALES);
// AL CONECTARSE EL CLIENTE, SE LE ENVIAN TODOS LOS MENSAJES
        String texto = ServidorChat.areaTexto.getText();
        EnviaMensajes(texto);
        while (true) {
            String cadena = "";
            try {
                cadena = fentrada.readUTF();
                if (cadena.trim().equals("*")) {
                    ServidorChat.ACTUALES--;
                    ServidorChat.mensaje.setText("NUMERO DE CONEXIONES ACTUALES "
                            + ServidorChat.CONEXIONES);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServidorChat.areaTexto.append(cadena + "\n");
            texto = ServidorChat.areaTexto.getText();
            EnviaMensajes(texto);
        }
    }

    private void EnviaMensajes(String texto) {
        int i;
        int conexiones = ServidorChat.CONEXIONES;
        for (i = 0; i < conexiones; i++) {
            Socket sc = ServidorChat.tabla[i];
            DataOutputStream fsalida;
            try {
                fsalida = new DataOutputStream(sc.getOutputStream());
                fsalida.writeUTF(texto);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
