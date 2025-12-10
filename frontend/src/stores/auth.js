import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../api/axios'

export const useAuthStore = defineStore('auth', () => {
    const token = ref(localStorage.getItem('token') || null)
    const user = ref(JSON.parse(localStorage.getItem('user')) || null)
    const customerId = ref(localStorage.getItem('customerId') || null)
    const loading = ref(false)
    const error = ref(null)

    const isAuthenticated = computed(() => !!token.value)

    async function login(username, password) {
        loading.value = true
        error.value = null
        try {
            const response = await api.post('/auth/login', { username, password })
            const data = response.data

            token.value = data.token
            user.value = {
                username: data.username,
                roles: data.roles,
                restaurantId: data.restaurantId,
                avatar: `https://ui-avatars.com/api/?name=${data.username}&background=0D8ABC&color=fff`
            }

            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(user.value))

            // 尝试获取客户档案
            await fetchAndStoreCustomerProfile(user.value.username)
            return true
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Login failed'
            return false
        } finally {
            loading.value = false
        }
    }

    async function register(username, email, password, name, phone) {
        loading.value = true
        error.value = null
        console.log('Starting registration for:', username)

        try {
            // 1. Register user account
            console.log('Calling register API...')
            const registerResponse = await api.post('/auth/register', {
                username,
                email,
                password,
                role: 'CUSTOMER'
            })

            const registerData = registerResponse.data
            console.log('Registration successful:', registerData)

            // Store token immediately from registration response
            token.value = registerData.token
            user.value = {
                username: registerData.username,
                roles: registerData.roles,
                restaurantId: registerData.restaurantId,
                avatar: `https://ui-avatars.com/api/?name=${registerData.username}&background=0D8ABC&color=fff`
            }
            localStorage.setItem('token', registerData.token)
            localStorage.setItem('user', JSON.stringify(user.value))

            // 2. Create customer profile (optional, don't fail if it fails)
            await createCustomerProfile(name || username, email, phone)

            return true
        } catch (err) {
            console.error('Registration error:', err)
            error.value = err.response?.data?.message || err.message || 'Registration failed'
            return false
        } finally {
            loading.value = false
        }
    }

    async function fetchAndStoreCustomerProfile(username) {
        try {
            const resp = await api.get('/customers')
            const list = resp.data
            // 尝试匹配当前用户的 email 或 name
            const found = list.find(c => c.email?.startsWith(username) || c.name === username) || list[0]
            if (found) {
                customerId.value = found.id
                localStorage.setItem('customerId', found.id)
                return
            }
            console.warn('Customer profile not found on fetch')
        } catch (e) {
            console.warn('fetch customer profile failed', e)
        }
    }

    async function createCustomerProfile(name, email, phone) {
        try {
            console.log('Creating customer profile...')
            const customerResponse = await api.post('/customers', {
                name,
                email,
                phone: phone || '000-0000-0000'
            })

            const customerData = customerResponse.data
            customerId.value = customerData.id
            localStorage.setItem('customerId', customerData.id)
            console.log('Customer profile created:', customerData.id)
        } catch (customerErr) {
            console.warn('Customer profile creation error:', customerErr)
            // 如果创建失败，再尝试拉取已有档案
            await fetchAndStoreCustomerProfile(name)
        }
    }

    function logout() {
        token.value = null
        user.value = null
        customerId.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('customerId')
    }

    return {
        token,
        user,
        customerId,
        loading,
        error,
        isAuthenticated,
        login,
        register,
        logout
    }
})
