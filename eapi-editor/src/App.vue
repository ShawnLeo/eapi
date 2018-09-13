<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
	export default {
		name: 'App',
		mounted() {
			// 自动设置语言
			const navLang = navigator.language;
			const localLang = (navLang === 'zh-CN' || navLang === 'en-US') ? navLang : false;
			this.$i18n.locale = window.localStorage.lang || localLang || 'zh-CN';
		}
	};
</script>

<style>
</style>
