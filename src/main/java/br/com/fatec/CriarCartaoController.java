/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fatec.model.bean.Cartao;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.CartaoDAO;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class CriarCartaoController implements Initializable {

    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_cpf;
    @FXML
    private ComboBox<String> cmb_tipoCartao;
    @FXML
    private ComboBox<String> cmb_dataValidade;
    @FXML
    private PasswordField txt_senha;
    @FXML
    private PasswordField txt_rep_senha;
    @FXML
    private Button btn_cadastrar;
    @FXML
    private CheckBox chb_termos;
    
    private Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        txt_nome.setText(clienteTemp.getNome());
        txt_cpf.setText(clienteTemp.getCpf());
        txt_nome.setEditable(false);
        txt_cpf.setEditable(false);
        preencherTipos();
        preencherDatas();
    }    

    @FXML
    private void cadastrar(ActionEvent event) throws IOException {
        if(validacaoCampos()){
            Cartao c = new Cartao();
            CartaoDAO cartaoDAO = new CartaoDAO();
            
            try{
                c.setTipo(cmb_tipoCartao.getValue());
                c.setNumero(gerarNumeroCartao());
                c.setId_cliente(clienteTemp.getId());
                c.setNumeroSeguranca(gerarNumeroSeguranca());
                c.setDataValidade(cmb_dataValidade.getValue());
                String senha = txt_senha.getText();
                senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
                c.setSenha(senha);
                
                cartaoDAO.inserir(c);
                App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Cartão criado com sucesso.");
                App.setRoot("view/meusCartoes");
            } catch (SQLException ex){
                App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro na criação do cartão");
                App.setRoot("view/Menu");
            }
        }
    }
    
    private void preencherTipos(){
        ObservableList<String> listaTipos = FXCollections.observableArrayList();
        listaTipos.add("Débito");
        listaTipos.add("Crédito");
        listaTipos.add("VR");
        listaTipos.add("VA");
        
        cmb_tipoCartao.setItems(listaTipos);
    }
    
    private void preencherDatas(){
        //converte o ano para string para utilizarmos o substring e recordar apenas a parte final do ano, dps convertemos para int de novo
        int anoHoje = Integer.parseInt(String.valueOf(LocalDate.now().getYear()).substring(2,4));
        ObservableList<String> listaDatas = FXCollections.observableArrayList();
        listaDatas.add("12/"+(anoHoje+4));
        listaDatas.add("12/"+(anoHoje+6));
        listaDatas.add("12/"+(anoHoje+8));
        listaDatas.add("12/"+(anoHoje+10));
        
        cmb_dataValidade.setItems(listaDatas);
    }
    
    private boolean validacaoCampos(){
        if(cmb_tipoCartao.getValue() == null || cmb_dataValidade.getValue() == null || txt_senha.getText().isEmpty() || txt_rep_senha.getText().isEmpty()){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Preencha todos os campos!");
            return false;
        }
        try{
            int senha = Integer.parseInt(txt_senha.getText());
        } catch (NumberFormatException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Senhas somente valores numéricos");
            return false;
        }
        if (txt_senha.getText().length() > 6 || txt_senha.getText().length() < 4){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Não é permitido senhas menores de 4 digitos ou maiores de 6 digitos.");
            return false;
        } else if (!txt_senha.getText().equals(txt_rep_senha.getText())){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "As senhas não correspondem");
            return false;
        }
        return true;
    }
    
    private String gerarNumeroCartao(){
        Random random = new Random();
        int numero1 = random.nextInt(9999);
        int numero2 = random.nextInt(9999);
        int numero3 = random.nextInt(9999);
        int numero4 = random.nextInt(9999);
        return String.format("%04d %04d %04d %04d", numero1, numero2, numero3, numero4);
    }
    
    private String gerarNumeroSeguranca(){
        Random random = new Random();
        int numero = random.nextInt(999);
        
        return String.format("%03d", numero);
    }

    @FXML
    private void voltarCartoes(MouseEvent event) throws IOException {
        App.setRoot("view/meusCartoes");
    }
    
}
