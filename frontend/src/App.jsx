import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';
import MainLayout from './layouts/MainLayout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import DoctorManagement from './pages/DoctorManagement';
import NurseManagement from './pages/NurseManagement';
import AccountSettings from './pages/AccountSettings';
import './App.css';

// A component to protect routes that require authentication
const ProtectedRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? children : <Navigate to="/login" />;
};

function App() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route 
        path="/*"
        element={
          <ProtectedRoute>
            <MainLayout>
              <Routes>
                <Route path="/dashboard" element={<Dashboard />} />
                {/* Admin-only routes */}
                {user?.role === 'ADMIN' && (
                  <>
                    <Route path="/doctors" element={<DoctorManagement />} />
                    <Route path="/nurses" element={<NurseManagement />} />
                  </>
                )}
                 {/* Doctor-only route */}
                 {user?.role === 'DOCTOR' && (
                    <Route path="/account-settings" element={<AccountSettings />} />
                )}
                 {/* Nurse-only route */}
                 {user?.role === 'NURSE' && (
                    <Route path="/account-settings" element={<AccountSettings />} />
                )}
                {/* Default route after login */}
                <Route path="*" element={<Navigate to="/dashboard" />} />
              </Routes>
            </MainLayout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;