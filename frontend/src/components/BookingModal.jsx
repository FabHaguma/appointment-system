import React, { useState } from 'react';
import { format } from 'date-fns';

const BookingModal = ({ slot, onClose, onSubmit }) => {
  const [patientName, setPatientName] = useState('');
  const [patientEmail, setPatientEmail] = useState('');
  const [patientPhone, setPatientPhone] = useState('');
  const [reasonForVisit, setReasonForVisit] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ patientName, patientEmail, patientPhone, reasonForVisit });
  };

  return (
    <div className="modal show" style={{ display: 'block' }} tabIndex="-1">
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <form onSubmit={handleSubmit}>
            <div className="modal-header">
              <h5 className="modal-title">Book Appointment</h5>
              <button type="button" className="close" onClick={onClose}><span>×</span></button>
            </div>
            <div className="modal-body">
              <p>
                <strong>Time:</strong> {format(new Date(slot.startTime), 'EEEE, MMM d @ h:mm a')}
              </p>
              <div className="form-group">
                <label>Patient Name</label>
                <input type="text" className="form-control" value={patientName} onChange={e => setPatientName(e.target.value)} required />
              </div>
              <div className="form-group">
                <label>Patient Phone</label>
                <input type="tel" className="form-control" value={patientPhone} onChange={e => setPatientPhone(e.target.value)} required />
              </div>
              <div className="form-group">
                <label>Patient Email (Optional)</label>
                <input type="email" className="form-control" value={patientEmail} onChange={e => setPatientEmail(e.target.value)} />
              </div>
              <div className="form-group">
                <label>Reason for Visit (Optional)</label>
                <textarea className="form-control" rows="2" value={reasonForVisit} onChange={e => setReasonForVisit(e.target.value)}></textarea>
              </div>
            </div>
            <div className="modal-footer">
              <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
              <button type="submit" className="btn btn-primary">Confirm Booking</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default BookingModal;