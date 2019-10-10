<template>
	<div class="project-interface">
		<Row>
			<i-col span="24">

				<Button icon="ios-arrow-back" @click="back" style="float: left;margin-top: 15px;">返回</Button>

				<Form ref="formInline" inline class="project-interface-form">
					<FormItem prop="search">
						<Input type="text" placeholder="名称搜索" v-model="searchModel" @on-keyup="searchData"/>
					</FormItem>
				</Form>
			</i-col>
		</Row>


		<div class="clearfix"></div>
		<Form ref="formInline" inline class="project-interface-form form-in-table" v-show="showEditMenus">
			<Button @click="exportSelect" size="small">生成代码</Button>
		</Form>
		<Table stripe ref="selection" :columns="columns" :loading="loading" :data="filterInterfaces"
			@on-selection-change="onSelectionChange"></Table>

		<Modal title="代码生成" v-model="modalVisible" :mask-closable='false' :width="500">
			<Form ref="form" :model="swaggerConfig" :label-width="80" :rules="formValidate">

				<FormItem label="模板" prop="lang">
					<Select v-model="swaggerConfig.lang" placeholder="请选择" @on-change="langChange">
						<Option value="meimeitechSpring">meimeitechSpring</Option>
						<Option value="axios-fetch">axios-fetch</Option>
					</Select>
				</FormItem>
				<FormItem label="library" prop="library" v-if="springShow">
					<Select v-model="swaggerConfig.library" placeholder="请选择">
						<Option value="spring-boot">spring-boot</Option>
					</Select>
				</FormItem>

				<FormItem label="Api包名" prop="apiPackage" v-if="springShow">
					<Input v-model="swaggerConfig.apiPackage"/>
				</FormItem>
				<FormItem label="Model包名" prop="modelPackage" v-if="springShow">
					<Input v-model="swaggerConfig.modelPackage"/>
				</FormItem>

			</Form>
			<div slot="footer">
				<Button type="text" @click="modalVisible=false">取消</Button>
				<Button type="primary" :loading="submitLoading" @click="gen">生成</Button>
			</div>
		</Modal>
	</div>
</template>

