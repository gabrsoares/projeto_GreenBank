/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.CartaoDAO;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.temp.CartaoTemporario;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class DadosCartaoController implements Initializable {
    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_cpf;
    
    CartaoTemporario cartaoTemp = CartaoTemporario.getInstance();
    @FXML
    private TextField txt_numero;
    @FXML
    private TextField txt_cod_seg;
    @FXML
    private Button btn_enviar;
    @FXML
    private TextField txt_tipo;
    @FXML
    private ComboBox<String> cmb_data;
    @FXML
    private Button lbl_editar;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            CartaoDAO cartaoDAO = new CartaoDAO();
                       
            Cliente cliente = clienteDAO.buscaPorId(cartaoTemp.getCartao().getId_cliente());
            preencherDatas();
            txt_nome.setText(cliente.getNome());
            txt_cpf.setText(cliente.getCep());
            txt_tipo.setText(cartaoTemp.getCartao().getTipo());
            txt_numero.setText(cartaoTemp.getCartao().getNumero());
            txt_cod_seg.setText(cartaoTemp.getCartao().getNumeroSeguranca());
            cmb_data.setValue(cartaoTemp.getCartao().getDataValidade());
            
        } catch (SQLException ex) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro na busca do cartão");
        }
     
    }    

    @FXML
    private void alterarOuRemoverCartao(ActionEvent event) throws IOException {
        CartaoDAO cartaoDAO = new CartaoDAO();
        try{
            if(btn_enviar.getText() == "Editar"){
                cartaoTemp.getCartao().setDataValidade(cmb_data.getValue());
                cartaoDAO.alterar(cartaoTemp.getCartao());
                cartaoTemp.removerInfo();
                App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Dados alterados com sucesso");
                App.setRoot("view/meusCartoes");
            } else {
                if(removerCadastro()){
                    cartaoDAO.remover(cartaoTemp.getCartao());
                    cartaoTemp.removerInfo();
                    App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Dados removidos com sucesso");
                    App.setRoot("view/meusCartoes");
                }
                
            }
        } catch (SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro com os dados do cartão.");
        }
        
    }
    
    private void preencherDatas(){
        //converte o ano para string para utilizarmos o substring e recordar apenas a parte final do ano, dps convertemos para int de novo
        int anoHoje = Integer.parseInt(String.valueOf(LocalDate.now().getYear()).substring(2,4));
        ObservableList<String> listaDatas = FXCollections.observableArrayList();
        listaDatas.add("12/"+(anoHoje+4));
        listaDatas.add("12/"+(anoHoje+6));
        listaDatas.add("12/"+(anoHoje+8));
        listaDatas.add("12/"+(anoHoje+10));
        
        cmb_data.setItems(listaDatas);
    }

    @FXML
    private void voltarCartoes(MouseEvent event) throws IOException {
        App.setRoot("view/meusCartoes");
    }

    @FXML
    private void editarDados(ActionEvent event) {
        btn_enviar.setText("Editar");
        cmb_data.setEditable(true);
    }
    
    private boolean removerCadastro(){
        Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText("Deseja realmente remover os seus dados permanentemente?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            return true;
        } else {
            return false;
        }
    }
     
}
