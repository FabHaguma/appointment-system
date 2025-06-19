import React, { createContext, useState, useContext } from 'react';
import * as api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(() => {
        const token = localStorage.getItem('token');
        const username = localStorage.getItem('username');
        const role = localStorage.getItem('role');
        const actualName = localStorage.getItem('actualName');
        if (token && username && role) {
            return { token, username, role, actualName };
        }
        return null;
    });

    const login = async (username, password) => {
        const response = await api.login(username, password);
        const { token, role, actualName } = response.data;
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        localStorage.setItem('role', role);
        localStorage.setItem('actualName', actualName);
        setUser({ token, username, role, actualName });
        api.setAuthHeader(token); // Set header for subsequent requests
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
        localStorage.removeItem('actualName');
        setUser(null);
        api.setAuthHeader(null); // Clear auth header
    };

    return (
        <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};