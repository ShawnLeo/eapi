<template>
	<div class="swagger">
		<Row>
			<i-col span="24">
				<!--<Button type="primary" icon="md-refresh" style="float: right;">模板管理</Button> -->
				<Button @click="init" type="primary" icon="md-refresh" style="float: right;">刷新</Button>
			</i-col>
		</Row>

		<Row class="project-list">
			<i-col span="24">
				<Table border :loading="loading" :columns="columnDef" :data="projects"></Table>
			</i-col>
		</Row>

		<Modal title="代码生成" v-model="modalVisible" :mask-closable='false' :width="500">
			<Form ref="form" :model="swaggerConfig" :label-width="80" :rules="formValidate">

				<FormItem label="模板" prop="lang">
					<Select v-model="swaggerConfig.lang" placeholder="请选择" @on-change="langChange">
						<Option value="meimeitechSpring">meimeitechSpring</Option>
						<Option value="axios-fetch">axios-fetch</Option>
					</Select>
				</FormItem>

				<spring-boot :swaggerConfig="swaggerConfig" v-if="swaggerConfig.lang == 'meimeitechSpring'"></spring-boot>
				<axios :swaggerConfig="swaggerConfig" v-if="swaggerConfig.lang === 'axios-fetch'"></axios>

				<!--<FormItem label="library" prop="library" v-if="swaggerConfig.lang === 'meimeitechSpring'">-->
					<!--<Select v-model="swaggerConfig.library" placeholder="请选择">-->
						<!--<Option value="spring-boot">spring-boot</Option>-->
					<!--</Select>-->
				<!--</FormItem>-->

				<!--<FormItem label="Api包名" prop="apiPackage" v-if="swaggerConfig.lang === 'meimeitechSpring'">-->
					<!--<Input v-model="swaggerConfig.apiPackage"/>-->
				<!--</FormItem>-->
				<!--<FormItem label="Model包名" prop="modelPackage" v-if="swaggerConfig.lang === 'meimeitechSpring'">-->
					<!--<Input v-model="swaggerConfig.modelPackage"/>-->
				<!--</FormItem>-->

			</Form>
			<div slot="footer">
				<Button type="text" @click="modalVisible=false">取消</Button>
				<Button type="primary" :loading="submitLoading" @click="gen">生成</Button>
			</div>
		</Modal>
	</div>
</template>

<script  type="text/ecmascript-6">
	import {getProjectList, generatorSwaggerGen, generatorSwaggerDownload} from '../../../utils/interface';
	import {setStore, getStore} from '../../../utils/storage';
	import * as consts from '../../../utils/const';
	import {realBaseUrl} from '../../../utils/env';
	import springBoot from '../../../components/generator/config/spring-boot';
	import axios from '../../../components/generator/config/axios';

	export default {
		name: "codeGeneratorSwagger",
    components: {
      'spring-boot': springBoot,
      'axios': axios
    },
		data() {
			return {
				projects: [],
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
				modalVisible: false,
				loading: false,
				submitLoading: false,
				columnDef: [
//					{
//						type: "index",
//						width: 60,
//						align: "center"
//					},
					{
						title: "项目Id",
						key: "id"
					},
					{
						title: "项目名称",
						key: "title",
						render: (h, params) => {
							return h('a', {
								on: {
									click: () => {
										this.$router.push({path: '/code/generator/swagger/interface', query: {projectId: params.row.id}});
									}
								}
							}, params.row.title);
						}
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
											type: "primary",
											size: "small",
											icon: "ios-create-outline"
										},
										style: {
											marginRight: "5px"
										},
										on: {
											click: () => {
												this.modalVisible = true;
												this.swaggerConfig.targetProjectId = params.row.id;
												this.swaggerConfig.targetProject = realBaseUrl + '/v2/api-docs/' +params.row.id + '?type=SWAGGER_JSON';
												this.packageInitFromDb() || this.packageInitFromConfig();
											}
										}
									},
									"全量生成"
								)
							]);
						}
					}
				]
			};
		},
		// computed: {
		// 	springShow() {
		// 		// TODO 显示配置服务器获取
		// 		return ;
		// 	}
		// },
		methods: {
			langChange() {
				this.swaggerConfig.library =  '';
				if (this.swaggerConfig.lang != 'meimeitechSpring') {
					this.swaggerConfig.apiPackage =  '';
					this.swaggerConfig.modelPackage =  '';
				} else {
					this.swaggerConfig.library =  'spring-boot';
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
			download(data) {
				if (!data) {
					return;
				}
				let url = window.URL.createObjectURL(new Blob([data]));
				let link = document.createElement('a');
				link.style.display = 'none';
				link.href = url;
				link.setAttribute('download', 'SwaggerCodeGen.zip');
				document.body.appendChild(link);
				link.click();
			},
			gen(row) {
				let download = this.download;
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
								download(response);
							});
						}
					});
				});
			},
			init() {
				this.loading = true;
				getProjectList({groupId: 'all'}, (response) => {
					this.projects = response.body;
					this.loading = false;
				});
			}
		},
		mounted() {
			this.init();
		}
	};

</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
	.swagger {
		padding: 20px;
	}

	.project-list {
		margin-top: 15px;
	}
</style>

