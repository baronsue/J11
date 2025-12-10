import { createI18n } from 'vue-i18n'

const messages = {
    en: {
        nav: {
            explore: 'Explore',
            bookings: 'Bookings',
            profile: 'Profile'
        },
        home: {
            discover: 'Discover',
            subtitle: 'Find the best restaurants in town.'
        },
        auth: {
            loginTitle: 'Welcome Back',
            registerTitle: 'Create Account',
            email: 'Email',
            password: 'Password',
            name: 'Full Name',
            phone: 'Phone Number',
            loginBtn: 'Log In',
            registerBtn: 'Register',
            noAccount: "Don't have an account? Register",
            hasAccount: 'Already have an account? Log in',
            logout: 'Log Out'
        },
        profile: {
            title: 'My Profile',
            memberSince: 'Member since'
        },
        detail: {
            bookBtn: 'Book a Table',
            about: 'About',
            notFound: 'Restaurant not found',
            goBack: 'Go Back'
        },
        booking: {
            title: 'Make a Reservation',
            step1: 'Select Date & Time',
            step2: 'Contact Info',
            checkAvailability: 'Check Availability',
            date: 'Date',
            guests: 'Guests',
            time: 'Time',
            contactName: 'Contact Name',
            contactPhone: 'Phone Number',
            confirmBtn: 'Confirm Reservation',
            successTitle: 'Booking Confirmed',
            reservationId: 'Reservation #',
            arriveEarly: 'Please arrive 10 minutes before your reservation time.',
            viewBookings: 'View My Reservations'
        },
        reservations: {
            title: 'My Bookings',
            upcoming: 'Upcoming',
            history: 'History',
            empty: 'No upcoming reservations.',
            findBtn: 'Find a Restaurant',
            cancelBtn: 'Cancel Reservation',
            cancelConfirm: 'Are you sure you want to cancel this reservation?',
            status: {
                confirmed: 'Confirmed',
                pending: 'Pending',
                cancelled: 'Cancelled',
                completed: 'Completed'
            }
        },
        common: {
            confirm: 'Confirm',
            cancel: 'Cancel'
        }
    },
    zh: {
        common: {
            confirm: '确认',
            cancel: '取消'
        },
        nav: {
            explore: '探索',
            bookings: '预订',
            profile: '我的'
        },
        home: {
            discover: '发现美食',
            subtitle: '寻找城中最好的餐厅'
        },
        auth: {
            loginTitle: '欢迎回来',
            registerTitle: '创建账户',
            email: '电子邮箱',
            password: '密码',
            name: '姓名',
            phone: '手机号',
            loginBtn: '登录',
            registerBtn: '注册',
            noAccount: '还没有账户？去注册',
            hasAccount: '已有账户？去登录',
            logout: '退出登录'
        },
        profile: {
            title: '个人中心',
            memberSince: '注册时间'
        },
        detail: {
            bookBtn: '立即预订',
            about: '关于餐厅',
            notFound: '未找到餐厅',
            goBack: '返回'
        },
        booking: {
            title: '预订餐位',
            step1: '选择日期和时间',
            step2: '联系人信息',
            checkAvailability: '查询空位',
            date: '日期',
            guests: '人数',
            time: '时间',
            contactName: '联系人姓名',
            contactPhone: '联系电话',
            confirmBtn: '确认预订',
            successTitle: '预订成功',
            reservationId: '预订编号',
            arriveEarly: '请提前10分钟到达餐厅。',
            viewBookings: '查看我的预订'
        },
        reservations: {
            title: '我的预订',
            upcoming: '即将到来',
            history: '历史订单',
            empty: '暂无即将到来的预订',
            findBtn: '去发现餐厅',
            cancelBtn: '取消预订',
            cancelConfirm: '确定要取消此预订吗？',
            status: {
                confirmed: '已确认',
                pending: '待确认',
                cancelled: '已取消',
                completed: '已完成'
            }
        }
    },
    fr: {
        common: {
            confirm: 'Confirmer',
            cancel: 'Annuler'
        },
        nav: {
            explore: 'Explorer',
            bookings: 'Réservations',
            profile: 'Profil'
        },
        home: {
            discover: 'Découvrir',
            subtitle: 'Trouvez les meilleurs restaurants.'
        },
        auth: {
            loginTitle: 'Bon retour',
            registerTitle: 'Créer un compte',
            email: 'Email',
            password: 'Mot de passe',
            name: 'Nom complet',
            phone: 'Téléphone',
            loginBtn: 'Se connecter',
            registerBtn: "S'inscrire",
            noAccount: "Pas de compte ? S'inscrire",
            hasAccount: 'Déjà un compte ? Se connecter',
            logout: 'Se déconnecter'
        },
        profile: {
            title: 'Mon Profil',
            memberSince: 'Membre depuis'
        },
        detail: {
            bookBtn: 'Réserver',
            about: 'À propos',
            notFound: 'Restaurant non trouvé',
            goBack: 'Retour'
        },
        booking: {
            title: 'Faire une réservation',
            step1: 'Date et Heure',
            step2: 'Coordonnées',
            checkAvailability: 'Vérifier la disponibilité',
            date: 'Date',
            guests: 'Invités',
            time: 'Heure',
            contactName: 'Nom du contact',
            contactPhone: 'Téléphone',
            confirmBtn: 'Confirmer',
            successTitle: 'Réservation Confirmée',
            reservationId: 'Réservation #',
            arriveEarly: "Veuillez arriver 10 minutes avant l'heure.",
            viewBookings: 'Voir mes réservations'
        },
        reservations: {
            title: 'Mes Réservations',
            upcoming: 'À venir',
            history: 'Historique',
            empty: 'Aucune réservation à venir.',
            findBtn: 'Trouver un restaurant',
            cancelBtn: 'Annuler',
            cancelConfirm: 'Êtes-vous sûr de vouloir annuler cette réservation ?',
            status: {
                confirmed: 'Confirmé',
                pending: 'En attente',
                cancelled: 'Annulé',
                completed: 'Terminé'
            }
        }
    }
}

const i18n = createI18n({
    legacy: false, // Use Composition API
    locale: 'en', // Default language
    fallbackLocale: 'en',
    messages
})

export default i18n
