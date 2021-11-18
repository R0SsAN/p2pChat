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
        DatagramPacket pacchetto;
        while(true)
        {
            byte[] buffer=new byte[1500];
            pacchetto= new DatagramPacket(buffer, buffer.length);
            String[] ricezione=getStringRicezione(pacchetto);
            
            //se ricevo richiesta connessione
            if(ricezione[0].equals("a"))
            {
                try {
                    gestisciRichiesta1(ricezione, pacchetto.getAddress());
                } catch (IOException ex) {
                    Logger.getLogger(Thread_Ascolto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(ricezione[0].equals("y") || ricezione[0].equals("n"))
            {
                try {
                    gestisciRichiesta2(ricezione, pacchetto.getAddress());
                } catch (IOException ex) {
                    Logger.getLogger(Thread_Ascolto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        } 
    }
    
    public String[] getStringRicezione(DatagramPacket pacchetto)
    {
        
        //ascolto quello che ricevo
        try {
            ascolto.receive(pacchetto);
        } catch (IOException ex) {
            Logger.getLogger(Thread_Ascolto.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] ricezione=(new String(pacchetto.getData()).trim()).split(";");
        return ricezione;
    }
    
    public void gestisciRichiesta1(String[] ricezione, InetAddress ip) throws IOException
    {
        //se ricevo dal mittente una richiesta di connessione e non ne ho un altra in corso
        if(gestione.statoConnessione == 0)
        {
            String temp="Nuova richiesta da: "+ricezione[1]+"\n {IP:"+ip.toString()+"}";
            int result = JOptionPane.showConfirmDialog(gestione.frame,temp, "Richiste connessione",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            //se accetto la richiesta di connessione gli invio la conferma più il mio nome
            if(result == JOptionPane.YES_OPTION)
            {
                gestione.statoConnessione=2;
                gestione.ip_destinatario=ip;
                gestione.aggiornaNickDestinatario(ricezione[1]);
                invio.invioGenerico("y;"+gestione.nickname_mittente+";", ip);
            }
            //se non la accetto gli invio il deny
            else if(result == JOptionPane.NO_OPTION)
            {
                invio.invioGenerico("n;", ip);
            }
        }
        //se ho già una connessione in corso / sono in attesa di qualcos'altro la rifiuto
        else
            invio.invioGenerico("n;", ip);
    }
    
    public void gestisciRichiesta2(String[] ricezione, InetAddress ip) throws IOException
    {
        //se sono in attesa della conferma del destinatario
        if(gestione.statoConnessione==1)
        {
            if(ip.equals(gestione.ip_destinatario))
            {
                //se il destinatario mi ha accettato la connessione
                if(ricezione[0].equals("y") && ricezione.length==2)
                {
                    String temp="Richiesta di connessione accettata da "+ ricezione[1];
                    Object[] options = {"Accetta e avvia chat","Rifiuta"};
                    int n = JOptionPane.showOptionDialog(gestione.frame,temp,null, JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                            null,options,options[1]);
                    //se io accetto la richiesta già accettata dal destinatario
                    if(n==0)
                    {
                        gestione.nickname_destinatario=ricezione[1];
                        invio.invioGenerico("y;", ip);
                        gestione.statoConnessione=3;
                        JOptionPane.showMessageDialog(null, "Connessione accettata, avvio chat!");
                        gestione.avviaChat();
                    }
                    //se io rifiuto la richiesta già accettata dal destinatario
                    else if(n==1)
                    {
                       invio.invioGenerico("n;", ip); 
                    }
                }
                //se ricevo una conferma a caso invio un rifiuto
                else if(ricezione[0].equals("y"))
                    invio.invioGenerico("n;", ip);
                //se invece il destinatario mi rifiuta la connessione
                else if(ricezione[0].equals("n"))
                    gestione.statoConnessione=0;
            }
        }
        //se invece sono il destinatario e sono in attesa della conferma del mittente
        else if(gestione.statoConnessione==2)
        {
            if(ip.equals(gestione.ip_destinatario))
            {
                //se ricevo una conferma avvio la chat
                if(ricezione[0].equals("y") && ricezione.length==1)
                {
                    JOptionPane.showMessageDialog(null, "Connessione accettata, avvio chat!");
                    gestione.statoConnessione=3;
                }
                //se ricevo un rifiuto cancello tutta la richiesta e torno in attesa di altre connessioni
                else if(ricezione[0].equals("n"))
                {
                    gestione.statoConnessione=0;
                }
            }
        }
        
    }   
}
