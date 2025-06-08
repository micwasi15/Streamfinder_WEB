<template>
  <div>
    <h2>Register</h2>
    <form @submit.prevent="register">
      <div>
        <label>Email:</label>
        <input type="email" v-model="email" />
      </div>
      <div>
        <label>Password:</label>
        <input type="password" v-model="password" />
      </div>
      <button class="btn btn-primary" type="submit">Register</button>
    </form>
    <p>
      Already have an account?
      <button class="btn btn-primary" @click="goToLogin">Back to login</button>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const router = useRouter()

const register = () => {
  alert('Account created!')
  router.push('/')
}

const goToLogin = () => {
  router.push('/login')
}
</script>