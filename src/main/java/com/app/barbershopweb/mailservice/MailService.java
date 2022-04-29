package com.app.barbershopweb.mailservice;

import com.app.barbershopweb.exception.MailException;
import com.app.barbershopweb.user.crud.User;
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

    public void notifyAboutOutOfBusiness(List<User> customers) {
        for (User customer : customers) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Your appointments canceled because barbershop is out of business");
            message.setText("""
                    Dear %s %s, your ...
                    """.formatted(customer.getFirstName(), customer.getLastName()));
            message.setTo(customer.getEmail());
            message.setFrom("no-reply@gmail.com");

            try {
                mailSender.send(message);
            } catch (Exception e) {
                throw new MailException(List.of(e.getMessage()));
            }
        }
    }

}
