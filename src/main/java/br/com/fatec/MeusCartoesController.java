/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec;

import br.com.fatec.model.bean.Cartao;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.dao.CartaoDAO;
import br.com.fatec.temp.CartaoTemporario;
import br.com.fatec.temp.CartaoVirtual;
import br.com.fatec.temp.ClienteTemporario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class MeusCartoesController implements Initializable {

    @FXML
    private ImageView btn_criar_cartao;
    @FXML
    private ImageView btn_cartao_v_debito;
    @FXML
    private ImageView btn_cartao_v_credito;
    @FXML
    private FlowPane cartoesPane;
    @FXML
    private ImageView btn_voltarMenu;
    
    private CartaoVirtual cartoesVirtuais = CartaoVirtual.getInstance();
    
    private Cliente clienteTemp = ClienteTemporario.getInstance().getCliente();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cartoesPane.setHgap(10);  // Espaço horizontal entre os cartões
        cartoesPane.setVgap(15);  // Espaço vertical entre os cartões
        cartoesPane.setAlignment(javafx.geometry.Pos.CENTER);  // Alinha o conteúdo no centro

        // Defina um valor fixo para a largura máxima para evitar a barra de rolagem
        double larguraMaxima = 400; // Aumentei a largura do FlowPane para comportar os cartões mais largos
        cartoesPane.setPrefWidth(larguraMaxima); // Ajusta a largura do FlowPane
        cartoesPane.setPrefWrapLength(larguraMaxima); // Impede que a largura ultrapasse o limite
        
        try{
            listarCartoes();
        } catch (SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao buscar os cartões: " + ex);
        }
    }    

    private void criarCartao(ActionEvent event) throws IOException {
        
    }

    @FXML
    private void voltarMenu(MouseEvent event) throws IOException {
        App.setRoot("view/Menu");
    }

    @FXML
    private void addCartao(MouseEvent event) throws IOException {
        App.setRoot("view/criarCartao");
    }

    @FXML
    private void addCartaoVirtualDebito(MouseEvent event) {
        Cartao c = new Cartao();
        c.setNumero(gerarNumeroCartaoVirtual());
        c.setNumeroSeguranca(gerarNumeroSegurancaVirtual());
        c.setId_cliente(clienteTemp.getId());
        c.setTipo("Virtual Débito");
        if(cartoesVirtuais.addCartao(c)){
            adicionarCartaoPainel(c);
        }
        

    }

    @FXML
    private void addCartaoVirtualCredito(MouseEvent event) {
        Cartao c = new Cartao();
        c.setNumero(gerarNumeroCartaoVirtual());
        c.setNumeroSeguranca(gerarNumeroSegurancaVirtual());
        c.setId_cliente(clienteTemp.getId());
        c.setTipo("Virtual Crédito");
        if(cartoesVirtuais.addCartao(c)){
            adicionarCartaoPainel(c);
        }
        
        
    }
    
    private void listarCartoes() throws SQLException{
        CartaoDAO dao = new CartaoDAO();
        List<Cartao> cartoes = dao.listarPorCliente(clienteTemp.getId());
        
        for(Cartao cartao : cartoes){
            adicionarCartaoPainel(cartao);
        }
        
        for(Cartao cartao : cartoesVirtuais.getListaCartoes()){
            adicionarCartaoPainel(cartao);
        }
    }
    
    private void adicionarCartaoPainel(Cartao cartao){
        Pane painelCartao = new Pane();
        painelCartao.setPrefSize(350, 220);  // Aumentei o tamanho do cartão para acomodar as informações

        String estiloBase = "-fx-border-color: black; " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10; " +
                            "-fx-padding: 15; ";
        painelCartao.setStyle(estiloBase + (validacaoCartaoVirtual(cartao) ? 
                        "-fx-background-color: #409015;" : 
                        "-fx-background-color: #83E509;"));

        // Adiciona o texto ao cartão
        Label textoCartao = new Label();
        textoCartao.setText(validacaoCartaoVirtual(cartao) ? cartao.getTipo() : "Cartão Físico");
        textoCartao.setStyle("-fx-text-fill: white;-fx-font-size: 20;");

        // Centraliza o texto no topo do cartão
        textoCartao.setLayoutX(100);  // Posição horizontal do texto
        textoCartao.setLayoutY(10);  // Posição vertical no topo
        textoCartao.setPrefWidth(150); // Largura do texto para centralização
        textoCartao.setAlignment(javafx.geometry.Pos.CENTER);  // Centraliza o texto no Label
        painelCartao.getChildren().add(textoCartao);  // Adiciona o texto no cartão

        // Número do cartão - Integração com o banco de dados 
        String numero = cartao.getNumero();  // Aqui você pegaria esse valor do banco de dados
        Label numeroCartao = new Label();
        numeroCartao.setText("Número: " + numero);  
        numeroCartao.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        numeroCartao.setLayoutX(10);
        numeroCartao.setLayoutY(40);
        painelCartao.getChildren().add(numeroCartao);

        // Nome do titular - Integração com o banco de dados
        String nome = clienteTemp.getNome();  // Aqui você pegaria esse valor do banco de dados
        Label nomeTitular = new Label();
        nomeTitular.setText("Titular: " + nome);  
        nomeTitular.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
        nomeTitular.setLayoutX(10);
        nomeTitular.setLayoutY(70);
        painelCartao.getChildren().add(nomeTitular);

        // Data de validade - Integração com o banco de dados
        if(!validacaoCartaoVirtual(cartao)){
            String validadeData = cartao.getDataValidade();  // Aqui você pegaria esse valor do banco de dados
            Label validade = new Label();
            validade.setText("Validade: " + validadeData);  
            validade.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            validade.setLayoutX(10);
            validade.setLayoutY(100);
            painelCartao.getChildren().add(validade);
        } else {
            String codigo = cartao.getNumeroSeguranca();  // Aqui você pegaria esse valor do banco de dados
            Label codigoLabel = new Label();
            codigoLabel.setText("Número de segurança: " + codigo);  
            codigoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            codigoLabel.setLayoutX(10);
            codigoLabel.setLayoutY(100);
            painelCartao.getChildren().add(codigoLabel);
        }

        // Botão "Visualizar" dentro do cartão - Centralizado
        if(!validacaoCartaoVirtual(cartao)){
            Button btnVisualizar = new Button();
            ImageView imageView = new ImageView(getClass().getResource("/verCartao.png").toExternalForm());
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            btnVisualizar.setGraphic(imageView);

            btnVisualizar.setLayoutX(painelCartao.getPrefWidth() / 2 - 10);  // Centraliza o botão horizontalmente
            btnVisualizar.setLayoutY(150);  // Posição vertical do botão
            btnVisualizar.setOnAction(e -> DadosCartao(cartao));
            
            // Adicionar botão ao Pane
            painelCartao.getChildren().add(btnVisualizar);
        }
        

       

        // Adicionar o cartão ao FlowPane
        cartoesPane.getChildren().add(painelCartao);
    }
    
    private void DadosCartao(Cartao cartao) {
        try {
            // Navega para a tela de dados
            CartaoTemporario cartaoTemp = CartaoTemporario.getInstance();
            cartaoTemp.setCartao(cartao);

            App.setRoot("view/DadosCartao");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private String gerarNumeroCartaoVirtual(){
        Random random = new Random();
        int numero1 = random.nextInt(9999);
        int numero2 = random.nextInt(9999);
        int numero3 = random.nextInt(9999);
        int numero4 = random.nextInt(9999);
        return String.format("%04d %04d %04d %04d", numero1, numero2, numero3, numero4);
    }
    
    private String gerarNumeroSegurancaVirtual(){
        Random random = new Random();
        int numero = random.nextInt(999);
        
        return String.format("%03d", numero);
    }
    
    private boolean validacaoCartaoVirtual(Cartao c){
        if(c.getTipo().equals("Virtual Crédito") || c.getTipo().equals("Virtual Débito")){
            return true;
        }
        return false;
    }
}
    

