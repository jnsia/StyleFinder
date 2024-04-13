import { create } from 'zustand';
import { RecommendCloth } from '../../entities/recommend/recommend-types';

export interface ClothStore {
  cloth: RecommendCloth | null;
  createCloth: (clothInfo: RecommendCloth) => void;
  deleteCloth: () => void;
}

const useClothStore = create<ClothStore>((set) => ({
  cloth: null,
  createCloth: (clothInfo: RecommendCloth) => set({ cloth: clothInfo }),
  deleteCloth: () => set({ cloth: null }),
}));

export default useClothStore;
