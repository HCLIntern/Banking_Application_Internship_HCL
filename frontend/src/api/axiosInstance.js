import axios from 'axios';

const axiosInstance = axios.create({
	baseURL: import.meta.env.VITE_API_URL || 'http://localhost:5000'
});

// Attach Authorization header if token present
axiosInstance.interceptors.request.use(config => {
	try {
		const token = localStorage.getItem('accessToken');
		if (token) {
			config.headers = config.headers || {};
			config.headers['Authorization'] = `Bearer ${token}`;
		}
	} catch (e) {
		// ignore
	}
	return config;
});

// Simple response interceptor to handle 401s
axiosInstance.interceptors.response.use(
	res => res,
	err => {
		if (err.response && err.response.status === 401) {
			// Optionally redirect to login or attempt refresh
			try { window.location.href = '/login'; } catch (e) { /* noop */ }
		}
		return Promise.reject(err);
	}
);

export default axiosInstance;
