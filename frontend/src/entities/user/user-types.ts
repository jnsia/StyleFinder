import { ClosetCloth } from '../closet/closet-types';

export interface SignUpRequestDTO {
  email: string;
  password: string;
  confirmPassword: string;
  nickname: string;
  likeCategories: string[];
  dislikeCategories: string[];
  height: number;
  weight: number;
}

export interface SignInRequestDTO {
  email: string;
  password: string;
}

export interface TokenReissueRequestDTO {
  refreshToken: string;
}

export interface UpdateUserInfoRequestDTO {
  nickname: string;
  likeCategories: string[];
  dislikeCategories: string[];
  height: number;
  weight: number;
}

export interface User {
  id: number;

  email: string;
  password: string;
  nickname: string;
  profileImage: string;
  likeCategories: string[];
  dislikeCategories: string[];
  height: number;
  weight: number;

  closets: ClosetCloth[];
}

export interface UserInfo {
  nickname: string;
  profileImage: string;
  introduce: string;
  likeCategories: string[];
  dislikeCategories: string[];
  height: number;
  weight: number;
}

export interface RefreshToken {
  refreshToken: string;
  accessToken: string;
  email: string;
}
