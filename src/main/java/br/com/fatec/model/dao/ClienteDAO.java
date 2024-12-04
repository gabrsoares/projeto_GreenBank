/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.dao;

import br.com.fatec.App;
import br.com.fatec.model.bean.Cliente;
import br.com.fatec.model.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author Gabriel
 */
public class ClienteDAO implements DAO<Cliente>{
    
    @Override
    public boolean inserir(Cliente c){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO clientes (nome,cpf,telefone,email,nacionalidade,data_nasc,endereco,complemento,cidade,estado,cep,senha)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getCpf());
            stmt.setString(3, c.getTelefone());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getNacionalidade());
            stmt.setDate(6, c.getDataNasc());
            stmt.setString(7, c.getEndereco());
            stmt.setString(8, c.getComplemento());
            stmt.setString(9, c.getCidade());
            stmt.setString(10, c.getEstado());
            stmt.setString(11, c.getCep());
            stmt.setString(12, c.getSenha());
            
            stmt.executeUpdate();
            
            return true;
            
        } catch (SQLException ex){
            return false;
            
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
        
    }
    
    @Override
    public List<Cliente> listar(){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cliente> listaClientes = new ArrayList<>();
        try {
            stmt = connection.prepareStatement("SELECT * FROM clientes");
            rs = stmt.executeQuery();
            
            //percore todo o resultado do select até a ultima linha (retorna false)
            while(rs.next()){
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setNacionalidade(rs.getString("nacionalidade"));
                c.setDataNasc(rs.getDate("data_nasc"));
                c.setEndereco(rs.getString("endereco"));
                c.setComplemento(rs.getString("complemento"));
                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setCep(rs.getString("cep"));
                c.setSenha(rs.getString("senha"));
                
                listaClientes.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return listaClientes;

    }
    
    public Cliente listarPorCpf(String cpf){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente c = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM clientes WHERE cpf = ?");
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
            
            //percorre todo o resultado do select até a ultima linha (retorna false)
            if(rs.next()){
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setNacionalidade(rs.getString("nacionalidade"));
                c.setDataNasc(rs.getDate("data_nasc"));
                c.setEndereco(rs.getString("endereco"));
                c.setComplemento(rs.getString("complemento"));
                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setCep(rs.getString("cep"));
                c.setSenha(rs.getString("senha"));
                
            }
        } catch (SQLException ex) {
            System.out.println("Erro na listagem por cpf" + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return c;

    }

    @Override
    public boolean remover(Cliente c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            stmt = connection.prepareStatement("DELETE FROM clientes WHERE id = ?");
            stmt.setInt(1, c.getId());     
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex){
            System.out.println("Erro ao alterar valores da tabela clientes: " + ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean alterar(Cliente c){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            stmt = connection.prepareStatement("UPDATE clientes SET nome = ?, telefone = ?, email = ?,"
                    + "endereco = ?, complemento = ?, cidade = ?, estado = ?, cep = ?, senha = ? WHERE id = ?");
            stmt.setString(1, c.getNome());
            stmt.setString(2, c.getTelefone());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getEndereco());
            stmt.setString(5, c.getComplemento());
            stmt.setString(6, c.getCidade());
            stmt.setString(7, c.getEstado());
            stmt.setString(8, c.getCep());
            stmt.setString(9, c.getSenha());
            stmt.setInt(10, c.getId());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao alterar valores da tabela clientes: " + ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public Cliente buscaPorId(int id) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente c = null;
        try {
            stmt = connection.prepareStatement("SELECT * FROM clientes WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            //percorre todo o resultado do select até a ultima linha (retorna false)
            if(rs.next()){
                c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setTelefone(rs.getString("telefone"));
                c.setEmail(rs.getString("email"));
                c.setNacionalidade(rs.getString("nacionalidade"));
                c.setDataNasc(rs.getDate("data_nasc"));
                c.setEndereco(rs.getString("endereco"));
                c.setComplemento(rs.getString("complemento"));
                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setCep(rs.getString("cep"));
                c.setSenha(rs.getString("senha"));
                
            }
        } catch (SQLException ex) {
            System.out.println("Erro na listagem por cpf" + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return c;
    }
}
