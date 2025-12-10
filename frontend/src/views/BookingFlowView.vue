<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useBookingStore } from '../stores/booking'
import { useRestaurantStore } from '../stores/restaurants'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const route = useRoute()
const bookingStore = useBookingStore()
const restaurantStore = useRestaurantStore()
const authStore = useAuthStore()

const restaurant = computed(() =>
  restaurantStore.getRestaurantById(bookingStore.currentBooking.restaurantId)
)

const tables = ref([])
const selectedTableId = ref(null)

// Steps: 1 = Date/Time, 2 = Contact, 3 = Confirmation
const step = ref(1)
const loading = ref(false)
const loadingData = ref(true)
const confirmedBookingId = ref(null)
const errorMessage = ref('')

const date = ref('')
const time = ref('')
const guests = ref(2)
const contactName = ref(authStore.user?.username || '')
const contactPhone = ref('')

// Calculate today's date for min attribute
const today = new Date().toISOString().split('T')[0]

const timeSlots = ['11:00', '11:30', '12:00', '12:30', '13:00', '18:00', '18:30', '19:00', '19:30', '20:00', '20:30', '21:00']

onMounted(async () => {
  // 必须登录后才能继续预约
  if (!authStore.isAuthenticated) {
    const target = route.fullPath || '/book'
    router.replace({ name: 'login', query: { redirect: target } })
    return
  }

  // 1) 如果 store 没有餐厅 ID，尝试从路由参数恢复
  if (!bookingStore.currentBooking.restaurantId && route.query.restaurantId) {
    bookingStore.startBooking(parseInt(route.query.restaurantId))
  }

  // 2) 如果依然没有，返回首页
  if (!bookingStore.currentBooking.restaurantId) {
    router.replace('/')
    return
  }

  try {
    // Fetch restaurant if not loaded
    if (!restaurant.value) {
      await restaurantStore.fetchRestaurantById(bookingStore.currentBooking.restaurantId)
    }

    // Fetch available tables
    tables.value = await restaurantStore.fetchTables(bookingStore.currentBooking.restaurantId)
  } catch (err) {
    console.error('Load booking data error:', err)
    errorMessage.value = '无法加载餐厅或餐桌信息，请稍后重试'
  } finally {
    loadingData.value = false
  }
})

async function checkAvailability() {
  if (!date.value || !time.value) return
  
  errorMessage.value = ''
  loading.value = true
  
  // Filter tables by capacity
  const availableTables = tables.value.filter(t => t.capacity >= guests.value)
  
  if (availableTables.length === 0) {
    errorMessage.value = 'No tables available for this party size'
    loading.value = false
    return
  }
  
  // Auto-select first available table
  selectedTableId.value = availableTables[0].id
  
  loading.value = false
  step.value = 2
}

async function confirm() {
  if (!contactName.value || !contactPhone.value) return
  
  // Check if user is logged in
  if (!authStore.isAuthenticated) {
    errorMessage.value = 'Please login to make a reservation'
    return
  }
  
  // Check for customer ID
  if (!authStore.customerId) {
    errorMessage.value = 'Customer profile not found. Please register first.'
    return
  }
  
  bookingStore.currentBooking.date = date.value
  bookingStore.currentBooking.time = time.value
  bookingStore.currentBooking.guests = guests.value
  bookingStore.currentBooking.contactName = contactName.value
  bookingStore.currentBooking.contactPhone = contactPhone.value
  
  try {
    errorMessage.value = ''
    const id = await bookingStore.confirmBooking(
      restaurant.value, 
      null, // 让后端自动分配可用桌位，避免前端选到已占用桌
      authStore.customerId
    )
    confirmedBookingId.value = id
    step.value = 3
  } catch (err) {
    errorMessage.value = err.message || 'Failed to create reservation'
  }
}

function goBack() {
  if (step.value === 2) {
    step.value = 1
  } else {
    router.back()
  }
}

function finishBooking() {
  // Clear restaurantId from store now that we are leaving
  bookingStore.currentBooking.restaurantId = null
  localStorage.removeItem('currentBooking')
  router.push('/reservations')
}
</script>

