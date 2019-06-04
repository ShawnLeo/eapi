<template>
	<div class="search">
		<br>
		<mybatis-fragment v-if="currView=='see'" @close="currView='index'" :column="column" :table="table" style="min-height: 400px"/>
		<Card v-show="currView=='index'" style="min-height: 400px">
			<Row class="operation">
				<Button @click="getDataList" type="primary" icon="md-refresh" style="margin-left: 5px;">刷新</Button>
			</Row>
			<Row>
				<Table :loading="loading" border :columns="columns" :data="data" ref="table"></Table>
			</Row>
		</Card>
	</div>
</template>

<script>
	import {generatorDatabaseAll} from "../../../utils/interface";
	import {setStore, getStore} from "../../../utils/storage";
	import {Message} from 'iview';
	import mybatisFragment from "./mybatisFragment.vue";

	export default {
		name: "codeGeneratorMbatis",
		components: {mybatisFragment},
		data() {
			return {
				column: [],
				table: {},
				currView: "index",
				loading: true, // 表单加载状态
				columns: [
					{
						type: "index",
						width: 60,
						align: "center"
					},
					{
						title: "表名",
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
											type: "primary",
											size: "small",
											icon: "ios-create-outline"
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
											type: "primary",
											size: "small",
											icon: "ios-create-outline"
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

							]);
						}
					}
				],
				data: [], // 表单数据
				datadb: {}, // 表单数据
			};
		},
		methods: {
			init() {
				this.getDataList();
			},
			submited() {
				this.currView = "index";
				this.getDataList();
			},
			setData(_data) {
				this.data = [];
				for (let i = 0; i < _data.table.length; i++) {
					this.data.push(_data.table[i]);
				}
				this.datadb = _data;
			},
			getDataList() {
				this.loading = true;

				const db = getStore("generator_db");
				if (!db) {
					Message.error("请先配置数据库");
					this.loading = false;
					return;
				}
				let setDataFun = this.setData;
				generatorDatabaseAll(JSON.parse(db), function (data) {
					if (data.header.code == '0') {

						setDataFun(data.body);
						Message.success("数据获取成功");
					} else {
						Message.error("数据获取失败");
					}
				});
				this.loading = false;
			},
			add() {
				this.currView = "add";
			},
			gen(v) {
				this.currView = "edit";
			},
			see(v) {
				this.table = v;
				this.column = this.datadb.column[v.tableName];
				this.currView = "see";
			}
		},
		mounted() {
			this.init();
		}
	};
</script>

<style>
	.search .operation {
		margin-bottom: 2vh;
	}

	.search .select-count {
		font-size: 13px;
		font-weight: 600;
		color: #40a9ff;
	}

	.select-clear {
		margin-left: 10px;
	}

	.search .page {
		margin-top: 2vh;
	}

	.search .drop-down {
		font-size: 13px;
		margin-left: 5px;
	}

	.back-title {
		color: #515a6e;
	}
</style>