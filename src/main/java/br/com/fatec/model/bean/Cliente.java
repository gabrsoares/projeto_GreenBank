/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.bean;

import java.sql.Date;
import java.util.Objects;


/**
 *
 * @author Gabriel
 */
public class Cliente {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String nacionalidade;
    private Date dataNasc;
    private String endereco;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private String senha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date data_nasc) {
        this.dataNasc = data_nasc;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return nome + " - CPF: " + formatarCpf(cpf);
    }
    
    private String formatarCpf(String cpf){
        StringBuilder sb = new StringBuilder(cpf);
        int[] indices = {0,1,2,10,11,12,13};
        char newChar = '•';
        
        for (int index : indices){
            if(index >= 0 && index < sb.length()){
                sb.setCharAt(index, newChar);
            }
        }
        
        return sb.toString();
    }
    
    
    
    
}
