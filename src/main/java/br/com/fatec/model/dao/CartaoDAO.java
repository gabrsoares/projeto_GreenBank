/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.dao;

import br.com.fatec.App;
import br.com.fatec.model.bean.Cartao;
import br.com.fatec.model.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.control.Alert;

/**
 *
 * @author Gabriel
 */
public class CartaoDAO implements DAO<Cartao>{

    @Override
    public boolean inserir(Cartao c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO cartoes (tipo, numero, id_cliente,"
                    + "numero_seguranca, data_validade, senha) values(?,?,?,?,?,?)");
            stmt.setString(1, c.getTipo());
            stmt.setString(2, c.getNumero());
            stmt.setInt(3, c.getId_cliente());
            stmt.setString(4, c.getNumeroSeguranca());
            stmt.setString(5, c.getDataValidade());
            stmt.setString(6, c.getSenha());
            
            stmt.executeUpdate();
            
            return true;
            
        } catch (SQLException ex){
            System.out.println(ex);
            return false;
            
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean remover(Cartao c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        
        try{
            stmt = connection.prepareStatement("DELETE FROM cartoes WHERE id = ?");
            stmt.setInt(1, c.getId());
            stmt.executeUpdate();
            
            return true;
        } catch (SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro na base de dados");
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean alterar(Cartao c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try{
            stmt = connection.prepareStatement("UPDATE cartoes SET data_validade = ? where id = ?");
            stmt.setString(1, c.getDataValidade());
            stmt.setInt(2, c.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao atualizar os dados do cartão");
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public Collection<Cartao> listar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public List<Cartao> listarPorCliente(int id){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cartao> listaCartoesCliente = new ArrayList<>();
        try{
            
            stmt = connection.prepareStatement("SELECT * FROM cartoes where id_cliente = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Cartao c = new Cartao();
                c.setId(rs.getInt("id"));
                c.setTipo(rs.getString("tipo"));
                c.setNumero(rs.getString("numero"));
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setNumeroSeguranca(rs.getString("numero_seguranca"));
                c.setDataValidade(rs.getString("data_validade"));
                c.setSenha(rs.getString("senha"));
                
                listaCartoesCliente.add(c);  
            }
        } catch (SQLException ex){
            System.out.println("Ocorreu um erro ao buscar os cartões: " + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return listaCartoesCliente;
    }

    @Override
    public Cartao buscaPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
