import { useState, useEffect } from 'react';
import * as api from '../services/api';

export const useDoctors = (isDoctor) => {
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState('');

  useEffect(() => {
    if (isDoctor) return;

    let isMounted = true;
    api.getDoctors().then(response => {
      if (isMounted) {
        const fetchedDoctors = response.data;
        setDoctors(fetchedDoctors);
        if (fetchedDoctors.length > 0) {
          setSelectedDoctor(fetchedDoctors[0].id);
        }
      }
    }).catch(error => {
      console.error("Error fetching doctors:", error);
      if (isMounted) setDoctors([]);
    });

    return () => { isMounted = false; };
  }, [isDoctor]);

  const doctorOptions = doctors.map(doc => ({
    value: doc.id,
    label: `${doc.name} - ${doc.specialization}`
  }));

  const selectedDoctorOption = doctorOptions.find(option => option.value === selectedDoctor);

  return { doctors, selectedDoctor, setSelectedDoctor, doctorOptions, selectedDoctorOption };
};
