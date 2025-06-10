<template>
  <div v-if="isLoading || message" class="text-center align-items-center d-flex flex-column justify-content-center vh-100">
    <p class="text-center text-white" v-if="message">{{ message }}</p>
  </div>
  <div v-else class="details-container">
    <div class="media-box mb-3" v-if="!isLoading && show">
      <iframe
        v-if="trailerEmbedUrl"
        :src="trailerEmbedUrl"
        frameborder="0"
        allowfullscreen
        class="trailer-iframe"
      ></iframe>
      <img
        v-else-if="posterUrl"
        :src="posterUrl"
        alt="plakat"
        class="poster-img"
      />
    </div>

    <div v-if="show" class="d-flex align-items-center justify-content-between mb-3 flex-wrap text-white">
      <div>
        <h2 class="mb-1">{{ show.title }}</h2>
        <div class="years" v-if="years">{{ years }}</div>
      </div>
      <div class="d-flex align-items-center gap-2">
        <span v-if="isLoggedIn" class="heart-icon" @click="toggleFavorite" style="cursor:pointer;">
          <i :class="['bi', isFavorite ? 'bi-heart-fill' : 'bi-heart']"></i>
        </span>
        <div class="rating-box" :class="ratingColorClass">
          {{ show.rating }}
        </div>
      </div>
    </div>

    <p v-if="show.overview" class="mb-3 text-white">{{ show.overview }}</p>

    <OptionsTable v-if="showType === 'film' && show" :streaming-options="show.streamingOptions"></OptionsTable>
    <SeriesDetail v-if="showType === 'series' && show" :show="show"></SeriesDetail>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/axios';
import SeriesDetail from './SeriesDetail.vue';
import { getShowHorizontalPosterUrl, getShowYears } from '@/utils/showUtils';
import OptionsTable from './OptionsTable.vue';
import { useUserStore } from '@/composables/useUserStore';

const $route = useRoute();

const show = ref(null);
const showType = ref(null);
const isLoading = ref(true);
const isFavorite = ref(false);

const userStore = useUserStore();
const isLoggedIn = computed(() => userStore.isAuthenticated);

const message = computed(() => {
  if (isLoading.value) return 'Ładowanie...';
  if (!show.value) return 'Nie znaleziono pokazu.';
  return '';
});

const posterUrl = computed(() => show.value ? getShowHorizontalPosterUrl(show.value) : '');
const years = computed(() => show.value ? getShowYears(show.value) : '');

const ratingColorClass = computed(() => {
  const rating = show.value?.rating;
  if (rating >= 80) return 'rating-darkgreen';
  if (rating >= 70) return 'rating-green';
  if (rating >= 60) return 'rating-yellow';
  if (rating >= 50) return 'rating-orange';
  return 'rating-red';
});

const trailerEmbedUrl = computed(() => {
  if (!show.value?.trailerURL) return null;
  if (show.value.trailerURL.includes('embed')) return show.value.trailerURL;
  const match = show.value.trailerURL.match(/(?:v=|\/)([0-9A-Za-z_-]{11})/);
  const id = match ? match[1] : show.value.trailerURL;
  return `https://www.youtube.com/embed/${id}`;
});

function toggleFavorite() {
  isFavorite.value = !isFavorite.value;
  if (isLoggedIn.value) {
    if (isFavorite.value) {
      api.post(`/shows/favorites/${show.value.id}`).catch(err => {
        console.error('Error adding to favorites:', err);
        isFavorite.value = false;
      });
    } else {
      api.delete(`/shows/favorites/${show.value.id}`).catch(err => {
        console.error('Error removing from favorites:', err);
        isFavorite.value = true;
      });
    }
  }
}

onMounted(async () => {
  let showId = $route.params.id;

  if (!showId) {
    isLoading.value = false;
    return;
  }

  let type;
  try {
    type = await api.get(`/api/public/shows/type/${showId}`);
  } catch {
    isLoading.value = false;
    return;
  }
  if (!type.data) {
    isLoading.value = false;
    return;
  }

  showType.value = type.data.showType;
  let res;
  try {
    res = await api.get(`/api/public/shows/${showType.value}/${showId}`);
  } catch {
    isLoading.value = false;
    return;
  }
  if (res.data) {
    show.value = res.data;
    if (isLoggedIn.value) {
      try {
        let favoriteRes = await api.get(`/shows/favorites/${showId}`);
        if (favoriteRes.data) {
          isFavorite.value = favoriteRes.data?.isFavorite || false;
        }
      } catch {}
    }
  }
  isLoading.value = false;
});
</script>

<style scoped>


.details-container {
  max-width: 700px;
  margin: 0 auto;
}

.media-box {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.trailer-iframe {
  width: 100%;
  height: 400px;
  border-radius: 12px;
  background: #222;
}

.poster-img {
  width: 100%;
  max-width: 320px;
  border-radius: 12px;
  object-fit: cover;
  background: #222;
}

.years {
  font-size: 1.1em;
  margin-bottom: 2px;
}

.btn-heart {
  font-size: 1.5rem;
  padding: 0.3em 0.6em;
}

.heart-icon {
  font-size: 2rem;
  color: #e74c3c;
  transition: color 0.2s;
}
.heart-icon:hover {
  color: #c0392b;
}
</style>