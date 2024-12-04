/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Agencia;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.bean.Conta;
import br.com.fatec.model.dao.AgenciaDAO;
import br.com.fatec.model.dao.ClienteDAO;
import br.com.fatec.model.dao.ContaDAO;
import br.com.fatec.temp.ClienteTemporario;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class AgenciasController implements Initializable {

    @FXML
    private ComboBox<Integer> cmb_num_agencias;
    @FXML
    private TableView<Agencia> tvw_agencias;
    
    AgenciaDAO dao = new AgenciaDAO();
    private ObservableList<Agencia> listaAgencias = FXCollections.observableArrayList(dao.listar());
    //transforma o retorno do objeto inteiro em apenas o numero da agencia dele, o collect é outra forma de fazer um toList porque essa versão
    //do javafx não suporta o toList()
    private ObservableList<Integer> listaIdAgencias = FXCollections.observableArrayList(listaAgencias.stream().map(Agencia::getNumAgencia).collect(Collectors.toList()));
    @FXML
    private Button btn_confirmar;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmb_num_agencias.setItems(listaIdAgencias);

        TableColumn<Agencia,Integer> numAgencia = new TableColumn("Número");
        numAgencia.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumAgencia()).asObject());
        
        TableColumn<Agencia,String> nome = new TableColumn("Nome Agência");
        nome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        
        TableColumn<Agencia,String> endereco = new TableColumn("Endereço");
        endereco.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndereco()));
        
        TableColumn<Agencia,String> cep = new TableColumn("CEP");
        cep.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCep()));
        
        TableColumn<Agencia,String> cidade = new TableColumn("Cidade");
        cidade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCidade()));
        
        TableColumn<Agencia,String> estado = new TableColumn("Estado");
        estado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));
        
        TableColumn<Agencia,String> telefone = new TableColumn("Telefone");
        telefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));
        
        TableColumn<Agencia,String> gerente = new TableColumn("Gerente respons.");
        gerente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGerente()));
        
        tvw_agencias.getColumns().addAll(numAgencia, nome, endereco, cep, cidade, estado, telefone, gerente);
        
        tvw_agencias.setItems(listaAgencias);
        
    }    

    @FXML
    private void cadastrarConta(ActionEvent event) {
        if(cmb_num_agencias.getValue() == null){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Escolha uma agência.");
        } else {
            Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();
            ContaDAO contaDAO = new ContaDAO();
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = null;
            Conta conta = new Conta();
            try{
                //aqui realiza o cadastro do cliente após escolher a agência               
                clienteDAO.inserir(clienteTemp);
                //atualiza o objeto cliente do clienteTemp para o valor que foi inserido no banco de dados.
                clienteTemp = clienteDAO.listarPorCpf(clienteTemp.getCpf());
                
                conta.setAgencia(cmb_num_agencias.getValue());
                conta.setIdCliente(clienteTemp.getId());
                contaDAO.inserir(conta);
                App.alerta(AlertType.CONFIRMATION, "Sucesso", "Agência cadastrada com sucesso!");
                App.setRoot("view/Login");
            
            } catch (Exception e){
                App.alerta(AlertType.ERROR, "Erro", "Erro ao cadastrar conta: " + e);
            }
            
        }
    }
    
}
