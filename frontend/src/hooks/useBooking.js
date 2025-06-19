import { useState } from 'react';
import * as api from '../services/api';

export const useBooking = (onSuccess) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedSlot, setSelectedSlot] = useState(null);

  const openBookingModal = (slot) => {
    setSelectedSlot(slot);
    setIsModalOpen(true);
  };

  const closeBookingModal = () => {
    setSelectedSlot(null);
    setIsModalOpen(false);
  };

  const handleBookingSubmit = async (patientDetails, doctorId) => {
    if (!selectedSlot) return;
    const appointmentData = {
      ...patientDetails,
      doctorId: doctorId,
      startTime: selectedSlot.startTime,
    };
    try {
      await api.createAppointment(appointmentData);
      closeBookingModal();
      onSuccess();
    } catch (error) {
      alert("Failed to book appointment. The slot may have been taken.");
      console.error("Booking failed", error);
    }
  };

  const handleCancelAppointment = async (slot) => {
    if (window.confirm(`Do you want to cancel the appointment for ${slot.patientName}?`)) {
      try {
        await api.cancelAppointment(slot.appointmentId);
        onSuccess();
      } catch (error) {
        console.error("Cancellation failed", error);
        alert("Failed to cancel appointment.");
      }
    }
  };

  return {
    isModalOpen,
    selectedSlot,
    openBookingModal,
    closeBookingModal,
    handleBookingSubmit,
    handleCancelAppointment,
  };
};
