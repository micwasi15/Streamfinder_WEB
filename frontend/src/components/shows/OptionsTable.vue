<template>
  <table class="table table-bordered text-center align-middle options-table">
    <thead>
      <tr>
        <th></th>
        <th v-for="service in services" :key="service.name">
          <img :src="service.logoURL" :alt="service.name" class="service-logo" />
        </th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="country in countries" :key="country.code">
        <th class="text-start">{{ country.name }}</th>
        <td v-for="service in services" :key="service.name">
          <button v-if="isAvailable(country.code, service.name)" class="btn btn-success btn-sm"
            @click="play(country.code, service.name)">
            Odtwórz
          </button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
import { getAvailabilityByCountry, getStreamingOptionsServices, getStreamingOptionsCountries } from '@/utils/streamingOptionsUtils'
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
  alert(`Odtwarzanie dla ${countryCode} w ${serviceName}`)
}
</script>

<style scoped>
.options-table {
  min-width: 400px;
}

.service-logo {
  height: 32px;
  max-width: 80px;
  object-fit: contain;
}
</style>