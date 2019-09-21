<template>
	<div class="project-interface">
		<Form ref="formInline" inline class="project-interface-form">
			<FormItem>
				<!--<Button type="primary" @click="handleSubmit('formInline')">Signin</Button>-->
				<Button type="primary" icon="md-add" @click="newInterface">新建接口</Button>
			</FormItem>
			<FormItem prop="search">
				<Input type="text" placeholder="名称搜索" v-model="searchModel" @on-keyup="searchData"/>
			</FormItem>
		</Form>
		<div class="clearfix"></div>
		<Form ref="formInline" inline class="project-interface-form form-in-table" v-show="showEditMenus">
			<Button @click="deleteInterface" size="small">删除</Button>
			<Button @click="editStatus = true;" size="small">设置状态</Button>
			<Button @click="showCopyModal" size="small" v-if="singleSelect">复制</Button>
			<Button @click="selectInterfaceDoc" size="small">接口文档</Button>
			<Button @click="exportSelect" size="small">导出选中</Button>
			<!--<Button size="small">设置标签</Button>-->
		</Form>
		<Table stripe ref="selection" :columns="columns" :loading="loading" :data="filterInterfaces"
			@on-selection-change="onSelectionChange"></Table>

		<Modal v-model="addInterfaceModal" title="新建接口" width="700" :mask-closable=false>
			<Form ref="interfaceItem" :model="interfaceItem" :label-width=80 :rules="ruleValidate">
				<FormItem label="接口名称" prop="name">
					<i-input v-model="interfaceItem.name" placeholder="最多20个中文或者40个英文字符"></i-input>
				</FormItem>
				<FormItem label="接口路径" prop="method" style="width: 30%;float:left;">
					<Select v-model="interfaceItem.method" require="true">
						<Option value="get">GET</Option>
						<Option value="post">POST</Option>
						<Option value="put">PUT</Option>
						<Option value="delete">DELETE</Option>
					</Select>
				</FormItem>
				<FormItem prop="path" style="width:70%;float:left;" :label-width=-1>
					<i-input v-model="interfaceItem.path" placeholder="例如：/api/get/{id}"></i-input>
				</FormItem>
				<div class="clearfix"></div>
				<FormItem label="标签" style="width:50%;float:left;" prop="tagIds">
					<Select v-model="interfaceItem.tagIds" multiple require="true">
						<Option v-for="(tag, index) in tags" :value="tag.id" :key="index">{{tag.name}}</Option>
					</Select>
				</FormItem>
				<FormItem label="操作Id(方法名)" style="width:50%;float:left;" prop="operationId" :label-width=120>
					<i-input v-model="interfaceItem.operationId" placeholder="例如：findByUserId"></i-input>
				</FormItem>
				<div class="clearfix"></div>
				<FormItem label="描述" prop="description">
					<i-input v-model="interfaceItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
						placeholder="描述"></i-input>
				</FormItem>
			</Form>
			<div slot="footer">
				<Button type="text" size="large" @click="reset">重置</Button>
				<Button type="primary" size="large" @click="addInterface">确定</Button>
			</div>
		</Modal>

		<Modal v-model="editStatus"
			title="设置状态"
			@on-ok="okEditStatus">
			<Form class="project-interface-form form-in-table" :label-width=100>
				<FormItem label="设置状态" style="width: 80%;float:left;">
					<Select v-model="status">
						<Option value="100">未开始</Option>
						<Option value="200">开发中</Option>
						<Option value="300">测试中</Option>
						<Option value="400">已完成</Option>
						<Option value="500">已废弃</Option>
					</Select>
				</FormItem>
			</Form>
			<div class="clearfix"></div>
		</Modal>

		<Modal v-model="copyStatus" title="复制接口" width="700" :mask-closable=false>
			<Form ref="copyInterfaceItem" :model="copyInterfaceItem" :label-width=80 :rules="copyRuleValidate">
				<FormItem label="接口名称" prop="name">
					<i-input v-model="copyInterfaceItem.name" placeholder="最多20个中文或者40个英文字符"></i-input>
				</FormItem>
				<FormItem label="接口路径" prop="method" style="width: 30%;float:left;">
					<Select v-model="copyInterfaceItem.method" require="true">
						<Option value="get">GET</Option>
						<Option value="post">POST</Option>
						<Option value="put">PUT</Option>
						<Option value="delete">DELETE</Option>
					</Select>
				</FormItem>
				<FormItem prop="path" style="width:70%;float:left;" :label-width=-1>
					<i-input v-model="copyInterfaceItem.path" placeholder="例如：/api/get/{id}"></i-input>
				</FormItem>
				<div class="clearfix"></div>
				<FormItem label="标签" style="width:50%;float:left;" prop="tagIds">
					<Select v-model="copyInterfaceItem.tagIds" multiple require="true">
						<Option v-for="(tag, index) in tags" :value="tag.id" :key="index">{{tag.name}}</Option>
					</Select>
				</FormItem>
				<FormItem label="操作Id(方法名)" style="width:50%;float:left;" prop="operationId" :label-width=120>
					<i-input v-model="copyInterfaceItem.operationId" placeholder="例如：findByUserId"></i-input>
				</FormItem>
				<div class="clearfix"></div>
				<FormItem label="描述" prop="description">
					<i-input v-model="copyInterfaceItem.description" type="textarea" :autosize="{minRows: 2,maxRows: 5}"
						placeholder="描述"></i-input>
				</FormItem>
			</Form>
			<div class="clearfix"></div>
			<div slot="footer">
				<Button type="text" size="large" @click="copyStatus = false">取消</Button>
				<Button type="primary" size="large" @click="okCopyInteface">确定</Button>
			</div>
		</Modal>
	</div>
