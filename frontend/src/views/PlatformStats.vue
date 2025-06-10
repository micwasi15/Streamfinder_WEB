<template>
  <div class="row py-4">
    <div class="col-12 col-md-4 mb-4 mb-md-0">
      <div class="card p-3">
        <h5 class="mb-3">Opcje</h5>
        <label class="form-label">Gatunek</label>
        <select v-model="selectedGenre" class="form-select mb-3">
          <option value="">Wszystkie</option>
          <option v-for="genre in genres" :key="genre" :value="genre">{{ genre }}</option>
        </select>
        <label class="form-label">Serwisy streamingowe</label>
        <div class="scrollable-list">
          <div class="form-check d-flex align-items-center" v-for="service in streamingServices" :key="service">
            <input class="form-check-input" type="checkbox" :id="service" v-model="selectedServices" :value="service">
            <label class="form-check-label ms-2" :for="service">
              {{ service }}
            </label>
          </div>
        </div>
      </div>
    </div>
    <div class="col-12 col-md-8">
      <div class="card p-3 h-100 d-flex align-items-center">
        <PlatformStatsChart :stats="stats" :selected-platforms="selectedServices" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
const genres = ref([])
const streamingServices = ref([])

const selectedGenre = ref('')
const selectedServices = ref([])
const stats = ref([])

onMounted(() => {
  const genresRes = api.get('/api/public/shows/genres')
  const servicesRes = api.get('/api/public/shows/platforms')
  Promise.all([genresRes, servicesRes]).then(([genresData, servicesData]) => {
    genres.value = genresData.data
    streamingServices.value = servicesData.data
    loadStats()
  }).catch(error => {
    console.error('Błąd podczas pobierania danych:', error)
  })
})

const loadStats = () => {
  const params = {
    platforms: streamingServices.value.join(','),
  }
  if (selectedGenre.value) {
    params.genre = selectedGenre.value
  } else {
    delete params.genre
  }
  const res = api.get('/api/public/shows/platform-stats', { params })
  res.then(response => {
    stats.value = response.data
  }).catch(error => {
    console.error('Błąd podczas pobierania statystyk:', error)
  })
}

watch(selectedGenre, () => {
  loadStats()
}, { immediate: true })

import PlatformStatsChart from '@/components/charts/PlatformStatsChart.vue'
import api from '@/axios'
</script>

<style scoped>
.card {
  min-height: 300px;
}
.scrollable-list {
  max-height: 350px;
  min-height: 120px;
  overflow-y: auto;
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  padding: 0.5rem 0.75rem;
  background: #f8f9fa;
}
</style>