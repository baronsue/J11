<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const localError = ref('')

async function handleLogin() {
  localError.value = ''
  
  if (!username.value) {
    localError.value = 'Please enter your username'
    return
  }
  if (!password.value) {
    localError.value = 'Please enter your password'
    return
  }
  
  const success = await authStore.login(username.value, password.value)
  if (success) {
    router.push('/')
  }
}
</script>

<template>
  <div class="auth-view">
    <h1 class="text-h1">{{ $t('auth.loginTitle') }}</h1>
    
    <div v-if="localError || authStore.error" class="error-message">
      {{ localError || authStore.error }}
    </div>
    
    <form @submit.prevent="handleLogin" class="auth-form">
      <div class="form-group">
        <label for="login-username">Username</label>
        <input id="login-username" name="username" type="text" v-model="username" class="input-field" placeholder="Your username" />
      </div>

      <div class="form-group">
        <label for="login-password">{{ $t('auth.password') }}</label>
        <input id="login-password" name="password" type="password" v-model="password" class="input-field" placeholder="Your password" />
      </div>

      <button type="submit" class="btn btn-primary btn-block" :disabled="authStore.loading">
        {{ authStore.loading ? 'Logging in...' : $t('auth.loginBtn') }}
      </button>
    </form>

    <div class="auth-footer">
      <RouterLink to="/register" class="link">{{ $t('auth.noAccount') }}</RouterLink>
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
