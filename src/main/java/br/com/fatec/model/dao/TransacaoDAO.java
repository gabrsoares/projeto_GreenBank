/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.dao;

import br.com.fatec.App;
import br.com.fatec.model.bean.Transacao;
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
public class TransacaoDAO implements DAO<Transacao>{
    
    @Override
    public boolean inserir(Transacao t) throws SQLException{
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            stmt = connection.prepareStatement("INSERT INTO transacoes (tipo_transacao, id_conta_remet, id_conta_dest, valor, data, hora)"
                    + "VALUES(?,?,?,?,?,?)");
            stmt.setString(1, t.getTipo());
            stmt.setInt(2, t.getRemetente().getId());
            stmt.setInt(3, t.getDestinatario().getId());
            stmt.setDouble(4, t.getValor());
            stmt.setDate(5, t.getData());
            stmt.setTime(6, t.getHora());
            stmt.executeUpdate();
   
            return true;
        } catch (SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro", "Erro ao realizar transferência: " + ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean remover(Transacao t) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        
        try{
            stmt = connection.prepareStatement("DELETE FROM transacoes WHERE ID = ?");
            stmt.setInt(1, t.getId());
            stmt.executeUpdate();
            
            return true;
        } catch (SQLException ex) {
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    @Override
    public boolean alterar(Transacao t) throws SQLException {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        try{
            stmt = connection.prepareStatement("UPDATE transacoes SET tipo_transacao = ?, id_conta_remet = ?, id_conta_dest = ?,"
                    + "valor = ?, data = ?, hora = ? WHERE id = ?");
            stmt.setString(1, t.getTipo());
            stmt.setInt(2, t.getRemetente().getId());
            stmt.setInt(3, t.getDestinatario().getId());
            stmt.setDouble(4, t.getValor());
            stmt.setDate(5, t.getData());
            stmt.setTime(6, t.getHora());
            stmt.setInt(7, t.getId());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex){
            System.out.println("Erro ao alterar valores da tabela transacoes: " + ex);
            return false;
        } finally {
            MySQLConnection.closeConnection(connection, stmt);
        }
    }

    public Collection<Transacao> listarPorId(int id){
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Transacao> listaTransacoes = new ArrayList<>();
        
        try {
            stmt = connection.prepareStatement("SELECT t.id, t.tipo_transacao, t.id_conta_remet, t.id_conta_dest, t.valor, t.data, t.hora\n" +
                                                    "FROM transacoes t\n" +
                                                    "INNER JOIN contas as remetente ON t.id_conta_remet = remetente.id\n" +
                                                    "INNER JOIN contas as destinatario ON t.id_conta_dest = destinatario.id\n"+
                                                    "WHERE remetente.id_cliente = ? or destinatario.id_cliente = ?");
            stmt.setInt(1, id);
            stmt.setInt(2, id);
            rs = stmt.executeQuery();
            
            ContaDAO contaDAO = new ContaDAO();
            while(rs.next()){
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setTipo(rs.getString("tipo_transacao"));
                t.setRemetente(contaDAO.buscaPorId(rs.getInt("id_conta_remet")));
                t.setDestinatario(contaDAO.buscaPorId(rs.getInt("id_conta_dest")));
                t.setValor(rs.getDouble("valor"));
                t.setData(rs.getDate("data"));
                t.setHora(rs.getTime("hora"));
                
                listaTransacoes.add(t);
            }
        } catch(SQLException ex){
            App.alerta(Alert.AlertType.ERROR, "Erro crítico", "Dados indisponíveis: " + ex);
        } finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
                       
        return listaTransacoes;
    }

    @Override
    public Transacao buscaPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Collection<Transacao> listar() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
