package br.com.fatec;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.temp.ClienteTemporario;
import br.com.fatec.util.TextFieldFormatter;
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoginController implements Initializable {

    @FXML
    private PasswordField txt_senha;
    @FXML
    private Button btn_login;
    @FXML
    private TextField txt_cpf;
    @FXML
    private Hyperlink lbl_cadastro;
    @FXML
    private Hyperlink lbl_esqueciSenha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void fazerLogin() throws IOException {
        Cliente c = new Cliente();
        ClienteDAO dao = new ClienteDAO();

        if (validacaoCampos()) {
            Cliente clienteEncontrado = dao.listarPorCpf(txt_cpf.getText().strip().replace(" ", ""));
            if (clienteEncontrado != null) {
                BCrypt.Result result = BCrypt.verifyer().verify(txt_senha.getText().toCharArray(), clienteEncontrado.getSenha());
                if (result.verified) {
                    App.alerta(AlertType.CONFIRMATION, "Sucesso", "Login realizado com sucesso");
                    ClienteTemporario cliente = ClienteTemporario.getInstance();
                    cliente.setCliente(clienteEncontrado);
                    App.setRoot("view/Menu");
                } else {
                    App.alerta(AlertType.ERROR, "Erro", "CPF ou senha incorretos, tente novamente");
                }
            } else {
                App.alerta(AlertType.ERROR, "Erro", "CPF ou senha incorretos, tente novamente");
            }
        }
    }

    private boolean validacaoCampos() {
        if (txt_cpf.getText().isEmpty() || txt_senha.getText().isEmpty()) {
            App.alerta(AlertType.ERROR, "Erro", "Campos vazios ou inválidos");
            return false;
        }
        return true;
    }

    @FXML
    private void fazerCadastro(ActionEvent event) throws IOException {
        try {
            App.setRoot("view/Cadastro");
        } catch (IOException ex) {
            App.alerta(AlertType.ERROR, "Erro", "Erro ao realizar login, tente novamente mais tarde");
        }
    }

    @FXML
    private void mudarSenha(ActionEvent event) throws IOException {
        App.setRoot("view/InserirCPF");
    }

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
    
     