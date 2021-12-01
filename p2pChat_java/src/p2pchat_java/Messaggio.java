/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pchat_java;

import java.net.InetAddress;

/**
 *
 * @author rossini_luca
 */
public class Messaggio {
    //tipo del messaggio
    // 1 -> testo normale
    // resto -> da implementare
    int type;
    InetAddress mittente_destinatario;
    //Oggetto generico che conterrà il dato del messaggio
    Object data;

    public Messaggio(int type, InetAddress address, Object data) {
        this.type = type;
        this.mittente_destinatario = address;
        this.data = data;
    }
    
}
