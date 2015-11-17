/*
 * Classe que controla a aplicação.
 */
package RunApplication;

import Exemplo.JFramePesquisarCEP;

/**
 *
 * @author Thiago Teodoro Rodrigues
 */
public class RunApplication {

    public static void main(String[] args) {
        
        //Instanciando TelaPrincipal JFramePesquisarCEP
        JFramePesquisarCEP TelaPrincipal = new JFramePesquisarCEP();
        
        //Exibindo a TelaPrincipal estanciada.
        TelaPrincipal.setVisible(true);

    }
    
}
