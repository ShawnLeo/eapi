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