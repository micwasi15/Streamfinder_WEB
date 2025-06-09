<template>
  <div class="table-responsive-custom">
    <table class="table table-bordered text-center align-middle options-table rounded-table">
      <thead>
        <tr>
          <th class="sticky-col"></th>
          <th v-for="service in services" :key="service.name">
            <img :src="service.logoURL" :alt="service.name" class="service-logo" />
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="country in countries" :key="country.code">
          <th class="text-start sticky-col bg-white">
            {{ country.name }}
          </th>
          <td v-for="service in services" :key="service.name">
            <div class="d-flex justify-content-center align-items-center h-100">
              <button
                v-if="isAvailable(country.code, service.name)"
                class="btn btn-success btn-sm"
                @click="play(country.code, service.name)"
              >
                Odtwórz
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { getAvailabilityByCountry, getStreamingOptionsServices, getStreamingOptionsCountries, getVideoLink } from '@/utils/streamingOptionsUtils'
import { ref, watch, onMounted } from 'vue'

const props = defineProps({
  streamingOptions: Object,
})

const services = ref([])
const countries = ref([])
const availability = ref({})

async function updateData() {
  if (!props.streamingOptions) {
    services.value = []
    countries.value = []
    availability.value = {}
    return
  }
  services.value = getStreamingOptionsServices(props.streamingOptions)
  countries.value = await getStreamingOptionsCountries(props.streamingOptions)
  availability.value = getAvailabilityByCountry(props.streamingOptions, services.value)
}

onMounted(updateData)
watch(() => props.streamingOptions, updateData, { deep: true })

function isAvailable(countryCode, serviceName) {
  return availability.value?.[countryCode]?.[serviceName] || false
}

function play(countryCode, serviceName) {
  const url = getVideoLink(props.streamingOptions, serviceName, countryCode)
  if (url)
    window.open(url, '_blank')
  else
    alert('Brak dostępnego linku do odtworzenia.')
}
</script>

<style scoped>
.table-responsive-custom {
  overflow-x: auto;
  border-radius: 1rem;
  background: white;
}

.rounded-table {
  border-radius: 0.5rem;
  overflow: hidden;
}

.sticky-col {
  position: sticky;
  left: 0;
  background: white;
  z-index: 2;
  min-width: 120px;
  max-width: 200px;
  box-shadow: 2px 0 2px -2px #ccc;
}

.service-logo {
  height: 32px;
  max-width: 80px;
  object-fit: contain;
}
</style>