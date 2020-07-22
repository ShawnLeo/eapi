<template>
	<div class="search">
		<Row>
			<Card>
				<Row>
					<Button icon="ios-arrow-back" @click="close" style="margin-right: 5px">返回</Button>
					<Button @click="add" type="primary" icon="md-add">生成</Button>
				</Row>
				<Row>
					<br>
					<Table border :columns="columns" :data="column"></Table>
				</Row>
			</Card>
		</Row>
		<Modal :title="modalTitle" v-model="modalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}">
			<Form ref="form" :model="form" :label-width="80" :rules="formValidate">
				<FormItem label="表名" prop="tableName">
					<Input v-model="form.tableName" disabled/>
				</FormItem>
				<FormItem label="JAVA类名" prop="domainObjectName">
					<Input v-model="form.domainObjectName"/>
				</FormItem>
				<FormItem label="包名" prop="targetPackage">
					<Input v-model="form.targetPackage"/>
				</FormItem>
				<FormItem label="自增列" prop="generatedKey">
					<Input v-model="form.generatedKey"/>
				</FormItem>
				<FormItem label="模板" prop="templateName">
					<Select v-model="form.templateName" placeholder="请选择">
						<Option value="generatorConfig-mysql.ftl">generatorConfig-mysql.ftl</Option>
						<Option value="tk-generatorConfig-mysql.ftl">tk-generatorConfig-mysql.ftl</Option>
					</Select>
				</FormItem>
			</Form>
			<div slot="footer">
				<Button type="text" @click="modalVisible=false">取消</Button>
				<Button type="primary" :loading="submitLoading" @click="handelSubmit">生成并下载</Button>
			</div>
		</Modal>
	</div>

</template>

<script>
	import {Message} from 'iview';
	import {setStore, getStore} from "../../../utils/storage";
	import * as consts from '../../../utils/const';

	export default {
		name: "mybatis-fragment",
		props: {
			column: Array,
			table: Object
		},
		data() {
			return {
				submitLoading: false,
				modalVisible: false,
				modalTitle: "Mybatis生成器",
				currView: "index",
				loading: true,
				form: {
					templateName: "",
					javaVoGeneratorFlag: "false",
					tableName: this.table.tableName,
					generatedKey: "",
					domainObjectName: ""
				},
				formValidate: {
					domainObjectName: [{required: true, message: "请输入JAVA类名"}],
					templateName: [{required: true, message: "请选择模板"}],
				},
				columns: [

					{
						type: "index",
						width: 60,
						align: "center"
					},
					{
						title: "actualColumnName",
						key: "actualColumnName"
					},
					{
						title: "autoIncrement",
						key: "autoIncrement"
					},
					{
						title: "defaultValue",
						key: "defaultValue"
					},
					{
						title: "generatedColumn",
						key: "generatedColumn"
					},
					{
						title: "nullable",
						key: "nullable"
					},
					{
						title: "remarks",
						key: "remarks"
					},
				]
			};
		},
		methods: {
			close() {
				this.$emit("close", true);
			},
			add() {
				this.$refs.form.resetFields();
				this.modalVisible = true;
			},
			ok() {
				this.$refs.form.resetFields();
				this.modalVisible = false;
			},
			submitLoadingFalse() {
				this.submitLoading = false;
			}, download(data) {
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
			handelSubmit() {
				this.submitLoading = true;
				let formData = this.form;
				let submitLoadingFalseFun = this.submitLoadingFalse;
				this.$refs.form.validate(valid => {
					if (valid) {
						const db = getStore(consts.GENERATOR_CONFIG);
						if (!db) {
							submitLoadingFalseFun();
							Message.error("请配置数据库信息");
						} else {
							let model = JSON.parse(db);
							let tableList = {};
							model.javaVoGeneratorFlag = formData.javaVoGeneratorFlag;
							tableList.tableName = formData.tableName;
							tableList.domainObjectName = formData.domainObjectName;
							if (formData.generatedKey) {
								tableList.generatedKey = formData.generatedKey;
							}
							model.tableList = [];
							model.tableList.push(tableList);


							let ok = this.ok;
							let download = this.download;
							generatorDatabaseGen(model, function (data) {
								if (data.header.code == '0') {
									Message.success("生成成功");
									ok();
									generatorDatabaseDownload({uuid: data.body}, (response) => {
										download(response);
									});
								} else {
									Message.error("生成失败");
								}
								submitLoadingFalseFun();
							});
						}
					}
				});
			}
		},
		mounted() {
		}
	};
</script>