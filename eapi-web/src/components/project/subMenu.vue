<template>
  <Card class="fl left-content">
    <Menu ref="orderNav" theme="light" :active-name="$route.query.group" width="200px"
          @on-select="selectMenu">
      <Menu-Item name="/">
        全部项目组
      </Menu-Item>
      <Menu-Item name="meimeitech">
        默认项目组
      </Menu-Item>
      <!--<Menu-Item name="meme2c">-->
        <!--美美理财-->
      <!--</Menu-Item>-->
    </Menu>
  </Card>
</template>

<script>
  export default {
    name: 'order-menu',
    methods: {
      selectMenu(name) {
        this.$router.push({path: '/project/list', query: {'group': name}});
      }
    },
    updated() {
      this.$nextTick(() => {
        if (this.$refs.orderNav) {
          this.$refs.orderNav.updateActiveName();
        }
      });
    }
  };
</script>

<style lang="less">
</style>
