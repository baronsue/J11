<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useRestaurantStore } from '../stores/restaurants'
import { useBookingStore } from '../stores/booking'
import { computed, onMounted, ref } from 'vue'
import { useAuthStore } from '../stores/auth'

const route = useRoute()
const router = useRouter()
const restaurantStore = useRestaurantStore()
const bookingStore = useBookingStore()
const authStore = useAuthStore()

const restaurantId = parseInt(route.params.id)
const loading = ref(true)
const restaurant = computed(() => restaurantStore.getRestaurantById(restaurantId))

onMounted(async () => {
  // Fetch restaurant if not in store
  if (!restaurant.value) {
    await restaurantStore.fetchRestaurantById(restaurantId)
  }
  loading.value = false
})

function goBack() {
  router.back()
}

function startBooking() {
  // 需要登录后才能预约
  if (!authStore.isAuthenticated) {
    router.push({ name: 'login', query: { redirect: `/restaurant/${restaurantId}` } })
    return
  }

  bookingStore.startBooking(restaurantId)
  router.push({ name: 'booking-flow', query: { restaurantId } })
}
</script>

<template>
  <!-- Loading State -->
  <div v-if="loading" class="loading-view">
    <p>Loading...</p>
  </div>
  
  <!-- Restaurant Detail -->
  <div v-else-if="restaurant" class="detail-view">
    <div class="hero-image">
      <button class="back-btn" @click="goBack">←</button>
      <img :src="restaurant.image" :alt="restaurant.name" />
    </div>
    
    <div class="content">
      <div class="header">
        <h1 class="name">{{ restaurant.name }}</h1>
        <div class="rating">★ {{ Number(restaurant.rating).toFixed(1) }}</div>
      </div>
      
      <div class="meta-row">
        <span>{{ restaurant.cuisine }}</span>
        <span>•</span>
        <span>{{ restaurant.priceRange }}</span>
      </div>
      
      <div class="address-row" v-if="restaurant.address">
        <span>📍 {{ restaurant.address }}</span>
      </div>
      
      <div class="hours-row" v-if="restaurant.openingTime">
        <span>🕐 {{ restaurant.openingTime }} - {{ restaurant.closingTime }}</span>
      </div>

      <div class="description">
        <h2 class="text-h2">{{ $t('detail.about') }}</h2>
        <p class="text-body">{{ restaurant.description }}</p>
      </div>

      <div class="action-bar">
        <button class="btn btn-primary btn-block" @click="startBooking">
          {{ $t('detail.bookBtn') }}
        </button>
      </div>
    </div>
  </div>
  
  <!-- Not Found -->
  <div v-else class="not-found">
    <p>{{ $t('detail.notFound') }}</p>
    <button class="btn btn-primary" @click="goBack">{{ $t('detail.goBack') }}</button>
  </div>
</template>

<style scoped>
.detail-view {
  background: var(--color-surface);
  min-height: 100vh;
  position: relative;
}

.hero-image {
  position: relative;
  height: 300px;
  width: 100%;
}

.hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.back-btn {
  position: absolute;
  top: var(--spacing-md);
  left: var(--spacing-md);
  background: rgba(255, 255, 255, 0.9);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: var(--shadow-sm);
  z-index: 10;
}

.content {
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg) var(--radius-lg) 0 0;
  background: var(--color-surface);
  margin-top: -24px;
  position: relative;
  min-height: 50vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--spacing-sm);
}

.name {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}

.rating {
  background: var(--color-primary);
  color: white;
  padding: 4px 8px;
  border-radius: var(--radius-full);
  font-weight: 600;
  font-size: 14px;
}

.meta-row {
  display: flex;
  gap: 8px;
  color: var(--color-text-sub);
  font-size: 16px;
  margin-bottom: var(--spacing-sm);
}

.address-row,
.hours-row {
  color: var(--color-text-sub);
  font-size: 14px;
  margin-bottom: var(--spacing-sm);
}

.loading-view {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  color: var(--color-text-sub);
}

.not-found {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  gap: var(--spacing-md);
}

.description {
  margin-bottom: 80px;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: var(--spacing-md);
  background: white;
  border-top: 1px solid var(--color-border);
  max-width: 600px;
  margin: 0 auto;
}
</style>
