package com.appointmentsystem.service;

import com.appointmentsystem.dto.ScheduleSlot;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.repository.AppointmentRepository;
import com.appointmentsystem.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    // private static final int CONSULTATION_DURATION_MINUTES = 30;
    private static final DayOfWeek[] CLINIC_DAYS = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
    private static final LocalTime CLINIC_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLINIC_END_TIME = LocalTime.of(17, 0);
    private static final LocalTime LUNCH_START_TIME = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END_TIME = LocalTime.of(13, 0);

    public ScheduleService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<ScheduleSlot> getWeeklySchedule(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        final int duration = doctor.getConsultationDuration();

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.FRIDAY);
        
        LocalDateTime weekStartDateTime = startOfWeek.atStartOfDay();
        LocalDateTime weekEndDateTime = endOfWeek.atTime(23, 59);

        // 1. Fetch all existing appointments for the week
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStartTimeBetween(doctorId, weekStartDateTime, weekEndDateTime);
        Map<LocalDateTime, Appointment> appointmentMap = appointments.stream()
            .collect(Collectors.toMap(Appointment::getStartTime, a -> a));

        // 2. Generate all potential slots for the week
        List<ScheduleSlot> schedule = new ArrayList<>();
        for (DayOfWeek day : CLINIC_DAYS) {
            LocalDate currentDay = startOfWeek.with(day);
            LocalDateTime slotTime = currentDay.atTime(CLINIC_START_TIME);

            while (slotTime.toLocalTime().isBefore(CLINIC_END_TIME)) {
                ScheduleSlot slot = new ScheduleSlot();
                slot.setStartTime(slotTime);
                slot.setEndTime(slotTime.plusMinutes(duration));

                // 3. Determine status of each slot // isLunchTime(slotTime.toLocalTime())
                if (isLunchTime(slotTime.toLocalTime()) || isLunchTime(slot.getEndTime().toLocalTime().minusNanos(1))) {
                    slot.setStatus("Lunch Break");
                } else if (appointmentMap.containsKey(slotTime)) {
                    Appointment booking = appointmentMap.get(slotTime);
                    slot.setStatus("Booked");
                    slot.setAppointmentId(booking.getId());
                    slot.setPatientName(booking.getPatientName());
                } else {
                    slot.setStatus("Available");
                }
                schedule.add(slot);
                slotTime = slotTime.plusMinutes(duration);
            }
        }
        return schedule;
    }

    // lunch check to prevent slots from ending inside the lunch break
    private boolean isLunchTime(LocalTime time) {
        return !time.isBefore(LUNCH_START_TIME) && time.isBefore(LUNCH_END_TIME);
    }
}