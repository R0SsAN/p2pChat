/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pchat_java;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Luca
 */
public class Gestione_Chat {
    static Gestione_Chat _instance=null;
    static synchronized public Gestione_Chat getInstance(Frame_chat frame) throws SocketException
    {
        if(_instance==null)
            _instance=new Gestione_Chat(frame);
        return _instance;
    }
    
    //frame usato per poter aggiungere i messaggi a runtime
    Frame_chat frame;
    
    //0 == attesa connessione
    //1 == richiesta connessione inviata, attesa risposta
    //2 == risposta connessione inviata, attesa risposta (solo se ho accettato la connessione)
    //3 == connessione avviata, chat in corso
    int statoConnessione;
    
    String nickname_mittente;
    String nickname_destinatario;
    
    InetAddress ip_destinatario;
    
    //lista che contiene tutti gli ip dei destinatari che non hanno risposto alla mia richiesta
    ArrayList<String> nessunaRisposta;
    
    //Classe per invio
    Invio invio;

    public Gestione_Chat(Frame_chat frame) throws SocketException {
        this.frame=frame;
        statoConnessione=0;
        nickname_mittente="ruossan";
        nickname_destinatario="napoli";
        ip_destinatario=null;
        nessunaRisposta=new ArrayList<>();
    }
    public void setMittente(String nick)
    {
        nickname_mittente=nick;
    }
    public void aggiornaNickDestinatario(String nick)
    {
        nickname_destinatario=nick;
        frame.getComponents();
    }
    public void avviaChat()
    {
        frame.aggiornaDestinatario(nickname_destinatario);
        frame.aggiornaGrafica();
        
    }
    public void annullaRichiesta()
    {
        statoConnessione=0;
        nessunaRisposta.add(ip_destinatario.toString());
        frame.ripristinaGrafica();
    }
    public void terminaChat(boolean check) throws IOException
    {
        //inizializzo l'istanza invio che non avevo inizializzato nel costruttore
        if(invio == null)
            invio=Invio.getInstance();
        
        statoConnessione=0;
        if(check)
            invio.invioGenerico("c;", ip_destinatario);
        frame.svuotaPanel();
        frame.ripristinaGrafica();
    }
    public boolean controllaPresenza(String ip)
    {
        for (int i = 0; i < nessunaRisposta.size(); i++) {
            if(nessunaRisposta.get(i).contains(ip))
            {
                nessunaRisposta.remove(i);
                return true;
            }   
        }
        return false;
    }
    //------------------------
    //METODI GESTIONE MESSAGGI
    public void gestioneMessaggioTesto(String s, boolean check)
    {
        //se sono io a inviare il messaggio
        if(check)
            frame.inserisciMessaggioTesto(s, true);
        //se invece sono quello che lo riceve
        else
            frame.inserisciMessaggioTesto(s, false);
    }
    
}
