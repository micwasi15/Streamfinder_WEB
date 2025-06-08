<template>
  <div class="details-container">
    <!-- Trailer lub plakat -->
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

    <!-- Tytuł, lata, serce, ocena -->
    <div v-if="!isLoading && show" class="d-flex align-items-center justify-content-between mb-3 flex-wrap">
      <div>
        <h2 class="mb-1">{{ show.title }}</h2>
        <div class="years" v-if="years">{{ years }}</div>
      </div>
      <div class="d-flex align-items-center gap-2">
        <span class="heart-icon" @click="toggleFavorite" style="cursor:pointer;">
          <i :class="['bi', isFavorite ? 'bi-heart-fill' : 'bi-heart']"></i>
        </span>
        <div class="rating-box" :class="ratingColorClass">
          {{ show.rating }}
        </div>
      </div>
    </div>

    <p v-if="!isLoading && show.overview" class="mb-3">{{ show.overview }}</p>

    <!-- Szczegóły -->
    <p class="text-center" v-if="message">{{ message }}</p>
    <OptionsTable v-if="!isLoading && showType === 'film' && show" :streaming-options="show.streamingOptions"></OptionsTable>
    <SeriesDetail v-if="!isLoading && showType === 'series' && show" :show="show"></SeriesDetail>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import api from '@/axios';
import SeriesDetail from './SeriesDetail.vue';
import { getShowHorizontalPosterUrl, getShowYears } from '@/utils/showUtils';
import OptionsTable from './OptionsTable.vue';

const $route = useRoute();

const show = ref(null);
const showType = ref(null);
const isLoading = ref(true);
const isFavorite = ref(false);

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
}

onMounted(async () => {
  let showId = $route.params.id;

  if (showId) {
    let type = await api.get(`/shows/type/${showId}`);
    if (type.data) {
      showType.value = type.data.showType;
      let res = await api.get(`/shows/${showType.value}/${showId}`);
      if (res.data) {
        show.value = res.data;
      }
    }
    isLoading.value = false;
  }
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
  max-width: 600px;
  height: 340px;
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
  color: #bbb;
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