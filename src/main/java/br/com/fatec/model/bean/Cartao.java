/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.bean;

/**
 *
 * @author Gabriel
 */
public class Cartao {
    private int id;
    private String tipo;
    private String numero;
    private int id_cliente;
    private String numeroSeguranca;
    private String dataValidade;
    private String senha;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNumeroSeguranca() {
        return numeroSeguranca;
    }

    public void setNumeroSeguranca(String numeroSeguranca) {
        this.numeroSeguranca = numeroSeguranca;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cartao other = (Cartao) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Cartao{" + "id=" + id + ", tipo=" + tipo + ", numero=" + numero + ", id_cliente=" + id_cliente + ", numeroSeguranca=" + numeroSeguranca + ", dataValidade=" + dataValidade + ", senha=" + senha + '}';
    }
    
    
    
    
}
