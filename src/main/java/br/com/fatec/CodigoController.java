/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.mailSender.EmailSender;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import br.com.fatec.temp.ClienteTemporario;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author Thais Oliveira
 */
public class CodigoController implements Initializable {

    @FXML
    private TextField txt_codigo;
    @FXML
    private Button txt_enviar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        enviarCodigo();
    }
    
    private void enviarCodigo(){
        EmailSender es = new EmailSender();
        ClienteTemporario cliente = ClienteTemporario.getInstance();
        try {
            es.enviarCodigoVerificacao(cliente.getCliente().getEmail(), cliente.getCodigoGerado());
        } catch (MessagingException ex) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao enviar email, tente novamente mais tarde");
        }
    }

    @FXML
    private void verificarCodigo(ActionEvent event) throws IOException {
        ClienteTemporario cliente = ClienteTemporario.getInstance();
        if(txt_codigo.getText().equals(cliente.getCodigoGerado())){
            App.setRoot("view/EsqueciSenha");
        }
    }
    
}
