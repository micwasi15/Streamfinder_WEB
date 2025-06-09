<template>
  <div>
    <table class="table table-bordered table-striped align-middle text-center">
      <thead>
        <tr>
          <th>Kraj</th>
          <th>Cena (EUR)</th>
          <th>Cena (USD)</th>
          <th>Cena (PLN)</th>
          <th>Różnica % względem Polski</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="row in data" :key="row.country + '-' + row.plan">
          <td>{{ row.country }}</td>
          <td>{{ row.priceEUR }}</td>
          <td>{{ row.priceUSD }}</td>
          <td>{{ row.pricePLN }}</td>
          <td>
            <span
              :class="{
                'text-success': row.diffPercent !== '-' && Number(row.diffPercent) < 0,
                'text-danger': row.diffPercent !== '-' && Number(row.diffPercent) > 0
              }"
            >
              {{ row.diffPercent }}<span v-if="row.diffPercent !== '-'">%</span>
            </span>
          </td>
        </tr>
        <tr v-if="!data || data.length === 0">
          <td colspan="6" class="text-muted">Brak danych do wyświetlenia.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
const props = defineProps({
  data: Array
})
</script>