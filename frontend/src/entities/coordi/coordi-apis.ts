import jwtAxiosInstance from '../../shared/utils/jwtAxiosInstance';
import { Coordi } from './coordi-types';

const url = '/api/coordi';
const api = {
  coordiCreate: (request: Coordi) => jwtAxiosInstance.post(`${url}`, request),
};

export default api;
