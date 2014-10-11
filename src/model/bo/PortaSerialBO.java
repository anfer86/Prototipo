/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import model.vo.DadoVO;

/**
 *
 * @author CAF
 */
public class PortaSerialBO extends Observable implements SerialPortEventListener {

    private SerialPort serialPort;
    
    private DadoVO dado;
            
    public PortaSerialBO() {

    }

    public ArrayList<String> getListaPortas() {
        return new ArrayList(Arrays.asList(SerialPortList.getPortNames()));
    }

    public ArrayList<Integer> getTaxasTransmissao() {
        ArrayList<Integer> taxas = new ArrayList<Integer>();
        taxas.add(SerialPort.BAUDRATE_4800);
        taxas.add(SerialPort.BAUDRATE_9600);
        return taxas;
    }

    public void conectar(String nomePorta, Integer taxaTransmissao) throws Exception {
        serialPort = new SerialPort(nomePorta);

        try {
            serialPort.openPort();
            serialPort.setParams(taxaTransmissao, 8, 1, 0);            
            serialPort.addEventListener(this);
            
        } catch (SerialPortException ex) {
            throw new Exception("Ocorreu um problema ao conectar com porta serial " + nomePorta);
        }
    }

    public void desconectar() throws Exception {

        try {
            serialPort.closePort();
        } catch (SerialPortException ex) {
            throw new Exception("Ocorreu um problema ao desconectar a porta serial " + serialPort.getPortName());
        } catch (NullPointerException ex) {
            throw new Exception("Ocorreu um problema ao desconectar, pois não existe conexão estabelecida");
        }

    }

    @Override
    public void serialEvent(SerialPortEvent spe) {
        
        String strDados = null;
        
        try {
            strDados = serialPort.readString(15);
            dado = strDadosMatcher(strDados);
            if (dado != null){                
                dado.setTempo(new Date());
                setChanged();
                notifyObservers(dado);
            }         

        } catch (SerialPortException ex) {
            Logger.getLogger(PortaSerialBO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PortaSerialBO.class.getName()).log(Level.SEVERE, null, ex);
        } 
         
    }
    
    private DadoVO strDadosMatcher(String strDados){
        
        Pattern pattern = Pattern.compile("([-]?)\\s+(\\d+[\\.]\\d+)\\s+(\\w+)\\s+(\\w?)");
        Matcher matcher = pattern.matcher(strDados);
        
        if (!matcher.find()) return null;
        
        DadoVO dado = new DadoVO();

        dado.setValor(Double.parseDouble(matcher.group(1)+matcher.group(2).trim()));
        dado.setUnidade(matcher.group(3));
        dado.setEstavel(matcher.group(4).equals("S"));
                
        return dado;    
    }

    public DadoVO getDado() {
        return dado;
    }

    
    
}
