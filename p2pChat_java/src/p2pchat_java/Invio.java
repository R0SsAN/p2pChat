/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pchat_java;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.swing.JFrame;

/**
 *
 * @author Luca
 */
public class Invio {
    static Invio _instance=null;
    static synchronized public Invio getInstance() throws SocketException
    {
        if(_instance==null)
            _instance=new Invio();
        return _instance;
    }
    Gestione_Chat gestione;
    DatagramSocket invio;
    public Invio() throws SocketException
    {
        gestione=Gestione_Chat.getInstance(null);
        invio = new DatagramSocket();
    }
    public void inviaApertura(String nomeMittente, InetAddress ip) throws IOException
    {
        byte[] buffer=("a;"+nomeMittente+";").getBytes();
        DatagramPacket pacchetto=new DatagramPacket(buffer,buffer.length);
        pacchetto.setAddress(ip);
        pacchetto.setPort(12345);
        invio.send(pacchetto);
    }
    public void invioGenerico(String str, InetAddress ip) throws IOException
    {
        byte[] buffer=str.getBytes();
        DatagramPacket pacchetto=new DatagramPacket(buffer,buffer.length);
        pacchetto.setAddress(ip);
        pacchetto.setPort(12345);
        invio.send(pacchetto);
    }
    public void inviaRichiestaConnessione() throws IOException
    {
        invioGenerico("a;"+gestione.nickname_mittente+";", gestione.ip_destinatario);
        gestione.statoConnessione=1;
    }
    //-------------------
    //METODI INVIO MESSAGGI
    public void inviaMessaggioTesto(String messaggio) throws IOException
    {
        invioGenerico("m;"+messaggio+";", gestione.ip_destinatario);
    }
    
}
