/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.mailSender.EmailSender;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.temp.ClienteTemporario;
import br.com.fatec.util.TextFieldFormatter;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Thais Oliveira
 */
public class InserirCPFController implements Initializable {

    @FXML
    private TextField txt_cpf;
    @FXML
    private AnchorPane tela_cpf;
    @FXML
    private Label lbl_texto;
    @FXML
    private Button btn_enviarCPF;
    @FXML
    private AnchorPane tela_codigo;
    @FXML
    private Button txt_enviarCodigo;
    @FXML
    private TextField txt_codigo;
    @FXML
    private Label lbl_cpf_codigo;
    @FXML
    private ImageView lbl_voltar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tela_cpf.setVisible(true);
        tela_codigo.setVisible(false);
    }
    
    @FXML
    private void verificarCPF(ActionEvent event) throws MessagingException, IOException {
        Cliente c;
        ClienteDAO dao = new ClienteDAO();
        EmailSender es = new EmailSender();
        String textoCPF = txt_cpf.getText().strip().replace(" ", "");
        
        
        if(textoCPF.length() == 14){
            //consulta do cliente no banco de dados pelo cpf
            c = dao.listarPorCpf(textoCPF);
            if (c != null){
                tela_codigo.setVisible(true);
                tela_cpf.setVisible(false);
                //Armazena a instância da classe clienteTemporário para definir os atributos que serão verificados no futuro
                ClienteTemporario clienteTemp = ClienteTemporario.getInstance();
                
                String codigoGerado = gerarCodigo();
                //definição dos atributos que serão verificados no futuro
                clienteTemp.setCodigoGerado(codigoGerado);
                clienteTemp.setCliente(c);
                //método para enviar uma mensagem com o código para o email cadastrado no banco de dados.
                //será feita em uma thread diferente para que ela seja executada enquanto as telas mudam.
                Task<Void> enviarEmail = new Task(){
                    @Override
                    protected Object call(){
                        try {
                            es.enviarCodigoVerificacao(c.getEmail(), codigoGerado);
                        } catch (MessagingException ex) {
                            App.alerta(AlertType.ERROR, "Erro", "Ocorreu um erro ao enviar o email para o usuário");
                        } catch (Exception ex){
                            App.alerta(AlertType.ERROR, "Erro", "Ocorreu um erro ao enviar o email para o usuário");
                        }
                        return null;
                    }
                    
                };
                new Thread(enviarEmail).start();
            } else {
                App.alerta(AlertType.ERROR, "Erro", "Não existem contas cadastradas com esse CPF");
            }
        } else {
            App.alerta(AlertType.WARNING, "Aviso", "Insira um CPF válido");
        }
    }
    
    public static String gerarCodigo(){
        Random random = new Random();
        int numero = random.nextInt(999999);
        
        return String.format("%06d", numero);
    }

    @FXML
    private void verificarCodigo(ActionEvent event) throws IOException {
        ClienteTemporario cliente = ClienteTemporario.getInstance();
        if(txt_codigo.getText().equals(cliente.getCodigoGerado())){
            App.setRoot("view/EsqueciSenha");
        } else {
            App.alerta(AlertType.ERROR, "Erro", "Código incorreto.");
        }
    }

    private void voltarMenu(MouseEvent event) throws IOException {
        Alert alert = new Alert(AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText("Deseja realmente voltar ao menu?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            App.setRoot("view/Menu");
        }
    }

    @FXML
    private void voltarLogin(MouseEvent event) throws IOException {
        App.setRoot("view/Login");
    }
//txt_cpf.getText().strip().replace(" ", "")
    @FXML
    private void cpfKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        //define o esqueleto da máscara
        tff.setMask("###.###.###-##");
        //define os caracteres permitidos
        tff.setCaracteresValidos("0123456789");
        //insere a mascara no txt
        tff.setTf(txt_cpf);
        //formata o txt
        tff.formatter();
    }
    
}
