<template>
  <div v-if="props.favoritesMode && !isLoggedIn" class="d-flex justify-content-center align-items-center"
    style="min-height: 60vh;">
    <div class="alert alert-info text-center w-100 mb-0 text-bg-light">
      Aby przeglądać ulubione, musisz się zalogować.
    </div>
  </div>
  <div v-else class="container py-4">

    <form class="row g-3 mb-4" @submit.prevent="fetchShows">
      <div class="col-sm-8 col-md-5 col-lg-4">
        <label class="form-label">Szukaj po nazwie</label>
        <input v-model="searchQuery" type="text" class="form-control" placeholder="np. Breaking Bad" />
      </div>
      <div class="col-sm-4 col-md-3 col-lg-2">
        <label class="form-label">Gatunek</label>
        <select v-model="selectedGenre" class="form-select">
          <option value="">Wszystkie</option>
          <option v-for="genre in genres" :key="genre" :value="genre">{{ genre }}</option>
        </select>
      </div>
      <div class="col-auto align-self-end">
        <button type="button" class="btn btn-outline-light" @click="showAdvanced = !showAdvanced">
          {{ showAdvanced ? 'Ukryj zaawansowane' : 'Zaawansowane opcje' }}
        </button>
      </div>
    </form>

    <Transition name="slide-fade">
      <form v-if="showAdvanced" class="row g-3 align-items-end mb-4 advanced-form" @submit.prevent="fetchShows">
        <div class="col-sm-3 col-md-2">
          <label class="form-label">Rok od</label>
          <input v-model.number="yearFrom" type="number" class="form-control" />
        </div>
        <div class="col-sm-3 col-md-2">
          <label class="form-label">Rok do</label>
          <input v-model.number="yearTo" type="number" class="form-control" />
        </div>
        <div class="col-sm-3 col-md-2">
          <label class="form-label">Min rating: <strong>{{ minRating }}</strong></label>
          <input v-model.number="minRating" type="range" min="0" max="100" step="1" class="form-range" />
        </div>
        <div class="col-12 mt-2">
          <label class="form-label">Aktorzy</label>
          <div v-for="(actor, idx) in actors" :key="idx" class="mb-2 row" style="max-width:400px;">
            <div class="col-10 pe-0">
              <input v-model="actors[idx]" type="text" class="form-control" :placeholder="`Aktor ${idx + 1}`"
                @input="onActorInput(idx)" :maxlength="50" />
            </div>
            <div class="col-2 d-flex align-items-center">
              <button type="button" class="btn btn-outline-danger btn-sm w-100" @click="removeActor(idx)"
                v-if="actors[idx]" tabindex="-1" aria-label="Wyczyść pole">
                &times;
              </button>
            </div>
          </div>
        </div>
      </form>
    </Transition>

    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-3">
      <div class="d-flex align-items-center gap-2">
        <button class="btn btn-outline-secondary" :disabled="page <= 1" @click="changePage(page - 1)">
          &lt;
        </button>
        <input type="number" class="form-control d-inline-block w-auto align-self-center no-spin"
          style="width: 70px; min-width: 70px; max-width: 70px;" v-model.number="pageInput" @input="goToPage" min="1"
          :max="totalPages" />
        <span>/ {{ totalPages }}</span>
        <button class="btn btn-outline-secondary" :disabled="page >= totalPages" @click="changePage(page + 1)">
          &gt;
        </button>
      </div>

      <div class="d-flex align-items-center gap-2 flex-wrap">
        <div class="btn-group" role="group">
          <button v-for="opt in [15, 30, 45]" :key="opt" @click="setPageSize(opt)"
            :class="['btn', pageSize === opt ? 'btn-secondary' : 'btn-outline-secondary']">{{ opt }}</button>
        </div>
        <div class="btn-group ms-3" role="group">
          <button @click="viewMode = 'details'"
            :class="['btn', viewMode === 'details' ? 'btn-info' : 'btn-outline-info']">
            <i class="bi bi-list-task"></i>
          </button>
          <button @click="viewMode = 'tiles'" :class="['btn', viewMode === 'tiles' ? 'btn-info' : 'btn-outline-info']">
            <i class="bi bi-grid-3x3-gap"></i>
          </button>
        </div>
      </div>
    </div>

    <div style="max-height: 600px; overflow-y: auto;">
      <div v-if="viewMode === 'details'" class="list-group">
        <ShowListElem v-for="show in shows" :key="show.id" :show="show" :tile="false" />
      </div>
      <div v-else class="row g-3">
        <div v-for="show in shows" :key="show.id" class="col-12 col-md-4">
          <ShowListElem :show="show" :tile="true" />
        </div>
      </div>
      <div v-if="!isLoading && shows.length === 0" class="d-flex justify-content-center align-items-center py-5">
        Brak wyników wyszukiwania.
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import api from '@/axios'
import ShowListElem from '@/components/shows/ShowListElem.vue'
import { useUserStore } from '@/composables/useUserStore'

