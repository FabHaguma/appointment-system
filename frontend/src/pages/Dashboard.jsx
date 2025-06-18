import React from 'react';
import Scheduler from '../components/Scheduler';
import { useAuth } from '../contexts/AuthContext';

const Dashboard = () => {
    const { user } = useAuth();
    const pageTitle = user?.role === 'DOCTOR' ? "My Schedule" : "Appointment Scheduler";
    
    return (
        <>
            <section className="content-header">
                <div className="container-fluid">
                    <h1>{pageTitle}</h1>
                </div>
            </section>
            <section className="content">
                <div className="container-fluid">
                    <Scheduler />
                </div>
            </section>
        </>
    );
};

export default Dashboard;