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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class PixController implements Initializable {

    @FXML
    private AnchorPane tela_contatos;
    @FXML
    private ComboBox<Cliente> cmb_contatos;
    @FXML
    private Button btn_outros_contatos;
    @FXML
    private AnchorPane tela_pix;
    @FXML
    private TextField txt_cpf;
    @FXML
    private TextField txt_valor;
    @FXML
    private ComboBox<String> cmb_tipo;
    @FXML
    private Button btn_confirmar;
    
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    
    private Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();
    @FXML
    private ImageView lbl_voltar;
    @FXML
    private ImageView lbl_voltar1;
    @FXML
    private ImageView btn_contatos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //definição padrão das telas
        tela_contatos.setVisible(true);
        tela_pix.setVisible(false);
        
        //Preenchimento dinâmico do combobox de contatos
        ClienteDAO dao = new ClienteDAO();
        //instância do cliente logado para verificação
        listaClientes.addAll(dao.listar());
        // é necessário formatar a lista para garantir que o usuário atual não aparecerá na lista de contatos
        ObservableList<Cliente> listaFormatada = FXCollections.observableArrayList(listaClientes.stream().filter(c -> c.getId() != clienteTemp.getId()).collect(Collectors.toList()));
        cmb_contatos.setItems(listaFormatada);
        
        //Preenchimento estático do combobox de tipos de conta
        // regra de negócio: pode fazer pix de uma conta corrente ou poupança mas é apenas possivel enviar para uma conta corrente
        ObservableList<String> listaContas = FXCollections.observableArrayList();
        listaContas.add("Conta Corrente");
        listaContas.add("Conta Poupança");
        cmb_tipo.setItems(listaContas);
    }    

    @FXML
    private void pixManual(ActionEvent event) {
        tela_pix.setVisible(true);
        tela_contatos.setVisible(false);
    }

    @FXML
    private void voltarMenu(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText("Deseja realmente voltar ao menu?");
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            App.setRoot("view/Menu");
        }
    }

    @FXML
    private void realizarPix(ActionEvent event) {
        ContaDAO contaDAO = new ContaDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        
        Conta contaRemet;
        
        if(cmb_tipo.getValue() == "Conta Corrente"){
            contaRemet = contaDAO.buscaPorIdUsuario(clienteTemp.getId(), 1);
        } else {
            contaRemet = contaDAO.buscaPorIdUsuario(clienteTemp.getId(), 2);
        }
        //armazena o cliente para buscar o id dele
        Cliente clienteDest = clienteDAO.listarPorCpf(txt_cpf.getText());
        //pix será feito apenas para conta corrente (regra de negócio)
        Conta contaDest;
        if(clienteDest != null){
            contaDest = contaDAO.buscaPorIdUsuario(clienteDest.getId(), 1);
        } else {
            contaDest = null;
        }
        
        if(validacaoTransferencia(contaRemet, contaDest)){
            Transacao t = new Transacao();
            TransacaoDAO transacaoDAO = new TransacaoDAO();
            
            try{
                t.setTipo("Pix");
                t.setValor(Double.parseDouble(txt_valor.getText()));
                
                //não é possivel agendar pix, é enviado na hora.
                LocalDate data = LocalDate.now();
                Date dataFormatada = Date.valueOf(data);
                LocalTime hora = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
                Time horaFormatada = (Time.valueOf(hora));                   
                t.setData(dataFormatada);
                t.setHora(horaFormatada);
                
                t.setRemetente(contaRemet);
                t.setDestinatario(contaDest);
                
                //inserção no banco de dados
                transacaoDAO.inserir(t);
                //atualização do saldo
                contaRemet.setSaldo(contaRemet.getSaldo() - t.getValor());
                contaDest.setSaldo(contaDest.getSaldo() + t.getValor());
                contaDAO.alterar(contaRemet);
                contaDAO.alterar(contaDest);
                
                App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Transação realizada com sucesso");
                App.setRoot("view/Menu");
            } catch (Exception ex){
                App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao realizar transferência: " + ex);
            }
        }
    }
    
    private boolean validacaoTransferencia(Conta contaRemet, Conta contaDest){
        //retorna false caso haja algum campo vazio
        if(cmb_tipo.getValue() == null || txt_valor.getText().isEmpty() || txt_cpf.getText().isEmpty()){
            App.alerta(Alert.AlertType.WARNING, "AVISO", "Preencha todos os campos!");
            return false;
        }
        if(contaDest == null){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "A chave pix inserida não existe.");
            return false;
        }
        //valida se os campos numéricos estão sendo preenchidos corretamente
        try{
            Double valor = Double.parseDouble(txt_valor.getText());
            //nao retorna true aqui pois não testará as outras condições, e o true já será retornado no fim caso esteja correto
        } catch (NumberFormatException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Insira apenas valores numéricos");
            return false;
        }
        //retorna false caso tente enviar dinheiro para uma conta que não existe
        if(contaDest == null ){
            App.alerta(Alert.AlertType.ERROR, "Erro", "A conta do destinatário informado não existe");
            return false;
        }
        //retorna false caso esteja tentando enviar para a mesma conta
        else if(contaRemet.getId() == contaDest.getId()){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Não é possivel fazer transação para a mesma conta!");
            return false;
        }
        //retorna false caso o saldo da conta seja menor do que a quantidade enviada na transação
        else if(Double.parseDouble(txt_valor.getText()) > contaRemet.getSaldo()){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Saldo insuficiente");
            return false;
        //retorna false caso tente enviar um valor negativo
        } else if (Double.parseDouble(txt_valor.getText()) < 0){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Não são permitidos valores negativos");
            return false;
        }
        return true;
    }

    @FXML
    private void pixContato(MouseEvent event) {
        if(cmb_contatos.getValue() != null){
            try{
                ContaDAO contaDAO = new ContaDAO();
                Conta conta = contaDAO.buscaPorIdUsuario(cmb_contatos.getValue().getId(), 1);
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente cliente = clienteDAO.buscaPorId(conta.getIdCliente());

                txt_cpf.setText(cliente.getCpf());
                //bloqueia a alteração dos campos de texto para o usuário não alterar
                txt_cpf.setEditable(false);

                //troca de telas
                tela_pix.setVisible(true);
                tela_contatos.setVisible(false);
            } catch (SQLException ex){
                App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao buscar contato: " + ex);
            } catch (Exception ex){
                 App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao buscar contato: " + ex);
            }
            
        } else {
            App.alerta(Alert.AlertType.ERROR, "ERRO", "Preencha todos os campos de contato.");
        }
    }
    
}
