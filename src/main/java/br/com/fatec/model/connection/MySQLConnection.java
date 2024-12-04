/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.connection;

import br.com.fatec.App;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author Gabriel
 */
public class MySQLConnection {
    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/banco_n2";
    private static final String USER = "root";
    private static final String PASS = "";
    
    
    public static Connection connect() {
        try{
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e){
            App.alerta(Alert.AlertType.ERROR, "ERRO CRÍTICO", "Os servidores estão fora do ar, tente novamente mais tarde");
            System.exit(0);
        }
        return null;
    }
    
    public static void closeConnection(Connection connection) {
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e);
        }
    }
    
    public static void closeConnection(Connection connection, PreparedStatement stmt) {
        
        closeConnection(connection);
        
        try{
            if(stmt != null){
                stmt.close();
            }
        } catch (SQLException e) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e);
        }
    }
    
    public static void closeConnection(Connection connection, PreparedStatement stmt, ResultSet rs) {
        
        closeConnection(connection, stmt);
        
        try{
            if(rs != null){
                rs.close();
            }
        } catch (SQLException e) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e);
        }
    }
}
