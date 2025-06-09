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
      await api.post('/auth/login', credentials, { withCredentials: true })
      await this.fetchUser()
      return this.user
    },
    async logout() {
      const res = await api.post('/auth/logout', {}, { withCredentials: true })
      if (res.status === 200) {
        this.user = null
        window.location.href = '/'
      }
    }
  },
  persist: true,
})
