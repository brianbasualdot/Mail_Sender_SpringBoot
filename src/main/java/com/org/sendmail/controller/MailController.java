package com.org.sendmail.controller;

import com.org.sendmail.domain.EmailDTO;
import com.org.sendmail.domain.EmailFileDTO;
import com.org.sendmail.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class MailController {

    @Autowired
    private IEmailService iEmailService;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO) {

        System.out.println("Mensaje Recibido! " + emailDTO);

        iEmailService.sendEmail(emailDTO.getToUser(), emailDTO.getSubject(), emailDTO.getMessage());

        Map<String, String> response = new HashMap<>();
        response.put("Estado", "Enviado");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendMessageFile")
    public ResponseEntity<?> receiveRequestEmailFile(@ModelAttribute EmailFileDTO emailFileDTO) {

        try{
            String fileName = emailFileDTO.getFile().getName();

            Path path = Paths.get("src/main/resources/files/" + fileName);

            Files.createDirectories(path.getParent());
            Files.copy(emailFileDTO.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            File file = path.toFile();

            iEmailService.sendEmailWhitFile(emailFileDTO.getToUser(), emailFileDTO.getSubject(), emailFileDTO.getMessage(), file);

            Map<String, String> response = new HashMap<>();
            response.put("Estado", "Enviado");
            response.put("Archivo", "fileName");

            return ResponseEntity.ok(response);

        } catch (Exception e){
            throw new RuntimeException("Hubo un error al enviar el mail con el archivo " + e.getMessage());
        }
    }
}