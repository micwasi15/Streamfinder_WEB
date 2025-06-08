<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <div class="container-fluid">
        <!-- Lewa strona -->
        <router-link class="navbar-brand fw-bold" to="/">StreamFinder</router-link>

        <div class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/search">Wyszukaj</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/favorites">Ulubione</router-link>
            </li>

            <!-- Dropdown: Statystyki -->
            <li class="nav-item dropdown">
              <a
                class="nav-link dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                Statystyki
              </a>
              <ul class="dropdown-menu">
                <li><router-link class="dropdown-item" to="/stats/top">Top 10</router-link></li>
                <li><router-link class="dropdown-item" to="/stats/user">Twoje statystyki</router-link></li>
              </ul>
            </li>
          </ul>

          <!-- Prawa strona -->
          <div class="d-flex align-items-center">
            <span v-if="user" class="text-white me-3">{{ user.name }}</span>
            <button
              class="btn btn-outline-light"
              @click="handleAuth"
            >
              {{ user ? 'Wyloguj' : 'Zaloguj' }}
            </button>
          </div>
        </div>
      </div>
    </nav>

    <main class="container py-4">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Prosty mock użytkownika — zamień na realne auth
const user = ref({ name: 'Jan Kowalski' }) // lub null

function handleAuth() {
  if (user.value) {
    user.value = null
  } else {
    user.value = { name: 'Jan Kowalski' }
  }
}
</script>

<style scoped>
main {
  background: #000000;
}
</style>
