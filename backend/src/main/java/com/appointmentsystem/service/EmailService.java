package com.appointmentsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentConfirmation(String to, String patientName, String appointmentTime, String doctorName) {
        if (to == null || to.isEmpty()) {
            return; // Don't send email if address is not provided
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Confirmation");
        message.setText("Dear " + patientName + ",\n\nYour appointment with Dr. " + doctorName + " is confirmed for " + appointmentTime + ".\n\nThank you,\nYour Clinic");

        mailSender.send(message);
    }

    public void sendAppointmentCancellation(String to, String patientName, String appointmentTime, String doctorName) {
        if (to == null || to.isEmpty()) {
            return; // Don't send email if address is not provided
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Cancelled");
        message.setText("Dear " + patientName + ",\n\nYour appointment with Dr. " + doctorName + " scheduled for " + appointmentTime + " has been cancelled.\n\nIf you have questions or wish to reschedule, please contact us.\n\nThank you,\nYour Clinic");

        mailSender.send(message);
    }
}
