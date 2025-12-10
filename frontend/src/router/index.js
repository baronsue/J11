import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import MyReservationsView from '../views/MyReservationsView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/RegisterView.vue')
        },
        {
            path: '/profile',
            name: 'profile',
            component: () => import('../views/ProfileView.vue')
        },
        {
            path: '/reservations',
            name: 'reservations',
            component: MyReservationsView
        },
        {
            path: '/restaurant/:id',
            name: 'restaurant-detail',
            component: () => import('../views/RestaurantDetailView.vue')
        },
        {
            path: '/book',
            name: 'booking-flow',
            component: () => import('../views/BookingFlowView.vue')
        }
    ]
})

export default router
