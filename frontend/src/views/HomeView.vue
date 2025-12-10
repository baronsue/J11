<script setup>
import { onMounted } from 'vue'
import { useRestaurantStore } from '../stores/restaurants'
import { useAuthStore } from '../stores/auth'
import RestaurantCard from '../components/RestaurantCard.vue'
import LanguageSwitcher from '../components/LanguageSwitcher.vue'
import { useRouter } from 'vue-router'

const store = useRestaurantStore()
const authStore = useAuthStore()
const router = useRouter()

onMounted(() => {
  store.fetchRestaurants()
})

function selectRestaurant(id) {
  router.push({ name: 'restaurant-detail', params: { id } })
}

function goToProfile() {
  router.push(authStore.isAuthenticated ? '/profile' : '/login')
}
</script>

<template>
  <div class="home-view">
    <header class="header">
      <div class="top-bar">
        <h1 class="text-h1">{{ $t('home.discover') }}</h1>
        <LanguageSwitcher />
      </div>
      <p class="text-body subtitle">{{ $t('home.subtitle') }}</p>
    </header>

    <!-- Loading State -->
    <div v-if="store.loading" class="loading-state">
      <p>Loading restaurants...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="store.error" class="error-state">
      <p>{{ store.error }}</p>
      <button class="btn btn-primary" @click="store.fetchRestaurants()">Retry</button>
    </div>

    <!-- Restaurant List -->
    <main v-else class="restaurant-list">
      <RestaurantCard 
        v-for="restaurant in store.restaurants" 
        :key="restaurant.id" 
        :restaurant="restaurant"
        @click="selectRestaurant(restaurant.id)"
      />
      <div v-if="store.restaurants.length === 0" class="empty-state">
        <p class="text-body">No restaurants found.</p>
      </div>
    </main>
    
    <!-- Bottom Nav -->
    <nav class="bottom-nav">
      <RouterLink to="/" class="nav-item active">
        <span class="icon">🏠</span>
        <span>{{ $t('nav.explore') }}</span>
      </RouterLink>
      <RouterLink to="/reservations" class="nav-item">
        <span class="icon">📅</span>
        <span>{{ $t('nav.bookings') }}</span>
      </RouterLink>
      <a class="nav-item" @click="goToProfile">
        <span class="icon">👤</span>
        <span>{{ $t('nav.profile') }}</span>
      </a>
    </nav>
  </div>
</template>

<style scoped>
.home-view {
  padding-top: var(--spacing-lg);
  padding-bottom: 80px; /* Space for bottom nav */
}

.header {
  margin-bottom: var(--spacing-lg);
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-sm);
}

.text-h1 {
  margin-bottom: 0;
}

.subtitle {
  margin-bottom: var(--spacing-md);
}

.loading-state,
.error-state,
.empty-state {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--color-text-sub);
}

.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-top: 1px solid var(--color-border);
  display: flex;
  justify-content: center;
  gap: var(--spacing-xl);
  padding: 12px 0;
  z-index: 100;
}

@media (max-width: 768px) {
  .bottom-nav {
    max-width: 100%;
  }
}

.restaurant-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  padding-bottom: 80px;
}

@media (min-width: 768px) {
  .restaurant-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: var(--spacing-lg);
  }
}
.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 10px;
  color: var(--color-text-sub);
  gap: 4px;
  cursor: pointer;
}

.nav-item.active {
  color: var(--color-primary);
}

.nav-item .icon {
  font-size: 20px;
}
</style>
