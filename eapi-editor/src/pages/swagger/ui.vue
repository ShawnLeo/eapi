<template>
  <div id="myDomId"></div>
</template>

<script type="text/ecmascript-6">
  import SwaggerUI from 'swagger-ui';
	import 'swagger-ui/dist/swagger-ui.css';
  import {baseUrl, context} from '../../utils/env';
	import { getByInterfaceIds } from '../../utils/interface';

  export default {

    mounted() {
  		let projectId = this.$route.query.projectId;

  		if (this.$route.query.interfaceIds && this.$route.query.interfaceIds.length > 0) {
				getByInterfaceIds(this.$route.query, (response) => {
					SwaggerUI({ dom_id: '#myDomId', spec: response.body });
				});
      } else {
				SwaggerUI({
          dom_id: '#myDomId',
          url: baseUrl + '/v2/api-docs/' + projectId
        });
      }
    }
  };
</script>