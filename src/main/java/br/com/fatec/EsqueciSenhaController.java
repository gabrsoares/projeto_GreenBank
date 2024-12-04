/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author Thais Oliveira
 */
public class EsqueciSenhaController implements Initializable {

    @FXML
    private PasswordField txt_senha;
    @FXML
    private PasswordField txt_repSenha;
    @FXML
    private Button btn_enviar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void mudarSenha(ActionEvent event) throws IOException {
        if(txt_senha.getText().equals(txt_repSenha.getText())){
            //Cliente temporário armazenado ao solicitar a troca de senha
            ClienteTemporario clienteTemp = ClienteTemporario.getInstance();
            Cliente c = new Cliente();
            ClienteDAO dao = new ClienteDAO();
            //busca o cliente pelo cpf do cliente temporário
            c = dao.listarPorCpf(clienteTemp.getCliente().getCpf());
            if(c != null){
                String senha = txt_senha.getText();
                senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
                c.setSenha(senha);

                //realiza a alteração dos dados do cliente
                if(dao.alterar(c)){
                    App.alerta(AlertType.CONFIRMATION, "Sucesso", "Senha alterada com sucesso");
                    App.setRoot("view/Login");
                } else {
                    App.alerta(AlertType.ERROR, "Erro", "Não foi possível alterar os dados do cliente");
                }
            } else {
                App.alerta(AlertType.ERROR, "Erro", "Não foi possível encontrar o cliente");
            }
        } else {
            App.alerta(AlertType.ERROR, "Erro", "As senhas não coincidem");

        }
    }
    
    
}
