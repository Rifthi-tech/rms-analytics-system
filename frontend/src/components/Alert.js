import React from 'react';

function Alert({ message, type }) {
  const alertClass = `alert alert-${type === 'error' ? 'danger' : type}`;
  
  return (
    <div className={alertClass} role="alert">
      {message}
    </div>
  );
}

export default Alert;
