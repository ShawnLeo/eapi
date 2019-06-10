<template>
	<div class="mybatis-table">
		<Row>
			<i-col span="24">
				<Button icon="ios-arrow-back" @click="back" style="float: left;">返回</Button>
				<Button @click="genModal" type="primary" icon="md-add" style="float: right;">生成</Button>
			</i-col>
		</Row>
		<Row>
			<br>
			<Table border :columns="columns" :data="column"></Table>
		</Row>

		<mybatis-gen v-model="modalVisible" :table="table" @on-cancel="modalVisible=false" @on-ok="modalVisible = false"></mybatis-gen>
		<!--<Modal :title="modalTitle" v-model="modalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}">-->
			<!--<Form ref="form" :model="form" :label-width="80" :rules="formValidate">-->
				<!--<FormItem label="表名" prop="tableName">-->
					<!--<Input v-model="form.tableName" disabled/>-->
				<!--</FormItem>-->
				<!--<FormItem label="JAVA类名" prop="domainObjectName">-->
					<!--<Input v-model="form.domainObjectName"/>-->
				<!--</FormItem>-->
				<!--<FormItem label="包名" prop="targetPackage">-->
					<!--<Input v-model="form.targetPackage"/>-->
				<!--</FormItem>-->
				<!--<FormItem label="自增列" prop="generatedKey">-->
					<!--<Input v-model="form.generatedKey"/>-->
				<!--</FormItem>-->
				<!--<FormItem label="模板" prop="templateName">-->
					<!--<Select v-model="form.templateName" placeholder="请选择">-->
						<!--<Option value="generatorConfig-mysql.ftl">generatorConfig-mysql.ftl</Option>-->
						<!--<Option value="tk-generatorConfig-mysql.ftl">tk-generatorConfig-mysql.ftl</Option>-->
					<!--</Select>-->
				<!--</FormItem>-->
			<!--</Form>-->
			<!--<div slot="footer">-->
				<!--<Button type="text" @click="modalVisible=false">取消</Button>-->
				<!--<Button type="primary" :loading="submitLoading" @click="handelSubmit">生成并下载</Button>-->
			<!--</div>-->
		<!--</Modal>-->
	</div>

</template>

<script>
	import {setStore, getStore} from "../../../utils/storage";
	import * as consts from '../../../utils/const';
	import {generatorDatabaseGen, generatorDatabaseDownload} from "../../../utils/interface";

	import mybatisGen from '../../../components/generator/mybatisGen.vue';

	export default {
		name: "mybatis-table",
		components: {
			mybatisGen
		},
		data() {
			return {
				column: [],
				table: {},
				submitLoading: false,
				modalVisible: false,
				loading: true,
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
			genModal() {
				const generatorConfig = getStore(consts.GENERATOR_CONFIG);

				if (!generatorConfig) {
					this.$Message.error("请配置数据库信息");
					this.$router.push("/code/generator/settings");
					return;
				}
				this.modalVisible = true;
			},
			back() {
				this.$router.go(-1);
			},
			submitLoadingFalse() {
				this.submitLoading = false;
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
			}
		},
		mounted() {
			this.column = this.$route.params.column;
			this.table = this.$route.params.table;
		}
	};
</script>

<style scoped>
	.mybatis-table{
		padding: 20px;
	}
</style>