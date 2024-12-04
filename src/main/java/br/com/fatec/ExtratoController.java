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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class ExtratoController implements Initializable {

    @FXML
    private ComboBox<String> cmb_filtro_tipos;
    @FXML
    private TextField txt_filtro;
    @FXML
    private TableView<Transacao> tvw_extrato;
    @FXML
    private ImageView btn_filtro;
    @FXML
    private ImageView btn_voltarMenu;
    
    TransacaoDAO transacaoDAO = new TransacaoDAO();
    Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();
    
    private ObservableList<Transacao> listaTransacao = FXCollections.observableArrayList(transacaoDAO.listarPorId(clienteTemp.getId()));
    @FXML
    private Button btn_estornar;
    @FXML
    private Button btn_alterar;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adicionarFiltros();
        
        //inserção da lista de transações no tableview
        TableColumn<Transacao, Integer> id = new TableColumn("Número");
        id.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        
        TableColumn<Transacao, String> tipo = new TableColumn("Tipo");
        tipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));
        
        TableColumn<Transacao, String> tipoConta = new TableColumn("Conta");
        tipoConta.setCellValueFactory(data -> {
            //utiliza a coluna id da conta para buscar a conta em si e retornar o tipo dela, se é 1, exibe conta corrente, se 2, exibe conta poupança.
            ContaDAO contaDAO = new ContaDAO();
            try {
                //busca a conta pelo id do objeto Conta retornado pelo DAO
                Conta c = contaDAO.buscaPorId(data.getValue().getDestinatario().getId());
                //caso nao encontre, retorna null para evitar erros ao tentar dar getTipo
                if (c != null){
                    //troca de valores para melhorar a exibição
                    if(c.getTipo() == 1){
                    return new SimpleStringProperty("Conta Corrente");
                    } else {
                        return new SimpleStringProperty("Conta Poupança");
                    }
                } else {
                    return null;
                }
            } catch (SQLException ex) {
                App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao buscar cliente: " + ex);
                return null;
            }
        });
        
        TableColumn<Transacao, String> contato = new TableColumn("Contato");
        contato.setCellValueFactory(data -> {
            //utiliza a coluna id_cliente da tabela conta para buscar o objeto cliente correspondente, por fim retorna o nome dele
            ClienteDAO clienteDAO = new ClienteDAO();
            try {
                Cliente destinatario = clienteDAO.buscaPorId(data.getValue().getDestinatario().getIdCliente());
                Cliente remetente = clienteDAO.buscaPorId(data.getValue().getRemetente().getIdCliente());
                if(clienteTemp.getId() == destinatario.getId()){
                    return new SimpleStringProperty(remetente.getNome());
                } else {
                    return new SimpleStringProperty(destinatario.getNome());
                }
            } catch (SQLException ex) {
                App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao buscar cliente: " + ex);
                return null;
            }
        });
        
        TableColumn<Transacao, String> valor = new TableColumn("Valor");
        valor.setCellValueFactory(data -> {
            //verifica se você enviou ou recebeu dinheiro, caso tenha recebido, aparece valor positivo, caso contrario, valor negativo
            if(clienteTemp.getId() == data.getValue().getDestinatario().getIdCliente()){
                return new SimpleStringProperty(String.format("R$ %.2f", data.getValue().getValor()));
            } else {
                return new SimpleStringProperty(String.format("R$ -%.2f", data.getValue().getValor()));
            }
        });
        
        
        TableColumn<Transacao, String> dataTransf = new TableColumn("Data");
        dataTransf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));
        
        TableColumn<Transacao, String> hora = new TableColumn("Hora");
        hora.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHora() != null? data.getValue().getHora().toString() : ""));
        
        tvw_extrato.getColumns().addAll(id, tipo, tipoConta, contato, valor, dataTransf, hora);
        tvw_extrato.setItems(listaTransacao);
        
        
        
    }    

    @FXML
    private void voltarMenu(MouseEvent event) throws IOException {   
        App.setRoot("view/Menu");
    }
    
    private void adicionarFiltros(){
        ObservableList<String> tipos = FXCollections.observableArrayList();
        tipos.add("Número");
        tipos.add("Ano");
        tipos.add("Mês");
        tipos.add("Dia");
        cmb_filtro_tipos.setItems(tipos);
    }

    @FXML
    private void estornarValor(ActionEvent event) throws IOException {
        
        Transacao t = tvw_extrato.getSelectionModel().getSelectedItem();
        if(t != null){
            // só permite o estorno se foi o usuário quem realizou a transação
            if(clienteTemp.getId() == t.getRemetente().getIdCliente()){
                try{
                    ClienteDAO clienteDAO = new ClienteDAO();
                    ContaDAO contaDAO = new ContaDAO();
                    String nomeDestinatario = clienteDAO.buscaPorId(t.getDestinatario().getIdCliente()).getNome();
                    if(alertaPergunta(String.format("Deseja realmente retornar o valor de R$%.2f realizado para %s?", t.getValor(), nomeDestinatario))){
                        //adiciona de volta na conta do remetente o valor enviado por ele
                        Conta remetente =  t.getRemetente();
                        Conta destinatario =  t.getDestinatario();
                        remetente.setSaldo(t.getRemetente().getSaldo() + t.getValor());
                        destinatario.setSaldo(t.getDestinatario().getSaldo() - t.getValor());
                        //atualiza o saldo das contas no banco de dados
                        contaDAO.alterar(remetente);
                        contaDAO.alterar(destinatario);
                        
                        transacaoDAO.remover(t);
                        App.alerta(AlertType.CONFIRMATION, "Sucesso", "Valor estornado com sucesso!");
                        //atualiza o tableview
                        listaTransacao = FXCollections.observableArrayList(transacaoDAO.listarPorId(clienteTemp.getId()));
                        tvw_extrato.setItems(listaTransacao);
                    }
                } catch (SQLException ex){
                    App.alerta(AlertType.ERROR, "Erro", "Ocorreu um erro ao realizar o estorno do valor");
                }
                
            } else {
                App.alerta(AlertType.WARNING, "Aviso", "Só é possivel estornar valores que você enviou");
            }
            
        } else {
            App.alerta(AlertType.WARNING, "Aviso", "Escolha uma linha da tabela para realizar o estorno");
        }
    }

    
    private boolean alertaPergunta(String header){
        Alert alert = new Alert(AlertType.INFORMATION, null, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Aviso");
        alert.setHeaderText(header);
        Optional<ButtonType> resultado = alert.showAndWait();
        if(resultado.get() == ButtonType.YES){
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void realizarFiltro(MouseEvent event) {
        ObservableList<Transacao> listaFiltrada = FXCollections.observableArrayList();
        listaTransacao = FXCollections.observableArrayList(transacaoDAO.listarPorId(clienteTemp.getId()));
        //se o texto de filtragem está vazio, ele retorna a lista completa
        if(txt_filtro.getText().isEmpty()){
            tvw_extrato.setItems(listaTransacao);
            return;
        }
        //aviso caso 
        else if(cmb_filtro_tipos.getValue() ==  null){
            App.alerta(Alert.AlertType.WARNING, "Aviso", "Selecione um filtro");
        }
        else{
            //verifica se o usuário inseriu um número válido
            try{
                //filtra por número da transação, utiliza o metodo filter do streamAPI para o filtro deixar apenas os correspondentes
                if(cmb_filtro_tipos.getValue().equals("Número")){
                    
                    listaFiltrada = listaTransacao.stream()
                        .filter(t -> t.getId() == Integer.parseInt(txt_filtro.getText()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));  
                }
                //filtra por ano da transação, utiliza o metodo filter do streamAPI para o filtro deixar apenas os correspondentes
                else if(cmb_filtro_tipos.getValue().equals("Ano")){
                    listaFiltrada = listaTransacao.stream()
                            .filter(t -> {
                                Calendar cal = Calendar.getInstance(Locale.getDefault());
                                cal.setTime(t.getData());

                                return cal.get(Calendar.YEAR) == Integer.parseInt(txt_filtro.getText());
                            })
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
                }
                //filtra por mês da transação, utiliza o metodo filter do streamAPI para o filtro deixar apenas os correspondentes
                else if(cmb_filtro_tipos.getValue().equals("Mês")){
                    listaFiltrada = listaTransacao.stream()
                            .filter(t -> {
                                Calendar cal =Calendar.getInstance(Locale.getDefault());
                                cal.setTime(t.getData());
                                //+1 pois o .MONTH retorna de 0 a 11
                                return cal.get(Calendar.MONTH) + 1 == Integer.parseInt(txt_filtro.getText());
                            })
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
                }
                //filtra por dia da transação, utiliza o metodo filter do streamAPI para o filtro deixar apenas os correspondentes
                else if(cmb_filtro_tipos.getValue().equals("Dia")){
                    listaFiltrada = listaTransacao.stream()
                            .filter(t -> {
                                Calendar cal =Calendar.getInstance(Locale.getDefault());
                                cal.setTime(t.getData());
                                        
                                return cal.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(txt_filtro.getText());
                            })
                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
                }
            } catch(NumberFormatException ex){
                App.alerta(Alert.AlertType.ERROR, "Erro", "Digite um número válido");
            }
            //armazena a lista filtrada na lista original
            listaTransacao = listaFiltrada;
        }
        //reinserção da lista no tableview
        tvw_extrato.setItems(listaTransacao);
    }

    @FXML
    private void alterarData(ActionEvent event) {
        Transacao t = tvw_extrato.getSelectionModel().getSelectedItem();
        
        if(t != null){
            //só permite alterar a data se o usuario é o remetente
            if(t.getRemetente().getIdCliente() == clienteTemp.getId()){
                //construção do pop up para trocar a data
                DatePicker datePicker = new DatePicker();
                datePicker.setEditable(false);
                VBox popUp = new VBox(10, datePicker);
                popUp.setAlignment(Pos.CENTER);
                popUp.setStyle("-fx-padding: 10;");
                Alert alertaPopUp = new Alert(Alert.AlertType.CONFIRMATION);
                alertaPopUp.setTitle("Alterar Data");
                alertaPopUp.setHeaderText("Selecione a nova data:");
                alertaPopUp.getDialogPane().setContent(popUp);

                Optional<ButtonType> resultado = alertaPopUp.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    //só realiza a troca para dias futuros
                    LocalDate dataHoje = LocalDate.now();
                    if (datePicker.getValue() != null && datePicker.getValue().isAfter(dataHoje)) {
                        try {
                            //atualiza a data no banco de dados
                            t.setData(Date.valueOf(datePicker.getValue()));
                            transacaoDAO.alterar(t);

                            // Atualizar a TableView
                            listaTransacao = FXCollections.observableArrayList(transacaoDAO.listarPorId(clienteTemp.getId()));
                            tvw_extrato.setItems(listaTransacao);

                            App.alerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Data da transação alterada com sucesso!");
                        } catch (SQLException ex) {
                            App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao alterar a data: " + ex.getMessage());
                        }
                    } else {
                        App.alerta(AlertType.WARNING, "Aviso", "Insira uma data válida");
                    }
                    
                }
            } else {
                App.alerta(AlertType.WARNING, "Aviso", "Só é permitido alteração da data de transações que você realizou");
            }
                
        } else {
            App.alerta(AlertType.WARNING, "Aviso", "Escolha uma linha da tabela para realizar a alteração da data");
        }
       
    }
}
