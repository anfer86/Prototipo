/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.bo.PortaSerialBO;
import model.vo.DadoVO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import view.DlgPortaSerial;
import view.FramePrototipo;

/**
 *
 * @author CAF
 */
public class ControllerPrototipo implements ActionListener, Observer {

    private FramePrototipo frmPrototipo;
    
    private PortaSerialBO portaSerialBO;    
    
    private ArrayList<DadoVO> serieBruta;
    
    public TimeSeries timeSeries;

    public ControllerPrototipo(FramePrototipo viewPrototipo) {        
        this.frmPrototipo = viewPrototipo;        

        initComponentsListeners();

        initComponentsAvailability();
    }

    public void initComponentsListeners() {

        frmPrototipo.getbConectar().addActionListener(this);
        frmPrototipo.getbAcao().addActionListener(this);
        frmPrototipo.getbSalvar().addActionListener(this);
        
        
        
    }

    public void initComponentsAvailability() {

        estadoDesconectado();

    }
    
    public void estadoDesconectado(){
        frmPrototipo.getbConectar().setEnabled(true);
        frmPrototipo.getbAcao().setEnabled(false);
        frmPrototipo.getbSalvar().setEnabled(false);
        
        frmPrototipo.getbConectar().setText("Conectar");
        frmPrototipo.getbAcao().setText("Iniciar");        
    }
    
    public void estadoConectadoNovaTransmissao(){
        frmPrototipo.getbConectar().setEnabled(false);
        frmPrototipo.getbAcao().setEnabled(true);
        frmPrototipo.getbSalvar().setEnabled(false);
        frmPrototipo.getbConectar().setText("Desconectar");
        frmPrototipo.getbAcao().setText("Iniciar");
    }
    
    public void estadoConectadoAposTransmissao(){
        frmPrototipo.getbConectar().setEnabled(true);
        frmPrototipo.getbAcao().setEnabled(true);
        frmPrototipo.getbSalvar().setEnabled(true);
        frmPrototipo.getbConectar().setText("Desconectar");
        frmPrototipo.getbAcao().setText("Iniciar");
    }
    
    public void estadoTransmitindo(){
        frmPrototipo.getbConectar().setEnabled(false);
        frmPrototipo.getbAcao().setEnabled(true);
        frmPrototipo.getbSalvar().setEnabled(false);        
        frmPrototipo.getbAcao().setText("Parar");
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == frmPrototipo.getbConectar()) {
            if (frmPrototipo.getbConectar().getText().equals("Conectar"))
                eventoConectar();
            else eventoDesconectar();
        } else if (source == frmPrototipo.getbAcao()) {
            if (frmPrototipo.getbAcao().getText().equals("Iniciar"))
                eventoIniciar();
                else eventoParar();
        
        } else if (source == frmPrototipo.getbSalvar()) {
            eventoSalvar();
        }
    }
    
    public void eventoConectar(){
        
        DlgPortaSerial dlgPortaSerial = new DlgPortaSerial(frmPrototipo,true);
        
        ControllerPortaSerial controllerPS =
            new ControllerPortaSerial(dlgPortaSerial);
        
        dlgPortaSerial.setVisible(true);
        
        portaSerialBO = controllerPS.getPortaSerialBO();
        
        if (portaSerialBO != null){
            frmPrototipo.getbConectar().setText("Desconectar");
            estadoConectadoNovaTransmissao();
            initChartComponent();           
        }        
    }
    
    public void eventoIniciar(){        
        portaSerialBO.addObserver(this);
        serieBruta = new ArrayList<DadoVO>();
        estadoTransmitindo();
        
    }
    
    public void eventoParar(){
        portaSerialBO.deleteObserver(this);
        frmPrototipo.getbAcao().setText("Iniciar");
        frmPrototipo.getbSalvar().setEnabled(true);
        frmPrototipo.getbConectar().setEnabled(true);
        estadoConectadoAposTransmissao();
        
    }
    
    public void eventoSalvar(){        
        
    
    }
    
    public void eventoDesconectar(){        
        try {
            portaSerialBO.desconectar();
            estadoDesconectado();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frmPrototipo, ex.getMessage());
        }
    }

    private void initChartComponent() {
        // Criação do Gráfico
        JFreeChart xyLineChart = ChartFactory.createTimeSeriesChart(null, "Tempo", "Peso", createDataset());
                
        // Criação de um panel para o gráfico
        ChartPanel chartPanel = new ChartPanel(xyLineChart);
      
        // Configuração do tamanho adequado
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        
        // Adição do gráfico para o panel gráfco
        frmPrototipo.getpGrafico().setLayout(new BorderLayout());
        frmPrototipo.getpGrafico().add(chartPanel,BorderLayout.CENTER);
        frmPrototipo.pack();
    }

   private XYDataset createDataset() {
      this.timeSeries = new TimeSeries("Dados", Millisecond.class);
      
      return new TimeSeriesCollection(timeSeries);      
     
  }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof DadoVO){   
            DadoVO dado = (DadoVO) arg;
            serieBruta.add((DadoVO) arg);
            timeSeries.add(
                    new Millisecond(dado.getTempo()),
                    dado.getValor());
            
            frmPrototipo.pack();
        }
        
        
    }

}