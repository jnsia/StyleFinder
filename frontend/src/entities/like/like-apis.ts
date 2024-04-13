import axiosInstance from '../../shared/utils/axiosInstance';

const url = '/api/feed/like';
const api = {
  feedLikes: (feedId: number) => axiosInstance.post(`${url}/${feedId}`, feedId),
};

export default api;
