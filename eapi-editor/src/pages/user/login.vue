<template>
	<div class="login">
		<div class="cont">
			<div class="login-main">
				<div class="cont1">
					<div class="cont1_o">
						<div class="cont1_div">
							{{ $t("login.title") }}
						</div>
						<Form class="login-form" ref="formValidate" :model="formValidate" :rules="ruleValidate">
							<div class="user">
								<Form-item prop="username" :label="$t('login.username')">
									<Input type="text" v-model="formValidate.username" :placeholder="$t('login.enterUserName')"
										@on-enter="handleSubmit('formValidate')"/>
								</Form-item>
							</div>
							<span class="glyphicon glyphicon-phone form-control-feedback"></span>
							<div class="user">
								<Form-item prop="password" :label="$t('login.password')">
									<Input type="password" v-model="formValidate.password" :placeholder="$t('login.enterPassword')"
										@on-enter="handleSubmit('formValidate')"/>
								</Form-item>
							</div>
							<span class="glyphicon glyphicon-lock form-control-feedback"></span>

							<Button type="primary" id="loginbutton" :loading="login_loading"
								@click="handleSubmit('formValidate')">
								<span v-if="!login_loading">{{$t("login.btnName")}}</span>
								<span v-else>{{$t("login.btnLoading")}}</span>
							</Button>

							<div class="invest">
								<a @click="goRestPass" class="left">{{$t("login.forgetPass")}}</a>
								<span>{{$t("login.noAccount")}}</span>
								<router-link to="/user/register">{{$t("login.registerNow")}}</router-link>
							</div>
						</Form>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import { login, getLoginUser } from '../../utils/interface';
	import {ACCESS_TOKEN} from '../../utils/const';
	import {setStore} from '../../utils/storage';

	export default {
		name: 'login',
		data() {
			return {
				btnLoading: false,
				formValidate: {
					username: "",
					password: ""
				},
				ruleValidate: {
					username: [
						{required: true, message: this.$t('login.enterUserName'), trigger: 'blur'}
					],
					password: [
						{required: true, message: this.$t('login.enterPassword'), trigger: 'blur'}
					]
				},
				login_loading: false
			};
		},
		methods: {
			handleSubmit(name) { // login
				this.$refs[name].validate(async (valid) => {
					// 登录
					if (valid) {
						await login({
							username: this.formValidate.username,
							password: this.formValidate.password,
						}, (response) => {
							this.$Message.success(this.$t('login.loginSuccess'));
							setStore(ACCESS_TOKEN, response.body);
							getLoginUser((resp) => {
								this.$store.dispatch('userInit', resp.body);
								this.$router.push('/');
							});
						});
					}
				});
			},
			goRestPass: async function () {
				this.$router.push({path: '/user/forget', query: { username: this.formValidate.username }});
			},
		},
		mounted() {
			this.$store.dispatch('userInit', '');
		}
	};
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style lang="less">
	@import '../../assets/css/enter.less';
</style>
