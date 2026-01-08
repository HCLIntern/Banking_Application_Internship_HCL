import axios from '../api/axiosInstance';
import { API_ENDPOINTS } from '../api/apiEndpoints';

export const paymentService = {
  createPayment: async (accountId, amount, description) => {
    const res = await axios.post(API_ENDPOINTS.PAYMENTS.BASE, null, { params: { accountId, amount, description } });
    return res.data;
  },

  getPaymentById: async (id) => {
    const res = await axios.get(`${API_ENDPOINTS.PAYMENTS.BASE}/${id}`);
    return res.data;
  },

  getPaymentHistory: async (accountId, page = 0, size = 10) => {
    const res = await axios.get(API_ENDPOINTS.PAYMENTS.BASE, { params: { accountId, page, size } });
    return res.data;
  },

  updateStatus: async (paymentId, status) => {
    const res = await axios.put(`${API_ENDPOINTS.PAYMENTS.BASE}/${paymentId}/status`, null, { params: { status } });
    return res.data;
  }
};
