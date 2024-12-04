/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.temp.ClienteTemporario;
import br.com.fatec.util.TextFieldFormatter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class CadastroController implements Initializable {

    @FXML
    private TextField txt_nome;
    @FXML
    private TextField txt_telefone;
    @FXML
    private TextField txt_endereco;
    @FXML
    private TextField txt_nacional;
    @FXML
    private TextField txt_complemento;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_estado;
    @FXML
    private TextField txt_cep;
    @FXML
    private TextField txt_cidade;
    @FXML
    private DatePicker txt_data_nasc;
    @FXML
    private PasswordField txt_senha;
    @FXML
    private PasswordField txt_rep_senha;
    @FXML
    private Button btn_cadastrar;
    @FXML
    private TextField txt_email;
    @FXML
    private ImageView btn_voltarLogin;
    
    private ClienteDAO dao = new ClienteDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void cadastrar(ActionEvent event) {
        Cliente c = new Cliente();
        
        if(validacaoCampos()){
            try{
                c.setNome(txt_nome.getText());
                c.setCpf(txt_cpf.getText().strip().replace(" ", ""));
                c.setTelefone(txt_telefone.getText().strip().replace(" ", ""));
                c.setEmail(txt_email.getText());
                c.setNacionalidade(txt_nacional.getText());
                //formatando localdate para date pois o statement não aceita localdate
                Date data_formatada = Date.valueOf(txt_data_nasc.getValue());
                c.setDataNasc(data_formatada);
                c.setEndereco(txt_endereco.getText());
                c.setComplemento(txt_complemento.getText());
                c.setCidade(txt_cidade.getText());
                c.setEstado(txt_estado.getText());
                c.setCep(txt_cep.getText());
                //criptografando a senha
                String senha = txt_senha.getText();
                senha = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
                c.setSenha(senha);

                //dao.inserir(c);
                ClienteTemporario clienteTemp = ClienteTemporario.getInstance();
                //o cliente só será cadastrado após inserir a agência
                clienteTemp.setCliente(c);
                
                App.alerta(AlertType.CONFIRMATION, "Sucesso", "Usuário criado com sucesso.");
                App.setRoot("view/Agencias");
            } catch (Exception e){
                App.alerta(AlertType.ERROR, "Erro", "Erro ao criar usuário.");
            }
        }
        
        
    }
    
    private boolean validacaoCampos(){
        LocalDate dataAtual = LocalDate.now();
        //instancia a classe cliente para validação caso já exista
        Cliente clienteValidacao = dao.listarPorCpf(txt_cpf.getText());
        //regex para validação cpf;
        //String regex = "^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$";
        //checa se os campos estão vazios
        if(txt_nome.getText().isEmpty() || txt_cpf.getText().isEmpty() || txt_nacional.getText().isEmpty() ||
                txt_data_nasc.getValue() == null || txt_senha.getText().isEmpty() || txt_rep_senha.getText().isEmpty()){
            App.alerta(AlertType.WARNING, "Aviso", "Preencha todos os campos obrigatórios");
            return false;
        }
        else if(clienteValidacao != null){
            App.alerta(AlertType.WARNING, "Aviso", "CPF já cadastrado.");
            return false;
        }
        //checa se o cep está no tamanho certo
        else if (txt_cep.getText().strip().replace(" ", "").length() != 9) {
            App.alerta(AlertType.WARNING, "Aviso", "Insira um CEP válido. (EX: 00001-001");
            return false;
        }
        //checa se o telefone está no tamanho certo
        else if (txt_telefone.getText().strip().replace(" ", "").length() != 14){
            App.alerta(AlertType.WARNING, "Aviso", "Insira um telefone válido.");
            return false;
        }
        //checa se o cpf está no tamanho certo
        else if (txt_cpf.getText().strip().replace(" ", "").length() != 14){
            App.alerta(AlertType.WARNING, "Aviso", "Insira um CPF válido!");
            return false;
        }
        //checa se o campo senha e repetir senha são iguais
        else if (!txt_senha.getText().equals(txt_rep_senha.getText())){
            App.alerta(AlertType.WARNING, "Aviso", "As senhas não coincidem. Tente novamente");
            return false;
        }
        //valida se o usuário inseriu corretamente a sigla do estado
        else if (txt_estado.getText().length() > 2){
            App.alerta(AlertType.WARNING, "Aviso", "Valor inválido para estado, máximo de 2 caracteres (Exemplo: SP)");
            return false;
        }
        //valida se a data inserida é válida
        else if(txt_data_nasc.getValue().isAfter(dataAtual)){
            App.alerta(AlertType.WARNING, "Aviso", "Insira uma data válida");
            return false;
        }
        //valida se o usuário é maior de 16 anos de idade (regra de negócio)
        else if (Period.between(txt_data_nasc.getValue(), dataAtual).getYears() < 16){
            App.alerta(AlertType.WARNING, "Aviso", "Não é permitido a criação de contas para pessoas menores de 16 anos de idade");
            return false;
        }
            
        return true;
    }
    
    private void limparCampos(){
        
    }

    @FXML
    private void voltarLogin(MouseEvent event) throws IOException {
        App.setRoot("view/Login");
    }

    @FXML
    private void telefoneKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        //define o esqueleto da máscara
        tff.setMask("(##)#####-####");
        //define os caracteres permitidos
        tff.setCaracteresValidos("0123456789");
        //insere a mascara no txt
        tff.setTf(txt_telefone);
        //formata o txt
        tff.formatter();
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

    @FXML
    private void cepKeyReleased(KeyEvent event) {
        TextFieldFormatter tff = new TextFieldFormatter();
        //define o esqueleto da máscara
        tff.setMask("#####-###");
        //define os caracteres permitidos
        tff.setCaracteresValidos("0123456789");
        //insere a mascara no txt
        tff.setTf(txt_cep);
        //formata o txt
        tff.formatter();
    }

    
}
