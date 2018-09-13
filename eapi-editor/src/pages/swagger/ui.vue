<template>
  <div id="myDomId"></div>
</template>

<script type="text/ecmascript-6">
  import SwaggerUI from 'swagger-ui';
	import 'swagger-ui/dist/swagger-ui.css';
  import {baseUrl, context} from '../../utils/env';
  export default {
    mounted() {
      SwaggerUI({
        dom_id: '#myDomId',
        url: baseUrl + context + '/' + this.$route.query.projectId
      });
    }
  };
</script>

<style>
  /*@import 'swagger-ui/dist/swagger-ui.css';*/
</style>
