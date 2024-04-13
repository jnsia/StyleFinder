/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable prefer-const */
import axios, { AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import axiosInstance from './axiosInstance';

// const navigate = useNavigate();
const jwtAxiosInstance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 60000,
  headers: {
    accept: '*/*',
    'Content-Type': 'application/json;charset=UTF-8',
  },
  withCredentials: true,
});

const refreshJWT = async (refreshToken: string) => {
  const body = { refreshToken: refreshToken };
  const res = await axiosInstance.post(`/api/user/token`, body);
  return res;
};

const beforeReq = (config: InternalAxiosRequestConfig<any>): InternalAxiosRequestConfig<any> | Promise<any> => {
  const userInfo = localStorage.getItem('userInfo');

  if (!userInfo) {
    console.log('USERINFO NOT FOUND');
    return Promise.reject({ response: { status: 401, data: { message: '로그인이 필요합니다.' } } });
  }

  const { accessToken } = JSON.parse(userInfo);

  // Authorization 헤더 처리
  config.headers.Authorization = `Bearer ${accessToken}`;

  return config;
};

//fail request
const requestFail = (err: AxiosError | Error): Promise<AxiosError> => {
  return Promise.reject(err);
};

//before return response
const beforeRes = async (res: AxiosResponse): Promise<any> => {
  const data = res.data;

  if (data?.message === 'TOKEN_EXPIRED') {
    const userInfo: any = localStorage.getItem('userInfo');

    if (!userInfo) {
      console.log('USERINFO NOT FOUND');
    }

    let confirmedUserInfo = JSON.parse(userInfo);

    const result: any = await refreshJWT(confirmedUserInfo.refreshToken);

    confirmedUserInfo.accessToken = result.data?.accessToken;
    localStorage.setItem('userInfo', JSON.stringify(confirmedUserInfo));

    //원래의 호출
    const originalRequest = res.config;

    console.log(originalRequest);

    originalRequest.headers.Authorization = `Bearer ${result.accessToken}`;

    return await axios(originalRequest);
  } else if (data?.message === 'MALFORMED_TOKEN' || data?.message === 'INVALID_TOKEN' || data?.message === 'JWT_TOKEN_ERROR') {
    return Promise.reject({ response: { status: 401, data: { message: '다시 로그인이 필요합니다.' } } });
  }

  return res;
};

//fail response
const responseFail = (err: AxiosError | Error): Promise<Error> => {
  console.log('response fail error.............');

  return Promise.reject(err);
};

jwtAxiosInstance.interceptors.request.use(beforeReq, requestFail);
jwtAxiosInstance.interceptors.response.use(beforeRes, responseFail);

export default jwtAxiosInstance;
