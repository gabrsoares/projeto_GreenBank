/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.temp;

import br.com.fatec.model.bean.Cartao;

/**
 *
 * @author Gabriel
 */
public class CartaoTemporario {
     private static CartaoTemporario instancia;
    
    private Cartao cartao;
    
    //construtor privado para não permitir novas instâncias
    private CartaoTemporario(){}
    
    public static CartaoTemporario getInstance(){
        if(instancia == null){
            instancia = new CartaoTemporario();
        }
        return instancia;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
    
    public void removerInfo(){
        cartao = null;
    }
}
