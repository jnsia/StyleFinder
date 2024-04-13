import axiosInstance from '../../shared/utils/axiosInstance';
import jwtAxiosInstance from '../../shared/utils/jwtAxiosInstance';

import { FeedCreateRequestDTO, FeedUpdateRequestDTO } from './feed-types';

const url = '/api/feed';
const api = {
  readFeedList: (page: number) => axiosInstance.get(`${url}?page=${page}`),
  createFeedCoordi: (request: FeedCreateRequestDTO, feedThumbnail: File) => jwtAxiosInstance.post(`${url}/create`, { request, feedThumbnail }),

  readFeed: (feedId: number) => jwtAxiosInstance.get(`${url}/${feedId}`),
  deleteFeed: (feedId: number) => jwtAxiosInstance.delete(`${url}/${feedId}`),
  updateFeed: (feedId: number, request: FeedUpdateRequestDTO, multipartFile: File[]) => jwtAxiosInstance.put(`${url}/update/${feedId}`, { feedId, request, multipartFile }),

  readPopularFeedList: (page: number) => axiosInstance.get(`${url}/popularity?page=${page}`),
  readMyFeed: (page: number) => jwtAxiosInstance.get(`${url}/myfeed?page=${page}`),
  searchByTitle: (title: string, page: number) => jwtAxiosInstance.get(`${url}/search?title=${title}&page=${page}`),
  likeFeed: (feedId: number) => jwtAxiosInstance.post(`${url}/like/${feedId}`),

  uploadFile: (multipartFile: File[]) => jwtAxiosInstance.post(`$/file`, multipartFile),
  readCoordi: (feedId: number) => jwtAxiosInstance.post(`${url}/${feedId}/fitting`),
};

export default api;
