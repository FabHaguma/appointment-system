package com.appointmentsystem.service;

import com.appointmentsystem.dto.AppointmentRequest;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.repository.AppointmentRepository;
import com.appointmentsystem.repository.DoctorRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

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

        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
}