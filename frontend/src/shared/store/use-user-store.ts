import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface UserStore {
  userId: number;
  setUserId: (newUserId: number) => void;
}

const useUserStore = create(
  persist<UserStore>(
    (set) => ({
      userId: 0,
      setUserId: (newUserId) => set({ userId: newUserId }),
    }),
    {
      name: 'userIdStorage',
    },
  ),
);

export default useUserStore;
