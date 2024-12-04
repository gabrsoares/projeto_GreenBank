/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.model.dao;

import br.com.fatec.model.bean.Agencia;
import br.com.fatec.model.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class AgenciaDAO implements DAO<Agencia>{

    @Override
    public boolean inserir(Agencia model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean remover(Agencia model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean alterar(Agencia model) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Collection<Agencia> listar() {
        Connection connection = MySQLConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Agencia> lista = new ArrayList<>();
        
        try{
            stmt = connection.prepareStatement("SELECT * FROM agencias");
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Agencia a = new Agencia();
                a.setNumAgencia(rs.getInt("numAgencia"));
                a.setNome(rs.getString("nome"));
                a.setEndereco(rs.getString("endereco"));
                a.setCep(rs.getString("cep"));
                a.setCidade(rs.getString("cidade"));
                a.setEstado(rs.getString("estado"));
                a.setTelefone(rs.getString("telefone"));
                a.setGerente(rs.getString("gerenteResponsavel"));
                
                lista.add(a);         
            }
        }catch (SQLException ex){
            System.out.println("erro na tabela agencia" + ex);
        }finally {
            MySQLConnection.closeConnection(connection, stmt, rs);
        }
        
        
        return lista;
    }

    @Override
    public Agencia buscaPorId(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
