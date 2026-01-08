import axios from '../api/axiosInstance';
import { API_ENDPOINTS } from '../api/apiEndpoints';

export const accountService = {
	getUserAccounts: async (userId) => {
		const res = await axios.get(API_ENDPOINTS.ACCOUNTS.BASE, { params: { userId } });
		return res.data;
	},

	getAccountById: async (id) => {
		const res = await axios.get(`${API_ENDPOINTS.ACCOUNTS.BASE}/${id}`);
		return res.data;
	},

	createAccount: async (userId, accountType) => {
		const res = await axios.post(API_ENDPOINTS.ACCOUNTS.BASE, null, { params: { userId, accountType } });
		return res.data;
	},

	updateAccount: async (id, dto) => {
		const res = await axios.put(`${API_ENDPOINTS.ACCOUNTS.BASE}/${id}`, dto);
		return res.data;
	},

	transfer: async (fromAccountId, toAccountNumber, amount, description) => {
		const res = await axios.post(API_ENDPOINTS.ACCOUNTS.TRANSFER, null, { params: { fromAccountId, toAccountNumber, amount, description } });
		return res.data;
	}
};
