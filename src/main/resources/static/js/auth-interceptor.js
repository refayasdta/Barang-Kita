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

// ── Universal Navbar Logic (Runs on every page automatically) ──
document.addEventListener('DOMContentLoaded', function() {
    const uToken = localStorage.getItem('token');
    const uRole = localStorage.getItem('role');

    // 1. Show Admin button if applicable
    if (uRole === 'Admin') {
        const adminBtn = document.getElementById('adminPanelBtn');
        if (adminBtn) adminBtn.style.display = 'flex';
    }

    // 2. Update Cart Badge dynamically
    if (uToken) {
        try {
            const payload = JSON.parse(atob(uToken.split('.')[1]));
            const userId = payload.id_akun || payload.id || null;
            
            if (userId) {
                fetch(`/api/carts/user/${userId}`, {
                    headers: { 'Authorization': 'Bearer ' + uToken }
                })
                .then(r => r.json())
                .then(carts => {
                    const badge = document.getElementById('cartBadge');
                    if (badge && carts && carts.length > 0) {
                        badge.textContent = carts.length;
                        badge.style.display = 'flex';
                    }
                }).catch(() => {});
            }
        } catch(e) {
            console.error("Failed to parse token for navbar cart update");
        }
    }
});