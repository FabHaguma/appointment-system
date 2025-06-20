package com.appointmentsystem.service;

import com.appointmentsystem.dal.AppointmentDao;
import com.appointmentsystem.dal.DoctorDao;
import com.appointmentsystem.dto.AppointmentRequest;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
public class AppointmentService {

    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;
    private final EmailService emailService;

    @Autowired
    public AppointmentService(AppointmentDao appointmentDao, DoctorDao doctorDao, EmailService emailService) {
        this.appointmentDao = appointmentDao;
        this.doctorDao = doctorDao;
        this.emailService = emailService;
    }

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        if (appointmentDao.existsByDoctorIdAndStartTime(request.getDoctorId(), request.getStartTime())) {
            throw new IllegalStateException("Time slot is already booked.");
        }

        Doctor doctor = doctorDao.findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + request.getDoctorId()));
        
        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctor.getId());
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setPatientPhone(request.getPatientPhone());
        appointment.setReasonForVisit(request.getReasonForVisit());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getStartTime().plusMinutes(doctor.getConsultationDuration()));

        Appointment savedAppointment = appointmentDao.create(appointment);

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

        savedAppointment.setDoctor(doctor); // Populate transient field for the response object
        return savedAppointment;
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        appointmentDao.findById(appointmentId).ifPresent(appointment -> {
            Doctor doctor = doctorDao.findById(appointment.getDoctorId())
                .orElseThrow(() -> new IllegalStateException("Data integrity issue: Doctor not found for appointment " + appointmentId));

            if (appointment.getPatientEmail() != null && !appointment.getPatientEmail().isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
                String formattedTime = appointment.getStartTime().format(formatter);
                emailService.sendAppointmentCancellation(
                    appointment.getPatientEmail(),
                    appointment.getPatientName(),
                    formattedTime,
                    doctor.getName()
                );
            }
            appointmentDao.cancel(appointmentId);
        });
    }
}