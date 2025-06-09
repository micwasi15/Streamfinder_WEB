<template>
  <div style="width:100%;max-width:700px;">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js'

ChartJS.register(BarElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

const props = defineProps({
  stats: Array,
  selectedPlatforms: Array
})

const filteredStats = computed(() => {
  if (!props.selectedPlatforms?.length) return props.stats || []
  return (props.stats || []).filter(item =>
    props.selectedPlatforms.includes(item.platformName)
  )
})

const chartData = computed(() => {
  const labels = filteredStats.value.map(item => item.platformName)
  const films = filteredStats.value.map(item => item.totalFilms)
  const series = filteredStats.value.map(item => item.totalSeries)

  return {
    labels,
    datasets: [
      {
        label: 'Filmy',
        data: films,
        backgroundColor: '#e50914'
      },
      {
        label: 'Seriale',
        data: series,
        backgroundColor: '#5a2d82'
      }
    ]
  }
})

const chartOptions = {
  responsive: true,
  plugins: {
    legend: { display: true },
    title: {
      display: true,
      text: 'Liczba filmów i seriali na platformach'
    }
  },
  scales: {
    y: { beginAtZero: true }
  }
}
</script>