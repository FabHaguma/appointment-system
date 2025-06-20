import React, { useState, useEffect } from 'react';

const NurseFormModal = ({ nurse, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        regNumber: '',
        firstName: '',
        lastName: '',
        gender: '',
        active: true,
        password: '',
    });

    useEffect(() => {
        if (nurse) {
            setFormData({
                regNumber: nurse.regNumber || '',
                firstName: nurse.firstName || '',
                lastName: nurse.lastName || '',
                gender: nurse.gender || '',
                active: nurse.active !== undefined ? nurse.active : true,
                password: '', // Password should not be pre-filled
            });
        } else {
            // Reset form for new nurse
            setFormData({
                regNumber: '',
                firstName: '',
                lastName: '',
                gender: 'MALE',
                active: true,
                password: '',
            });
        }
    }, [nurse]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    const modalTitle = nurse ? 'Edit Nurse' : 'Create Nurse';

    return (
        <div className="modal show" style={{ display: 'block' }} tabIndex="-1">
            <div className="modal-dialog">
                <div className="modal-content">
                    <form onSubmit={handleSubmit}>
                        <div className="modal-header">
                            <h5 className="modal-title">{modalTitle}</h5>
                            <button type="button" className="close" onClick={onClose}><span>&times;</span></button>
                        </div>
                        <div className="modal-body">
                            <div className="form-group">
                                <label>Registration Number</label>
                                <input type="text" name="regNumber" value={formData.regNumber} onChange={handleChange} className="form-control" required />
                            </div>
                            <div className="form-group">
                                <label>First Name</label>
                                <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} className="form-control" required />
                            </div>
                            <div className="form-group">
                                <label>Last Name</label>
                                <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} className="form-control" required />
                            </div>
                            <div className="form-group">
                                <label>Gender</label>
                                <select name="gender" value={formData.gender} onChange={handleChange} className="form-control">
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>
                            </div>
                            {!nurse && (
                                <div className="form-group">
                                    <label>Password</label>
                                    <input type="password" name="password" value={formData.password} onChange={handleChange} className="form-control" required />
                                </div>
                            )}
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" onClick={onClose}>Close</button>
                            <button type="submit" className="btn btn-primary">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default NurseFormModal;
