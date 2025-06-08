<template>
  <div>
    <h2>Login</h2>
    <form @submit="goToHome">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" v-model="password" />
      </div>
      <button class="btn btn-primary" type="submit">Log in</button>
    </form>
    <p>
      Don't have an account?
      <button class="btn btn-primary" @click="goToRegister">Register</button>
    </p>
    <button class="btn btn-secondary" @click="googleLogin">Login with Google</button>
  </div>
</template>

<script setup>
import api from '@/axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const router = useRouter()

const goToHome = () => {
  router.push('/home')
}

const goToRegister = () => {
  router.push('/login/register')
}

const googleLogin = () => {
  window.location.href = api.defaults.baseURL + '/auth/google'
}
</script>