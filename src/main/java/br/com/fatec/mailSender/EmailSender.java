/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.fatec.mailSender;


import br.com.fatec.App;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import javafx.scene.control.Alert;

/**
 *
 * @author Gabriel
 */
public class EmailSender {
    
    private final String usuario = "greenbankfatec@gmail.com";
    private final String senha = "ddex lxwf cemd gogo";
    private final String from = "anyname@freelance.mailtrap.link";
    private String codigoRecuperacao;
    
    public void enviarCodigoVerificacao(String destinatario, String codigo) throws AddressException, MessagingException{
        codigoRecuperacao = codigo;
        Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, senha);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(destinatario)
            );
            message.setSubject("Código de alteração de senha");
            message.setText("Aqui está o seu código: " + codigo);
            Transport.send(message);

        } catch (MessagingException e) {
            App.alerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado ao enviar o email");
        }
        
    }
    
    public String getCodigoRecuperacao(){
        return this.codigoRecuperacao;
    }
    
}
