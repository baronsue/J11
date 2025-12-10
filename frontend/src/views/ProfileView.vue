<script setup>
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

const authStore = useAuthStore()
const router = useRouter()

onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push('/login')
  }
})

function handleLogout() {
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <div class="profile-view" v-if="authStore.user">
    <header class="header">
      <h1 class="text-h1">{{ $t('profile.title') }}</h1>
    </header>

    <div class="profile-card">
      <img :src="authStore.user.avatar" class="avatar" />
      <div class="info">
        <h2 class="name">{{ authStore.user.name }}</h2>
        <p class="email">{{ authStore.user.email }}</p>
        <p class="meta">{{ $t('profile.memberSince') }} 2025</p>
      </div>
    </div>

    <div class="actions">
      <button class="btn btn-outline btn-block logout-btn" @click="handleLogout">
        {{ $t('auth.logout') }}
      </button>
    </div>

    <!-- Bottom Nav -->
    <nav class="bottom-nav">
      <RouterLink to="/" class="nav-item">
        <span class="icon">🏠</span>
        <span>{{ $t('nav.explore') }}</span>
      </RouterLink>
      <RouterLink to="/reservations" class="nav-item">
        <span class="icon">📅</span>
        <span>{{ $t('nav.bookings') }}</span>
      </RouterLink>
      <RouterLink to="/profile" class="nav-item active">
        <span class="icon">👤</span>
        <span>{{ $t('nav.profile') }}</span>
      </RouterLink>
    </nav>
  </div>
</template>

<style scoped>
.profile-view {
  padding-top: var(--spacing-lg);
  padding-bottom: 80px;
}

.profile-card {
  background: var(--color-surface);
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-xl);
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: var(--spacing-md);
  border: 4px solid var(--color-bg);
}

.name {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 4px;
}

.email {
  color: var(--color-text-sub);
  margin-bottom: var(--spacing-sm);
}

.meta {
  font-size: 12px;
  color: var(--color-text-sub);
  background: var(--color-bg);
  padding: 4px 12px;
  border-radius: var(--radius-full);
}

.logout-btn {
  border: 1px solid var(--color-primary);
  color: var(--color-primary);
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

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 10px;
  color: var(--color-text-sub);
  gap: 4px;
}

.nav-item.active {
  color: var(--color-primary);
}

.nav-item .icon {
  font-size: 20px;
}
</style>
