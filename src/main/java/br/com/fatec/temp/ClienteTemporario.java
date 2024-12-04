/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.temp;

import br.com.fatec.model.bean.Cliente;

/**
 *
 * @author Gabriel
 */
public class ClienteTemporario {
    
    private static ClienteTemporario instancia;
    
    private Cliente cliente;
    private String codigoGerado;
    
    //construtor privado para não permitir novas instâncias
    private ClienteTemporario(){}
    
    public static ClienteTemporario getInstance(){
        if(instancia == null){
            instancia = new ClienteTemporario();
        }
        return instancia;
    }

    public String getCodigoGerado() {
        return codigoGerado;
    }

    public void setCodigoGerado(String codigoGerado) {
        this.codigoGerado = codigoGerado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void removerInfo(){
        cliente = null;
        codigoGerado = null;
    }
    
    
    
    
}
