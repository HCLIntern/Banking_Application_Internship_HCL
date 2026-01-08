import axios from '../api/axiosInstance';
import { API_ENDPOINTS } from '../api/apiEndpoints';

export const authService = {
	login: async (username, password) => {
		const res = await axios.post(API_ENDPOINTS.AUTH.LOGIN, { username, password });
		// backend returns ApiResponse with `data` containing LoginResponse { token, type, id, username... }
		if (res && res.data && res.data.data) {
			const payload = res.data.data;
			if (payload.token) localStorage.setItem('accessToken', payload.token);
			// return the inner data for callers
			return payload;
		}
		return res.data;
	},

	signup: async (username, email, password) => {
		const res = await axios.post(API_ENDPOINTS.AUTH.SIGNUP, { username, email, password });
		// signup returns an ApiResponse wrapper
		return res && res.data ? res.data : null;
	},

	logout: () => {
		localStorage.removeItem('accessToken');
		localStorage.removeItem('refreshToken');
		window.location.href = '/login';
	}
};
