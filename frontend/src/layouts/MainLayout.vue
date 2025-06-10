<template>
  <div class="bg-secondary">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <div class="container-fluid">
        <router-link class="navbar-brand fw-bold" to="/">StreamFinder</router-link>

        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
          aria-controls="navbarSupportedContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/search">Wyszukaj</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/favorites">Ulubione</router-link>
            </li>

            <li class="nav-item dropdown custom-dropdown">
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
                <li><router-link class="dropdown-item" to="/platform/stats">Show na platformach</router-link></li>
                <li><router-link class="dropdown-item" to="/platform/prices">Ceny platform</router-link></li>
              </ul>
            </li>
          </ul>

          <div class="d-flex align-items-center">
            <span v-if="user" class="text-white me-3">{{ user.email }}</span>
            <router-link
              v-if="!user"
              class="btn btn-outline-light me-2"
              to="/login"
            >Zaloguj</router-link>
            <button
              v-if="user"
              class="btn btn-outline-light"
              @click="() => userStore.logout()"
            >Wyloguj</button>
          </div>
        </div>
      </div>
    </nav>

    <main class="container py-4 min-vh-100">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useUserStore } from '@/composables/useUserStore'
import { onMounted } from 'vue'

const user = ref(null)
const userStore = useUserStore()

onMounted(() => {
  useUserStore().fetchUser()
  user.value = userStore.user
})

watch(() => userStore.user, (newUser) => {
  user.value = newUser
})

</script>

<style scoped>
main {
  background: #000000;
  width: 65%;
}

.custom-dropdown {
  position: relative;
}

.custom-dropdown .dropdown-menu {
  display: none;
  position: absolute;
  top: -45px;
  left: 0;
  top: 100%;
  min-width: 180px;
  max-height: 0;
  overflow: hidden;
  opacity: 0;
  transition: max-height 0.3s cubic-bezier(.4,2,.6,1), opacity 0.2s;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
  margin-top: 0.5rem;
  pointer-events: none;
  border-radius: 0.5rem;
  z-index: 1000;
}

.custom-dropdown:hover .dropdown-menu,
.custom-dropdown:focus-within .dropdown-menu {
  display: block;
  max-height: 500px;
  opacity: 1;
  pointer-events: auto;
}
</style>
