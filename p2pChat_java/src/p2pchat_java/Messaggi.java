/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pchat_java;

import java.net.SocketException;
import java.util.ArrayList;

/**
 *
 * @author rossini_luca
 */
public class Messaggi {
    static Messaggi _instance=null;
    static synchronized public Messaggi getInstance()
    {
        if(_instance==null)
            _instance=new Messaggi();
        return _instance;
    }
    ArrayList<Messaggio> messaggiRicevuti;
    ArrayList<Messaggio> messaggiInviati;
    public Messaggi()
    {
        messaggiRicevuti=new ArrayList<Messaggio>();
        messaggiInviati=new ArrayList<Messaggio>();
    }
    public void inserisciMessagioRicevuto(Messaggio mex)
    {
        messaggiRicevuti.add(mex);
    }
    public void inserisciMessagioInviato(Messaggio mex)
    {
        messaggiInviati.add(mex);
    }
    //vari metodi da implementare se serviranno
}
