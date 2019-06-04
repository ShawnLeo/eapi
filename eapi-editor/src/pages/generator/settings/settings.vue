<template>
	<div>
		<br>
		<Card style="min-height: 400px">
			<Form ref="databaseForm" :model="database" :label-width="200" label-position="right" :rules="databaseValidate">
				<FormItem label="Mysql数据库url：" prop="dbConnectionURL">
					<Input type="text" v-model="database.dbConnectionURL" placeholder="请输入数据库url" style="width: 700px"/>
				</FormItem>
				<FormItem label="Mysql数据库用户名：" prop="dbUsername">
					<Input type="text" v-model="database.dbUsername" placeholder="请输入数据库用户名" style="width: 350px"/>
				</FormItem>
				<FormItem label="Mysql数据库密码：" prop="dbPassword">
					<Input type="text" v-model="database.dbPassword" placeholder="请输入数据库密码" style="width: 350px"/>
				</FormItem>

				<FormItem label="Mysql数据库驱动类：" prop="dbDriverClass">
					<Input type="text" v-model="database.dbDriverClass" placeholder="请输入数据库密码" disabled
						style="width: 350px"/>
				</FormItem>

				<FormItem label="生成JAVA类包名：" prop="targetPackage">
					<Input type="text" v-model="database.targetPackage" placeholder="请输入生成JAVA类包名" style="width: 350px"/>
				</FormItem>

				<FormItem>
					<Button type="primary" style="width: 100px;margin-right:5px" @click="seeDatabase">测试并配置</Button>
				</FormItem>
			</Form>
		</Card>
	</div>
</template>

<script>
	import {generatorDatabaseTest} from "../../../utils/interface";
	import {setStore, getStore} from "../../../utils/storage";
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
					dbPassword: [{required: true, message: "不能为空", trigger: "blur"}],
					targetPackage: [{required: true, message: "不能为空", trigger: "blur"}]
				},
			};
		},
		mounted: function () {

			const db = getStore("generator_db");
			if (db) {
				this.database = JSON.parse(db);
			}
		},
		methods: {
			seeDatabase() {
				this.$refs.databaseForm.validate(valid => {
					if (valid) {
						let db = this.database;
						generatorDatabaseTest(this.database, function (data) {
							if (data.header.code == '0' && data.body) {
								setStore("generator_db", db);
								Message.success("设置成功");
							} else {
								Message.error("设置失败");
							}
						});
					}
				});
			}
		}
	};
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>

