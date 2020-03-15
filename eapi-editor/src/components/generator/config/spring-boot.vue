<template>
  <div>
    <FormItem label="library" prop="library">
      <Select v-model="swaggerConfig.library" placeholder="请选择">
        <Option value="spring-boot">spring-boot</Option>
      </Select>
    </FormItem>

    <FormItem label="Api包名" prop="apiPackage">
      <Input v-model="swaggerConfig.apiPackage"/>
    </FormItem>
    <FormItem label="Model包名" prop="modelPackage">
      <Input v-model="swaggerConfig.modelPackage"/>
    </FormItem>
  </div>
</template>

<script>
  export default {
    name: "spring-boot",
    props: {
      swaggerConfig: Object
    }
  };
</script>

<style scoped>

</style>