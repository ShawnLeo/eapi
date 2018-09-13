<template>
  <div class="footer">
    <Footer>
      <BackTop :bottom="200">
        <!--<div class="top">返回顶端</div>-->
      </BackTop>
    </Footer>
  </div>
</template>

<script>
  export default {
    name: 't-footer',
    data() {
      return {
        msg: ''
      };
    },
    methods: {}
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
