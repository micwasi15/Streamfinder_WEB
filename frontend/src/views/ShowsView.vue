<template>
  <div v-if="props.favoritesMode && !isLoggedIn" class="d-flex justify-content-center align-items-center"
    style="min-height: 60vh;">
    <div class="alert alert-warning text-center text-black">
      Aby przeglądać ulubione, musisz się zalogować.
    </div>
  </div>
  <div v-else class="container py-4">

    <!-- Pasek wyszukiwania i lata -->
    <form class="row g-3 align-items-end mb-4" @submit.prevent="fetchShows">
      <div class="col-sm-6 col-md-4">
        <label class="form-label">Szukaj po nazwie</label>
        <input v-model="searchQuery" type="text" class="form-control" placeholder="np. Breaking Bad" />
      </div>
      <div class="col-sm-3 col-md-2">
        <label class="form-label">Rok od</label>
        <input v-model.number="yearFrom" type="number" class="form-control" />
      </div>
      <div class="col-sm-3 col-md-2">
        <label class="form-label">Rok do</label>
        <input v-model.number="yearTo" type="number" class="form-control" />
      </div>
    </form>

    <!-- Paginacja + opcje -->
    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-3">
      <!-- Strona -->
      <div class="d-flex align-items-center gap-2">
        <button class="btn btn-outline-secondary" :disabled="page <= 1" @click="changePage(page - 1)">
          &lt;
        </button>
        <input type="number" class="form-control d-inline-block w-auto" style="width: 70px;" v-model.number="pageInput"
          @keyup.enter="goToPage" min="1" :max="totalPages" />
        <span>/ {{ totalPages }}</span>
        <button class="btn btn-outline-secondary" :disabled="page >= totalPages" @click="changePage(page + 1)">
          &gt;
        </button>
      </div>

      <!-- 📏 Ilość + Tryb -->
      <div class="d-flex align-items-center gap-2 flex-wrap">
        <div class="btn-group" role="group">
          <button v-for="opt in [15, 30, 45]" :key="opt" @click="setPageSize(opt)"
            :class="['btn', pageSize === opt ? 'btn-secondary' : 'btn-outline-secondary']">{{ opt }}</button>
        </div>
        <div class="btn-group ms-3" role="group">
          <button @click="viewMode = 'details'"
            :class="['btn', viewMode === 'details' ? 'btn-info' : 'btn-outline-info']">Szczegóły</button>
          <button @click="viewMode = 'tiles'"
            :class="['btn', viewMode === 'tiles' ? 'btn-info' : 'btn-outline-info']">Kafelki</button>
        </div>
      </div>
    </div>

    <!-- Lista wyników -->
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
import { computed, ref, watch } from 'vue'
import api from '@/axios'
import ShowListElem from '@/components/shows/ShowListElem.vue'
import { useUserStore } from '@/composables/useUserStore'

const props = defineProps({
  favoritesMode: { type: Boolean, default: false }
})

const searchQuery = ref('')
const yearFrom = ref(2000)
const yearTo = ref(2025)

const page = ref(1)
const pageInput = ref(1)
const pageSize = ref(15)
const totalPages = ref(1)

const viewMode = ref('details')
const shows = ref([])
const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isAuthenticated)

const fetchShows = async () => {
  const params = {
    title: searchQuery.value,
    maxYear: yearTo.value,
    minYear: yearFrom.value,
    page: page.value - 1,
    size: pageSize.value
  }

  let res
  if (props.favoritesMode) {
    res = await api.get('/shows/favorites', { params })
  } else {
    res = await api.get('/shows/search', { params })
  }
  shows.value = res.data.content
  totalPages.value = res.data.total_pages
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

watch([searchQuery, yearFrom, yearTo, pageSize, isLoggedIn], () => {
  page.value = 1
  pageInput.value = 1
  fetchShows()
})

watch(() => props.favoritesMode, () => {
  fetchShows()
})
</script>

<style scoped>
div {
  color: white;
}
</style>