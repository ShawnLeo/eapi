(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6f37f422"],{"43fb":function(t,e,s){"use strict";s.r(e);var a=function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"project"},[s("div",{staticClass:"title"},[s("h2",[s("Icon",{attrs:{type:"ios-bookmarks-outline"}}),s("router-link",{attrs:{to:{path:"/project/list"}}},[t._v(" "+t._s(t.state.project.groupName))]),t._v(" / "+t._s(t.state.project.title))],1),s("div")]),s("Card",{staticClass:"main-content right-content clearfix"},[s("div",{staticClass:"sub-menu"},[s("Menu",{ref:"mainNav",staticClass:"fl",attrs:{mode:"horizontal",theme:"light","active-name":t.$route.meta.subMenuActive},on:{"on-select":t.selectMenu}},[s("Menu-Item",{attrs:{name:"/project/interface"}},[t._v("\n          接口\n        ")]),s("Menu-Item",{attrs:{name:"/project/datamodel"}},[t._v("\n          数据模型\n        ")]),s("Menu-Item",{attrs:{name:"/project/tags"}},[t._v("\n          标签管理\n        ")]),s("Menu-Item",{attrs:{name:"/project/settings"}},[t._v("\n          管理\n        ")])],1),s("div",{staticClass:"tab-bar"},[s("Button",{staticClass:"fr sub-menu-button",attrs:{icon:"md-play"},on:{click:t.publish}},[t._v("发布")]),s("Button",{staticClass:"fr sub-menu-button",attrs:{icon:"md-eye"},on:{click:t.preview}},[t._v("接口文档")])],1),s("div",{staticClass:"clearfix"})],1),s("router-view")],1)],1)},n=[],o=s("5d2d"),i=s("ae31"),c={data:function(){return{activeTabName:this.$route.path}},computed:{state:function(){return this.$store.state.app},projectId:function(){return this.$store.state.app.projectId}},methods:{selectMenu:function(t){this.$router.push({path:t})},preview:function(){window.open("#/swagger?projectId="+this.projectId,"_blank")},publish:function(){var t=this;Object(i["Z"])({id:this.projectId},(function(e){t.$Message.success(e.body)}))},getProjectById:function(){var t=this;Object(i["J"])({id:this.projectId},(function(e){t.$store.dispatch("project",e.body)}))},getSystemDataModelList:function(){var t=this;Object(i["F"])({type:"system"},(function(e){t.$store.dispatch("systemDataModel",e.body)}))},getDataModelList:function(){var t=this;Object(i["D"])({projectId:this.projectId},(function(e){t.$store.dispatch("customDataModel",e.body)}))}},mounted:function(){this.$store.state.app.projectId||this.$store.dispatch("projectId",Object(o["a"])("projectId")),this.getProjectById(),this.getSystemDataModelList(),this.getDataModelList()}},r=c,u=(s("7e1a"),s("2877")),d=Object(u["a"])(r,a,n,!1,null,null,null);e["default"]=d.exports},"54dd":function(t,e,s){},"7e1a":function(t,e,s){"use strict";var a=s("54dd"),n=s.n(a);n.a}}]);