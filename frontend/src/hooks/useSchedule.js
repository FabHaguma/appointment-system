import { useState, useCallback } from 'react';
import * as api from '../services/api';

const formatEvent = (slot, isDoctor) => {
  const isAvailable = slot.status === 'Available';
  const isBooked = slot.status === 'Booked';
  const isLunch = slot.status === 'Lunch Break';

  const getTitle = () => {
    if (isBooked) return slot.patientName;
    if (isLunch) return 'Lunch Break';
    return 'Available';
  };

  const getColors = () => {
    if (isAvailable) return { backgroundColor: '#28a745', borderColor: '#28a745' }; // Green
    if (isBooked) {
      const color = isDoctor ? '#6c757d' : '#007bff'; // Gray for doctor, blue for others
      return { backgroundColor: color, borderColor: color };
    }
    if (isLunch) return { backgroundColor: '#ffc107', borderColor: '#ffc107' }; // Yellow
    return {};
  };

  const canInteract = !isDoctor && isAvailable;

  return {
    title: getTitle(),
    start: new Date(slot.startTime),
    end: new Date(slot.endTime),
    allDay: false,
    ...getColors(),
    extendedProps: slot,
    classNames: canInteract ? ['cursor-pointer'] : ['read-only-event'],
    editable: canInteract,
  };
};

export const useSchedule = (isDoctor, selectedDoctor, calendarRef) => {
  const [events, setEvents] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const fetchSchedule = useCallback(async () => {
    if ((!selectedDoctor && !isDoctor) || !calendarRef.current) {
      setEvents([]);
      return;
    }

    setIsLoading(true);
    try {
      const calendarApi = calendarRef.current.getApi();
      const view = calendarApi.view;

      const schedulePromise = isDoctor
        ? api.getMySchedule(view.activeStart)
        : api.getWeeklySchedule(selectedDoctor, view.activeStart);

      const response = await schedulePromise;
      const formattedEvents = response.data.map(slot => formatEvent(slot, isDoctor));
      setEvents(formattedEvents);
    } catch (error) {
      console.error("Error fetching schedule:", error);
      setEvents([]);
    } finally {
      setIsLoading(false);
    }
  }, [isDoctor, selectedDoctor, calendarRef]);

  return { events, isLoading, fetchSchedule };
};
