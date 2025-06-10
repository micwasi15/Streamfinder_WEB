<template>
  <div v-if="isLoading" class="d-flex justify-content-center align-items-center" style="min-height: 40vh;">
    <div class="alert alert-info text-center w-100 mb-0">
      Ładowanie danych...
    </div>
  </div>
  <div v-else class="row py-4">
    <!-- Panel opcji -->
    <div class="col-12 col-md-4 mb-4 mb-md-0">
      <div class="card p-3">
        <h5 class="mb-3">Opcje</h5>
        <!-- Platforma -->
        <label class="form-label">Platforma</label>
        <select v-model="selectedPlatform" class="form-select mb-3">
          <option v-for="platform in platforms" :key="platform" :value="platform">
            {{ platform }}
          </option>
        </select>
        <!-- Plan -->
        <div v-if="plans.length > 1">
          <label class="form-label">Plan</label>
          <select v-model="selectedPlan" class="form-select mb-3">
            <option v-for="plan in plans" :key="plan.name" :value="plan.name">
              {{ plan.name }}
            </option>
          </select>
        </div>
      </div>
    </div>
    <!-- Panel tabeli -->
    <div class="col-12 col-md-8">
      <div class="card p-3">
        <PlatformPricesTable :data="tableData" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import api from '@/axios'
import PlatformPricesTable from '@/components/tables/PlatformPricesTable.vue'

const platforms = ref([])
const selectedPlatform = ref('')
const plans = ref([])
const selectedPlan = ref('')
const tableData = ref([])
const currencyExchangeRates = ref({})
const isLoading = ref(true)

onMounted(async () => {
  isLoading.value = true
  // Pobierz listę platform
  const res = await api.get('/api/public/shows/platforms')
  if (res.data) {
    platforms.value = res.data
    selectedPlatform.value = platforms.value.length > 0 ? platforms.value[0] : ''
  }
  const ratesRes = await api.get('/api/public/currency/latest')
  if (ratesRes.data) {
    currencyExchangeRates.value = ratesRes.data.currencyExchanges || []
  }
  // Pobierz plany i dane tabeli dla domyślnej platformy
  if (selectedPlatform.value) {
    const res2 = await api.get(`/api/public/streaming-platforms/${selectedPlatform.value}`)
    plans.value = res2.data.plans || []
    selectedPlan.value = plans.value.length > 0 ? plans.value[0].name : ''
    loadTableData()
  }
  isLoading.value = false
})

watch(selectedPlatform, async (platformId) => {
  tableData.value = []
  if (!platformId) {
    plans.value = []
    selectedPlan.value = ''
    return
  }
  const res = await api.get(`/api/public/streaming-platforms/${platformId}`)
  plans.value = res.data.plans || []
  selectedPlan.value = plans.value.length > 0 ? plans.value[0].name : ''
})

watch(selectedPlan, () => {
  loadTableData()
})

function getRates(currency) {
  return currencyExchangeRates.value.find(r => r.baseCurrency === currency) || {}
}

function loadTableData() {
  tableData.value = []
  if (!selectedPlatform.value || plans.value.length === 0 || !selectedPlan.value) {
    return
  }

  const plan = plans.value.find(p => p.name === selectedPlan.value)
  if (!plan) return

  const prices = plan.pricesPerCountry.map(priceObj => ({
    planName: plan.name,
    countryName: priceObj.countryName,
    currency: priceObj.currency,
    price: priceObj.price
  }))

  const polishPriceObj = prices.find(
    p => p.countryName === 'Poland'
  )
  let polishPricePLN = null
  if (polishPriceObj) {
    const rate = getRates(polishPriceObj.currency).plnExchangeRate || 1
    polishPricePLN = polishPriceObj.price * rate
  }

  tableData.value = prices.map(p => {
    const rates = getRates(p.currency)
    const priceEUR = p.price / (rates.euroExchangeRate || 1)
    const priceUSD = p.price / (rates.usdExchangeRate || 1)
    const pricePLN = p.price / (rates.plnExchangeRate || 1)
    let diff = null
    if (polishPricePLN) {
      diff = ((pricePLN - polishPricePLN) / polishPricePLN) * 100
    }
    return {
      country: p.countryName,
      plan: p.planName,
      priceEUR: priceEUR.toFixed(2),
      priceUSD: priceUSD.toFixed(2),
      pricePLN: pricePLN.toFixed(2),
      diffPercent: diff !== null ? diff.toFixed(1) : '-'
    }
  })
}
</script>

<style scoped>
.card {
  min-height: 300px;
}
</style>