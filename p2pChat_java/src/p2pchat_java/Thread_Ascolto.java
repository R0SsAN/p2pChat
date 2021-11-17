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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Luca
 */
public class Thread_Ascolto extends Thread{
    Gestione_Chat gestione;
    Invio invio;
    DatagramSocket ascolto;
    public Thread_Ascolto() throws SocketException
    {
        gestione=Gestione_Chat.getInstance(null);
        invio=Invio.getInstance();
        ascolto = new DatagramSocket(12345);
    }

    @Override
    public void run() {
        DatagramPacket pacchetto=new DatagramPacket(null, 0);
        while(true)
        {
            String[] ricezione=getStringRicezione(pacchetto);
            
            //se ricevo richiesta connessione
            if(ricezione[0].equals("a"))
            {
                try {
                    gestisciRichiesta1(ricezione, pacchetto.getAddress(), pacchetto.getPort());
                } catch (IOException ex) {
                    Logger.getLogger(Thread_Ascolto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(ricezione[0].equals("b"))
            {
                
            }
                
        } 
    }
    
    public String[] getStringRicezione(DatagramPacket pacchetto)
    {
        byte[] buffer=new byte[1500];
        pacchetto= new DatagramPacket(buffer, buffer.length);
        //ascolto quello che ricevo
        buffer=new byte[1500];
        pacchetto=new DatagramPacket(buffer, buffer.length);
        try {
            ascolto.receive(pacchetto);
        } catch (IOException ex) {
            Logger.getLogger(Thread_Ascolto.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] ricezione=(new String(buffer).trim()).split(";");
        return ricezione;
    }
    
    public void gestisciRichiesta1(String[] ricezione, InetAddress ip, int port) throws IOException
    {
        if(gestione.statoConnessione == 0)
        {
            String temp="Nuova richiesta da: "+ricezione[1]+"\n {IP:"+ip.toString()+" PORT:"+Integer.toString(port)+"}";
            Object[] options = {"Accetta","Rifiuta"};
            int n = JOptionPane.showOptionDialog(gestione.frame,temp,null, JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                    null,options,options[1]);
            //se accetto la richiesta di connessione gli invio la conferma più il mio nome
            if(n==0)
            {
                gestione.aggiornaNickDestinatario(ricezione[1]);
                invio.invioGenerico("y;"+gestione.nickname_mittente+";", ip);
            }
            
        }
        else
            invio.invioGenerico("n;", ip);
    }
        
    
}
