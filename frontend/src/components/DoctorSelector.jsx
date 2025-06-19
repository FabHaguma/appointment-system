import React from 'react';
import Select from 'react-select';

const DoctorSelector = ({ selectedDoctorOption, onChange, options }) => (
  <div className="form-group row">
    <label className="col-sm-2 col-form-label">Select Doctor</label>
    <div className="col-sm-4">
      <Select
        value={selectedDoctorOption}
        onChange={option => onChange(option ? option.value : '')}
        options={options}
        placeholder="Search by name or specialization..."
        isClearable
        isSearchable
      />
    </div>
  </div>
);

export default DoctorSelector;
