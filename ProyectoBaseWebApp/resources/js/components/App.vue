<template>
  <div>
    <m-header></m-header>
    <router-view></router-view>
    <m-footer></m-footer>
  </div>
</template>

<script>
import MHeader from "./layouts/Header";
import MFooter from "./layouts/Footer";
export default {
  components: {
    MHeader,
    MFooter
  }
};
</script>
