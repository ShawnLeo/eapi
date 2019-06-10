<template>
	<Row class="settings">
		<Form ref="databaseForm" :model="database" :label-width="200" :rules="databaseValidate">
			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">数据库配置</h2>
				</div>
				<div class="wrapper-content box">
					<FormItem label="数据库驱动类：" prop="dbDriverClass">
						<Input type="text" v-model="database.dbDriverClass" disabled />
					</FormItem>
					<FormItem label="数据库url：" prop="dbConnectionURL">
						<Input type="text" v-model="database.dbConnectionURL" placeholder="请输入数据库url" />
					</FormItem>
					<FormItem label="数据库用户名：" prop="dbUsername">
						<Input type="text" v-model="database.dbUsername" placeholder="请输入数据库用户名" />
					</FormItem>
					<FormItem label="数据库密码：" prop="dbPassword">
						<Input type="text" v-model="database.dbPassword" placeholder="请输入数据库密码" />
					</FormItem>
				</div>
			</i-col>

			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">其他配置</h2>
				</div>
				<div class="wrapper-content box">
					<FormItem label="包名：" prop="targetPackage">
						<Input type="text" v-model="database.targetPackage" placeholder="包名"/>
					</FormItem>
				</div>
			</i-col>

			<i-col span="24">
					<Button type="primary" style="margin: 20px auto; display: block;" @click="seeDatabase">测试并配置</Button>
			</i-col>
		</Form>
	</Row>
</template>

<script>
	import {generatorDatabaseTest} from "../../../utils/interface";
	import {setStore, getStore} from "../../../utils/storage";
	import * as consts from '../../../utils/const';
	import {Message} from 'iview';

	export default {
		name: "codeGeneratorSetting",
		data() {
			return {
				database: {
					dbConnectionURL: "",
					dbUsername: "",
					dbPassword: "",
					dbDriverClass: "com.mysql.jdbc.Driver",
					targetPackage: ""
				},
				databaseValidate: {
					// 表单验证规则
					dbConnectionURL: [{required: true, message: "不能为空", trigger: "blur"}],
					dbUsername: [{required: true, message: "不能为空", trigger: "blur"}],
//					dbPassword: [{required: true, message: "不能为空", trigger: "blur"}],
					targetPackage: [{required: true, message: "不能为空", trigger: "blur"}]
				},
			};
		},
		mounted: function () {

			const db = getStore(consts.GENERATOR_CONFIG);
			if (db) {
				this.database = JSON.parse(db);
			}
		},
		methods: {
			seeDatabase() {
				this.$refs.databaseForm.validate(valid => {
					if (valid) {
						let db = this.database;
						generatorDatabaseTest(this.database, () => {
							setStore(consts.GENERATOR_CONFIG, db);
							this.$Message.success("设置成功");
						});
					}
				});
			}
		}
	};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.settings {
		padding: 20px 80px;
	}
</style>

