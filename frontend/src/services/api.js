import axios from 'axios';
import { format } from 'date-fns';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor to add the token to every request
export const setAuthHeader = (token) => {
    if (token) {
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        delete apiClient.defaults.headers.common['Authorization'];
    }
};

// Set token from localStorage on initial load
const token = localStorage.getItem('token');
if (token) {
    setAuthHeader(token);
}

// --- Auth Endpoints ---
export const login = (username, password) => {
    return apiClient.post('/auth/login', { username, password });
};

// --- Schedule Endpoints (Updated for roles) ---
export const getDoctors = () => {
  return apiClient.get('/doctors');
};

export const getWeeklySchedule = (doctorId, date) => {
  const formattedDate = format(date, 'yyyy-MM-dd');
  return apiClient.get(`/schedules/doctors/${doctorId}?date=${formattedDate}`);
};

export const getMySchedule = (date) => {
    const formattedDate = format(date, 'yyyy-MM-dd');
    return apiClient.get(`/schedules/my-schedule?date=${formattedDate}`);
};

// --- Appointment Endpoints ---
export const createAppointment = (appointmentData) => {
  return apiClient.post('/appointments', appointmentData);
};

export const cancelAppointment = (appointmentId) => {
  return apiClient.delete(`/appointments/${appointmentId}`);
};

// --- Admin Endpoints ---
export const getAllDoctorsAdmin = () => {
    return apiClient.get('/admin/doctors');
};
export const createDoctor = (doctorData) => {
    return apiClient.post('/admin/doctors', doctorData);
};
export const updateDoctor = (doctorId, doctorData) => {
    return apiClient.put(`/admin/doctors/${doctorId}`, doctorData);
};

export const activateDoctor = (doctorId) => {
    return apiClient.patch(`/admin/doctors/${doctorId}/activate`);
};

export const deactivateDoctor = (doctorId) => {
    return apiClient.patch(`/admin/doctors/${doctorId}/deactivate`);
};

export const getAllNursesAdmin = () => {
    return apiClient.get('/admin/nurses');
};
export const createNurse = (nurseData) => {
    return apiClient.post('/admin/nurses', nurseData);
};
export const updateNurse = (nurseId, nurseData) => {
    return apiClient.put(`/admin/nurses/${nurseId}`, nurseData);
};

export const activateNurse = (nurseId) => {
    return apiClient.patch(`/admin/nurses/${nurseId}/activate`);
};

export const deactivateNurse = (nurseId) => {
    return apiClient.patch(`/admin/nurses/${nurseId}/deactivate`);
};

// --- Account Management ---
export const updateCredentials = (newPassword) => {
    // Only updating password, username is not changeable from this page.
    return apiClient.put('/account/credentials', { newPassword });
};