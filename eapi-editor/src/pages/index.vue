<template>
  <div class="index">
    <div class="main-content">
      <!--<sub-menu></sub-menu>-->
      <router-view/>
    </div>
  </div>
</template>

<script>
  import subMenu from '../components/project/subMenu.vue';
  export default {
    name: 'index',
    components: {
      subMenu
    },
    data() {
      return {
        msg: '首页'
      };
    },
    methods: {
      goInterface() {
        this.$router.push('/project/interface');
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">

</style>
