/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pchat_java;

import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Luca
 */
public class Gestione_Chat {
    static Gestione_Chat _instance=null;
    static synchronized public Gestione_Chat getInstance(Frame_chat frame)
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

    public Gestione_Chat(Frame_chat frame) {
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
        
    }
    
    
}
