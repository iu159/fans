import Vue from 'vue';
import Router from 'vue-router';
import Index from './pages/Index.vue';
import Login from './pages/Login.vue';
import Register from './pages/Register.vue';
import Profile from './pages/Profile.vue';
import Upload from './pages/Upload.vue';
import Picture from './pages/Picture.vue';
import Popular from './pages/Popular.vue';
import Fresh from './pages/Fresh.vue';
import Show from './pages/Show.vue';
import EditProfile from './pages/EditProfile.vue';
import WhitePage from './pages/components/WhitePage.vue';
import MainNavbar from './layout/MainNavbar.vue';
import MainFooter from './layout/MainFooter.vue';

Vue.use(Router);

export default new Router({
  linkExactActiveClass: 'active',
  routes: [
    {
      path: '/',
      name: 'index',
      components: { default: Index, header: MainNavbar, footer: MainFooter },
      props: {
        header: { colorOnScroll: 400 },
        footer: { backgroundColor: 'black' }
      }
    },
    {
      path: '/login',
      name: 'login',
      components: { default: Login, header: MainNavbar },
      props: {
        header: { colorOnScroll: 400 }
      }
    },
    {
      path: '/editProfile',
      name: 'editProfile',
      components: { default: EditProfile, header: MainNavbar },
      props: {
        header: { colorOnScroll: 400 },
      }
    },
    {
      path: '/register',
      name: 'register',
      components: { default: Register, header: MainNavbar },
      props: {
        header: { colorOnScroll: 400 }
      }
    },
    {
      path: '/popular',
      name: 'popular',
      components: { default: Popular, header: MainNavbar },
      props: {
        header: { colorOnScroll: 0 }
      }
    },
    {
      path: '/fresh',
      name: 'fresh',
      components: { default: Fresh, header: MainNavbar },
      props: {
        header: { colorOnScroll: 0 }
      }
    },
    {
      path: '/show',
      name: 'show',
      components: { default: Show, header: MainNavbar },
      props: {
        header: { colorOnScroll: 0 }
      }
    },
    {
      path: '/upload',
      name: 'upload',
      components: { default: Upload, header: MainNavbar },
      props: {
        header: { colorOnScroll: 0 }
      }
    },
    {
      path: '/profile/:uid',
      name: 'profile',
      components: { default: Profile, header: MainNavbar, footer: MainFooter },
      props: {
        header: { colorOnScroll: 400 },
        footer: { backgroundColor: 'black' },
        default: true
      }
    },
    {
      path: '/picture/:pid',
      name: 'picture',
      components: { default: Picture },
      props: {
        default: true
      }
    },
    {
      path: '/whitePage',
      name: 'whitePage',
      components: { default: WhitePage },
      props: {
        default: true
      }
    }
  ],
  scrollBehavior: to => {
    if (to.hash) {
      return { selector: to.hash };
    } else {
      return { x: 0, y: 0 };
    }
  }
});
