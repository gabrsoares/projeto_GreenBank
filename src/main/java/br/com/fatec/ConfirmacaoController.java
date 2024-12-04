/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class ConfirmacaoController implements Initializable {

    @FXML
    private Label lbl_num_conta;
    @FXML
    private Label lbl_agencia;
    @FXML
    private Label lbl_tipo;
    @FXML
    private Label lbl_valor;
    @FXML
    private Label lbl_data;
    @FXML
    private Button btn_confirmar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void realizarTransacao(ActionEvent event) {
    }
    
}
