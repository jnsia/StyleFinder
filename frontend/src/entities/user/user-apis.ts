import axiosInstance from '../../shared/utils/axiosInstance';
import jwtAxiosInstance from '../../shared/utils/jwtAxiosInstance';
import { SignInRequestDTO, SignUpRequestDTO, TokenReissueRequestDTO, UpdateUserInfoRequestDTO } from './user-types';

const url = '/api/user';
const api = {
  signUp: (request: SignUpRequestDTO) => {
    const headers = { 'Content-Type': 'multipart/form-data' };
    return axiosInstance.post(`${url}/signUp`, request, { headers });
  },
  signIn: (request: SignInRequestDTO) => axiosInstance.post(`${url}/signIn`, request),
  getUserInfo: () => jwtAxiosInstance.get(`${url}/profile`),

  tokenReissue: (request: TokenReissueRequestDTO) => axiosInstance.post(`${url}/token`, request),
  updateUserInfo: (request: UpdateUserInfoRequestDTO) => {
    const headers = { 'Content-Type': 'multipart/form-data' };
    return jwtAxiosInstance.put(`${url}/update`, request, { headers });
  },
  removeUserInfo: () => jwtAxiosInstance.delete(`${url}/remove`),
  analysisFavor: () => jwtAxiosInstance.get(`${url}/favor`),
};

export default api;
