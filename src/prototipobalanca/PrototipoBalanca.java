/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prototipobalanca;

import controller.ControllerPrototipo;
import view.FramePrototipo;

/**
 *
 * @author CAF
 */
public class PrototipoBalanca {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FramePrototipo pv = new FramePrototipo();
        new ControllerPrototipo(pv);
        pv.setVisible(true);
        
    }
    
}
