<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const name = ref('')
const email = ref('')
const password = ref('')
const phone = ref('')
const localError = ref('')

async function handleRegister() {
  console.log('handleRegister called')
  localError.value = ''
  
  // Manual validation
  if (!username.value || username.value.length < 3) {
    localError.value = 'Username must be at least 3 characters'
    return
  }
  if (!email.value || !email.value.includes('@')) {
    localError.value = 'Please enter a valid email'
    return
  }
  if (!password.value || password.value.length < 6) {
    localError.value = 'Password must be at least 6 characters'
    return
  }
  
  console.log('Starting registration...', { username: username.value, email: email.value })
  
  try {
    const success = await authStore.register(
      username.value, 
      email.value, 
      password.value, 
      name.value || username.value,
      phone.value
    )
    
    console.log('Registration result:', success)
    if (success) {
      router.push('/')
    }
  } catch (err) {
    console.error('Registration error:', err)
    localError.value = err.message || 'Registration failed'
  }
}
</script>

<template>
  <div class="auth-view">
    <h1 class="text-h1">{{ $t('auth.registerTitle') }}</h1>
    
    <div v-if="localError || authStore.error" class="error-message">
      {{ localError || authStore.error }}
    </div>
    
    <form @submit.prevent="handleRegister" class="auth-form">
      <div class="form-group">
        <label for="reg-username">Username *</label>
        <input id="reg-username" name="username" type="text" v-model="username" class="input-field" placeholder="Login username (min 3 chars)" />
      </div>

      <div class="form-group">
        <label for="reg-name">{{ $t('auth.name') }}</label>
        <input id="reg-name" name="name" type="text" v-model="name" class="input-field" placeholder="Display name (optional)" />
      </div>

      <div class="form-group">
        <label for="reg-email">{{ $t('auth.email') }} *</label>
        <input id="reg-email" name="email" type="email" v-model="email" class="input-field" placeholder="your@email.com" />
      </div>

      <div class="form-group">
        <label for="reg-phone">{{ $t('auth.phone') }}</label>
        <input id="reg-phone" name="phone" type="tel" v-model="phone" class="input-field" placeholder="Optional" />
      </div>

      <div class="form-group">
        <label for="reg-password">{{ $t('auth.password') }} *</label>
        <input id="reg-password" name="password" type="password" v-model="password" class="input-field" placeholder="Min 6 characters" />
      </div>

      <button type="submit" class="btn btn-primary btn-block" :disabled="authStore.loading">
        {{ authStore.loading ? 'Registering...' : $t('auth.registerBtn') }}
      </button>
    </form>

    <div class="auth-footer">
      <RouterLink to="/login" class="link">{{ $t('auth.hasAccount') }}</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.auth-view {
  padding-top: var(--spacing-xl);
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 80vh;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.form-group label {
  display: block;
  font-weight: 600;
  margin-bottom: var(--spacing-xs);
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

.auth-footer {
  text-align: center;
}

.link {
  color: var(--color-primary);
  font-weight: 500;
}

.error-message {
  background: #fee;
  color: #c00;
  padding: 12px;
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-md);
  font-size: 14px;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
