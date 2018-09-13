<template>
  <div class="app-wrapper">
    <Layout>
      <t-header></t-header>
      <Content>
        <transition name="fade" mode="out-in">
          <router-view></router-view>
        </transition>
      </Content>
      <t-footer></t-footer>
    </Layout>
  </div>
</template>

<script>
  import tHeader from './header.vue';
  import tFooter from './footer.vue';
  export default {
    name: 'layout',
    components: {
      tHeader,
      tFooter
    },
    data() {
      return {
        msg: ''
      };
    },
    methods: {
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
