/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.bean.Conta;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.model.dao.ContaDAO;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class DadosUsuarioController implements Initializable {

    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_endereco;
    @FXML
    private TextField txt_complemento;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_cep;
    @FXML
    private Button btn_acao;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_telefone;
    @FXML
    private Button lbl_editar;
    @FXML
    private TextField txt_data;
    @FXML
    private TextField txt_estado;
    @FXML
    private TextField txt_cidade;
    
    private ClienteTemporario clienteTemp = ClienteTemporario.getInstance();
    @FXML
    private ImageView btn_voltar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Cliente c = clienteTemp.getCliente();
        
        txt_nome.setText(c.getNome());
        txt_cpf.setText(c.getCpf());
        txt_endereco.setText(c.getEndereco());
        txt_cep.setText(c.getCep());
        txt_email.setText(c.getEmail());
        txt_telefone.setText(c.getTelefone());
        txt_complemento.setText(c.getComplemento());
        txt_data.setText(c.getDataNasc().toString().replace('-', '/'));
        txt_cidade.setText(c.getCidade());
        txt_estado.setText(c.getEstado());
    }    

    @FXML
    private void realizarAcao(ActionEvent event) throws IOException{
        try{
            ClienteDAO clienteDAO = new ClienteDAO();
            if(btn_acao.getText() == "EDITAR"){
                if(editarCadastro() && validacaoDados()){
                    //puxa os dados atuais pois nem todos serão alterados (cpf, data de nascimento e senha)
                    Cliente c = clienteTemp.getCliente();
                    c.setNome(txt_nome.getText());
                    c.setEndereco(txt_endereco.getText());
                    c.setCep(txt_cep.getText());
                    c.setEmail(txt_email.getText());
                    c.setTelefone(txt_telefone.getText());
                    c.setComplemento(txt_complemento.getText());
                    c.setCidade(txt_cidade.getText());
                    c.setEstado(txt_estado.getText());

                    clienteDAO.alterar(c);
                    clienteTemp.setCliente(c);
                    App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Dados alterados com sucesso!");
                    App.setRoot("view/Menu");                    
                }       
            } else {
                if(removerCadastro()){
                    Cliente c = clienteTemp.getCliente();
                    ContaDAO contaDAO = new ContaDAO();
                    Conta contaCorrente = contaDAO.buscaPorIdUsuario(c.getId(), 1);
                    Conta contaPoupanca = contaDAO.buscaPorIdUsuario(c.getId(), 2);
                    if(contaCorrente.getSaldo() != 0 || contaPoupanca.getSaldo() != 0){
                        App.alerta(Alert.AlertType.WARNING, "Aviso", "Impossível remover os dados do usuário.\nAmbas as contas precisam estar com o saldo vazio.");
                    } else {
                        clienteDAO.remover(c);
                        clienteTemp.removerInfo();
                        App.alerta(Alert.AlertType.CONFIRMATION, "Remoção", "Dados removidos com sucesso!");
                        App.setRoot("view/Login");
                    }
                    
                    
                }
            }            
        } catch (Exception ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao alterar dados do usuário: " + ex);
        }
    }

    @FXML
    private void editarDados(ActionEvent event) {
        txt_nome.setEditable(true);
        txt_endereco.setEditable(true);
        txt_cep.setEditable(true);
        txt_email.setEditable(true);
        txt_telefone.setEditable(true);
        txt_complemento.setEditable(true);
        txt_cidade.setEditable(true);
        txt_estado.setEditable(true);
        btn_acao.setText("EDITAR");
    }
    
    private boolean validacaoDados(){
        ContaDAO contaDAO = new ContaDAO();
        Conta contaCorrente = contaDAO.buscaPorIdUsuario(clienteTemp.getCliente().getId(), 1);
        Conta contaPoupanca = contaDAO.buscaPorIdUsuario(clienteTemp.getCliente().getId(), 2);
        //verifica se os campos editáveis estão vazios
        if(txt_nome.getText().isEmpty() || txt_endereco.getText().isEmpty() || txt_cep.getText().isEmpty() ||
                txt_email.getText().isEmpty() || txt_telefone.getText().isEmpty() || txt_complemento.getText().isEmpty()
                || txt_cidade.getText().isEmpty() || txt_estado.getText().isEmpty()){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos!");
            return false;
        }
        //verifica se o estado foi preenchido com mais de 2 caracteres
        else if (txt_estado.getText().length() > 2){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Valor inválido para estado, máximo de 2 caracteres (Exemplo: SP)");
            return false;
        }
        
        return true;
    }
    
    private boolean editarCadastro(){
        Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText("Deseja realmente editar os seus dados?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            return true;
        } else {
            return false;
        }
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

    @FXML
    private void voltarMenu(MouseEvent event) throws IOException {
        App.setRoot("view/Menu");
    }
    
}
