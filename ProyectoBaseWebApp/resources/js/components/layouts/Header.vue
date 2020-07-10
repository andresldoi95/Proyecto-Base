<template>
  <b-navbar>
    <template slot="brand">
      <b-navbar-item tag="router-link" :to="{ path: '/' }">
        <img src="/img/logo.png" alt="Logo" />
      </b-navbar-item>
    </template>
    <template slot="start">
      <b-navbar-item tag="router-link" to="/">{{ $t('link.home') }}</b-navbar-item>
      <b-navbar-item tag="router-link" to="/admin">{{ $t('link.admin') }}</b-navbar-item>
    </template>

    <template slot="end">
      <b-navbar-dropdown v-if="$store.state.usuario.id !== ''" :label="$store.state.usuario.nombre">
        <b-navbar-item @click="logout">{{ $t('link.logout') }}</b-navbar-item>
      </b-navbar-dropdown>
      <b-navbar-item v-else tag="div">
        <div class="buttons">
          <router-link :to="{ name: 'Login' }" class="button is-primary">{{ $t('link.login') }}</router-link>
        </div>
      </b-navbar-item>
    </template>
  </b-navbar>
</template>

<script>
export default {
  methods: {
    logout: function() {
      this.$store.dispatch("loggedOut");
      this.$session.destroy();
      this.$router.push({
        name: "Login"
      });
    }
  }
};
</script>
