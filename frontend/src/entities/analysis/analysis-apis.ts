import jwtAxiosInstance from '../../shared/utils/jwtAxiosInstance';

const url = '/api/recommend';
const api = {
  recommend: () => jwtAxiosInstance.get(`${url}`),
  recommendByCategory: (category: string) => jwtAxiosInstance.get(`${url}/category?category=${category}`),
  recommendByColor: (color: string) => jwtAxiosInstance.get(`${url}/color?color=${color}`),
  recommendByStyle: (style: string) => jwtAxiosInstance.get(`${url}/style?style=${style}`),

  getOuterItems: () => jwtAxiosInstance.get('/get_outer_items'),
  getTopItems: () => jwtAxiosInstance.get('/get_top_items'),
  getBottomItems: () => jwtAxiosInstance.get('/get_bottom_items'),
  getDressItems: () => jwtAxiosInstance.get('/get_dress_items'),
};

export default api;
