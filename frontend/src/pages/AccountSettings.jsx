import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import * as api from '../services/api';

const AccountSettings = () => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const { logout } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');

        if (newPassword !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }
        if (newPassword.length < 6) {
            setError("Password must be at least 6 characters long.");
            return;
        }

        try {
            const res = await api.updateCredentials(newPassword);
            setMessage(res.data + " You will be logged out now.");
            setTimeout(() => {
                logout();
            }, 3000);
        } catch (err) {
            setError(err.response?.data?.message || "An error occurred while updating your password.");
        }
    };

    return (
        <>
            <section className="content-header">
                <div className="container-fluid">
                    <h1>Account Settings</h1>
                </div>
            </section>
            <section className="content">
                <div className="container-fluid">
                    <div className="card card-primary">
                        <div className="card-header">
                            <h3 className="card-title">Change Password</h3>
                        </div>
                        <form onSubmit={handleSubmit}>
                            <div className="card-body">
                                {message && <div className="alert alert-success">{message}</div>}
                                {error && <div className="alert alert-danger">{error}</div>}
                                <div className="form-group">
                                    <label htmlFor="newPassword">New Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        id="newPassword"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                        required
                                    />
                                </div>
                                <div className="form-group">
                                    <label htmlFor="confirmPassword">Confirm New Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        id="confirmPassword"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </div>
                            </div>
                            <div className="card-footer">
                                <button type="submit" className="btn btn-primary">Update Password</button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </>
    );
};

export default AccountSettings;
