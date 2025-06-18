import React, { useState, useEffect } from 'react';
import * as api from '../services/api';
import DoctorFormModal from '../components/DoctorFormModal';

const DoctorManagement = () => {
    const [doctors, setDoctors] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchDoctors = () => {
        setIsLoading(true);
        api.getAllDoctorsAdmin()
            .then(res => {
                setDoctors(res.data);
                setIsLoading(false);
            })
            .catch(err => console.error(err));
    };

    useEffect(() => {
        fetchDoctors();
    }, []);

    const handleEditClick = (doctor) => {
        setSelectedDoctor(doctor);
        setIsModalOpen(true);
    };

    const handleSaveDoctor = async (doctorData) => {
        try {
            if (selectedDoctor && selectedDoctor.id) {
                // It's an update
                await api.updateDoctor(selectedDoctor.id, doctorData);
            } else {
                // It's a create 
                await api.createDoctor(doctorData);
            }
            setIsModalOpen(false);
            setSelectedDoctor(null);
            fetchDoctors(); // Refresh the list
        } catch (error) {
            console.error("Failed to save doctor:", error);
            alert("Error saving doctor details.");
        }
    };

    const handleToggleActive = async (doctor) => {
        const originalDoctors = [...doctors];
        // Optimistically update the UI
        const updatedDoctors = doctors.map(d =>
            d.id === doctor.id ? { ...d, active: !d.active } : d
        );
        setDoctors(updatedDoctors);

        try {
            if (doctor.active) {
                await api.deactivateDoctor(doctor.id);
            } else {
                await api.activateDoctor(doctor.id);
            }
        } catch (error) {
            // If the API call fails, revert the changes
            setDoctors(originalDoctors);
            console.error("Failed to update doctor status:", error);
            alert("Failed to update doctor status. Please try again.");
        }
    };

    return (
        <>
            <section className="content-header">
                <div className="container-fluid">
                    <h1>Doctor Management</h1>
                </div>
            </section>
            <section className="content">
                <div className="container-fluid">
                    <div className="card">
                        <div className="card-header">
                            {/* The Create button would call setIsModalOpen(true) with setSelectedDoctor(null) */}
                            <button className="btn btn-primary" onClick={() => { setIsModalOpen(true); setSelectedDoctor(null); }}>Create New Doctor</button>
                        </div>
                        <div className="card-body">
                            {isLoading ? <p>Loading...</p> : (
                                <table className="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Specialization</th>
                                            <th>Consultation Duration</th>
                                            <th>Employment Type</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {doctors.map(doc => (
                                            <tr key={doc.id}>
                                                <td>{doc.name}</td>
                                                <td>{doc.specialization}</td>
                                                <td>{doc.consultationDuration} minutes</td>
                                                <td>{doc.employmentType}</td>
                                                <td>{doc.active ? <span className="badge bg-success">Active</span> : <span className="badge bg-danger">Inactive</span>}</td>
                                                <td>
                                                    <button className="btn btn-sm btn-info mr-2" onClick={() => handleEditClick(doc)}>Edit</button>
                                                    <button onClick={() => handleToggleActive(doc)} className="btn btn-sm btn-warning">
                                                        {doc.active ? 'Deactivate' : 'Activate'}
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            )}
                        </div>
                    </div>
                </div>
            </section>
            {isModalOpen && <DoctorFormModal doctor={selectedDoctor} onClose={() => setIsModalOpen(false)} onSave={handleSaveDoctor} />}
        </>
    );
};

export default DoctorManagement;