import { defineStore } from 'pinia'
import api from '@/axios'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.user,
    userName: (state) => state.user?.email || '',
  },
  actions: {
    async fetchUser() {
      try {
        const res = await api.get('/me', { withCredentials: true })
        this.user = res.data
      } catch (err) {
        this.user = null
      }
    },
    async login(credentials) {
      await api.post('login', credentials, { withCredentials: true })
      await this.fetchUser()
    },
    async logout() {
      await api.post('logout', {}, { withCredentials: true })
      this.user = null
    }
  },
  persist: true,
})
