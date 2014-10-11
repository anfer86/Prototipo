/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.vo;

import java.util.Date;

/**
 *
 * @author Aluno
 */
public class DadoVO {
    
    private double valor;
    private String unidade;
    private boolean estavel;
    private Date tempo;

    public DadoVO(double valor, String unidade, boolean estabilidade) {
        this.valor = valor;
        this.unidade = unidade;
        this.estavel = estabilidade;
    }

    public DadoVO() {
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public void setEstavel(boolean estavel) {
        this.estavel = estavel;
    }

    public void setTempo(Date tempo) {
        this.tempo = tempo;
    }
    
    

    public String getUnidade() {
        return unidade;
    }

    public double getValor() {
        return valor;
    }

    public boolean isEstavel() {
        return estavel;
    }    

    public Date getTempo() {
        return tempo;
    }
    
    
}
