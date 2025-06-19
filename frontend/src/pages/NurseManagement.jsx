import React, { useState, useEffect } from 'react';
import * as api from '../services/api';
import NurseFormModal from '../components/NurseFormModal';

const NurseManagement = () => {
    const [nurses, setNurses] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [selectedNurse, setSelectedNurse] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchNurses = () => {
        setIsLoading(true);
        api.getAllNursesAdmin()
            .then(res => {
                setNurses(res.data);
                setIsLoading(false);
            })
            .catch(err => {
                console.error(err);
                setIsLoading(false);
            });
    };

    useEffect(() => {
        fetchNurses();
    }, []);

    const handleEditClick = (nurse) => {
        setSelectedNurse(nurse);
        setIsModalOpen(true);
    };

    const handleSaveNurse = async (nurseData) => {
        try {
            if (selectedNurse) {
                await api.updateNurse(selectedNurse.id, nurseData);
            } else {
                await api.createNurse(nurseData);
            }
            fetchNurses(); // Refresh the list
            setIsModalOpen(false);
            setSelectedNurse(null);
        } catch (error) {
            console.error('Failed to save nurse:', error);
        }
    };

    const handleToggleActive = async (nurse) => {
        const originalNurses = [...nurses];
        const updatedNurses = nurses.map(n =>
            n.id === nurse.id ? { ...n, active: !n.active } : n
        );
        setNurses(updatedNurses);

        try {
            if (nurse.active) {
                await api.deactivateNurse(nurse.id);
            } else {
                await api.activateNurse(nurse.id);
            }
        } catch (error) {
            console.error('Failed to toggle nurse status:', error);
            setNurses(originalNurses); // Revert on error
        }
    };

    return (
        <>
            <section className="content-header">
                <div className="container-fluid">
                    <h1>Nurse Management</h1>
                </div>
            </section>
            <section className="content">
                <div className="container-fluid">
                    <div className="card">
                        <div className="card-header">
                            <button className="btn btn-primary" onClick={() => { setIsModalOpen(true); setSelectedNurse(null); }}>Create New Nurse</button>
                        </div>
                        <div className="card-body">
                            {isLoading ? <p>Loading...</p> : (
                                <table className="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Name</th>
                                            <th>Reg-Number</th>
                                            <th>Gender</th>
                                            <th>Status</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {nurses.map(nurse => (
                                            <tr key={nurse.id}>
                                                <td>{nurse.name}</td>
                                                <td>{nurse.regNumber}</td>
                                                <td>{nurse.gender}</td>
                                                <td>{nurse.active ? <span className="badge bg-success">Active</span> : <span className="badge bg-danger">Inactive</span>}</td>
                                                <td>
                                                    <button className="btn btn-sm btn-info mr-2" onClick={() => handleEditClick(nurse)}>Edit</button>
                                                    <button onClick={() => handleToggleActive(nurse)} className="btn btn-sm btn-warning">
                                                        {nurse.active ? 'Deactivate' : 'Activate'}
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
            {isModalOpen && <NurseFormModal nurse={selectedNurse} onClose={() => setIsModalOpen(false)} onSave={handleSaveNurse} />}
        </>
    );
};

export default NurseManagement;
