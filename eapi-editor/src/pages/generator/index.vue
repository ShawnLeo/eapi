<template>
	<div class="project">

		<Card class="main-content right-content clearfix">
			<div class="sub-menu">
				<Menu ref="mainNav" mode="horizontal" theme="light" :active-name="$route.meta.subMenuActive" class="fl"
					@on-select="selectMenu">
					<Menu-Item name="/code/generator/settings">
						配置
					</Menu-Item>
					<Menu-Item name="/code/generator/mybatis">
						Mybatis生成
					</Menu-Item>
					<Menu-Item name="/code/generator/swagger">
						Swagger生成
					</Menu-Item>
					<!--<Menu-Item name="/code/generator/vue">-->
						<!--Vue生成-->
					<!--</Menu-Item>-->
					<!--<Menu-Item name="/code/generator/vuetest">-->
						<!--Vue测试-->
					<!--</Menu-Item>-->
				</Menu>
				<div class="clearfix"></div>
			</div>
			<router-view></router-view>
		</Card>
	</div>
</template>

<script type="text/ecmascript-6">
	import {getStore} from '../../utils/storage';
	import {publishProject, getProjectById, getCustomDataModelList, getDataModelList} from '../../utils/interface';

	export default {
		data() {
			return {
				activeTabName: this.$route.path
			};
		},
		computed: {
			state() {
				return this.$store.state.app;
			},
			projectId() {
				return this.$store.state.app.projectId;
			}
		},
		methods: {
			selectMenu(name) {
				this.$router.push({path: name});
			}
		}
	};
</script>

<style lang="less">
	.project {
		.title {
			margin-bottom: 15px;
		}
		.main-content {
			.sub-menu {
				border-bottom: 1px solid #e9eaec;
				.ivu-menu {
					&:after {
						background: none;
					}
				}
				.tab-bar {
					float: right;
					/*width: 50%;*/
					position: relative;
					top: 16px;
					.sub-menu-button {
						margin-left: 10px;
					}
				}
			}
		}
	}
</style>
