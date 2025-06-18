import React, { useState, useEffect, useMemo } from 'react';
import * as api from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import { format, startOfWeek, addDays, isSameDay } from 'date-fns';
import BookingModal from './BookingModal';

const Scheduler = () => {
  const { user } = useAuth();
  // Determine if the user is a doctor
  console.log("User:", user);
  const isDoctor = user.role === 'DOCTOR';
  
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState('');
  const [currentWeek, setCurrentWeek] = useState(new Date());
  const [schedule, setSchedule] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  // Fetch doctor list for Admin/Nurse
  useEffect(() => {
    if (!isDoctor) {
      api.getDoctors().then(response => {
        setDoctors(response.data);
        if (response.data.length > 0) {
          setSelectedDoctor(response.data[0].id);
        }
      });
    }
  }, [isDoctor]);

  // Fetch schedule based on role
  useEffect(() => {
    const fetchSchedule = () => {
      setIsLoading(true);
      const schedulePromise = isDoctor
        ? api.getMySchedule(currentWeek)
        : api.getWeeklySchedule(selectedDoctor, currentWeek);

      schedulePromise
        .then(response => {
          setSchedule(response.data);
          setIsLoading(false);
        })
        .catch(error => {
          console.error("Error fetching schedule:", error);
          setIsLoading(false);
        });
    };
    
    // Don't fetch if nurse hasn't selected a doctor yet
    if (isDoctor || selectedDoctor) {
      fetchSchedule();
    }
  }, [selectedDoctor, currentWeek, isDoctor]);

  const handleSlotClick = (slot) => {
    // Doctors have read-only view
    if (isDoctor) return;

    if (slot.status === 'Available') {
      setSelectedSlot(slot);
      setIsModalOpen(true);
    } else if (slot.status === 'Booked') {
      if (window.confirm(`Do you want to cancel the appointment for ${slot.patientName}?`)) {
        api.cancelAppointment(slot.appointmentId).then(() => {
          // Refresh schedule
          setCurrentWeek(prev => new Date(prev));
        });
      }
    }
  };

  
  const handleBookingSubmit = async (patientDetails) => {
    const appointmentData = {
        ...patientDetails,
        doctorId: selectedDoctor,
        startTime: selectedSlot.startTime,
    };
    try {
        await api.createAppointment(appointmentData);
        setIsModalOpen(false);
        // Refresh schedule
        setCurrentWeek(prev => new Date(prev));
    } catch (error) {
        alert("Failed to book appointment. The slot may have been taken.");
        console.error("Booking failed", error);
    }
  };

  const groupedSchedule = useMemo(() => {
    const weekStart = startOfWeek(currentWeek, { weekStartsOn: 1 }); // Monday
    const days = Array.from({ length: 5 }).map((_, i) => addDays(weekStart, i));
    
    return days.map(day => ({
      date: day,
      slots: schedule.filter(slot => isSameDay(new Date(slot.startTime), day)),
    }));
  }, [schedule, currentWeek]);


  return (
    <div className="card">
      <div className="card-header">
        {/* Hide doctor selector for Doctors */}
        {!isDoctor && (
          <div className="form-group row">
            <label className="col-sm-2 col-form-label">Select Doctor</label>
            <div className="col-sm-4">
              <select className="form-control" value={selectedDoctor} onChange={e => setSelectedDoctor(e.target.value)}>
                {doctors.map(doc => <option key={doc.id} value={doc.id}>{doc.name}</option>)}
              </select>
            </div>
          </div>
        )}
      </div>
      <div className="card-body">
        {isLoading ? <p>Loading schedule...</p> : (
          <div className="calendar-container">
            {groupedSchedule.map(({ date, slots }) => (
              <div key={date.toString()} className="day-column">
                <div className="day-header">{format(date, 'EEE, MMM d')}</div>
                {slots.map(slot => (
                  <div
                    key={slot.startTime}
                    className={`time-slot slot-${slot.status.toLowerCase()} ${isDoctor ? 'read-only' : ''}`}
                    onClick={() => handleSlotClick(slot)}
                    style={{ cursor: isDoctor ? 'default' : '' }}
                  >
                    {format(new Date(slot.startTime), 'p')}
                    {slot.status === 'Booked' && <div style={{fontSize: '0.8em', fontWeight: 'bold'}}>{slot.patientName}</div>}
                  </div>
                ))}
              </div>
            ))}
          </div>
        )}
      </div>
      {isModalOpen && (
        <BookingModal 
          slot={selectedSlot}
          onClose={() => setIsModalOpen(false)}
          onSubmit={handleBookingSubmit}
        />
      )}
    </div>
  );
};

export default Scheduler;