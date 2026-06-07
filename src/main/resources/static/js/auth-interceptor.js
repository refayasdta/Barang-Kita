// File: src/main/resources/static/js/auth-interceptor.js

const originalFetch = window.fetch;
window.fetch = async function() {
    try {
        const response = await originalFetch.apply(this, arguments);
        
        // If the backend rejects the token
        if (response.status === 401 || response.status === 403) {
            // Prevent infinite redirect loops if we are already on the login page
            if (window.location.pathname !== '/login') {
                localStorage.removeItem('token');
                localStorage.removeItem('role');
                alert("Sesi Anda telah berakhir. Silakan login kembali.");
                window.location.href = '/login';
            }
        }
        return response;
    } catch (error) {
        throw error;
    }
};