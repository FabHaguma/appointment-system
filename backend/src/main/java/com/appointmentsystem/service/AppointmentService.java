package com.appointmentsystem.service;

import com.appointmentsystem.dto.AppointmentRequest;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.repository.AppointmentRepository;
import com.appointmentsystem.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService; // Inject EmailService

    public Appointment createAppointment(AppointmentRequest request) {
        // Prevent double booking
        if (appointmentRepository.existsByDoctorIdAndStartTime(request.getDoctorId(), request.getStartTime())) {
            throw new IllegalStateException("Time slot is already booked.");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found."));
        
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setPatientPhone(request.getPatientPhone());
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getStartTime().plusMinutes(doctor.getConsultationDuration())); // Use doctor's duration

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send confirmation email
        if (savedAppointment.getPatientEmail() != null && !savedAppointment.getPatientEmail().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
            String formattedTime = savedAppointment.getStartTime().format(formatter);
            emailService.sendAppointmentConfirmation(
                savedAppointment.getPatientEmail(),
                savedAppointment.getPatientName(),
                formattedTime,
                doctor.getName()
            );
        }

        return savedAppointment;
    }

    public void cancelAppointment(Long appointmentId) {
        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            // Send cancellation email if patient email exists
            if (appointment.getPatientEmail() != null && !appointment.getPatientEmail().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
                String formattedTime = appointment.getStartTime().format(formatter);
                emailService.sendAppointmentCancellation(
                    appointment.getPatientEmail(),
                    appointment.getPatientName(),
                    formattedTime,
                    appointment.getDoctor().getName()
                );
            }
            appointmentRepository.deleteById(appointmentId);
        });
    }
}