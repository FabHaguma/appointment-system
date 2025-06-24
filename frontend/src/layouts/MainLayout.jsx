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

    const currentYear = new Date().getFullYear();

    return (
        <div className="wrapper">
            {/* Navbar */}
            <nav className="main-header navbar navbar-expand navbar-white navbar-light">
                {/* Left navbar links */}
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <a className="nav-link" data-widget="pushmenu" href="#" role="button"><i className="fas fa-bars"></i></a>
                    </li>
                </ul>
                 {/* Right navbar links */}
                 <ul className="navbar-nav ml-auto">
                    <li className="nav-item dropdown">
                        <a className="nav-link" data-toggle="dropdown" href="#">
                            <i className="far fa-user"></i>
                        </a>
                        <div className="dropdown-menu dropdown-menu-lg dropdown-menu-right">
                            <span className="dropdown-item dropdown-header">{user?.actualName}</span>
                            <div className="dropdown-divider"></div>
                            {(user?.role === 'DOCTOR' || user?.role === 'NURSE' || user?.role === 'ADMIN') && (
                                <Link to="/account-settings" className="dropdown-item">
                                    <i className="fas fa-key mr-2"></i> Change Password
                                </Link>
                            )}
                            <div className="dropdown-divider"></div>
                            <a href="#" className="dropdown-item" onClick={handleLogout}>
                                <i className="fas fa-sign-out-alt mr-2"></i> Logout
                            </a>
                        </div>
                    </li>
                </ul>
            </nav>

            {/* Main Sidebar Container */}
            <aside className="main-sidebar sidebar-dark-primary elevation-4">
                {/* Brand Logo */}
                <Link to="/dashboard" className="brand-link">
                    <span className="brand-text font-weight-light">Appointment System</span>
                </Link>

                {/* Sidebar */}
                <div className="sidebar">
                    {/* Sidebar user panel (optional) */}
                    <div className="user-panel mt-3 pb-3 mb-3 d-flex">
                        <div className="info">
                            <a href="#" className="d-block">{user?.role}</a>
                        </div>
                    </div>

                    {/* Sidebar Menu */}
                    <nav className="mt-2">
                        <ul className="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                            <li className="nav-item">
                                <Link to="/dashboard" className="nav-link">
                                    <i className="nav-icon fas fa-tachometer-alt"></i>
                                    <p>Dashboard</p>
                                </Link>
                            </li>
                            {user?.role === 'ADMIN' && (
                                <>
                                <li className="nav-item">
                                    <Link to="/doctors" className="nav-link">
                                        <i className="nav-icon fas fa-user-md"></i>
                                        <p>Doctor Management</p>
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/nurses" className="nav-link">
                                        <i className="nav-icon fas fa-user-nurse"></i>
                                        <p>Nurse Management</p>
                                    </Link>
                                </li>
                                </>
                            )}
                        </ul>
                    </nav>
                </div>
            </aside>

            {/* Content Wrapper. Contains page content */}
            <div className="content-wrapper">
                {children}
            </div>

            {/* Main Footer */}
            <footer className="main-footer">
                <strong>Copyright &copy; {currentYear}.</strong> All rights reserved.
            </footer>
        </div>
    );
};

export default MainLayout;