import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const MainLayout = ({ children }) => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <div className="wrapper">
            {/* Navbar */}
            <nav className="main-header navbar navbar-expand navbar-white navbar-light">
                <ul className="navbar-nav">
                    <li className="nav-item"><a className="nav-link" data-widget="pushmenu" href="#" role="button"><i className="fas fa-bars"></i></a></li>
                </ul>
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item">
                        <button onClick={handleLogout} className="btn btn-link nav-link">
                            <i className="fas fa-sign-out-alt"></i> Logout
                        </button>
                    </li>
                </ul>
            </nav>

            {/* Main Sidebar Container */}
            <aside className="main-sidebar sidebar-dark-primary elevation-4">
                <Link to="/dashboard" className="brand-link">
                    <span className="brand-text font-weight-light">Appointment System</span>
                </Link>
                <div className="sidebar">
                    <div className="user-panel mt-3 pb-3 mb-3 d-flex">
                        <div className="info">
                            <a href="#" className="d-block">{user?.username} ({user?.role})</a>
                        </div>
                    </div>
                    <nav className="mt-2">
                        <ul className="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                            {/* Nurse and Admin see Dashboard */}
                            {(user?.role === 'ADMIN' || user?.role === 'NURSE') && (
                                <li className="nav-item">
                                    <Link to="/dashboard" className="nav-link"><i className="nav-icon fas fa-tachometer-alt"></i><p>Dashboard</p></Link>
                                </li>
                            )}
                            {/* Doctor sees My Schedule */}
                            {user?.role === 'DOCTOR' && (
                                <li className="nav-item">
                                    <Link to="/dashboard" className="nav-link"><i className="nav-icon fas fa-calendar-alt"></i><p>My Schedule</p></Link>
                                </li>
                            )}
                            {/* Admin only */}
                            {user?.role === 'ADMIN' && (
                                <li className="nav-item">
                                    <Link to="/doctors" className="nav-link"><i className="nav-icon fas fa-user-md"></i><p>Doctor Management</p></Link>
                                </li>
                            )}
                        </ul>
                    </nav>
                </div>
            </aside>

            {/* Content Wrapper. Contains page content */}
            <div className="content-wrapper">
                {children}
            </div>
        </div>
    );
};

export default MainLayout;