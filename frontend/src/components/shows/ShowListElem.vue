<!-- components/ShowListElem.vue -->
<template>
  <router-link
    :to="`/shows/${show.id}`"
    class="list-group-item"
    :class="tile ? 'tile-flex' : 'show-flex'"
    style="cursor: pointer; text-decoration: none; color: inherit;"
  >
    <img
      v-if="posterUrl"
      :src="posterImgSrc"
      alt="plakat"
      class="poster-img"
      :class="tile ? 'poster-img-tile' : ''"
      @error="onImgError"
    />
    <div v-if="!tile" class="show-content">
      <div class="row-flex">
        <div class="title-years">
          <h5 class="mb-1 flex-grow-1">{{ show.title }}</h5>
          <div class="years" v-if="years">{{ years }}</div>
        </div>
        <div class="rating-box" :class="ratingColorClass">
          {{ show.rating }}
        </div>
      </div>
      <p class="mb-0">{{ show.overview }}</p>
    </div>
    <div v-else class="tile-content">
      <div class="tile-row">
        <div class="tile-title-years">
          <h5 class="mb-1">{{ show.title }}</h5>
          <div class="years" v-if="years">{{ years }}</div>
        </div>
        <div class="rating-box" :class="ratingColorClass">
          {{ show.rating }}
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { getShowHorizontalPosterUrl, getShowYears } from '@/utils/showUtils'
import placeholderImg from '@/assets/img/placeholder.jpg'

defineEmits(['show-clicked'])

const props = defineProps({
  show: Object,
  tile: Boolean
})

const posterUrl = computed(() => {
  if (props.show) {
    return getShowHorizontalPosterUrl(props.show)
  }
  return null
})

const years = computed(() => getShowYears(props.show))

const ratingColorClass = computed(() => {
  const rating = props.show.rating
  if (rating >= 80) return 'rating-darkgreen'
  if (rating >= 70) return 'rating-green'
  if (rating >= 60) return 'rating-yellow'
  if (rating >= 50) return 'rating-orange'
  return 'rating-red'
})

const posterImgSrc = ref(posterUrl.value)
watch(posterUrl, (val) => {
  posterImgSrc.value = val
})

function onImgError(e) {
  posterImgSrc.value = placeholderImg
}
</script>

<style scoped>
.list-group-item {
  background-color: #2c3e50;
  color: white;
  border: none;
  position: relative;
  padding: 16px;
}

.show-flex {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.poster-img {
  width: 20%;
  height: 10%;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
  background: #222;
}

.show-content {
  flex: 1;
  min-width: 0;
}

.row-flex {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title-years {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.years {
  font-size: 0.95em;
  color: #bbb;
  margin-top: -2px;
  margin-bottom: 2px;
}

.flex-grow-1 {
  flex-grow: 1;
  margin-bottom: 0;
}

/* --- Kafelkowy widok --- */
.tile-flex {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0;
  background: #2c3e50;
  border-radius: 10px;
  overflow: hidden;
}

.poster-img-tile {
  width: 100%;
  height: 70%;
  object-fit: cover;
  border-radius: 0;
  background: #222;
}

.tile-content {
  width: 100%;
  padding: 12px 16px 16px 16px;
  box-sizing: border-box;
}

.tile-row {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: space-between;
}

.tile-title-years {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
