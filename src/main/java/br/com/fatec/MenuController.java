/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.bean.Conta;
import br.com.fatec.model.connection.MySQLConnection;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.model.dao.ContaDAO;
import br.com.fatec.temp.CartaoVirtual;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class MenuController implements Initializable {

    @FXML
    private Button btn_cartoes;
    @FXML
    private Button btn_extrato;
    @FXML
    private Button btn_pix;
    @FXML
    private Button btn_transferencia;
    @FXML
    private Label lbl_nome;
    @FXML
    private ImageView btn_perfil;
    
    private ClienteTemporario cliente = ClienteTemporario.getInstance();
    @FXML
    private Label lbl_saldoC;
    @FXML
    private Label lbl_saldoP;
    @FXML
    private Button btn_verSaldoC;
    @FXML
    private Button btn_verSaldoP;
    @FXML
    private ImageView btn_voltarLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //puxa o objeto cliente já instanciado
        
        ClienteDAO clienteDAO = new ClienteDAO();
        ContaDAO contaDAO = new ContaDAO();
        Cliente c = null;
        List<Conta> contas = null;
        
        c = clienteDAO.listarPorCpf(cliente.getCliente().getCpf());
        contas = contaDAO.buscaPorIdUsuario(c.getId());
        if(c != null && contas != null){
            String nome = c.getNome();
            int indexEspaco = nome.indexOf(" ");
            //caso o usuario insira apenas 1 nome, não entrará aqui
            if(indexEspaco != -1){
                nome = nome.substring(0, indexEspaco);
            }
            lbl_nome.setText("Olá, " + nome);
            lbl_saldoC.setText(String.format("R$ %.2f", contas.get(0).getSaldo()));
            lbl_saldoP.setText(String.format("R$ %.2f", contas.get(1).getSaldo()));
        }    
    }

    @FXML
    private void telaCartoes(ActionEvent event) throws IOException {
        App.setRoot("view/meusCartoes");
    }

    @FXML
    private void telaExtrato(ActionEvent event) throws IOException {
        App.setRoot("view/extrato");
    }

    @FXML
    private void telaPix(ActionEvent event) throws IOException {
        App.setRoot("view/Pix");
    }

    @FXML
    private void telaTransacao(ActionEvent event) throws IOException {
        App.setRoot("view/transferencia");
    }

    @FXML
    private void editarPerfil(MouseEvent event) throws IOException {
        App.setRoot("view/dadosUsuario");
    }

    @FXML
    private void voltarLogin(MouseEvent event) throws IOException {
        Alert alert = new Alert(AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText("Deseja realmente voltar para a tela de login?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            cliente.removerInfo();
            CartaoVirtual.getInstance().removerDados();
            App.setRoot("view/Login");
        }
    }

    @FXML
    private void exibirSaldoC(ActionEvent event) {
        if(lbl_saldoC.isVisible()){
            lbl_saldoC.setVisible(false);
        } else {
            lbl_saldoC.setVisible(true);
        }
    }

    @FXML
    private void exibirSaldoP(ActionEvent event) {
        if(lbl_saldoP.isVisible()){
            lbl_saldoP.setVisible(false);
        } else {
            lbl_saldoP.setVisible(true);
        }
    }
}
