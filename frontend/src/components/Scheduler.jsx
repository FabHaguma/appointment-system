import React, { useState, useEffect, useRef, useCallback } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { useAuth } from '../contexts/AuthContext';

import { useDoctors } from '../hooks/useDoctors';
import { useSchedule } from '../hooks/useSchedule';
import { useBooking } from '../hooks/useBooking';
import DoctorSelector from './DoctorSelector';
import BookingModal from './BookingModal';

const Scheduler = () => {
  const { user } = useAuth();
  const isDoctor = user.role === 'DOCTOR';
  
  const calendarRef = useRef(null);
  const [currentView, setCurrentView] = useState('timeGridWeek');

  const { selectedDoctor, setSelectedDoctor, doctorOptions, selectedDoctorOption } = useDoctors(isDoctor);
  const { events, isLoading, fetchSchedule } = useSchedule(isDoctor, selectedDoctor, calendarRef);
  const { 
    isModalOpen, 
    selectedSlot, 
    openBookingModal, 
    closeBookingModal, 
    handleBookingSubmit, 
    handleCancelAppointment 
  } = useBooking(fetchSchedule);

  useEffect(() => {
    fetchSchedule();
  }, [selectedDoctor, fetchSchedule]);

  const handleEventClick = (clickInfo) => {
    if (isDoctor) return;

    const slot = clickInfo.event.extendedProps;
    if (slot.status === 'Available') {
      openBookingModal(slot);
    } else if (slot.status === 'Booked') {
      handleCancelAppointment(slot);
    }
  };
  
  const onBookingSubmit = (patientDetails) => {
    handleBookingSubmit(patientDetails, selectedDoctor);
  };

  const handleDatesSet = useCallback((dateInfo) => {
    setCurrentView(dateInfo.view.type);
    fetchSchedule();
  }, [fetchSchedule]);

  const handleDateClick = (arg) => {
    const calendarApi = calendarRef.current?.getApi();
    if (calendarApi?.view.type === 'dayGridMonth') {
        calendarApi.changeView('timeGridDay', arg.date);
    }
  };

  return (
    <div className="card">
      <div className="card-header">
        {!isDoctor && (
          <DoctorSelector 
            selectedDoctorOption={selectedDoctorOption}
            onChange={setSelectedDoctor}
            options={doctorOptions}
          />
        )}
      </div>
      <div className="card-body">
        {isLoading && <p>Loading schedule...</p>}
        <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={{
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
          }}
          initialView="timeGridWeek"
          events={currentView === 'dayGridMonth' ? [] : events}
          eventClick={handleEventClick}
          datesSet={handleDatesSet}
          dateClick={handleDateClick}
          weekends={true}
          firstDay={1}
          slotMinTime="09:00:00"
          slotMaxTime="17:00:00"
          height="auto"
          navLinks={true}
        />
      </div>
      {isModalOpen && (
        <BookingModal 
          slot={selectedSlot}
          onClose={closeBookingModal}
          onSubmit={onBookingSubmit}
        />
      )}
      <style>{`
        .cursor-pointer {
          cursor: pointer;
        }
        .read-only-event {
          cursor: default !important;
        }
      `}</style>
    </div>
  );
};

export default Scheduler;