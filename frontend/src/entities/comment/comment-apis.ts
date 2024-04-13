import jwtAxiosInstance from '../../shared/utils/jwtAxiosInstance';

const url = '/api/comment';
const commentApi = {
  createComment: (feedId: number, content: string) => jwtAxiosInstance.post(`${url}/${feedId}/create?content=${content}`),
  updateComment: (commentId: number, content: string) => jwtAxiosInstance.put(`${url}/${commentId}/update`, { commentId, content }),
  deleteComment: (commentId: number) => jwtAxiosInstance.delete(`${url}/${commentId}/delete`),
};

export default commentApi;