<template>
  <div class="booking-view" v-if="!loadingData && restaurant">
    <!-- Header -->
    <header class="header" v-if="step < 3">
      <button class="back-btn" @click="goBack">←</button>
      <h1 class="text-h2">{{ $t('booking.title') }}</h1>
    </header>

    <!-- Step 1: Date & Time -->
    <div v-if="step === 1" class="step-content">
      <div class="summary-card">
        <h3 class="text-h2">{{ restaurant.name }}</h3>
        <p class="text-body">{{ restaurant.address || restaurant.city }}</p>
      </div>

      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

      <div class="form-section">
        <div class="form-group">
          <label for="booking-date">{{ $t('booking.date') }}</label>
          <input id="booking-date" name="reservationDate" type="date" v-model="date" class="input-field" :min="today" />
        </div>

        <div class="form-group">
          <label for="booking-guests">{{ $t('booking.guests') }}</label>
          <div class="guest-selector">
            <button @click="guests > 1 && guests--" class="counter-btn">-</button>
            <span>{{ guests }}</span>
            <button @click="guests < 10 && guests++" class="counter-btn">+</button>
          </div>
        </div>

        <div class="form-group">
          <label>{{ $t('booking.time') }}</label>
          <div class="time-grid">
            <button 
              v-for="slot in timeSlots" 
              :key="slot"
              class="time-chip"
              :class="{ active: time === slot }"
              @click="time = slot"
            >
              {{ slot }}
            </button>
          </div>
        </div>
      </div>

      <div class="footer">
        <button 
          class="btn btn-primary btn-block" 
          :disabled="!date || !time || loading"
          @click="checkAvailability"
        >
          {{ loading ? '...' : $t('booking.checkAvailability') }}
        </button>
      </div>
    </div>

    <!-- Step 2: Contact Info -->
    <div v-if="step === 2" class="step-content">
      <div class="form-section">
        <h2 class="text-h2">{{ $t('booking.step2') }}</h2>

        <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        
        <div class="form-group">
          <label for="booking-contact-name">{{ $t('booking.contactName') }}</label>
          <input id="booking-contact-name" name="contactName" type="text" v-model="contactName" class="input-field" />
        </div>

        <div class="form-group">
          <label for="booking-contact-phone">{{ $t('booking.contactPhone') }}</label>
          <input id="booking-contact-phone" name="contactPhone" type="tel" v-model="contactPhone" class="input-field" />
        </div>

        <div v-if="!authStore.isAuthenticated" class="login-hint">
          <p>Please <RouterLink to="/login">login</RouterLink> to make a reservation.</p>
        </div>
      </div>

      <div class="footer">
        <button 
          class="btn btn-primary btn-block" 
          :disabled="!contactName || !contactPhone || !authStore.isAuthenticated || bookingStore.loading"
          @click="confirm"
        >
          {{ bookingStore.loading ? '...' : $t('booking.confirmBtn') }}
        </button>
      </div>
    </div>

    <!-- Step 3: Confirmation -->
    <div v-if="step === 3" class="confirmation-view">
      <div class="success-icon">✓</div>
      <h1 class="text-h1">{{ $t('booking.successTitle') }}</h1>
      
      <div class="ticket-card">
        <div class="ticket-row">
          <span class="label">{{ $t('booking.reservationId') }}</span>
          <span class="value mono">DH-2025-{{ confirmedBookingId }}</span>
        </div>
        <div class="divider"></div>
        <div class="ticket-row">
          <span class="label">Restaurant</span>
          <span class="value">{{ restaurant.name }}</span>
        </div>
        <div class="ticket-row">
          <span class="label">Address</span>
          <span class="value">123 Tang Street, {{ restaurant.city }}</span>
        </div>
        <div class="ticket-row">
          <span class="label">{{ $t('booking.date') }}</span>
          <span class="value">{{ date }}</span>
        </div>
        <div class="ticket-row">
          <span class="label">{{ $t('booking.time') }}</span>
          <span class="value">{{ time }}</span>
        </div>
        <div class="ticket-row">
          <span class="label">{{ $t('booking.guests') }}</span>
          <span class="value">{{ guests }}</span>
        </div>
      </div>

      <p class="text-body text-center arrive-note">
        {{ $t('booking.arriveEarly') }}
      </p>

      <div class="footer">
        <button class="btn btn-primary btn-block" @click="finishBooking">
          {{ $t('booking.viewBookings') }}
        </button>
      </div>
    </div>
  </div>
  <div v-else class="loading-view">
    <p v-if="loadingData">加载中...</p>
    <div v-else class="not-found">
      <p>{{ errorMessage || '未找到餐厅信息，请返回选择餐厅后再预约' }}</p>
      <div class="not-found-actions">
        <button class="btn btn-primary" @click="router.push('/')">返回首页</button>
        <button class="btn btn-outline" @click="router.back()">返回上一页</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.booking-view {
  padding-top: var(--spacing-lg);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.back-btn {
  font-size: 24px;
  padding: 0 var(--spacing-sm);
}

.step-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.summary-card {
  background: var(--color-bg);
  padding: var(--spacing-md);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-xl);
}

.form-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.form-group label {
  display: block;
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
  color: var(--color-text-main);
}

.input-field {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  font-size: 16px;
  font-family: inherit;
}

.guest-selector {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  font-size: 20px;
  font-weight: 600;
}

.counter-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.time-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-sm);
}

.time-chip {
  padding: 8px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  font-size: 14px;
  transition: all 0.2s;
}

.time-chip.active {
  background: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
}

.footer {
  padding: var(--spacing-md) 0;
  margin-top: auto;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Confirmation Styles */
.confirmation-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: var(--spacing-xl);
}

.success-icon {
  width: 60px;
  height: 60px;
  background: var(--color-secondary);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  margin-bottom: var(--spacing-md);
}

.ticket-card {
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: var(--spacing-lg);
  width: 100%;
  margin: var(--spacing-lg) 0;
  box-shadow: var(--shadow-sm);
}

.ticket-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--spacing-sm);
}

.ticket-row .label {
  color: var(--color-text-sub);
}

.ticket-row .value {
  font-weight: 600;
  text-align: right;
}

.mono {
  font-family: monospace;
}

.divider {
  height: 1px;
  background: var(--color-border);
  margin: var(--spacing-md) 0;
  border-style: dashed;
}

.arrive-note {
  margin-bottom: var(--spacing-xl);
  font-style: italic;
}

.text-center {
  text-align: center;
}

.error-message {
  background: #fee;
  color: #c00;
  padding: 12px;
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-md);
  font-size: 14px;
}

.login-hint {
  background: var(--color-bg);
  padding: 12px;
  border-radius: var(--radius-md);
  text-align: center;
  font-size: 14px;
}

.login-hint a {
  color: var(--color-primary);
  font-weight: 600;
}

.not-found {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
  gap: var(--spacing-md);
}

.not-found-actions {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
  justify-content: center;
}
</style>
