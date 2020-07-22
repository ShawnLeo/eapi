<template>
	<div class="enter-email">
		<div class="cont">
			<div class="cont1">
				<div class="cont1_o">
					<div class="cont1_div">
						{{ $t("enterEmail.title") }}
					</div>
					<Form class="password-form" ref="formValidate" :model="formValidate" :rules="ruleValidate">
						<div class="user">
							<Form-item prop="email" :label="$t('enterEmail.email')">
								<Input type="text" v-model="formValidate.email" :placeholder="$t('enterEmail.enterEmail')"/>
							</Form-item>
						</div>
						<div id="your-dom-id" class="nc-container user"></div>
						<Button type="primary" id="loginbutton" :loading="loading" @click="handleSubmit('formValidate')">
							<span v-if="!loading">{{$t("enterEmail.btnName")}}</span>
							<span v-else>{{$t("enterEmail.btnLoading")}}</span>
						</Button>
					</Form>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import {emailResetSend} from '../../utils/interface';

	export default {
		name: 'enterEmail',
		data() {
			return {
				formValidate: {
					email: this.$route.query.username
				},
				ruleValidate: {
					email: [
						{required: true, message: this.$t('enterEmail.enterEmail'), trigger: 'blur'}
					]
				},
				loading: false
			};
		},
		methods: {
			handleSubmit(name) { // login
				this.$refs[name].validate(async (valid) => {
					if (valid) {
						await emailResetSend({email: this.formValidate.email}, response => {
							if (response.header.code === '0') {
								this.$router.push({
									path: '/user/reset/email',
									query: {username: this.formValidate.email}
								});
							}
						});
					}
				});
			},
			closeModal() {
				this.openValid = false;
			}
		},
		mounted() {
		}
	};
</script>

<style lang="less">
	@import '../../assets/css/enter.less';
</style>
