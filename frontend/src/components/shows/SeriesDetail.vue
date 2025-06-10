<template>
  <div>
    <h3 class="text-white mb-3">Sezony</h3>
    <select v-model="selectedSeason" class="form-select w-auto mb-4">
      <option v-for="season in seasons" :key="season.title" :value="season.title">
        {{ season.title }}
      </option>
    </select>

    <div v-if="currentSeason">
      <div
        v-for="(episode, idx) in currentSeason.episodes"
        :key="episode.id || idx"
        class="mb-3 border rounded bg-dark-subtle p-3"
      >
        <div class="d-flex justify-content-between align-items-center">
          <div>
            <strong>{{ episode.title }}</strong>
            <span class="text-muted ms-2"> {{ episode.number }}</span>
          </div>
          <button class="btn btn-sm btn-outline-dark" @click="toggleEpisode(idx)">
            {{ expandedEpisodes[idx] ? 'Ukryj opcje' : 'Pokaż opcje' }}
          </button>
        </div>
        <div class="mt-2">{{ episode.overview }}</div>
        <Transition name="slide-fade">
          <div v-if="expandedEpisodes[idx]" class="mt-3">
            <OptionsTable :streaming-options="episode.streamingOptions" />
          </div>
        </Transition>
      </div>
    </div>
    <div v-else class="text-muted">Brak danych o sezonach.</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import OptionsTable from './OptionsTable.vue'

const props = defineProps({
  show: Object
})

const seasons = computed(() => props.show?.seasons || [])
const selectedSeason = ref(null)

onMounted(() => {
  if (seasons.value.length > 0) {
    selectedSeason.value = seasons.value[0].title
  }
})

const currentSeason = computed(() =>
  seasons.value.find(season => season.title === selectedSeason.value)
)

const expandedEpisodes = ref({})

watch(selectedSeason, () => {
  expandedEpisodes.value = {}
})

function toggleEpisode(idx) {
  expandedEpisodes.value[idx] = !expandedEpisodes.value[idx]
}
</script>

<style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: max-height 0.7s cubic-bezier(.4,2,.6,1), opacity 0.3s;
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