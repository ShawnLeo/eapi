<template>
	<div id="myDomId"></div>
</template>

<script type="text/ecmascript-6">
	import SwaggerUI from 'swagger-ui';
	import 'swagger-ui/dist/swagger-ui.css';
	import {baseUrl, context} from '../../utils/env';

	export default {

		mounted() {
			let projectId = this.$route.query.projectId;
			let url = baseUrl + '/v2/api-docs/' + projectId;
			let interfaceIds = this.$route.query.interfaceIds;

			if (interfaceIds && interfaceIds.length > 0) {
				let params = '?';
				interfaceIds.forEach(interfaceId => {
					params = params + 'interfaceIds[]=' + interfaceId + '&';
				});
				url = baseUrl + '/swagger/export/byinteface/' + projectId + params;
//				getByInterfaceIds(this.$route.query, (response) => {
//					SwaggerUI({ dom_id: '#myDomId', spec: response.body });
//				});
			}
			SwaggerUI({dom_id: '#myDomId', url: url});
		}
	};
</script>