<script setup>
import { ref, computed, onMounted } from 'vue'
import { useBookingStore } from '../stores/booking'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import ConfirmModal from '../components/ConfirmModal.vue'
import api from '../api/axios'

const { t } = useI18n()
const store = useBookingStore()
const authStore = useAuthStore()
const router = useRouter()
const activeTab = ref('upcoming')
const cancellingId = ref(null)
const cancelError = ref('')

// Modal State
const showConfirmModal = ref(false)
const pendingCancelId = ref(null)

const upcomingBookings = computed(() => 
  store.myBookings.filter(b => b.status === 'CONFIRMED' || b.status === 'PENDING')
)

const historyBookings = computed(() => 
  store.myBookings.filter(b => b.status === 'CANCELLED' || b.status === 'COMPLETED')
)

onMounted(async () => {
  if (!authStore.isAuthenticated) {
    router.replace({ name: 'login', query: { redirect: '/reservations' } })
    return
  }
  await ensureCustomerId()
  if (authStore.customerId) {
    await store.fetchMyBookings(authStore.customerId)
  }
})

function requestCancel(id) {
  pendingCancelId.value = id
  showConfirmModal.value = true
}

async function confirmCancel() {
  if (!pendingCancelId.value) return
  
  showConfirmModal.value = false
  const id = pendingCancelId.value
  pendingCancelId.value = null
  
  cancelError.value = ''
  cancellingId.value = id
  
  const ok = await store.cancelBooking(id)
  if (ok && authStore.customerId) {
    await store.fetchMyBookings(authStore.customerId)
  } else if (!ok) {
    cancelError.value = store.error || '取消失败，请稍后重试'
  }
  cancellingId.value = null
}

async function ensureCustomerId() {
  if (authStore.customerId) return
  // 尝试拉取客户列表并匹配当前用户（需要后端支持 GET /api/customers）
  try {
    const resp = await api.get('/customers')
    const list = resp.data
    const username = authStore.user?.username
    const found = list.find(c => c.email?.startsWith(username) || c.name === username) || list[0]
    if (found) {
      authStore.customerId = found.id
      localStorage.setItem('customerId', found.id)
    }
  } catch (e) {
    console.warn('Fetch customers failed', e)
  }
}

function goToProfile() {
  router.push(authStore.isAuthenticated ? '/profile' : '/login')
}
</script>

