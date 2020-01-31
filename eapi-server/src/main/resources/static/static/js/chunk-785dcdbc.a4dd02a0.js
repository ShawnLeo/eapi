(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-785dcdbc"],{"2da9":function(t,e,r){"use strict";var o=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"group"},["1"===t.currUserRole||"2"===t.currUserRole?r("i-col",{attrs:{span:"24"}},[r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("项目组信息")])]),r("div",{staticClass:"wrapper-content box"},[r("Form",{attrs:{model:t.group,"label-width":100}},[r("FormItem",{attrs:{label:"项目组名称"}},[r("Input",{attrs:{type:"text"},model:{value:t.group.name,callback:function(e){t.$set(t.group,"name",e)},expression:"group.name"}})],1),r("FormItem",{attrs:{label:"项目组描述"}},[r("Input",{attrs:{type:"textarea",autosize:{minRows:2,maxRows:5}},model:{value:t.group.description,callback:function(e){t.$set(t.group,"description",e)},expression:"group.description"}})],1),r("FormItem",[r("Button",{staticStyle:{float:"right"},attrs:{type:"success"},on:{click:t.updateGroup}},[t._v("修改")])],1)],1)],1)]):t._e(),"1"!==t.currUserRole?r("i-col",{attrs:{span:"24"}},[r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("退出项目组")])]),r("Form",{attrs:{model:t.group,"label-width":100}},[r("FormItem",{attrs:{"label-width":-1}},[r("Button",{attrs:{type:"error",long:""},on:{click:t.quitGroup}},[t._v("退出此项目组")])],1)],1)],1):t._e(),"1"===t.currUserRole?r("i-col",{attrs:{span:"24"}},[r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("删除项目组")])]),r("div",{staticClass:"wrapper-content box danger-box"},[r("Form",{attrs:{model:t.group,"label-width":100}},[r("p",[t._v("此操作不可逆，将删除"),r("b",[t._v(t._s(t.group.name))]),t._v(", 及以下所有项目、用户，且"),r("b",[t._v("不可恢复")]),t._v("！")]),r("br"),r("p",[t._v("请输入项目组名称以进行确认:")]),r("br"),r("Input",{attrs:{placeholder:"项目组名称"},model:{value:t.title,callback:function(e){t.title=e},expression:"title"}}),r("br"),r("br"),r("FormItem",{attrs:{"label-width":-1}},[t.showDeleteButton?r("Button",{attrs:{type:"error",long:""},on:{click:t.handleRemove}},[t._v("我已了解风险，删除此项目组")]):r("Button",{attrs:{type:"error",long:"",disabled:""}},[t._v("我已了解风险，删除此项目组")])],1)],1)],1)]):t._e()],1)},n=[],i=(r("7f7f"),r("ae31")),a={props:{groupId:String,currUserRole:String,group:Object},data:function(){return{title:"",showDeleteButton:!1}},methods:{handleRemove:function(){var t=this;Object(i["N"])({id:this.groupId},(function(e){t.$Message.success("删除成功！"),window.location.reload()}))},updateGroup:function(){var t=this;Object(i["R"])(this.group,(function(){t.$Message.success("修改成功!"),t.$emit("update")}))},quitGroup:function(){var t=this;this.$Modal.confirm({title:"确认退出？",content:"<p>确认退出 <b>["+this.group.name+"]</b> 吗？</p>",onOk:function(){Object(i["V"])({groupId:t.groupId},(function(e){t.$Message.success("退出成功!"),t.$emit("quit")}))}})}},watch:{title:function(t,e){t===this.group.name?this.showDeleteButton=!0:this.showDeleteButton=!1}},mounted:function(){}},s=a,l=r("2877"),c=Object(l["a"])(s,o,n,!1,null,null,null);e["a"]=c.exports},3559:function(t,e,r){"use strict";var o=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"member"},["1"===t.currUserRole||"2"===t.currUserRole?r("i-col",{attrs:{span:"24"}},[r("div",{staticClass:"wrapper-content clearfix"},[r("h2",{staticClass:"title-border"},[t._v("添加成员")])]),r("div",{staticClass:"wrapper-content box"},[r("Form",{ref:"formItem",attrs:{model:t.formItem,"label-width":80,rules:t.ruleValidate}},[r("FormItem",{attrs:{label:"用户",prop:"users"}},[r("Select",{attrs:{multiple:"",filterable:"",remote:"","remote-method":t.remoteUser,loading:t.loading2,placeholder:"请输入用户名"},model:{value:t.formItem.users,callback:function(e){t.$set(t.formItem,"users",e)},expression:"formItem.users"}},t._l(t.options,(function(e,o){return r("Option",{key:o,attrs:{value:e.value}},[t._v(t._s(e.label))])})),1)],1),r("FormItem",{attrs:{label:"角色",prop:"role"}},[r("Select",{model:{value:t.formItem.role,callback:function(e){t.$set(t.formItem,"role",e)},expression:"formItem.role"}},[r("Option",{attrs:{value:"2"}},[t._v("管理员")]),r("Option",{attrs:{value:"3"}},[t._v("开发者")]),r("Option",{attrs:{value:"4"}},[t._v("观察者")])],1)],1),r("FormItem",[r("Button",{staticStyle:{float:"right"},attrs:{type:"success"},on:{click:t.addMember}},[t._v("添加成员")])],1)],1)],1)]):t._e(),r("i-col",{attrs:{span:"24"}},[r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("「"+t._s(t.group.name)+"」成员")])]),r("Table",{staticStyle:{"margin-bottom":"60px"},attrs:{columns:t.columns1,data:t.data1}})],1)],1)},n=[],i=(r("ac6a"),r("96cf"),r("3b8d")),a=(r("7f7f"),r("ae31")),s={props:{groupId:String,currUserRole:String,group:Object},data:function(){var t=this;return{formItem:{groupId:this.groupId,users:[],role:""},options:[],loading2:!1,columns1:[{renderHeader:function(t,e){return[t("Icon",{props:{type:"md-person"}}),t("span"," 用户名")]},key:"userName",render:function(e,r){var o=[e("span",r.row.userName+" ")];return r.row.userId===t.$store.state.app.user.id&&o.push(e("Tag",{props:{color:"success",type:"border"}},"你")),e("div",o)}},{title:"角色",key:"role",render:function(e,r){var o="",n="3"===t.currUserRole||"4"===t.currUserRole;switch(r.row.role){case"1":o="创建者",n=!0;break;case"2":o="管理员";break;case"3":o="开发者";break;case"4":o="观察者";break}return e("Dropdown",{props:{trigger:"click"},on:{"on-click":function(e){r.row.role!==e&&Object(a["W"])({groupUserId:r.row.id,role:e},(function(e){t.init(),t.$emit("change")}))}}},[e("Button",{props:{type:"info",disabled:n,size:"small",ghost:!0}},o),e("DropdownMenu",{slot:"list"},[e("DropdownItem",{props:{name:"2"}},"管理者"),e("DropdownItem",{props:{name:"3"}},"开发者"),e("DropdownItem",{props:{name:"4"}},"观察者")])])}},{title:"加入时间",key:"joinTime",render:function(t,e){return t("span",e.row.joinTime.substring(0,10))}},{title:"操作",width:120,render:function(e,r){return"1"===r.row.role?null:"1"===t.currUserRole?e("div",[e("Button",{props:{type:"primary",size:"small"},style:{marginRight:"5px",display:"1"===r.row.role?"none":""},on:{click:function(){t.$Modal.confirm({title:"确认转交？",content:"<p>确认将项目组转交给"+r.row.userName+" 么？</p>",onOk:function(){Object(a["Q"])({groupId:t.groupId,transferId:r.row.userId},(function(e){t.init(),t.$emit("change")}))}})}}},"转交"),e("Button",{props:{type:"error",size:"small"},style:{marginRight:"5px"},on:{click:function(){t.$Modal.confirm({title:"确认删除？",content:"<p>确认将用户"+r.row.userName+" 从项目组中删除么？</p>",onOk:function(){Object(a["V"])({groupId:t.groupId,userId:r.row.userId},(function(e){t.init()}))},onCancel:function(){t.$Message.success("取消删除！")}})}}},"删除")]):t.$store.state.app.user.id===r.row.userId?e("div",[e("Button",{props:{type:"primary",size:"small"},style:{marginRight:"5px",display:"1"===r.row.role?"none":""},on:{click:function(){t.$Modal.confirm({title:"确认退出？",content:"<p>确认退出 <b>["+t.group.name+"]</b> 吗？</p>",onOk:function(){Object(a["V"])({groupId:t.groupId},(function(e){t.$emit("quit")}))}})}}},"退出")]):null}}],data1:[],ruleValidate:{users:[{required:!0,type:"array",message:"请选择用户",trigger:"blur"}],role:[{required:!0,message:"请选择角色",trigger:"blur"}]}}},methods:{init:function(){var t=this;Object(a["T"])({groupId:this.groupId},(function(e){t.data1=e.body}))},remoteUser:function(){var t=Object(i["a"])(regeneratorRuntime.mark((function t(e){var r=this;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(this.options=[],e){t.next=3;break}return t.abrupt("return");case 3:return this.loading2=!0,t.next=6,Object(a["U"])({username:e,groupId:this.groupId},(function(t){r.loading2=!1,t.body.forEach((function(t){r.options.push({value:t.userName,label:t.userName})}))}));case 6:case"end":return t.stop()}}),t,this)})));function e(e){return t.apply(this,arguments)}return e}(),addMember:function(){var t=this,e=this;this.$refs["formItem"].validate(function(){var r=Object(i["a"])(regeneratorRuntime.mark((function r(o){return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:o&&Object(a["S"])(t.formItem,(function(t){e.init(),e.$Message.success("添加成功！"),e.formItem.users=[],e.formItem.role=""}));case 1:case"end":return r.stop()}}),r)})));return function(t){return r.apply(this,arguments)}}())}},watch:{groupId:{handler:function(t,e){this.formItem.groupId=t,this.init()}}},mounted:function(){this.init()}},l=s,c=r("2877"),u=Object(c["a"])(l,o,n,!1,null,null,null);e["a"]=u.exports},"4a60":function(t,e,r){"use strict";var o=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"permission"},[t._m(0),r("div",{staticClass:"wrapper-content box"},[r("Form",{attrs:{model:t.group,"label-width":140}},[r("FormItem",{staticStyle:{float:"left"},attrs:{label:"用户申请验证方式："}},[r("RadioGroup",{on:{chang:function(e){t.group.verifyRole=null}},model:{value:t.group.verify,callback:function(e){t.$set(t.group,"verify",e)},expression:"group.verify"}},[r("Radio",{attrs:{label:"1"}},[t._v("需管理员验证")]),r("Radio",{attrs:{label:"2"}},[t._v("自动通过为")])],1)],1),"2"===t.group.verify?r("FormItem",{staticStyle:{width:"500px",float:"right"},attrs:{label:"","label-width":-1}},[r("Select",{model:{value:t.group.verifyRole,callback:function(e){t.$set(t.group,"verifyRole",e)},expression:"group.verifyRole"}},[r("Option",{attrs:{value:"2"}},[t._v("管理员")]),r("Option",{attrs:{value:"3"}},[t._v("开发者")]),r("Option",{attrs:{value:"4"}},[t._v("观察者")])],1)],1):t._e(),r("div",{staticClass:"clearfix"}),r("FormItem",[r("Button",{staticStyle:{float:"right"},attrs:{type:"success"},on:{click:t.updatePremission}},[t._v("修改")])],1)],1)],1),t._m(1),r("Table",{staticStyle:{"margin-bottom":"60px"},attrs:{columns:t.columns2,data:t.data2}})],1)},n=[function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("权限控制")])])},function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"wrapper-content"},[r("h2",{staticClass:"title-border"},[t._v("申请列表")])])}],i=(r("7f7f"),r("ae31")),a={props:{group:Object},data:function(){return{columns2:[{renderHeader:function(t,e){return[t("Icon",{props:{type:"md-person"}}),t("span"," 用户名")]},key:"name",render:function(t,e){return t("div",[t("span",e.row.name+" ")])}},{title:"申请时间",key:"date"},{title:"申请理由",key:"reason"},{title:"操作",width:120,render:function(t,e){return t("div",[t("Button",{props:{type:"primary",size:"small"},style:{marginRight:"5px",display:1===e.row.roleType?"none":""},on:{click:function(){}}},"通过"),t("Button",{props:{type:"error",size:"small"},style:{marginRight:"5px"},on:{click:function(){}}},"拒绝")])}}],data2:[{name:"464328772@qq.com",date:"2016-10-03",reason:"项目开发"},{name:"17611221187",date:"2016-10-03",reason:"看看"}],formItem:{title:"",description:""}}},methods:{updatePremission:function(){var t=this;Object(i["R"])(this.group,(function(){t.$Message.success("修改成功!"),t.$emit("update")}))}}},s=a,l=r("2877"),c=Object(l["a"])(s,o,n,!1,null,null,null);e["a"]=c.exports},"7f7f":function(t,e,r){var o=r("86cc").f,n=Function.prototype,i=/^\s*function ([^ (]*)/,a="name";a in n||r("9e1e")&&o(n,a,{configurable:!0,get:function(){try{return(""+this).match(i)[1]}catch(t){return""}}})},ac6a:function(t,e,r){for(var o=r("cadf"),n=r("0d58"),i=r("2aba"),a=r("7726"),s=r("32e9"),l=r("84f2"),c=r("2b4c"),u=c("iterator"),p=c("toStringTag"),d=l.Array,m={CSSRuleList:!0,CSSStyleDeclaration:!1,CSSValueList:!1,ClientRectList:!1,DOMRectList:!1,DOMStringList:!1,DOMTokenList:!0,DataTransferItemList:!1,FileList:!1,HTMLAllCollection:!1,HTMLCollection:!1,HTMLFormElement:!1,HTMLSelectElement:!1,MediaList:!0,MimeTypeArray:!1,NamedNodeMap:!1,NodeList:!0,PaintRequestList:!1,Plugin:!1,PluginArray:!1,SVGLengthList:!1,SVGNumberList:!1,SVGPathSegList:!1,SVGPointList:!1,SVGStringList:!1,SVGTransformList:!1,SourceBufferList:!1,StyleSheetList:!0,TextTrackCueList:!1,TextTrackList:!1,TouchList:!1},f=n(m),g=0;g<f.length;g++){var h,b=f[g],v=m[b],I=a[b],w=I&&I.prototype;if(w&&(w[u]||s(w,u,d),w[p]||s(w,p,b),l[b]=d,v))for(h in o)w[h]||i(w,h,o[h],!0)}},bbcc:function(t,e,r){"use strict";r.r(e);var o=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{staticClass:"project-list main-content"},[r("Card",{},[r("Tabs",{model:{value:t.tabSelect,callback:function(e){t.tabSelect=e},expression:"tabSelect"}},[r("TabPane",{attrs:{label:"项目管理",name:"pm"}},[t.loading?r("Spin",{attrs:{size:"large",fix:""}}):t._e(),r("div",{staticClass:"project-box"},[t._l(t.projects,(function(e,o){return r("Card",{key:o,staticClass:"project-item"},[r("div",{staticStyle:{"text-align":"center"},on:{click:function(r){return t.goInterface(e.id)}}},[r("div",{staticClass:"avatar"},[r("Avatar",{staticClass:"project-avatar",attrs:{size:"large"}},[t._v(t._s(e.title.substring(0,1)))])],1),r("br"),r("h3",[t._v(t._s(e.title))])])])})),r("Card",{staticStyle:{width:"220px",margin:"15px","text-align":"center",cursor:"pointer"}},[r("div",{staticStyle:{"text-align":"center","line-height":"10"},on:{click:t.newProject}},[r("Icon",{attrs:{type:"md-add",size:"100"}})],1)])],2),0===t.projects.length&&"all"===t.groupId?r("div",{staticClass:"no-project"},[t._v("\n          暂无可用项目\n        ")]):t._e()],1)],1)],1),r("Modal",{attrs:{title:"新建项目",width:"700"},model:{value:t.addProjectModal,callback:function(e){t.addProjectModal=e},expression:"addProjectModal"}},[r("Form",{ref:"projectFormItem",attrs:{model:t.formItem,"label-width":120,rules:t.ruleValidate}},[r("FormItem",{attrs:{label:"项目名称",prop:"title"}},[r("i-input",{attrs:{placeholder:"项目名称"},model:{value:t.formItem.title,callback:function(e){t.$set(t.formItem,"title",e)},expression:"formItem.title"}})],1),r("FormItem",{attrs:{label:"项目描述",prop:"description"}},[r("i-input",{attrs:{type:"textarea",autosize:{minRows:2,maxRows:5},placeholder:"最多20个中文或者40个英文字符"},model:{value:t.formItem.description,callback:function(e){t.$set(t.formItem,"description",e)},expression:"formItem.description"}})],1)],1),r("div",{attrs:{slot:"footer"},slot:"footer"},[r("Button",{attrs:{type:"text",size:"large"},on:{click:t.reset}},[t._v("重置")]),r("Button",{attrs:{type:"primary",size:"large"},on:{click:t.addProject}},[t._v("确定")])],1)],1),r("Modal",{attrs:{title:"新建项目组",width:"700"},model:{value:t.addGroupModal,callback:function(e){t.addGroupModal=e},expression:"addGroupModal"}},[r("Form",{ref:"groupFormItem",attrs:{model:t.group,"label-width":120,rules:t.groupRuleValidate}},[r("FormItem",{attrs:{label:"项目组名称",prop:"name"}},[r("i-input",{attrs:{placeholder:"项目名称"},model:{value:t.group.name,callback:function(e){t.$set(t.group,"name",e)},expression:"group.name"}})],1),r("FormItem",{attrs:{label:"项目组描述",prop:"description"}},[r("i-input",{attrs:{type:"textarea",autosize:{minRows:2,maxRows:5},placeholder:"最多20个中文或者40个英文字符"},model:{value:t.group.description,callback:function(e){t.$set(t.group,"description",e)},expression:"group.description"}})],1)],1),r("div",{attrs:{slot:"footer"},slot:"footer"},[r("Button",{attrs:{type:"text",size:"large"},on:{click:function(e){t.addGroupModal=!1}}},[t._v("取消")]),r("Button",{attrs:{type:"primary",size:"large"},on:{click:t.addGroup}},[t._v("确定")])],1)],1)],1)},n=[],i=(r("96cf"),r("3b8d")),a=r("ae31"),s=r("2da9"),l=r("3559"),c=r("4a60"),u={components:{group:s["a"],member:l["a"],permission:c["a"]},data:function(){var t=this,e=function(e,r,o){Object(a["d"])(t.formItem,(function(t){"0"===t.header.code?t.body?o(new Error("项目已存在！")):o():o(new Error(t.header.message))}))};return{activeName:"all",loading:!0,addProjectModal:!1,addGroupModal:!1,projects:[],groups:[],groupId:this.$route.query.groupId?this.$route.query.groupId:"all",currUserRole:"",group:{name:"",description:""},selectGroup:{},formItem:{title:"",description:""},ruleValidate:{title:[{required:!0,message:"请输入项目名称",trigger:"blur"},{validator:e,trigger:"blur"}],description:[{required:!0,message:"请输入描述",trigger:"blur"}]},tabSelect:"pm",groupRuleValidate:{name:[{required:!0,message:"请输入项目组名称",trigger:"blur"}],description:[{required:!0,message:"请输入描述",trigger:"blur"}]}}},methods:{init:function(){this.getProjectList(),this.getGroupList()},getProjectList:function(){var t=this;this.loading=!0,Object(a["K"])({groupId:this.groupId},(function(e){t.projects=e.body,t.loading=!1}))},getCurrUserRole:function(t){var e=this;Object(a["C"])({groupId:t},(function(t){e.currUserRole=t.body}))},getGroupList:function(){var t=Object(i["a"])(regeneratorRuntime.mark((function t(){var e=this;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return this.loading=!0,t.next=3,Object(a["P"])((function(t){e.groups=t.body,e.$nextTick((function(){e.$refs.groupMenus.updateActiveName()})),e.loading=!1}));case 3:case"end":return t.stop()}}),t,this)})));function e(){return t.apply(this,arguments)}return e}(),goInterface:function(t){this.$store.dispatch("projectId",t),this.$router.push({path:"/project/interface"})},reset:function(){this.formItem={title:"",description:""},this.group={name:"",description:""}},addProject:function(){var t=this;this.formItem.groupId=this.groupId,this.$refs["projectFormItem"].validate(function(){var e=Object(i["a"])(regeneratorRuntime.mark((function e(r){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(!r){e.next=4;break}return e.next=3,Object(a["i"])(t.formItem,(function(e){t.getProjectList(),t.reset()}));case 3:t.addProjectModal=!1;case 4:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}())},newProject:function(){this.addProjectModal=!0},addGroup:function(){var t=this;this.$refs["groupFormItem"].validate(function(){var e=Object(i["a"])(regeneratorRuntime.mark((function e(r){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(!r){e.next=4;break}return e.next=3,Object(a["M"])(t.group,(function(e){t.init(),t.reset()}));case 3:t.addGroupModal=!1;case 4:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}())},selectMenu:function(t){this.currUserRole="",this.tabSelect="pm",this.groupId=t,this.getProjectList(),"all"!==t&&this.getCurrUserRole(t)},quitGroup:function(){window.location.reload()}},watch:{groupId:{handler:function(t,e){var r=this;"all"!==t&&Object(a["O"])({id:this.groupId},(function(t){r.selectGroup=t.body}))}}},mounted:function(){this.init()}},p=u,d=(r("c7af"),r("2877")),m=Object(d["a"])(p,o,n,!1,null,null,null);e["default"]=m.exports},c7af:function(t,e,r){"use strict";var o=r("e196"),n=r.n(o);n.a},e196:function(t,e,r){}}]);