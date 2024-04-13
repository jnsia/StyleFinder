import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface LoginStore {
  isLogin: boolean;
  setLogin: () => void;
  setLogout: () => void;
}

const useLoginStore = create(
  persist<LoginStore>(
    (set) => ({
      isLogin: false,
      setLogin: () => set({ isLogin: true }),
      setLogout: () => set({ isLogin: false }),
    }),
    {
      name: 'loginStorage',
    },
  ),
);

export default useLoginStore;
