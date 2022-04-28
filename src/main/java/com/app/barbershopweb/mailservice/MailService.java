package com.app.barbershopweb.mailservice;

import com.app.barbershopweb.exception.MailException;
import com.app.barbershopweb.user.crud.Users;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyAboutOutOfBusiness(List<Users> customers, String barbershopEmail) {
        for (Users customer : customers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Your appointments canceled because barbershop is out of business");
            message.setText("""
                    Dear %s %s, your ...
                    """.formatted(customer.getFirstName(), customer.getLastName()));
            message.setTo(customer.getEmail());
            message.setFrom("no-reply@" + barbershopEmail.split("@")[1]);

            try {
                mailSender.send(message);
            } catch (Exception e) {
                throw new MailException(List.of(e.getMessage()));
            }
        }
    }

}
