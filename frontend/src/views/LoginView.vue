<template>
  <div>
    <h2>{{ isRegisterMode ? 'Rejestracja' : 'Logowanie' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" required />
      </div>
      <div>
        <label>Hasło:</label>
        <input type="password" v-model="password" required />
      </div>
      <div v-if="isRegisterMode">
        <label>Powtórz hasło:</label>
        <input type="password" v-model="repeatPassword" required />
      </div>
      <button class="btn btn-primary" type="submit">
        {{ isRegisterMode ? 'Zarejestruj się' : 'Zaloguj się' }}
      </button>
    </form>
    <p>
      <button class="btn btn-link" @click="toggleMode">
        {{ isRegisterMode ? 'Masz już konto? Zaloguj się' : 'Nie masz konta? Zarejestruj się' }}
      </button>
    </p>
    <button class="btn btn-secondary" @click="googleLogin">Zaloguj przez Google</button>
  </div>
</template>

<script setup>
import api from '@/axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/composables/useUserStore'

const email = ref('')
const password = ref('')
const repeatPassword = ref('')
const isRegisterMode = ref(false)
const router = useRouter()
const userStore = useUserStore()

const handleSubmit = async () => {
  if (isRegisterMode.value) {
    // Rejestracja
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
    // Logowanie
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
      router.push('/home')
    } catch (error) {
      alert('Logowanie nieudane. Sprawdź dane.')
    }
  }
}

const toggleMode = () => {
  isRegisterMode.value = !isRegisterMode.value
  password.value = ''
  repeatPassword.value = ''
}

const googleLogin = () => {
  window.location.href = api.defaults.baseURL + '/auth/google'
}
</script>