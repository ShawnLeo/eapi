<template>
	<div class="mybatis">
		<!--<mybatis-fragment v-if="currView=='see'" @close="currView='index'" :column="column" :table="table" style="min-height: 400px"/>-->
		<Row>
			<i-col span="24">
				<Form ref="formInline" inline style="float: right;">
					<FormItem>
						<Button @click="init" type="primary" icon="md-refresh">刷新</Button>
					</FormItem>

					<FormItem prop="search">
						<Input type="text" placeholder="表名搜索" v-model="searchModel" @on-keyup="searchData"/>
					</FormItem>
				</Form>
			</i-col>
		</Row>

		<Row class="table-list">
			<Form ref="formInline" inline class="project-interface-form form-in-table" v-show="showEditMenus">
				<Button @click="batchModal = true" size="small">批量生成</Button>
			</Form>
			<Table :loading="loading" stripe :columns="tableColumns" :data="filterTables" ref="tables"
				@on-selection-change="onSelectionChange"></Table>
		</Row>

		<mybatis-gen v-model="modalVisible" :table="table" :columns="columns" @on-cancel="modalVisible=false"
			@on-ok="modalVisible = false"></mybatis-gen>


		<Modal title="Mybatis批量生成" v-model="batchModal" :mask-closable='false' :width="860">
			<Row>
				<i-col span="12">
					<Form ref="form" :model="mybatisConfig" :label-width="60" :rules="formValidate" width="">
						<FormItem label="包名" prop="targetPackage">
							<Input v-model="mybatisConfig.targetPackage"/>
						</FormItem>
						<FormItem label="模板" prop="templateName">
							<Select v-model="mybatisConfig.templateName" placeholder="请选择">
								<Option value="generatorConfig-mysql.ftl">generatorConfig-mysql.ftl</Option>
								<!--<Option value="tk-generatorConfig-mysql.ftl">tk-generatorConfig-mysql.ftl</Option>-->
							</Select>
						</FormItem>
					</Form>
				</i-col>
			</Row>
			<Table :loading="loading" stripe :columns="batchColumns" :data="selection"></Table>
				<!--<FormItem label="表名" prop="tableName">-->
					<!--<Input v-model="mybatisConfig.tableName" disabled/>-->
				<!--</FormItem>-->
				<!--<FormItem label="JAVA类名" prop="domainObjectName">-->
					<!--<Input v-model="mybatisConfig.domainObjectName"/>-->
				<!--</FormItem>-->

				<!--</FormItem>-->
				<!--<FormItem label="generatedKey" prop="generatedKey">-->
					<!--<Input v-model="mybatisConfig.generatedKey" placeholder="MySQL插入返回自增主键"/>-->
				<!--</FormItem>-->
			<div slot="footer">
				<Button type="text" @click="batchModal = false">取消</Button>
				<Button type="primary" :loading="submitLoading" @click="handelSubmit">批量生成</Button>
			</div>
		</Modal>
	</div>
</template>

