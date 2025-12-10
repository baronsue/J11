<script setup>
defineProps({
  isOpen: Boolean,
  title: String,
  message: String,
  confirmText: String,
  cancelText: String
})

const emit = defineEmits(['confirm', 'cancel'])
</script>

<template>
  <div v-if="isOpen" class="modal-overlay">
    <div class="modal-content">
      <h3 class="modal-title">{{ title }}</h3>
      <p class="modal-message">{{ message }}</p>
      <div class="modal-actions">
        <button class="btn btn-outline" @click="emit('cancel')">{{ cancelText || 'Cancel' }}</button>
        <button class="btn btn-primary" @click="emit('confirm')">{{ confirmText || 'Confirm' }}</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: var(--color-surface);
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  width: 90%;
  max-width: 320px;
  box-shadow: var(--shadow-lg);
  text-align: center;
  animation: popIn 0.2s ease-out;
}

.modal-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: var(--spacing-sm);
}

.modal-message {
  color: var(--color-text-sub);
  margin-bottom: var(--spacing-lg);
  line-height: 1.5;
}

.modal-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
}

.btn {
  flex: 1;
}

@keyframes popIn {
  from { transform: scale(0.9); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}
</style>