const props = defineProps({
  favoritesMode: { type: Boolean, default: false }
})

const searchQuery = ref('')
const yearFrom = ref(1970)
const yearTo = ref(new Date().getFullYear())
const minRating = ref(0)
const actors = ref([''])
const showAdvanced = ref(false)
const selectedGenre = ref('')
const genres = ref([])

const page = ref(1)
const pageInput = ref(1)
const pageSize = ref(15)
const totalPages = ref(1)

const viewMode = ref('details')
const shows = ref([])
const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isAuthenticated)
const isLoading = ref(false)

onMounted(() => {
  fetchGenres()
  fetchShows()
})

function onActorInput(idx) {
  if (
    actors.value.length < 3 &&
    idx === actors.value.length - 1 &&
    actors.value[idx]?.trim()
  ) {
    actors.value.push('')
  }

  while (
    actors.value.length > 1 &&
    !actors.value[actors.value.length - 1] &&
    !actors.value[actors.value.length - 2]
  ) {
    actors.value.pop()
  }
}

function removeActor(idx) {
  if (actors.value.length - 1 === idx) {
    actors.value[idx] = ''
    return
  }
  actors.value.splice(idx, 1)
  if (actors.value.length === 0 || (actors.value.length === 1 && !actors.value[0])) {
    actors.value.push('')
    onActorInput(0)
  }
}

const fetchGenres = async () => {
  try {
    const res = await api.get('/api/public/shows/genres')
    genres.value = res.data
  } catch (error) {
    console.error('Błąd podczas pobierania gatunków:', error)
  }
}

const fetchShows = async () => {
  isLoading.value = true
  const params = {
    title: searchQuery.value,
    maxYear: yearTo.value,
    minYear: yearFrom.value,
    minRating: minRating.value,
    page: page.value - 1,
    size: pageSize.value
  }

  if (selectedGenre.value) {
    params.genres = selectedGenre.value
  }

  const filteredActors = actors.value.map(a => a.trim()).filter(a => a)
  if (filteredActors.length) {
    params.actors = filteredActors.join(',')
  }

  let res
  if (props.favoritesMode) {
    res = await api.get('/shows/favorites', { params })
  } else {
    res = await api.get('/api/public/shows/search', { params })
  }
  shows.value = res.data.content
  totalPages.value = res.data.totalPages
  isLoading.value = false
}

const setPageSize = (size) => {
  pageSize.value = size
  page.value = 1
  pageInput.value = 1
  fetchShows()
}

const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    page.value = newPage
    pageInput.value = newPage
  }
}

const goToPage = () => {
  if (pageInput.value >= 1 && pageInput.value <= totalPages.value) {
    page.value = pageInput.value
  } else {
    pageInput.value = page.value
  }
}

watch(page, (val) => {
  pageInput.value = val
  fetchShows()
}, { immediate: true })

watch(
  [searchQuery, yearFrom, yearTo, pageSize, isLoggedIn, minRating, showAdvanced, () => actors.value.slice(), selectedGenre],
  () => {
    page.value = 1
    pageInput.value = 1
    fetchShows()
  }
)

watch(() => props.favoritesMode, () => {
  fetchShows()
})
</script>

<style scoped>
div {
  color: white;
}

.no-spin::-webkit-inner-spin-button,
.no-spin::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.no-spin {
  -moz-appearance: textfield;
}

.btn:focus,
.btn:active,
.btn:focus-visible {
  box-shadow: none !important;
  outline: none !important;
  background-color: inherit !important;
  color: inherit !important;
  border-color: inherit !important;
}

.btn:active {
  filter: none !important;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: max-height 0.7s cubic-bezier(.4, 2, .6, 1), opacity 0.3s;
  overflow: hidden;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  max-height: 500px;
  opacity: 1;
}

.advanced-form {
  will-change: max-height, opacity;
}
</style>