/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.fatec.model.dao;

import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author Gabriel
 * @param <T>
 */
public interface DAO <T>{
    public boolean inserir(T model) throws SQLException;
    public boolean remover(T model) throws SQLException;
    public boolean alterar(T model) throws SQLException;
    public Collection<T> listar() throws SQLException;
    public T buscaPorId(int id) throws SQLException;

}
