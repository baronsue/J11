<script setup>
defineProps({
  restaurant: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <div class="restaurant-card">
    <div class="image-wrapper">
      <img 
        :src="restaurant.image" 
        :alt="restaurant.name" 
        loading="lazy" 
        @error="$event.target.style.display='none'; $event.target.nextElementSibling.style.display='flex'"
      />
      <div class="fallback-image">🍽️</div>
      <div class="rating-badge">
        <span>★</span> {{ Number(restaurant.rating).toFixed(1) }}
      </div>
    </div>
    <div class="content">
      <div class="header">
        <h3 class="name">{{ restaurant.name }}</h3>
        <span class="price">{{ restaurant.priceRange }}</span>
      </div>
      <p class="meta">{{ restaurant.cuisine }} • {{ restaurant.city }}</p>
    </div>
  </div>
</template>

<style scoped>
.restaurant-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
  margin-bottom: var(--spacing-md);
}

.restaurant-card:active {
  transform: scale(0.98);
}

.image-wrapper {
  position: relative;
  height: 200px;
  width: 100%;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.fallback-image {
  display: none;
  width: 100%;
  height: 100%;
  background: var(--color-bg);
  align-items: center;
  justify-content: center;
  font-size: 40px;
}

.rating-badge {
  position: absolute;
  top: var(--spacing-sm);
  right: var(--spacing-sm);
  background: white;
  padding: 4px 8px;
  border-radius: var(--radius-full);
  font-weight: 600;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
  box-shadow: var(--shadow-sm);
}

.rating-badge span {
  color: #FFB400;
}

.content {
  padding: var(--spacing-md);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.name {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text-main);
}

.price {
  font-size: 14px;
  color: var(--color-text-sub);
}

.meta {
  font-size: 14px;
  color: var(--color-text-sub);
}
</style>
