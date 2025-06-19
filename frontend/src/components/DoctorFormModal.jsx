import React, { useState, useEffect } from 'react';

const DoctorFormModal = ({ doctor, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        regNumber: '',
        firstName: '',
        lastName: '',
        gender: '',
        specialization: '',
        consultationDuration: 30,
        employmentType: '',
        active: true,
        password: '',
    });

    useEffect(() => {
        if (doctor) {
            setFormData({
                regNumber: doctor.regNumber || '',
                firstName: doctor.firstName || '',
                lastName: doctor.lastName || '',
                gender: doctor.gender || '',
                employmentType: doctor.employmentType || '',
                specialization: doctor.specialization || '',
                consultationDuration: doctor.consultationDuration || 30,                
                active: doctor.active,
                password: '',
            });
        } else {
            // Reset for new doctor form
            setFormData({
                regNumber: '',
                firstName: '',
                lastName: '',
                gender: '',
                specialization: '',
                consultationDuration: 30,
                employmentType: '',
                active: true,
                password: '',
            });
        }
    }, [doctor]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value,
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    const modalTitle = doctor ? 'Edit Doctor' : 'Create Doctor';

    return (
        <div className="modal show" style={{ display: 'block', backgroundColor: 'rgba(0,0,0,0.5)' }} tabIndex="-1">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <form onSubmit={handleSubmit}>
                        <div className="modal-header">
                            <h5 className="modal-title">{modalTitle}</h5>
                            <button type="button" className="close" onClick={onClose}><span>×</span></button>
                        </div>
                        <div className="modal-body">
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label>First Name</label>
                                    <input type="text" name="firstName" className="form-control" value={formData.firstName} onChange={handleChange} required />
                                </div>
                                <div className="form-group col-md-6">
                                    <label>Last Name</label>
                                    <input type="text" name="lastName" className="form-control" value={formData.lastName} onChange={handleChange} required />
                                </div>
                            </div>
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label>Registration Number</label>
                                    <input type="text" name="regNumber" className="form-control" value={formData.regNumber} onChange={handleChange} required />
                                </div>
                                <div className="form-group col-md-6">
                                    <label>Specialization</label>
                                    <input type="text" name="specialization" className="form-control" value={formData.specialization} onChange={handleChange} />
                                </div>
                            </div>
                            <div className='form-group'>
                                <label>Gender</label>
                                <select name="gender" className="form-control" value={formData.gender} onChange={handleChange}>
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>
                            </div>
                            {/* User fields only for new doctors */}
                            {!doctor && (
                                <>
                                    <div className="form-group">
                                        <label>Temporary Password</label>
                                        <input type="password" name="password" className="form-control" value={formData.password} onChange={handleChange} required />
                                    </div>
                                </>
                            )}
                            <div className='form-group'>
                                <label>Employment Type</label>
                                <select name="employmentType" className="form-control" value={formData.employmentType} onChange={handleChange}>
                                    <option value="FULL_TIME">Full Time</option>
                                    <option value="PART_TIME">Part Time</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Consultation Duration (minutes)</label>
                                <input type="number" name="consultationDuration" className="form-control" value={formData.consultationDuration} onChange={handleChange} required min="5" />
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
                            <button type="submit" className="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default DoctorFormModal;