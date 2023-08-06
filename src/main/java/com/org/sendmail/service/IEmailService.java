package com.org.sendmail.service;

import java.io.File;

public interface IEmailService {

    void sendEmail(String[] toUser, String subject, String message); // Aqui enviamos solo con texto

    void sendEmailWhitFile(String[] toUser, String subject, String message, File file); // Aqui podemos adjuntar archivos
}
