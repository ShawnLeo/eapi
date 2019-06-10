<template>
	<Row class="settings">
		<Form ref="projectItem" :model="project" :label-width=90 style="margin-top: 15px;" :rules="ruleValidate">
			<Spin size="large" fix v-if="spinShow"></Spin>
			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">项目信息</h2>
				</div>
				<div class="wrapper-content box">
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

					<FormItem label="发布地址">
						<Input v-model="project.deployUrl" placeholder="发布构建地址"/>
					</FormItem>

					<FormItem>
						<Button @click="updateProject" style="float: right">保存</Button>
					</FormItem>
				</div>
			</i-col>

			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">导入/导出</h2>
				</div>

				<div class="wrapper-content box clearfix">
					<div style="float: left;width: 50%;padding-left: 30px; border-right: 1px solid #d1d5da;">
						<h3><Icon type="ios-cloud-download-outline" /> 导出swagger文档</h3><br>
						<p>说明:</p>
						<p style="text-indent:2em;">导出标准格式的swagger.json文档</p><br><br>

						<Button type="success" @click="exportSwagger">导出</Button>

						<br><br>
					</div>
					<div style="float: right;width: 50%;padding-left: 60px">
						<h3><Icon type="ios-cloud-upload-outline" /> 导入swagger文档</h3><br>
						<p>说明:</p>
						<p style="text-indent:2em;">1.导入格式只支持标准的swagger.json</p>
						<p style="text-indent:2em;">2.导入swagger.json会导致现有数据丢失</p>
						<br>
						<Button type="info" @click="importSwagger" >导入</Button>

					</div>
				</div>
			</i-col>

			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">通用响应</h2>
				</div>

				<div class="wrapper-content box clearfix">
					<p>说明:</p>
					<p style="text-indent:2em;">开启通用响应，选择通用数据模型，在接口文档和Mock服务中所有 <b>自定义的响应数据模型</b> 会自动添加在 <b>通用数据模型</b> result字段中。</p>

					<Form :model="project" :label-width=90 style="margin-top: 15px;">
						<FormItem label="通用响应"  >
							<i-switch v-model="project.commonResponse"></i-switch>
						</FormItem>

						<FormItem label="通用数据模型" style="width: 500px;"  v-if="project.commonResponse">
							<Select v-model="project.commonResponseId">
								<Option :value="item.id" v-for="item in customDataModel" :key="item.id">{{item.name}}</Option>
							</Select>
						</FormItem>

						<FormItem label="字段名" style="width: 500px;"  v-if="project.commonResponse">
							<Input v-model="project.commonResponseField" placeholder="字段名，默认值：data"/>
						</FormItem>

						<FormItem>
							<Button @click="updateProject" type="success" style="float: right">保存</Button>
						</FormItem>
					</Form>


				</div>
			</i-col>

			<i-col span="24">
				<div class="wrapper-content">
					<h2 class="title-border">删除此项目</h2>
				</div>

				<div class="wrapper-content box danger-box">
					<p>此操作不可逆，将删除<b>{{project.title}}</b>项目, 及项目下接口、数据模型和标签等全部内容，且<b>不可恢复</b>！</p>
					<br>
					<p>请输入项目名称以进行确认:</p><br>
					<Input v-model="title" placeholder="项目名称"/>
					<br>
					<br>
					<FormItem :label-width=-1>
						<Button type="error" long  v-if="showDeleteButton" @click="handleRemove">我已了解风险，删除此项目</Button>
						<Button type="error" long disabled v-else>我已了解风险，删除此项目</Button>
					</FormItem>
				</div>
			</i-col>
		</Form>

		<Modal
				v-model="showImportModal"
				title="导入"
				@on-ok="okImport"
				@on-cancel="cancelImport">
			<Tabs value="name1">
				<TabPane label="本地导入" name="name1">
					<Upload
							multiple
							:on-success="handleSuccess"
							:on-error="handleError"
							type="drag"
							:action="uploadUrl"
							:headers="headers">
						<div style="padding: 20px 0">
							<Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
							<p>点击或拖拽上传</p>
						</div>
					</Upload>
				</TabPane>
				<TabPane label="URL导入" name="name2">
					<Form :label-width="80" style="margin-top: 20px">
						<FormItem label="URL地址">
							<Input v-model="swaggerUrl" placeholder="能直接访问的Swagger Json的URL地址"/>
						</FormItem>
					</Form>
				</TabPane>
			</Tabs>
		</Modal>
	</Row>
</template>

<script type="text/ecmascript-6">
	import {getProjectById, updateProject, checkProjectExists, deleteProjectById, importFromSwaggerUrl, exportSwaggerJson} from '../../../utils/interface';
	import {getStore} from '../../../utils/storage';
	import {ACCESS_TOKEN} from '../../../utils/const';
	import {responseHandler} from '../../../utils/fetch';
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
				showDeleteButton: false,
				customDataModel:[],
				project: {},
				title: '',
				uploadUrl: '',
				headers: {'Authorization': 'Bearer ' + getStore(ACCESS_TOKEN)},
				swaggerUrl: '',
				showImportModal: false,
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
				this.uploadUrl = baseUrl + '/swagger/import/file/' + this.projectId;
				this.getProjectById();
				this.customDataModel = JSON.parse(getStore('customDataModel'));
			},
			getProjectById() {
				getProjectById({id: this.projectId}, (response) => {
					this.project = response.body;
					this.$store.dispatch('project', response.body);
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
				exportSwaggerJson({
					projectId: this.project.id
				}, (response) => {
					this.download(response);
				});
			},
			// 下载文件
			download (data) {
				if (!data) {
					return;
				}
				let url = window.URL.createObjectURL(new Blob([data]));
				let link = document.createElement('a');
				link.style.display = 'none';
				link.href = url;
				link.setAttribute('download', 'swagger.json');
				document.body.appendChild(link);
				link.click();
			},
			importSwagger() {
				this.$Modal.confirm({
					title: '提示',
					content: '<p>导入仅支持标准的swagger.json，且会导致现有<span style="color: red;font-weight: bold;">数据丢失</span>，确认继续操作么？</p>',
					okText: '确认',
					cancelText: '取消',
					onOk: () => {
						this.showImportModal = true;
					}
				});
			},
			handleSuccess(res, file) {
				responseHandler(res, {callback: () => {
					this.$Message.success('数据导入成功！');
					this.showImportModal = false;
				}});
			},
			handleError(error, file) {
				this.$Message.error(error.message);
			},
			handleRemove() {
				deleteProjectById({id: this.project.id}, (response) => {
					this.$Message.success('删除成功！');
					this.$router.push({path: '/project/list'});
				});
			},
			okImport() {
				importFromSwaggerUrl({
					projectId: this.project.id,
					swaggerUrl: this.swaggerUrl
				}, (response) => {
					this.$Message.success('导入成功！');
					this.swaggerUrl = '';
					this.showImportModal = false;
				});
			},
			cancelImport() {
				this.showImportModal = false;
			}
		},
		computed: {
			state() {
				return this.$store.state.app;
			},
			projectId(){
				return this.$store.state.app.projectId || getStore('projectId');
			}
		},
		mounted() {
			this.init();
		},
		watch: {
			title: function (newTitle, oldTitle) {
					this.showDeleteButton = newTitle === this.project.title;
			}
		}
	};
</script>

<style scoped>
	.ivu-upload-list{
		display: none;
	}
	.settings{
		padding: 20px 80px;
	}
</style>
