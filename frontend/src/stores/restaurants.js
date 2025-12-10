import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../api/axios'

export const useRestaurantStore = defineStore('restaurants', () => {
    const restaurants = ref([])
    const loading = ref(false)
    const error = ref(null)

    // BBQ restaurant images for display
    const defaultImages = [
        'https://images.unsplash.com/photo-1594041680534-e8c8cdebd659?auto=format&fit=crop&w=800&q=80',
        'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&fit=crop&w=800&q=80',
        'https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?auto=format&fit=crop&w=800&q=80'
    ]

    async function fetchRestaurants() {
        loading.value = true
        error.value = null
        try {
            const response = await api.get('/restaurants')
            const data = response.data

            // Cities from documentation
            const cities = ['Xi\'an', 'Beijing', 'Chengdu']

            // Map backend data to frontend format with images
            restaurants.value = data.map((r, index) => ({
                id: r.id,
                name: r.name,
                address: r.address,
                phone: r.phone,
                description: r.description,
                openingTime: r.openingTime,
                closingTime: r.closingTime,
                // Use backend city if available and distinct, otherwise distribute across demo cities
                city: (r.city && r.city !== 'Shanghai') ? r.city : cities[index % cities.length],
                cuisine: r.cuisine || 'BBQ / 烧烤',
                rating: parseFloat((4.8 + Math.random() * 0.2).toFixed(1)),
                priceRange: '$$$',
                image: defaultImages[index % defaultImages.length]
            }))
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Failed to fetch restaurants'
            console.error('Error fetching restaurants:', err)
        } finally {
            loading.value = false
        }
    }

    async function fetchRestaurantById(id) {
        try {
            const response = await api.get(`/restaurants/${id}`)
            const r = response.data

            const cities = ['Xi\'an', 'Beijing', 'Chengdu']
            const cityIndex = (r.id - 1) >= 0 ? (r.id - 1) : 0

            const existingIndex = restaurants.value.findIndex(rest => rest.id === r.id)
            const restaurant = {
                id: r.id,
                name: r.name,
                address: r.address,
                phone: r.phone,
                description: r.description,
                openingTime: r.openingTime,
                closingTime: r.closingTime,
                city: (r.city && r.city !== 'Shanghai') ? r.city : cities[cityIndex % cities.length],
                cuisine: r.cuisine || 'BBQ / 烧烤',
                rating: 4.9,
                priceRange: '$$$',
                image: defaultImages[(r.id - 1) % defaultImages.length]
            }

            if (existingIndex >= 0) {
                restaurants.value[existingIndex] = restaurant
            } else {
                restaurants.value.push(restaurant)
            }

            return restaurant
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Restaurant not found'
            return null
        }
    }

    async function fetchTables(restaurantId) {
        try {
            const response = await api.get(`/restaurants/${restaurantId}/tables`)
            return response.data
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Failed to fetch tables'
            return []
        }
    }

    function getRestaurantById(id) {
        return restaurants.value.find(r => r.id === parseInt(id))
    }

    return {
        restaurants,
        loading,
        error,
        fetchRestaurants,
        fetchRestaurantById,
        fetchTables,
        getRestaurantById
    }
})
