<script setup>
import { onMounted } from 'vue';
import TheWelcome from '../components/TheWelcome.vue'

const apiURL = window.__APP_CONFIG__.API_URL;
onMounted(() => {
try {
  fetch(`${apiURL}/shows`)
    .then(response => response.json())
    .then(data => {
      document.getElementById('msg').innerText = data.content;
      console.log('Message fetched successfully:', data);
    })
    .catch(error => {
      console.error('Error fetching message:', error);
    });
} catch (error) {
  console.error('Unexpected error:', error);
}
});

const goLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};
</script>

<template>
  <main>
    <h1>Message from api</h1>
    <p id="msg"></p>
    <p>ApiURL</p>
    <p>{{ apiURL }}</p>
    <button @click="goLogin">Login with Google</button>
  </main>
</template>