<template>
  <div class="reservations-view">
    <h1 class="text-h1">{{ $t('reservations.title') }}</h1>
    
    <div class="tabs">
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'upcoming' }"
        @click="activeTab = 'upcoming'"
      >
        {{ $t('reservations.upcoming') }}
      </button>
      <button 
        class="tab-btn" 
        :class="{ active: activeTab === 'history' }"
        @click="activeTab = 'history'"
      >
        {{ $t('reservations.history') }}
      </button>
    </div>

    <div class="tab-content">
      <!-- Upcoming Tab -->
      <div v-if="activeTab === 'upcoming'">
        <div v-if="upcomingBookings.length === 0" class="empty-state">
          <p class="text-body">{{ $t('reservations.empty') }}</p>
          <button class="btn btn-primary" @click="router.push('/')">{{ $t('reservations.findBtn') }}</button>
        </div>

        <div class="booking-list" v-else>
          <div v-for="booking in upcomingBookings" :key="booking.id" class="booking-card">
            <div class="card-header">
              <img :src="booking.restaurantImage" class="thumb" />
              <div>
                <h3 class="name">{{ booking.restaurantName }}</h3>
                <span class="status confirmed">{{ $t('reservations.status.confirmed') }}</span>
              </div>
            </div>
            <div class="card-details">
              <div class="detail-item">
                <span class="label">{{ $t('booking.date') }}</span>
                <span>{{ booking.date }}</span>
              </div>
              <div class="detail-item">
                <span class="label">{{ $t('booking.time') }}</span>
                <span>{{ booking.time }}</span>
              </div>
              <div class="detail-item">
                <span class="label">{{ $t('booking.guests') }}</span>
                <span>{{ booking.guests }}</span>
              </div>
            </div>
            <div class="card-actions">
              <button class="btn-text cancel-btn"
                      :disabled="cancellingId === booking.id"
                      @click="requestCancel(booking.id)">
                {{ cancellingId === booking.id ? '...' : $t('reservations.cancelBtn') }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- History Tab -->
      <div v-if="activeTab === 'history'">
        <div v-if="historyBookings.length === 0" class="empty-state">
          <p class="text-body">No history.</p>
        </div>
        
        <div class="booking-list" v-else>
          <div v-for="booking in historyBookings" :key="booking.id" class="booking-card history-card">
            <div class="card-header">
              <img :src="booking.restaurantImage" class="thumb grayscale" />
              <div>
                <h3 class="name">{{ booking.restaurantName }}</h3>
                <span class="status" :class="booking.status.toLowerCase()">
                  {{ booking.status === 'CANCELLED' ? $t('reservations.status.cancelled') : $t('reservations.status.completed') }}
                </span>
              </div>
            </div>
            <div class="card-details">
              <div class="detail-item">
                <span class="label">{{ $t('booking.date') }}</span>
                <span>{{ booking.date }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="cancelError" class="cancel-error">
      {{ cancelError }}
    </div>

    <ConfirmModal
      :isOpen="showConfirmModal"
      :title="$t('reservations.cancelBtn')"
      :message="$t('reservations.cancelConfirm')"
      :confirmText="$t('common.confirm')"
      :cancelText="$t('common.cancel')"
      @confirm="confirmCancel"
      @cancel="showConfirmModal = false"
    />

    <!-- Bottom Nav -->
    <nav class="bottom-nav">
      <RouterLink to="/" class="nav-item">
        <span class="icon">🏠</span>
        <span>{{ $t('nav.explore') }}</span>
      </RouterLink>
      <RouterLink to="/reservations" class="nav-item active">
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
.reservations-view {
  padding-top: var(--spacing-lg);
  padding-bottom: 80px;
}

.tabs {
  display: flex;
  border-bottom: 1px solid var(--color-border);
  margin-bottom: var(--spacing-md);
}

.tab-btn {
  flex: 1;
  padding: 12px;
  font-weight: 600;
  color: var(--color-text-sub);
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.empty-state {
  text-align: center;
  margin-top: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
}

.booking-card {
  background: var(--color-surface);
  border-radius: var(--radius-md);
  padding: var(--spacing-md);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-md);
  border: 1px solid var(--color-border);
}

@media (min-width: 768px) {
  .booking-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: var(--spacing-lg);
  }
  
  .booking-card {
    margin-bottom: 0;
  }
}

.card-header {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-md);
  align-items: center;
}

.thumb {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-sm);
  object-fit: cover;
}

.thumb.grayscale {
  filter: grayscale(100%);
}

.name {
  font-size: 16px;
  font-weight: 600;
}

.status {
  font-size: 12px;
  font-weight: 500;
  padding: 2px 6px;
  border-radius: 4px;
}

.status.confirmed {
  color: var(--color-secondary);
  background: rgba(0, 166, 153, 0.1);
}

.status.cancelled {
  color: var(--color-text-sub);
  background: var(--color-bg);
}

.card-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-sm);
  border-top: 1px solid var(--color-border);
  padding-top: var(--spacing-sm);
}

.detail-item {
  display: flex;
  flex-direction: column;
  font-size: 14px;
}

.label {
  color: var(--color-text-sub);
  font-size: 12px;
}

.card-actions {
  margin-top: var(--spacing-sm);
  border-top: 1px solid var(--color-border);
  padding-top: var(--spacing-sm);
  text-align: right;
}

.cancel-btn {
  color: var(--color-text-sub);
  font-size: 14px;
  text-decoration: underline;
}

.cancel-error {
  margin-top: var(--spacing-sm);
  color: #c00;
  font-size: 14px;
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
  cursor: pointer;
}

.nav-item.active {
  color: var(--color-primary);
}

.nav-item .icon {
  font-size: 20px;
}
</style>
