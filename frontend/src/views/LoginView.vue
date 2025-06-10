<template>
  <div class="login-container">
    <h2 class="text-center mb-4">{{ isRegisterMode ? 'Rejestracja' : 'Logowanie' }}</h2>
    <form @submit.prevent="handleSubmit" class="login-form">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" required class="form-control" />
      </div>
      <div>
        <label>Hasło:</label>
        <input type="password" v-model="password" required class="form-control" />
      </div>
      <div v-if="isRegisterMode">
        <label>Powtórz hasło:</label>
        <input type="password" v-model="repeatPassword" required class="form-control" />
      </div>
      <button class="btn btn-primary w-100 mt-2" type="submit">
        {{ isRegisterMode ? 'Zarejestruj się' : 'Zaloguj się' }}
      </button>
    </form>
    <button class="btn btn-primary w-100 mt-2" @click="toggleMode">
      {{ isRegisterMode ? 'Masz już konto? Zaloguj się' : 'Nie masz konta? Zarejestruj się' }}
    </button>
    <button class="btn btn-secondary w-100 mt-2" @click="googleLogin">Zaloguj przez Google</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/axios'
import { useUserStore } from '@/composables/useUserStore'

const email = ref('')
const password = ref('')
const repeatPassword = ref('')
const isRegisterMode = ref(false)
const router = useRouter()
const userStore = useUserStore()

const toggleMode = () => {
  isRegisterMode.value = !isRegisterMode.value
  password.value = ''
  repeatPassword.value = ''
}

const googleLogin = () => {
  window.location.href = api.defaults.baseURL + '/auth/google'
}

const handleSubmit = async () => {
  if (isRegisterMode.value) {
    if (password.value !== repeatPassword.value) {
      alert('Hasła nie są takie same!')
      return
    }
    try {
      await api.post('/auth/register', {
        email: email.value,
        password: password.value
      })
      alert('Rejestracja udana! Możesz się teraz zalogować.')
      isRegisterMode.value = false
      password.value = ''
      repeatPassword.value = ''
    } catch (error) {
      alert('Rejestracja nieudana: ' + (error.response?.data?.message || error.message))
    }
  } else {
    try {
      const credentials = {
        email: email.value,
        password: password.value
      }
      const user = await userStore.login(credentials)
      if (!user) {
        alert('Logowanie nieudane. Sprawdź dane.')
        return
      }
      router.push('/')
    } catch (error) {
      alert('Logowanie nieudane. Sprawdź dane.')
    }
  }
}
</script>

<style scoped>
.login-container {
  width: 100%;
  margin: 0 auto;
  background: rgb(255, 255, 255);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  padding: 2rem 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.login-form > div {
  margin-bottom: 1rem;
}

.login-form label {
  margin-bottom: 0.25rem;
  font-weight: 500;
}

.login-form input {
  width: 100%;
}

.btn {
  min-width: 100px;
}

.btn-link {
  text-align: center;
  padding-left: 0;
  padding-right: 0;
}
</style>