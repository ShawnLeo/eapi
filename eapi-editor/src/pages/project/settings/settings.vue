<template>
	<Row class="settings">
		<Form ref="projectItem" :model="project" :label-width=120 style="margin-top: 15px;" :rules="ruleValidate">
			<Spin size="large" fix v-if="spinShow"></Spin>
			<i-col span="24">
				<div class="wrapper-content">
					<h2>项目信息</h2>
				</div>
				<div class="wrapper-content">
					<FormItem label="项目名称" prop="title">
						<Input v-model="project.title" placeholder="项目名称"/>
					</FormItem>
					<Row>
						<i-col span="12">
							<FormItem label="项目版本" prop="version">
								<Input v-model="project.version" placeholder="文档版本"/>
							</FormItem>
						</i-col>
						<i-col span="12">
							<FormItem label="联系人邮箱" prop="contactEmail">
								<Input v-model="project.contactEmail" placeholder="联系人邮箱"/>
							</FormItem>
						</i-col>
					</Row>
					<Row>
						<i-col span="12">
							<FormItem label="域名" prop="host">
								<Input v-model="project.host" placeholder="域名"/>
							</FormItem>
						</i-col>
						<i-col span="12">
							<FormItem label="基础路径" prop="basePath">
								<Input v-model="project.basePath" placeholder="基础路径"/>
							</FormItem>
						</i-col>
					</Row>
					<FormItem label="项目描述" prop="description">
						<i-input v-model="project.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="项目描述"></i-input>
					</FormItem>
				</div>
			</i-col>
			<i-col span="24">
				<div class="wrapper-content">
					<h2>导入 Swagger.json</h2>
				</div>

				<div class="wrapper-content">
					<Upload
							multiple
							:on-success="handleSuccess"
							:on-error="handleError"
							type="drag"
							:action="uploadUrl">
						<div style="padding: 50px 0">
							<Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
							<p>点击或拖拽上传</p>
						</div>
					</Upload>
				</div>
			</i-col>
			<i-col span="24">
				<div class="wrapper-content">
					<h2>导出 Swagger.json</h2>
				</div>

				<div class="wrapper-content">
					<Button type="primary" @click="exportSwagger">导出</Button>
				</div>
			</i-col>

			<i-col span="24">
				<div class="wrapper-content">
					<h2>发布</h2>
				</div>
				<div class="wrapper-content">
					<FormItem label="发布地址">
						<Input v-model="project.deployUrl" placeholder="发布构建地址"/>
					</FormItem>
				</div>
			</i-col>
			<i-col span="24">
				<div class="wrapper-content">
					<FormItem>
						<Button type="primary" size="large" @click="updateProject">保存</Button>
					</FormItem>
				</div>
			</i-col>
		</Form>
	</Row>
</template>

<script type="text/ecmascript-6">
	import {getProjectById, updateProject, checkProjectExists} from '../../../utils/const';
	import {getStore} from '../../../utils/storage';
	import {baseUrl} from '../../../utils/env';

	export default {
		name: 'setting',
		data() {
			const validateNameExists = (rule, value, callback) => {
				checkProjectExists(this.project, (response) => {
					if (response.header.code === '0') {
						if (response.body) {
							callback(new Error('项目名称已存在'));
						} else {
							callback();
						}
					} else {
						callback(new Error(response.header.message));
					}
				});
			};
			return {
				spinShow: false,
				project: {},
				uploadUrl: '',
				ruleValidate: {
					title: [
						{required: true, message: '请输入项目名称', trigger: 'blur'},
						{validator: validateNameExists, trigger: 'blur'}
					],
					version: [
						{required: true, message: '请输入版本号', trigger: 'blur'}
					],
					contactEmail: [
						{required: true, message: '请输入邮箱', trigger: 'blur'}
					],
					host: [
						{required: true, message: '请输入域名', trigger: 'blur'}
					],
					basePath: [
						{required: true, message: '请输入基础路径', trigger: 'blur'}
					],
					description: [
						{required: true, message: '请输入项目描述', trigger: 'blur'}
					]
				}
			};
		},
		methods: {
			init() {
				this.getProjectById();
			},
			getProjectById() {
				let projectId = this.state.projectId || getStore('projectId');
				getProjectById({id: projectId}, (response) => {
					if (response.header.code === '0') {
						this.project = response.body;
						this.uploadUrl = baseUrl + '/v2/api-import/' + this.project.id;
					} else {
						this.$Message.error(response.header.message);
					}
				});
			},
			updateProject() {
				this.$refs['projectItem'].validate(async (valid) => {
					if (valid) {
						this.spinShow = true;
						updateProject(this.project, (response) => {
							if (response.header.code === '0') {
								this.$Message.success('更新成功！');
								this.init();
							} else {
								this.$Message.error(response.header.message);
							}
							this.spinShow = false;
						});
					}
				});
			},
			exportSwagger() {
				window.open(baseUrl + "/v2/api-export/" + this.project.id);
			},
			handleSuccess(res, file) {
				this.$Message.success('上传成功！');
			},
			handleError(error, file) {
				this.$Message.error(error.message);
			}
		},
		computed: {
			state() {
				return this.$store.state.app;
			}
		},
		mounted() {
			this.init();
		}
	};
</script>

<style>
	.wrapper-content {
		padding: 20px 25px;
		position: relative;
	}
	.ivu-upload-list{
		display: none;
	}
</style>