</template>

<script type="text/ecmascript-6">
	import {
		createInterface,
		copyInterface,
		getInterfaceList,
		getTagList,
		deleteInterfaceInBatch,
		checkInterfaceExists,
		updateInterface,
		changeStatus,
		exportByInterfaceIds
	} from '../../../utils/interface';
	import {getStore} from '../../../utils/storage';
	import {download} from '../../../utils/utils';

	export default {
		data() {
			const validateNameExists = (rule, value, callback) => {
				checkInterfaceExists(this.interfaceItem, (response) => {
					if (response.header.code === '0') {
						if (response.body) {
							callback(new Error('接口已存在'));
						} else {
							callback();
						}
					} else {
						callback(new Error(response.header.message));
					}
				});
			};
			const validateCopyNameExists = (rule, value, callback) => {
				let validate = {
					method: this.copyInterfaceItem.method,
					path: this.copyInterfaceItem.path,
					projectId: this.copyInterfaceItem.projectId
				};
				checkInterfaceExists(validate, (response) => {
					if (response.header.code === '0') {
						if (response.body) {
							callback(new Error('接口已存在'));
						} else {
							callback();
						}
					} else {
						callback(new Error(response.header.message));
					}
				});
			};
			return {
				projectId: '',
				addInterfaceModal: false,
				interfaceItem: {
					name: '',
					method: 'get',
					path: '',
					tagIds: [],
					status: 100,
					operationId: '',
					requestType: '',
					description: '',
					deprecated: false,
					projectId: ''
				},
				copyInterfaceItem: {},
				editStatus: false,
				copyStatus: false,
				status: '',
				loading: false,
				showEditMenus: false,
				singleSelect: false,
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
								return h('a', {
									on: {
										click: () => {
											this.$router.push({path: '/project/interface/edit', query: {id: params.row.id}});
										}
									}
								}, params.row.name);
							}

						}
					},
					{
						title: '方法',
						width: 80,
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
								return h('a', {
									on: {
										click: () => {
											this.$router.push({path: '/project/interface/edit', query: {id: params.row.id}});
										}
									}
								}, params.row.path);
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
						title: '状态',
						key: 'status',
						sortable: true,
						width: 90,
						render: (h, params) => {
							let color = 'primary';
							let label = '未开始';
							let isdeprecated = false;
							switch (params.row.status) {
								case 100:
									color = 'primary';
									label = '未开始';
									break;
								case 200:
									color = 'warning';
									label = '开发中';
									break;
								case 300:
									color = 'error';
									label = '测试中';
									break;
								case 400:
									color = 'success';
									label = '已完成';
									break;
								case 500:
									color = 'info';
									label = '已废弃';
									isdeprecated = true;
									break;
							}
							return h('Dropdown', {
								props: {
									trigger: 'click'
								},
								on: {
									'on-click': (name) => {
//										console.log(params.row.id);
										if (params.row.status === name) {
											return;
										}
										this.changStatus(params.row.id, name);
									}
								}
							}, [
								h('Button', {
									'class': {
										'isdeprecated': isdeprecated
									},
									props: {
										type: color,
										size: "small",
										ghost: true
									}
								}, label),
								h('DropdownMenu', {slot: 'list'}, [
									h('DropdownItem', {props: {name: 100}}, '未开始'),
									h('DropdownItem', {props: {name: 200}}, '开发中'),
									h('DropdownItem', {props: {name: 300}}, '测试中'),
									h('DropdownItem', {props: {name: 400}}, '已完成'),
									h('DropdownItem', {props: {name: 500}}, '已废弃')
								])
							]);
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
				searchModel: '',
				selection: [],
				tags: [],
				ruleValidate: {
					name: [
						{required: true, message: '请输入接口名称', trigger: 'blur'}
					],
					path: [
						{required: true, message: '请输入接口路径', trigger: 'blur'},
						{validator: validateNameExists, trigger: 'blur'}
					],
					method: [
						{required: true, message: '请输入接口路径', trigger: 'blur'},
						{validator: validateNameExists, trigger: 'change'}
					]
				},
				copyRuleValidate: {
					name: [
						{required: true, message: '请输入接口名称', trigger: 'blur'}
					],
					path: [
						{required: true, message: '请输入接口路径', trigger: 'blur'},
						{validator: validateCopyNameExists, trigger: 'blur'}
					],
					method: [
						{required: true, message: '请输入接口路径', trigger: 'blur'},
						{validator: validateCopyNameExists, trigger: 'change'}
					]
				}
			};
		},
		computed: {
			state() {
				return this.$store.state.app;
			}
		},
		methods: {
			init() {
				this.projectId = this.state.projectId || getStore('projectId');
				this.getInterfaceList();
				this.getTagList();
			},
			getInterfaceList() {
				this.loading = true;
				let projectId = this.projectId;
				this.interfaceItem.projectId = projectId;
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
			reset() {
				this.interfaceItem = {
					name: '',
					method: 'get',
					path: '',
					tagIds: [],
					status: 100,
					operationId: '',
					description: '',
					deprecated: false,
					projectId: this.projectId
				};
			},
			addInterface() {
				this.$refs['interfaceItem'].validate((valid) => {
					if (valid) {
						if (this.interfaceItem.method.toLowerCase() !== 'get') {
							this.interfaceItem.requestType = 'body'; // 默认值
						}
						createInterface(this.interfaceItem, (response) => {
							if (response.header.code === '0') {
								this.init();
								this.reset();
							} else {
								this.$Message.error(response.header.message);
							}
						});
						this.addInterfaceModal = false;
					}
				});
			},
			okCopyInteface: function () {
				this.$refs['copyInterfaceItem'].validate((valid) => {
					if (valid) {
						copyInterface(this.copyInterfaceItem, (response) => {
							this.init();
						});
						this.copyStatus = false;
					}
				});
			},
			onSelectionChange(selection) {
				this.showEditMenus = selection.length !== 0;
				this.singleSelect = selection.length === 1;
				this.selection = selection;
			},
			deleteInterface() {
				this.$Modal.confirm({
					title: '确认删除？',
					content: '<p>删除数据不可恢复！</p><p>确认要删除吗？</p>',
					onOk: () => {
						deleteInterfaceInBatch(this.selection, (response) => {
							if (response.header.code === '0') {
								this.$Message.success('删除成功！');
								this.showEditMenus = false;
								this.init();
							} else {
								this.$Message.error(response.header.message);
							}
						});
					},
					onCancel: () => {
						this.$Message.success("取消删除！");
					}
				});
			},
			exportSelect() {
				let selectIds = [];
				this.selection.forEach(selected => selectIds.push(selected.id));
				exportByInterfaceIds({projectId: this.projectId, interfaceIds: selectIds}, (response) => {
					download(response, 'swagger.json');
				});
			},
			selectInterfaceDoc() {
				let selectIds = [];
				this.selection.forEach(selected => selectIds.push(selected.id));
				this.$router.push({name: 'swaggerUI', query: {projectId: this.projectId, interfaceIds: selectIds}});
			},
			newInterface() {
				this.addInterfaceModal = true;
			},
			changStatus(interfaceId, status) {
				changeStatus([{
					id: interfaceId, status: status
				}], (resp) => {
					this.getInterfaceList();
				});
			},
			okEditStatus() {
				this.selection.forEach(item => {
					item.status = this.status;
				});
				changeStatus(this.selection, (response) => {
					if (response.header.code === '0') {
						this.$Message.success('设置成功！');
						this.showEditMenus = false;
						this.init();
					} else {
						this.$Message.error(response.header.message);
					}
				});
			},
			searchData() {
				this.filterInterfaces = this.interfaces.filter(item => item.name.toLowerCase().indexOf(this.searchModel.toLowerCase()) > -1);
			},
			showCopyModal() {
				this.copyStatus = true;
				this.copyInterfaceItem = this.selection[0];
				let tagIds = [];
				this.copyInterfaceItem.tags.forEach(tag => {
					tagIds.push(tag.id);
				});
				this.copyInterfaceItem.tagIds = tagIds;
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
