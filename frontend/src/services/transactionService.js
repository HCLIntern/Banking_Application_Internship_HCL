import axios from '../api/axiosInstance';
import { API_ENDPOINTS } from '../api/apiEndpoints';

export const transactionService = {
	getUserTransactions: async (userId, page = 0, size = 10) => {
		const res = await axios.get(API_ENDPOINTS.TRANSACTIONS.BASE, { params: { userId, page, size } });
		return res.data;
	},

	getTransactionById: async (id) => {
		const res = await axios.get(`${API_ENDPOINTS.TRANSACTIONS.BASE}/${id}`);
		return res.data;
	},

	transfer: async (fromAccountId, toAccountId, amount, description) => {
		const res = await axios.post(`${API_ENDPOINTS.TRANSACTIONS.BASE}/transfer`, null, { params: { fromAccountId, toAccountId, amount, description } });
		return res.data;
	},

	getAccountTransactions: async (accountId) => {
		const res = await axios.get(`${API_ENDPOINTS.TRANSACTIONS.BASE}/account/${accountId}`);
		return res.data;
	}
};
