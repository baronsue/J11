<script setup>
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()

const languages = [
  { code: 'en', label: '🇺🇸 EN' },
  { code: 'zh', label: '🇨🇳 中文' },
  { code: 'fr', label: '🇫🇷 FR' }
]

function switchLanguage(code) {
  locale.value = code
}
</script>

<template>
  <div class="language-switcher">
    <button 
      v-for="lang in languages" 
      :key="lang.code"
      class="lang-btn"
      :class="{ active: locale === lang.code }"
      @click="switchLanguage(lang.code)"
    >
      {{ lang.label }}
    </button>
  </div>
</template>

<style scoped>
.language-switcher {
  display: flex;
  gap: 8px;
  background: var(--color-surface);
  padding: 4px;
  border-radius: var(--radius-full);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-sm);
}

.lang-btn {
  padding: 4px 12px;
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
  color: var(--color-text-sub);
  transition: all 0.2s;
}

.lang-btn.active {
  background: var(--color-primary);
  color: white;
  box-shadow: var(--shadow-sm);
}

.lang-btn:hover:not(.active) {
  background: var(--color-bg);
}
</style>
