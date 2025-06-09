<template>
  <div class="bg-secondary">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">
      <div class="container-fluid">
        <router-link class="navbar-brand fw-bold" to="/">StreamFinder</router-link>
      </div>
    </nav>
    <div class="auth-layout">
      <div>
        <router-view />
      </div>
    </div>
  </div>
</template>

<style scoped>

.auth-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
}

.auth-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 70%;
}
</style>