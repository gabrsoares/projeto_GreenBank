/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.temp;

import br.com.fatec.App;
import br.com.fatec.model.bean.Cartao;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

/**
 *
 * @author Gabriel
 */
public class CartaoVirtual {
    private static CartaoVirtual instancia;
    
    private List<Cartao> listaCartoes = new ArrayList<>();
    
    //construtor privado para não permitir novas instâncias
    private CartaoVirtual(){}
    
    public static CartaoVirtual getInstance(){
        if(instancia == null){
            instancia = new CartaoVirtual();
        }
        return instancia;
    }
    
    public boolean addCartao(Cartao cartao){
        List<Cartao> listaParaValidacao = listaCartoes;
        if(listaParaValidacao.stream().anyMatch(c -> cartao.getTipo().equals(c.getTipo())) ){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Só é possivel ter um cartão virtual desse tipo.");
            return false;
        } else {
            listaCartoes.add(cartao);
            return true;
        }
        
    }

    public List<Cartao> getListaCartoes() {
        return listaCartoes;
    }

    public void setListaCartoes(List<Cartao> listaCartoes) {
        this.listaCartoes = listaCartoes;
    }
    
    public void removerDados(){
        listaCartoes.clear();
    }
    
    
}
