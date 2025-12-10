import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useAuthStore } from './auth'
import api from '../api/axios'

export const useBookingStore = defineStore('booking', () => {
    const persisted = localStorage.getItem('currentBooking')
    const currentBooking = ref(persisted ? JSON.parse(persisted) : {
        restaurantId: null,
        tableId: null,
        date: null,
        time: null,
        guests: 2,
        contactName: '',
        contactPhone: ''
    })

    const myBookings = ref([])
    const loading = ref(false)
    const error = ref(null)

    function startBooking(restaurantId) {
        currentBooking.value = {
            restaurantId,
            tableId: null,
            date: null,
            time: null,
            guests: 2,
            contactName: '',
            contactPhone: ''
        }
        localStorage.setItem('currentBooking', JSON.stringify(currentBooking.value))
    }

    async function confirmBooking(restaurant, tableId, customerId) {
        loading.value = true
        error.value = null

        try {
            const body = {
                restaurantId: currentBooking.value.restaurantId,
                customerId: customerId,
                reservationDate: currentBooking.value.date,
                reservationTime: currentBooking.value.time + ':00',
                numberOfGuests: currentBooking.value.guests
            }

            // 若传入 tableId 且可用则带上，否则让后端自动分配
            if (tableId) {
                body.tableId = tableId
            }

            const response = await api.post('/reservations', body)
            const reservation = response.data

            // Add to local bookings list with restaurant info
            const booking = {
                id: reservation.id,
                restaurantId: currentBooking.value.restaurantId,
                restaurantName: restaurant.name,
                restaurantImage: restaurant.image,
                date: reservation.reservationDate,
                time: reservation.reservationTime,
                guests: reservation.numberOfGuests,
                status: reservation.status,
                tableNumber: reservation.tableNumber
            }
            myBookings.value.unshift(booking)

            // Reset current booking but keep restaurantId for success view
            // The view will handle full reset when navigating away
            currentBooking.value = {
                ...currentBooking.value,
                tableId: null,
                date: null,
                time: null,
                guests: 2,
                contactName: '',
                contactPhone: ''
            }
            localStorage.setItem('currentBooking', JSON.stringify(currentBooking.value))

            return reservation.id
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Failed to create reservation'
            throw err
        } finally {
            loading.value = false
        }
    }

    async function fetchMyBookings(customerId) {
        loading.value = true

        try {
            const response = await api.get(`/customers/${customerId}/reservations`)
            const reservations = response.data

            myBookings.value = reservations.map(r => ({
                id: r.id,
                restaurantId: r.restaurantId,
                restaurantName: r.restaurantName,
                restaurantImage: 'https://images.unsplash.com/photo-1594041680534-e8c8cdebd659?auto=format&fit=crop&w=800&q=80',
                date: r.reservationDate,
                time: r.reservationTime,
                guests: r.numberOfGuests,
                status: r.status,
                tableNumber: r.tableNumber
            }))
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Failed to fetch reservations'
        } finally {
            loading.value = false
        }
    }

    async function cancelBooking(id) {
        loading.value = true

        try {
            await api.put(`/reservations/${id}/cancel`)

            // Update local state
            const booking = myBookings.value.find(b => b.id === id)
            if (booking) {
                booking.status = 'CANCELLED'
            }
            return true
        } catch (err) {
            error.value = err.response?.data?.message || err.message || 'Failed to cancel reservation'
            return false
        } finally {
            loading.value = false
        }
    }

    return {
        currentBooking,
        myBookings,
        loading,
        error,
        startBooking,
        confirmBooking,
        fetchMyBookings,
        cancelBooking
    }
})
