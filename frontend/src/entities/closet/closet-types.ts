export interface ClosetUploadResponseDTO {
  category: string[];
  detail: string[];
  texture: string[];
}

export interface ClosetListReadResponseDTO {
  image: string[];
  part: string[];

  categories: string[];
  details: string[];
  textures: string[];
}

export interface ClosetCloth {
  id: number;
  image: string;
  imageUrl: string;
  part: string;
  categories: string[];
  details: string[];
  textures: string[];
}
