<template>
	<Modal title="Mybatis生成器" v-model="visible" :mask-closable='false' :width="500" @on-cancel="cancel">
		<Form ref="form" :model="mybatisConfig" :label-width="80" :rules="formValidate">
			<FormItem label="表名" prop="tableName">
				<Input v-model="mybatisConfig.tableName" disabled/>
			</FormItem>
			<FormItem label="JAVA类名" prop="domainObjectName">
				<Input v-model="mybatisConfig.domainObjectName"/>
			</FormItem>
			<FormItem label="包名" prop="targetPackage">
				<Input v-model="mybatisConfig.targetPackage"/>
			</FormItem>
			<FormItem label="模板" prop="templateName">
				<Select v-model="mybatisConfig.templateName" placeholder="请选择">
					<Option value="generatorConfig-mysql.ftl">generatorConfig-mysql.ftl</Option>
					<!--<Option value="tk-generatorConfig-mysql.ftl">tk-generatorConfig-mysql.ftl</Option>-->
				</Select>
			</FormItem>
			<FormItem label="generatedKey" prop="generatedKey">
				<Input v-model="mybatisConfig.generatedKey" placeholder="MySQL插入返回自增主键"/>
			</FormItem>
		</Form>
		<div slot="footer">
			<Button type="text" @click="cancel">取消</Button>
			<Button type="primary" :loading="submitLoading" @click="handelSubmit">生成并下载</Button>
		</div>
	</Modal>
</template>

<script>
	import {generatorDatabaseGen, generatorDatabaseDownload} from "../../utils/interface";
	import {setStore, getStore} from '../../utils/storage';
	import * as consts from '../../utils/const';

	export default {
		name: 'mybatis-gen',
		props: {
			value: Boolean,
			table: Object,
			columns: Array
		},
		data() {
			return {
				submitLoading: false,
				visible: this.value,
				mybatisConfig: {
					templateName: 'generatorConfig-mysql.ftl',
					javaVoGeneratorFlag: false,
					tableName: '',
					targetPackage: 'com.xxx.gen.mybatis',
//					tableName: '',
					generatedKey: '',
					domainObjectName: ''
				},
				formValidate: {
					domainObjectName: [{required: true, message: "请输入JAVA类名"}],
					targetPackage: [{required: true, message: "请输入包名"}],
					templateName: [{required: true, message: "请选择模板"}]
				}
			};
		},
		methods: {
			camelUpper(name) { // 驼峰 & 首字母大写
				let str = name.replace(/\_(\w)/g, function (all, letter) {
					return letter.toUpperCase();
				});
				return str.replace(str[0], str[0].toUpperCase());
			},
			cancel() {
				this.visible = false;
				this.$emit('on-cancel');
			},
			download(data) {
				if (!data) {
					return;
				}
				let url = window.URL.createObjectURL(new Blob([data]));
				let link = document.createElement('a');
				link.style.display = 'none';
				link.href = url;
				link.setAttribute('download', 'MybatisCodeGen.zip');
				document.body.appendChild(link);
				link.click();
			},
			intConfig() {
				this.mybatisConfig.tableName = this.table.tableName;
				this.mybatisConfig.domainObjectName = this.camelUpper(this.table.tableName);

				const db = getStore(consts.GENERATOR_CONFIG);
				if (!db) {
					return false;
				}
				let model = JSON.parse(db);
				this.mybatisConfig.targetPackage = model.targetPackage + '.mybatis';

				if (!this.columns) {
					return false;
				}
				for (let i = 0; i < this.columns.length; i++) {
					let column = this.columns[i];
					if (!column.autoIncrement) {
						continue;
					}
					this.mybatisConfig.generatedKey = column.actualColumnName;
					break;
				}
			},
			handelSubmit() {
				this.submitLoading = true;
				this.$refs.form.validate(valid => {
					if (!valid) {
						return;
					}
					const db = getStore(consts.GENERATOR_CONFIG);

					let model = JSON.parse(db);
					let table = {};
					model.javaVoGeneratorFlag = this.mybatisConfig.javaVoGeneratorFlag;
					model.targetPackage = this.mybatisConfig.targetPackage;

					table.tableName = this.mybatisConfig.tableName;
					table.domainObjectName = this.mybatisConfig.domainObjectName;
					if (this.mybatisConfig.generatedKey) {
						table.generatedKey = this.mybatisConfig.generatedKey;
					}
					model.tableList = [];
					model.tableList.push(table);

					generatorDatabaseGen(model, (data) => {
						this.$Message.success("生成成功");
						this.$emit('on-ok');
						generatorDatabaseDownload({uuid: data.body}, (response) => {
							this.download(response);
							this.submitLoading = false;
						});
					});
				});
			}
		},
		watch: {
			value(val) {
				this.visible = val;
				val && this.intConfig();
			}
		}
	};
</script>

<style>
</style>