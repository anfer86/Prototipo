/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import model.bo.PortaSerialBO;
import view.DlgPortaSerial;

/**
 *
 * @author CAF
 */
public class ControllerPortaSerial implements ActionListener{

    private DlgPortaSerial dlgPortaSerial;

    private PortaSerialBO portaSerialBO;

    public ControllerPortaSerial(DlgPortaSerial frmPortaSerial) {
        this.dlgPortaSerial = frmPortaSerial;
        this.portaSerialBO = new PortaSerialBO();

        initComponentsValues();

        initComponentsListeners();
    }

    private void initComponentsListeners() {

        dlgPortaSerial.getbConfirmar().addActionListener(this);
        dlgPortaSerial.getbCancelar().addActionListener(this);

    }

    private void initComponentsValues() {

        // Mostra a lista de portas no combobox
        dlgPortaSerial.getCbNomePortas().removeAllItems();
        for (String nomePorta : portaSerialBO.getListaPortas()) {
            dlgPortaSerial.getCbNomePortas().addItem(nomePorta);
        }

        // Mostra a lista de taxas de transmissão
        dlgPortaSerial.getCbTaxa().removeAllItems();
        for (Integer taxa : portaSerialBO.getTaxasTransmissao()) {
            dlgPortaSerial.getCbTaxa().addItem(taxa);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == dlgPortaSerial.getbConfirmar()) {
            eventoConfirmar();
            dlgPortaSerial.dispose();            
        } else if (source == dlgPortaSerial.getbCancelar()) {
            eventoCancelar();
        }
    }

    public PortaSerialBO getPortaSerialBO() {
        return portaSerialBO;
    }

    private void eventoConfirmar() {
        try {
            portaSerialBO.conectar(
                    (String) dlgPortaSerial.getCbNomePortas().getSelectedItem(),
                    (Integer) dlgPortaSerial.getCbTaxa().getSelectedItem());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dlgPortaSerial, ex.getMessage());
        }
    }

    private void eventoCancelar() {
        dlgPortaSerial.dispose();
    }

}
