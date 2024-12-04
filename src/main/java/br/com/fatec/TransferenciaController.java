/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.bean.Conta;
import br.com.fatec.model.bean.Transacao;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.model.dao.ContaDAO;
import br.com.fatec.model.dao.TransacaoDAO;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class TransferenciaController implements Initializable {

    @FXML
    private AnchorPane tela_contatos;
    @FXML
    private ComboBox<Cliente> cmb_contatos;
    @FXML
    private Button btn_outros_contatos;
    @FXML
    private AnchorPane tela_transacao;
    @FXML
    private TextField txt_numero;
    @FXML
    private TextField txt_agencia;
    @FXML
    private TextField txt_valor;
    @FXML
    private ComboBox<String> cmb_tipo;
    @FXML
    private DatePicker txt_data;
    @FXML
    private Button btn_transferencia;
    @FXML
    private ComboBox<String> cmb_contas_contato;
    @FXML
    private ImageView btn_continuar;
    @FXML
    private CheckBox chb_agora;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    
    private Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //definição padrão das telas
        tela_contatos.setVisible(true);
        tela_transacao.setVisible(false);
        
        //Preenchimento dinâmico do combobox de contatos
        ClienteDAO dao = new ClienteDAO();
        //instância do cliente logado para verificação
        listaClientes.addAll(dao.listar());
        // é necessário formatar a lista para garantir que o usuário atual não aparecerá na lista de contatos
        ObservableList<Cliente> listaFormatada = FXCollections.observableArrayList(listaClientes.stream().filter(c -> c.getId() != clienteTemp.getId()).collect(Collectors.toList()));
        cmb_contatos.setItems(listaFormatada);
        
        //Preenchimento estático do combobox de tipos de conta
        ObservableList<String> listaContas = FXCollections.observableArrayList();
        listaContas.add("Conta Corrente");
        listaContas.add("Conta Poupança");
        cmb_tipo.setItems(listaContas);
        cmb_contas_contato.setItems(listaContas);
    }
    
    //monta a transferência com inserção manual de dados.
    @FXML
    private void transferenciaManual(ActionEvent event) {
        tela_transacao.setVisible(true);
        tela_contatos.setVisible(false);
    }

    @FXML
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
    private void realizarTransferencia(ActionEvent event) throws SQLException {
        //validação de campos numéricos é feita antes para tratar o usuario enviado
        if(validacaoCamposNumericos()){
            ContaDAO contaDAO = new ContaDAO();
            Conta contaRemet;

            if(cmb_tipo.getValue() == "Conta Corrente"){
                contaRemet = contaDAO.buscaPorIdUsuario(clienteTemp.getId(), 1);
            } else {
                contaRemet = contaDAO.buscaPorIdUsuario(clienteTemp.getId(), 2);
            }
            Conta contaDest = contaDAO.buscaPorId(Integer.parseInt(txt_numero.getText()));

            if(validacaoTransferencia(contaRemet, contaDest)){
                Transacao t = new Transacao();
                TransacaoDAO transacaoDAO = new TransacaoDAO();

                try{
                    t.setTipo("Transferência");
                    t.setValor(Double.parseDouble(txt_valor.getText()));
                    //checa caso o checkbox esteja marcado, pegará a data atual, caso contrário, pegará a data agendada
                    if(chb_agora.isSelected()){
                        LocalDate data = LocalDate.now();
                        Date dataFormatada = Date.valueOf(data);
                        LocalTime hora = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
                        Time horaFormatada = (Time.valueOf(hora));

                        t.setData(dataFormatada);
                        t.setHora(horaFormatada);
                    } else {
                        t.setData(Date.valueOf(txt_data.getValue()));
                        t.setHora(null);
                    }
                    t.setRemetente(contaRemet);
                    t.setDestinatario(contaDest);

                    //inserção no banco de dados
                    transacaoDAO.inserir(t);
                    //atualização do saldo
                    contaRemet.setSaldo(contaRemet.getSaldo() - t.getValor());
                    contaDest.setSaldo(contaDest.getSaldo() + t.getValor());
                    contaDAO.alterar(contaRemet);
                    contaDAO.alterar(contaDest);

                    App.alerta(AlertType.CONFIRMATION, "Sucesso", "Transação realizada com sucesso");
                    App.setRoot("view/Menu");
                } catch (NumberFormatException ex){
                    App.alerta(AlertType.ERROR, "Erro", "Erro no realizarTransferencia");
                } catch (Exception ex){
                    App.alerta(AlertType.ERROR, "Erro", "Erro ao realizar transferência: " + ex);
                }
            }
        }
        
        
    }
    
    private boolean validacaoTransferencia(Conta contaRemet, Conta contaDest){
        //retorna false caso haja algum campo vazio
        LocalDate dataAtual = LocalDate.now();
        if(cmb_tipo.getValue() == null || txt_numero.getText().isEmpty() || txt_agencia.getText().isEmpty()
                || txt_valor.getText().isEmpty() || (txt_data.getValue() == null && !chb_agora.isSelected())){
            App.alerta(AlertType.WARNING, "AVISO", "Preencha todos os campos!");
            return false;
        }
        //retorna false caso tente enviar dinheiro para uma conta que não existe
        if(contaDest == null ){
            App.alerta(AlertType.ERROR, "Erro", "A conta do destinatário informado não existe");
            return false;
        }
        //retorna false caso esteja tentando enviar para a mesma conta
        else if(contaRemet.getId() == contaDest.getId()){
            App.alerta(AlertType.ERROR, "Erro", "Não é possivel fazer transação para a mesma conta!");
            return false;
        }
        //retorna false caso o saldo da conta seja menor do que a quantidade enviada na transação
        else if(Double.parseDouble(txt_valor.getText()) > contaRemet.getSaldo()){
            App.alerta(AlertType.WARNING, "Aviso", "Saldo insuficiente");
            return false;
        //retorna false caso tente enviar um valor negativo
        } else if (Double.parseDouble(txt_valor.getText()) < 0){
            App.alerta(AlertType.WARNING, "Aviso", "Não são permitidos valores negativos");
            return false;
        //verifica se a data agendada é antes de hoje, antes verifica se não está vazia para não dar erro ao executar o .isBefore()
        } else if (txt_data.getValue() != null && !chb_agora.isSelected()){
            if (txt_data.getValue().isBefore(dataAtual)){
                App.alerta(AlertType.WARNING, "Aviso", "Insira uma data válida");
                return false;
            }
        }
            
        return true;
    }
    //valida se os campos numéricos estão sendo preenchidos corretamente
    private boolean validacaoCamposNumericos(){
       
        try{
            Double valor1 = Double.parseDouble(txt_valor.getText());
            int valor2 = Integer.parseInt(txt_numero.getText());
            int valor3 = Integer.parseInt(txt_agencia.getText());
            return true;
        } catch (NumberFormatException ex){
            App.alerta(AlertType.ERROR, "Erro", "Insira apenas valores numéricos nos campos de número da conta, agência e valor");
            return false;
        }
    }
    
    //monta a transferência através de um contato
    @FXML
    private void transferenciaContato(MouseEvent event) {
        if(cmb_contatos.getValue() != null && cmb_contas_contato.getValue() != null){
            ContaDAO dao = new ContaDAO();
            Conta c;
            //no banco de dados, o tipo 1 é para conta corrente e o tipo 2 é para conta poupança
            if(cmb_contas_contato.getValue() == "Conta Corrente"){
                c = dao.buscaPorIdUsuario(cmb_contatos.getValue().getId(), 1);
            } else {
                c = dao.buscaPorIdUsuario(cmb_contatos.getValue().getId(), 2);
            }
            
            txt_numero.setText(Integer.toString(c.getId()));
            txt_agencia.setText(Integer.toString(c.getAgencia()));
            
            
            //bloqueia a alteração dos campos de texto para o usuário não alterar
            txt_numero.setEditable(false);
            txt_agencia.setEditable(false);
            
            //troca de telas
            tela_transacao.setVisible(true);
            tela_contatos.setVisible(false);
        } else {
            App.alerta(Alert.AlertType.ERROR, "ERRO", "Preencha todos os campos de contato.");
        }
    }
    
}
