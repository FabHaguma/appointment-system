package com.appointmentsystem.service;

import com.appointmentsystem.dal.AppointmentDao;
import com.appointmentsystem.dal.DoctorDao;
import com.appointmentsystem.dto.ScheduleSlot;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.model.Doctor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final AppointmentDao appointmentDao;
    private final DoctorDao doctorDao;
    
    private static final DayOfWeek[] CLINIC_DAYS = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
    private static final LocalTime CLINIC_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLINIC_END_TIME = LocalTime.of(18, 0);
    private static final LocalTime LUNCH_START_TIME = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END_TIME = LocalTime.of(13, 0);

    public ScheduleService(AppointmentDao appointmentDao, DoctorDao doctorDao) {
        this.appointmentDao = appointmentDao;
        this.doctorDao = doctorDao;
    }

    public List<ScheduleSlot> getWeeklySchedule(Long doctorId, LocalDate date) {
        Doctor doctor = doctorDao.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        final int duration = doctor.getConsultationDuration();

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SATURDAY);
        
        LocalDateTime weekStartDateTime = startOfWeek.atStartOfDay();
        LocalDateTime weekEndDateTime = endOfWeek.atTime(23, 59, 59);

        List<Appointment> appointments = appointmentDao.findByDoctorIdAndStartTimeBetween(doctorId, weekStartDateTime, weekEndDateTime);
        Map<LocalDateTime, Appointment> appointmentMap = appointments.stream()
            .collect(Collectors.toMap(Appointment::getStartTime, a -> a));

        List<ScheduleSlot> schedule = new ArrayList<>();
        for (DayOfWeek day : CLINIC_DAYS) {
            LocalDate currentDay = startOfWeek.with(day);
            LocalDateTime slotTime = currentDay.atTime(CLINIC_START_TIME);

            while (slotTime.toLocalTime().isBefore(CLINIC_END_TIME)) {
                ScheduleSlot slot = new ScheduleSlot();
                slot.setStartTime(slotTime);
                slot.setEndTime(slotTime.plusMinutes(duration));

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

    private boolean isLunchTime(LocalTime time) {
        return !time.isBefore(LUNCH_START_TIME) && time.isBefore(LUNCH_END_TIME);
    }
}