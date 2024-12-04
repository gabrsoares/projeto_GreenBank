/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.dao;

import br.com.fatec.App;
import br.com.fatec.model.bean.Conta;
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
public class ContaDAO implements DAO<Conta>{

    @Override
    public boolean inserir(Conta c) {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            /* regra de negócio
            *  ao cadastrar o cliente, os dois tipos de conta (corrente, poupança) são criadas automaticamente
            */
            //conta corrente
            stmt = connection.prepareStatement("INSERT INTO contas (tipo, id_agencia, id_cliente)"
                    + "VALUES(?,?,?)");
            stmt.setInt(1, 1);
            stmt.setInt(2, c.getAgencia());
            stmt.setInt(3, c.getIdCliente());               
            stmt.executeUpdate();
            
            //conta poupança
            stmt = connection.prepareStatement("INSERT INTO contas (tipo, id_agencia, id_cliente)"
                    + "VALUES(?,?,?)");
            stmt.setInt(1, 2);
            stmt.setInt(2, c.getAgencia());
            stmt.setInt(3, c.getIdCliente());
            
            stmt.executeUpdate();
            
            return true;
        } catch (SQLException ex){
            System.out.println(ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }
    
    public List<Conta> buscaPorIdUsuario(int id) {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Conta> listaContasCliente = new ArrayList<>();
        try{
            
            stmt = connection.prepareStatement("SELECT * FROM contas where id_cliente = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Conta c = new Conta();
                c.setId(rs.getInt("id"));
                c.setTipo(rs.getInt("tipo"));
                c.setAgencia(rs.getInt("id_agencia"));
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setSaldo(rs.getDouble("saldo"));
                
                listaContasCliente.add(c);  
            }
        } catch (SQLException ex){
            System.out.println("Erro na busca: " + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return listaContasCliente;
    }
    
    //busca o a conta do usuário pelo id e pelo tipo da conta, 1 para conta corrente e 2 para conta poupança;
    public Conta buscaPorIdUsuario(int id, int tipo) {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Conta c = null;
        try{
            
            stmt = connection.prepareStatement("SELECT * FROM contas where id_cliente = ? AND tipo = ?");
            stmt.setInt(1, id);
            stmt.setInt(2,tipo);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                c = new Conta();
                c.setId(rs.getInt("id"));
                c.setTipo(rs.getInt("tipo"));
                c.setAgencia(rs.getInt("id_agencia"));
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setSaldo(rs.getDouble("saldo"));
                
            }
        } catch (SQLException ex){
            System.out.println("Erro na busca de conta: " + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return c;
    }

    @Override
    public boolean remover(Conta c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        
        try{
            stmt = connection.prepareStatement("DELETE FROM contas WHERE id = ?");
            stmt.setInt(1, c.getId());
            stmt.executeUpdate();
            
            return true;
        } catch(SQLException ex ){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorrou um erro na remoção dos dados" + ex);
            return false;
        } finally{
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean alterar(Conta c) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            stmt = connection.prepareStatement("UPDATE contas SET tipo = ?, id_agencia = ?, id_cliente = ?,"
                    + "saldo = ? WHERE id = ?");
            stmt.setInt(1, c.getTipo());
            stmt.setInt(2, c.getAgencia());
            stmt.setInt(3, c.getIdCliente());
            stmt.setDouble(4, c.getSaldo());
            stmt.setInt(5, c.getId());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex){
            System.out.println("Erro ao alterar valores da tabela contas: " + ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public Collection<Conta> listar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Conta buscaPorId(int id) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Conta c = null;
        try{
            
            stmt = connection.prepareStatement("SELECT * FROM contas where id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                c = new Conta();
                c.setId(rs.getInt("id"));
                c.setTipo(rs.getInt("tipo"));
                c.setAgencia(rs.getInt("id_agencia"));
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setSaldo(rs.getDouble("saldo"));
                
            }
        } catch (SQLException ex){
            System.out.println("Erro na busca de conta: " + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        return c;
    }

    
}
