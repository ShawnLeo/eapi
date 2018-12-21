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
	import {getLoginUser} from '../../utils/interface';
	import {getStore} from '../../utils/storage';
	import {ACCESS_TOKEN} from '../../utils/const';

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
			getUserInfo() {
				if (!getStore(ACCESS_TOKEN)) {
					return;
				}
				getLoginUser((resp) => {
					this.$store.dispatch('userInit', resp.body);
					this.$router.push('/');
				});
			}
		},
		mounted() {
			this.getUserInfo();
		}
	};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