<script>
	import {generatorDatabaseAll, generatorDatabaseGen, generatorDatabaseDownload} from "../../../utils/interface";
	import {setStore, getStore} from "../../../utils/storage";
	import * as consts from '../../../utils/const';
	import {camel, firstUpper, download} from '../../../utils/utils';
	import mybatisGen from '../../../components/generator/mybatisGen.vue';

	export default {
		name: "codeGeneratorMbatis",
		components: {mybatisGen},
		data() {
			return {
				tables: [], // 表单数据
				batchModal: false,
				filterTables: [],
				columnMaps: {},
				selection: [],
				searchModel: '',
				table: {},
				columns: [],
				mybatisConfig: {
					targetPackage: 'com.xxx.gen.mybatis',
					templateName: 'generatorConfig-mysql.ftl'
				},
				formValidate: {
					targetPackage: [{required: true, message: "请输入包名"}],
					templateName: [{required: true, message: "请选择模板"}]
				},
				modalVisible: false,
				loading: true, // 表单加载状态
				showEditMenus: false,
				submitLoading: false,

				batchColumns: [
					{
						title: "表名",
						key: "tableName"
					},
					{
						title: "JAVA类名",
						key: "domainObjectName",
						render: (h, params) => {
							return h('div', [
								h('i-input', {
									attrs: {
										id: 'edit-name-' + params.index,
										placeholder: '名称',
										value: params.row.domainObjectName
									},
									on: {
										'on-blur': (event) => {
											this.selection[params.index].domainObjectName = event.target.value;
										}
									}
								})
							]);
						}
					},
					{
						title: "generatedKey",
						key: "generatedKey",
						render: (h, params) => {
							return h('div', [
								h('i-input', {
									attrs: {
										id: 'edit-generatedKey-' + params.index,
										placeholder: '名称',
										value: params.row.generatedKey
									},
									on: {
										'on-blur': (event) => {
											this.selection[params.index].generatedKey = event.target.value;
										}
									}
								})
							]);
						}
					},
					{
						title: '取消',
						key: 'action',
						width: 60,
						align: 'center',
						render: (h, params) => {
							return h('div', [
								h('Button', {
									props: {
										shape: 'circle',
										size: 'small',
										icon: 'md-remove'
									},
									style: {
										marginRight: '5px'
									},
									on: {
										click: () => {
											this.selection.splice(params.index, 1);
										}
									}
								})
							]);
						}
					}
				],
				tableColumns: [
//					{
//						type: "index",
//						width: 60,
//						align: "center"
//					},
					{
						type: 'selection',
						width: 50,
						align: 'center'
					},
					{
						title: "表名",
						sortable: true,
						key: "tableName"
					},
					{
						title: "描述",
						key: "remarks"
					},
					{
						title: "操作",
						key: "action",
						align: "center",
						render: (h, params) => {
							return h("div", [
								h(
									"Button",
									{
										props: {
											type: "info",
											size: "small",
											icon: "md-eye"
										},
										style: {
											marginRight: "5px"
										},
										on: {
											click: () => {
												this.see(params.row);
											}
										}
									},
									"查看"
								),
								h(
									"Button",
									{
										props: {
											type: "success",
											size: "small",
											icon: "md-cloud-download"
										},
										style: {
											marginRight: "5px"
										},
										on: {
											click: () => {
												this.gen(params.row);
											}
										}
									},
									"生成"
								),
								h(
									"Button",
									{
										props: {
											type: "primary",
											size: "small",
											icon: "md-copy"
										},
										style: {
											marginRight: "5px"
										},
										on: {
											click: () => {
												this.copy(params.row);
											}
										}
									},
									"复制"
								)
							]);
						}
					}
				],
				data: {} // 数据
			};
		},
		methods: {
			init() {
				this.getDataList();
			},
			onSelectionChange(selection) {
				this.showEditMenus = selection.length !== 0;
//				this.singleSelect = selection.length === 1;
				this.selection = selection;

				const db = getStore(consts.GENERATOR_CONFIG);
				if (!db) {
					return false;
				}
				let model = JSON.parse(db);
				this.mybatisConfig.targetPackage = model.targetPackage + '.mybatis';
				this.selection.forEach(item => {

					item.domainObjectName = firstUpper(camel(item.tableName));

					if (!this.columnMaps[item.tableName]) {
						return false;
					}
					for (let i = 0; i < this.columnMaps[item.tableName].length; i++) {
						let column = this.columnMaps[item.tableName][i];

						if (!column.autoIncrement) {
							continue;
						}
						item.generatedKey = column.actualColumnName;
						break;
					}

				});


			},
			getDataList() {
				this.loading = true;
				const db = getStore(consts.GENERATOR_CONFIG);
				if (!db) {
					this.$Message.error("请先配置数据库");
					this.loading = false;
					return;
				}
				generatorDatabaseAll(JSON.parse(db), (data) => {
					this.columnMaps = data.body.column;
					this.tables = data.body.table;
					this.filterTables = data.body.table;
					this.loading = false;
				});

			},
			gen(v) {
				this.table = v;
				this.modalVisible = true;
				this.columns = this.columnMaps[v.tableName];
			},
			copy(v) {
				let dataModel = [];
				let columns = this.columnMaps[v.tableName];
				columns.forEach(column => {
					dataModel.push({
						name: camel(column.actualColumnName),
						description: column.remarks
					});
				});
				this.$copyText(JSON.stringify(dataModel)).then(() => {
					this.$Message.success('复制成功');
				}, () => {
					this.$Message.error('复制失败');
				});
			},
			see(v) {
				this.$router.push({
					name: 'codeGeneratorMybatisTable',
					params: {table: v, column: this.columnMaps[v.tableName]}
				});
			},
			searchData() {
				this.filterTables = this.tables.filter(item => item.tableName.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
			},
			handelSubmit() {
				this.submitLoading = true;


				const db = getStore(consts.GENERATOR_CONFIG);

				let model = JSON.parse(db);

				model.javaVoGeneratorFlag = this.mybatisConfig.javaVoGeneratorFlag;
				model.targetPackage = this.mybatisConfig.targetPackage;

				model.tableList = [];

				this.selection.forEach((item) => {
					let table = {};
					table.tableName = item.tableName;
					table.domainObjectName = item.domainObjectName;
					if (item.generatedKey) {
						table.generatedKey = item.generatedKey;
					}
					model.tableList.push(table);
				});

				generatorDatabaseGen(model, (data) => {

					this.submitLoading = false;
//						this.$Message.success("生成成功");
					this.$emit('on-ok');

					this.$Modal.confirm({
						title: '生成成功',
						okText: '确定',
						iconType: 'success',
						cancelText: '取消',
						content: '<p>点击确定下载</p>',
						onOk: () => {
							generatorDatabaseDownload({id: data.body}, (response) => {
								download(response, 'MybatisCodeGen.zip');
							});
						}
					});
				});
			}
		},
		mounted() {
			this.init();
		}
	};
</script>

<style lang="less" scoped>
	.mybatis {
		padding: 20px;
	}

	.form-in-table {
		width: 95%;
		background: #f8f8f9;
		position: absolute;
		z-index: 2;
		height: 39px;
		line-height: 38px;
		left: 47px;
		top: 1px;
		button {
			margin-left: 10px;
		}
	}
</style>