<script type="text/ecmascript-6">
	import {
		getInterfaceList,
		getTagList,
		generatorSwaggerDownload,
		generatorSwaggerGen
	} from '../../../utils/interface';

	import {setStore, getStore} from '../../../utils/storage';
	import {baseUrl} from '../../../utils/env';
	import * as consts from '../../../utils/const';
	import {download} from '../../../utils/utils';

	export default {
		data() {
			return {
				projectId: this.$route.query.projectId,
				loading: false,
				showEditMenus: false,
				searchModel: '',
				modalVisible: false,
				swaggerConfig: {
					apiPackage: "com.xxx.gen.swagger.controller",
					modelPackage: "com.xxx.gen.swagger.model",
					lang: "meimeitechSpring",
					library: "spring-boot"
				},
				formValidate: {
					apiPackage: [
						{required: true, message: '请输入Api包名', trigger: 'blur'}
					],
					modelPackage: [
						{required: true, message: '请输入Model包名', trigger: 'blur'}
					],
					lang: [
						{required: true, message: '请选择模板', trigger: 'blur'}
					]
				},
				submitLoading: false,
				columns: [
					{
						type: 'selection',
						width: 50,
						align: 'center'
					},
					{
						title: '名称',
						key: 'name',
						render: (h, params) => {
							if (params.row.status === 500) {
								return h('span', {
									style: {
										'text-decoration': 'line-through'
									}
								}, params.row.name);
							} else {
								return h('a', {}, params.row.name);
							}

						}
					},
					{
						title: '方法',
						width: 120,
						sortable: true,
						render: (h, params) => {
							let color = 'green';
							switch (params.row.method) {
								case 'get':
									color = 'blue';
									break;
								case 'put':
									color = 'yellow';
									break;
								case 'post':
									color = 'green';
									break;
								case 'delete':
									color = 'red';
									break;
							}
							if (params.row.status === 500) {
								color = 'default';
							}
							return h('Tag', {
								props: {
									color: color
								}
							}, params.row.method);
						},
						key: 'method'
					},
					{
						title: '路径',
						key: 'path',
						sortable: true,
						render: (h, params) => {
							if (params.row.status === 500) {
								return h('span', {
									style: {
										'text-decoration': 'line-through'
									}
								}, params.row.path);
							} else {
								return h('a', {}, params.row.path);
							}

						}
					},
					{
						title: '标签',
						key: 'tag',
						sortable: true,
						render: (h, params) => {
							let tags = [];
							params.row.tags.forEach((tag) => {
								tags.push(h('Tag', tag.name));
							});
							return tags;
						}
					},
					{
						title: '创建人',
						key: 'createrUserName'
					},
					{
						title: '创建时间',
						width: 110,
						sortable: true,
						render: (h, params) => {
							return h('span', params.row.createTime.substring(0, 10));
						}
					}
				],
				interfaces: [],
				filterInterfaces: [],
				tags: []
			};
		},
		computed: {
			state() {
				return this.$store.state.app;
			},
			springShow() {
				// TODO 显示配置服务器获取
				return this.swaggerConfig.lang === 'meimeitechSpring';
			}
		},
		methods: {
			init() {
//				this.projectId = this.state.projectId || getStore('projectId');
				this.getInterfaceList();
				this.getTagList();
			},
			getInterfaceList() {
				this.loading = true;
				let projectId = this.projectId;
				getInterfaceList({projectId: projectId}, (response) => {
					if (response.header.code === '0') {
						this.interfaces = response.body;
						this.filterInterfaces = response.body;
					} else {
						this.$Message.error(response.header.message);
					}
				});
				this.loading = false;
			},
			getTagList() {
				getTagList({projectId: this.projectId}, (response) => {
					if (response.header.code === '0') {
						this.tags = response.body;
					} else {
						this.$Message.error(response.header.message);
					}
				});
			},
			onSelectionChange(selection) {
				this.showEditMenus = selection.length !== 0;
				this.singleSelect = selection.length === 1;
				this.selection = selection;
			},
			exportSelect() {
//				let selectIds = [];
				this.packageInitFromDb() || this.packageInitFromConfig();
				this.modalVisible = true;
				this.swaggerConfig.targetProjectId = this.projectId;
				let url = baseUrl + '/swagger/export/byinteface/' + this.projectId + '?type=SWAGGER_JSON';
				if (this.selection && this.selection.length > 0) {
					let params = '?type=SWAGGER_JSON';
					this.selection.forEach(selected => {
						params = params + '&interfaceIds[]=' + selected.id;
					});
					url = baseUrl + '/swagger/export/byinteface/' + this.projectId + params;
				}
				this.swaggerConfig.targetProject = url;

//				exportByInterfaceIds({projectId: this.projectId, interfaceIds: selectIds}, (response) => {});
			},
			searchData() {
				this.filterInterfaces = this.interfaces.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
			},
			langChange() {
				this.swaggerConfig.library = '';
				if (!this.springShow) {
					this.swaggerConfig.apiPackage = '';
					this.swaggerConfig.modelPackage = '';
				} else {
					this.swaggerConfig.library = 'spring-boot';
					this.packageInitFromConfig();
				}
			},
			packageInitFromDb() { // 自定义过的配置
				const swaggerConfigDb = getStore(consts.SWAGGER_CONFIG + this.swaggerConfig.targetProjectId);
				if (!swaggerConfigDb) {
					return false;
				}
				this.swaggerConfig = JSON.parse(swaggerConfigDb);
				return true;
			},
			packageInitFromConfig() { // 默认配置
				const db = getStore(consts.GENERATOR_CONFIG);
				if (!db) {
					return false;
				}
				let model = JSON.parse(db);
				this.swaggerConfig.apiPackage = model.artifactId + '.' + model.groupId + '.gen.swagger.controller';
				this.swaggerConfig.modelPackage = model.artifactId + '.' + model.groupId + '.gen.swagger.model';
				this.swaggerConfig.artifactId = model.artifactId;
				this.swaggerConfig.groupId = model.groupId;
				return true;
			},
			gen(row) {
				setStore(consts.SWAGGER_CONFIG + this.swaggerConfig.targetProjectId, this.swaggerConfig);
				generatorSwaggerGen(this.swaggerConfig, (data) => {
					this.modalVisible = false;
					this.$Modal.confirm({
						title: '生成成功',
						okText: '确定',
						iconType: 'success',
						cancelText: '取消',
						content: '<p>点击确定下载</p>',
						onOk: () => {
							generatorSwaggerDownload({id: this.swaggerConfig.targetProjectId}, (response) => {
								download(response, 'SwaggerCodeGen.zip');
							});
						}
					});
				});
			},
			back() {
				this.$router.go(-1);
			}
		},
		mounted() {
			this.init();
		}
	};
</script>

<style lang="less">
	.project-interface {
		.project-interface-form {
			margin-top: 15px;
			float: right;
			.ivu-form-item {
				margin-bottom: 0px
			}
		}
		.form-in-table {
			width: 93%;
			background: #f8f8f9;
			position: absolute;
			z-index: 2;
			height: 38px;
			line-height: 38px;
			left: 60px;
			top: 127px;
			button {
				margin-left: 10px;
			}
		}
		.path-input .ivu-form-item-content {
			margin-left: 0px !important;
		}
		.ivu-table-wrapper, .ivu-page {
			margin-top: 15px;
		}
		.isdeprecated {
			color: rgba(0, 0, 0, .25);
			border-color: #dcdee2;
			&:hover {
				color: rgba(0, 0, 0, .25);
			}
		}
	}
</style>
