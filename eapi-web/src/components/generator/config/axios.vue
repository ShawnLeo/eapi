<template>
  <div>
    <FormItem label="上下文" prop="context">
        <Input v-model="swaggerConfig.context"/>
    </FormItem>
  </div>
</template>

<script>
  export default {
    name: "axios",
    props: {
      swaggerConfig: Object
    }
  };
</script>

<style scoped>

</style>