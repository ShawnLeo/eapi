(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3d920657"],{"15af":function(e,t,a){"use strict";a.d(t,"f",(function(){return r})),a.d(t,"g",(function(){return n})),a.d(t,"c",(function(){return o})),a.d(t,"b",(function(){return i})),a.d(t,"a",(function(){return s})),a.d(t,"e",(function(){return c})),a.d(t,"d",(function(){return l}));const r=e=>{let t=e.split("/{"),a=[];return t.forEach((e,r)=>{if(t[r+1]){let e=t[r+1].split("}");e.length>1&&a.push(e[0])}}),a},n=e=>{e.forEach(e=>{("object"===e.dataType||"array"===e.dataType||e.dataModel&&("object"===e.dataModel.dataType||"array"===e.dataModel.dataType))&&(e._expanded=!0),("array"===e.dataType||e.dataModel&&"array"===e.dataModel.dataType)&&(e.isArrayItem=!0),e.children&&e.children.length>0&&n(e.children),e.dataModel&&e.dataModel.children&&e.dataModel.children.length>0&&n(e.dataModel.children)})},o=(e,t)=>{for(let a in e){let r=e[a];if(!e[a]||"string"===typeof r&&r.constructor===String)t.push({name:a,description:"",dataType:"string",example:r,children:[],required:!1,_expanded:!1});else if("number"==typeof r&&r.constructor==Number)t.push({name:a,description:"",dataType:"integer",example:r,children:[],required:!1,_expanded:!1});else if("object"==typeof r&&r.constructor==Object){let e={name:a,description:"",dataType:"object",example:"",children:[],required:!1,_expanded:!1};o(r,e.children),t.push(e)}else if("object"==typeof r&&r.constructor==Array){let e={name:a,description:"",dataType:"array",example:"",children:[],required:!1,_expanded:!1};if(r.length>0){let t=r.slice(0,1);o(t,e.children)}t.push(e)}}},i=(e,t)=>{e.forEach(e=>{let a={name:e.name,description:e.description,dataType:"string",example:e.example,children:[],required:!1,_expanded:!1};t.push(a)})},s=e=>{return e.replace(/\_(\w)/g,(function(e,t){return t.toUpperCase()}))},c=e=>{return e.replace(e[0],e[0].toUpperCase())},l=(e,t)=>{if(!e)return;let a=window.URL.createObjectURL(new Blob([e])),r=document.createElement("a");r.style.display="none",r.href=a,r.setAttribute("download",t),document.body.appendChild(r),r.click()}},"3a88":function(e,t,a){},"7f4c":function(e,t,a){"use strict";var r=a("3a88"),n=a.n(r);n.a},"7f7f":function(e,t,a){var r=a("86cc").f,n=Function.prototype,o=/^\s*function ([^ (]*)/,i="name";i in n||a("9e1e")&&r(n,i,{configurable:!0,get:function(){try{return(""+this).match(o)[1]}catch(e){return""}}})},ac6a:function(e,t,a){for(var r=a("cadf"),n=a("0d58"),o=a("2aba"),i=a("7726"),s=a("32e9"),c=a("84f2"),l=a("2b4c"),d=l("iterator"),p=l("toStringTag"),u=c.Array,f={CSSRuleList:!0,CSSStyleDeclaration:!1,CSSValueList:!1,ClientRectList:!1,DOMRectList:!1,DOMStringList:!1,DOMTokenList:!0,DataTransferItemList:!1,FileList:!1,HTMLAllCollection:!1,HTMLCollection:!1,HTMLFormElement:!1,HTMLSelectElement:!1,MediaList:!0,MimeTypeArray:!1,NamedNodeMap:!1,NodeList:!0,PaintRequestList:!1,Plugin:!1,PluginArray:!1,SVGLengthList:!1,SVGNumberList:!1,SVGPathSegList:!1,SVGPointList:!1,SVGStringList:!1,SVGTransformList:!1,SourceBufferList:!1,StyleSheetList:!0,TextTrackCueList:!1,TextTrackList:!1,TouchList:!1},m=n(f),h=0;h<m.length;h++){var I,g=m[h],y=f[g],b=i[g],v=b&&b.prototype;if(v&&(v[d]||s(v,d,u),v[p]||s(v,p,g),c[g]=u,y))for(I in r)v[I]||o(v,I,r[I],!0)}},eee0:function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"project-interface"},[a("Form",{ref:"formInline",staticClass:"project-interface-form",attrs:{inline:""}},[a("FormItem",[a("Button",{attrs:{type:"primary",icon:"md-add"},on:{click:e.newInterface}},[e._v("新建接口")])],1),a("FormItem",{attrs:{prop:"search"}},[a("Input",{attrs:{type:"text",placeholder:"名称搜索"},on:{"on-keyup":e.searchData},model:{value:e.searchModel,callback:function(t){e.searchModel=t},expression:"searchModel"}})],1)],1),a("div",{staticClass:"clearfix"}),a("Form",{directives:[{name:"show",rawName:"v-show",value:e.showEditMenus,expression:"showEditMenus"}],ref:"formInline",staticClass:"project-interface-form form-in-table",attrs:{inline:""}},[a("Button",{attrs:{size:"small"},on:{click:e.deleteInterface}},[e._v("删除")]),a("Button",{attrs:{size:"small"},on:{click:function(t){e.editStatus=!0}}},[e._v("设置状态")]),e.singleSelect?a("Button",{attrs:{size:"small"},on:{click:e.showCopyModal}},[e._v("复制")]):e._e(),a("Button",{attrs:{size:"small"},on:{click:e.selectInterfaceDoc}},[e._v("接口文档")]),a("Button",{attrs:{size:"small"},on:{click:e.exportSelect}},[e._v("导出选中")])],1),a("Table",{ref:"selection",attrs:{stripe:"",columns:e.columns,loading:e.loading,data:e.filterInterfaces},on:{"on-selection-change":e.onSelectionChange}}),a("Modal",{attrs:{title:"新建接口",width:"700","mask-closable":!1},model:{value:e.addInterfaceModal,callback:function(t){e.addInterfaceModal=t},expression:"addInterfaceModal"}},[a("Form",{ref:"interfaceItem",attrs:{model:e.interfaceItem,"label-width":80,rules:e.ruleValidate}},[a("FormItem",{attrs:{label:"接口名称",prop:"name"}},[a("i-input",{attrs:{placeholder:"最多20个中文或者40个英文字符"},model:{value:e.interfaceItem.name,callback:function(t){e.$set(e.interfaceItem,"name",t)},expression:"interfaceItem.name"}})],1),a("FormItem",{staticStyle:{width:"30%",float:"left"},attrs:{label:"接口路径",prop:"method"}},[a("Select",{attrs:{require:"true"},model:{value:e.interfaceItem.method,callback:function(t){e.$set(e.interfaceItem,"method",t)},expression:"interfaceItem.method"}},[a("Option",{attrs:{value:"get"}},[e._v("GET")]),a("Option",{attrs:{value:"post"}},[e._v("POST")]),a("Option",{attrs:{value:"put"}},[e._v("PUT")]),a("Option",{attrs:{value:"delete"}},[e._v("DELETE")])],1)],1),a("FormItem",{staticStyle:{width:"70%",float:"left"},attrs:{prop:"path","label-width":-1}},[a("i-input",{attrs:{placeholder:"例如：/api/get/{id}"},model:{value:e.interfaceItem.path,callback:function(t){e.$set(e.interfaceItem,"path",t)},expression:"interfaceItem.path"}})],1),a("div",{staticClass:"clearfix"}),a("FormItem",{staticStyle:{width:"50%",float:"left"},attrs:{label:"标签",prop:"tagIds"}},[a("Select",{attrs:{multiple:"",require:"true"},model:{value:e.interfaceItem.tagIds,callback:function(t){e.$set(e.interfaceItem,"tagIds",t)},expression:"interfaceItem.tagIds"}},e._l(e.tags,(function(t,r){return a("Option",{key:r,attrs:{value:t.id}},[e._v(e._s(t.name))])})),1)],1),a("FormItem",{staticStyle:{width:"50%",float:"left"},attrs:{label:"操作Id(方法名)",prop:"operationId","label-width":120}},[a("i-input",{attrs:{placeholder:"例如：findByUserId"},model:{value:e.interfaceItem.operationId,callback:function(t){e.$set(e.interfaceItem,"operationId",t)},expression:"interfaceItem.operationId"}})],1),a("div",{staticClass:"clearfix"}),a("FormItem",{attrs:{label:"描述",prop:"description"}},[a("i-input",{attrs:{type:"textarea",autosize:{minRows:2,maxRows:5},placeholder:"描述"},model:{value:e.interfaceItem.description,callback:function(t){e.$set(e.interfaceItem,"description",t)},expression:"interfaceItem.description"}})],1)],1),a("div",{attrs:{slot:"footer"},slot:"footer"},[a("Button",{attrs:{type:"text",size:"large"},on:{click:e.reset}},[e._v("重置")]),a("Button",{attrs:{type:"primary",size:"large"},on:{click:e.addInterface}},[e._v("确定")])],1)],1),a("Modal",{attrs:{title:"设置状态"},on:{"on-ok":e.okEditStatus},model:{value:e.editStatus,callback:function(t){e.editStatus=t},expression:"editStatus"}},[a("Form",{staticClass:"project-interface-form form-in-table",attrs:{"label-width":100}},[a("FormItem",{staticStyle:{width:"80%",float:"left"},attrs:{label:"设置状态"}},[a("Select",{model:{value:e.status,callback:function(t){e.status=t},expression:"status"}},[a("Option",{attrs:{value:"100"}},[e._v("未开始")]),a("Option",{attrs:{value:"200"}},[e._v("开发中")]),a("Option",{attrs:{value:"300"}},[e._v("测试中")]),a("Option",{attrs:{value:"400"}},[e._v("已完成")]),a("Option",{attrs:{value:"500"}},[e._v("已废弃")])],1)],1)],1),a("div",{staticClass:"clearfix"})],1),a("Modal",{attrs:{title:"复制接口",width:"700","mask-closable":!1},model:{value:e.copyStatus,callback:function(t){e.copyStatus=t},expression:"copyStatus"}},[a("Form",{ref:"copyInterfaceItem",attrs:{model:e.copyInterfaceItem,"label-width":80,rules:e.copyRuleValidate}},[a("FormItem",{attrs:{label:"接口名称",prop:"name"}},[a("i-input",{attrs:{placeholder:"最多20个中文或者40个英文字符"},model:{value:e.copyInterfaceItem.name,callback:function(t){e.$set(e.copyInterfaceItem,"name",t)},expression:"copyInterfaceItem.name"}})],1),a("FormItem",{staticStyle:{width:"30%",float:"left"},attrs:{label:"接口路径",prop:"method"}},[a("Select",{attrs:{require:"true"},model:{value:e.copyInterfaceItem.method,callback:function(t){e.$set(e.copyInterfaceItem,"method",t)},expression:"copyInterfaceItem.method"}},[a("Option",{attrs:{value:"get"}},[e._v("GET")]),a("Option",{attrs:{value:"post"}},[e._v("POST")]),a("Option",{attrs:{value:"put"}},[e._v("PUT")]),a("Option",{attrs:{value:"delete"}},[e._v("DELETE")])],1)],1),a("FormItem",{staticStyle:{width:"70%",float:"left"},attrs:{prop:"path","label-width":-1}},[a("i-input",{attrs:{placeholder:"例如：/api/get/{id}"},model:{value:e.copyInterfaceItem.path,callback:function(t){e.$set(e.copyInterfaceItem,"path",t)},expression:"copyInterfaceItem.path"}})],1),a("div",{staticClass:"clearfix"}),a("FormItem",{staticStyle:{width:"50%",float:"left"},attrs:{label:"标签",prop:"tagIds"}},[a("Select",{attrs:{multiple:"",require:"true"},model:{value:e.copyInterfaceItem.tagIds,callback:function(t){e.$set(e.copyInterfaceItem,"tagIds",t)},expression:"copyInterfaceItem.tagIds"}},e._l(e.tags,(function(t,r){return a("Option",{key:r,attrs:{value:t.id}},[e._v(e._s(t.name))])})),1)],1),a("FormItem",{staticStyle:{width:"50%",float:"left"},attrs:{label:"操作Id(方法名)",prop:"operationId","label-width":120}},[a("i-input",{attrs:{placeholder:"例如：findByUserId"},model:{value:e.copyInterfaceItem.operationId,callback:function(t){e.$set(e.copyInterfaceItem,"operationId",t)},expression:"copyInterfaceItem.operationId"}})],1),a("div",{staticClass:"clearfix"}),a("FormItem",{attrs:{label:"描述",prop:"description"}},[a("i-input",{attrs:{type:"textarea",autosize:{minRows:2,maxRows:5},placeholder:"描述"},model:{value:e.copyInterfaceItem.description,callback:function(t){e.$set(e.copyInterfaceItem,"description",t)},expression:"copyInterfaceItem.description"}})],1)],1),a("div",{staticClass:"clearfix"}),a("div",{attrs:{slot:"footer"},slot:"footer"},[a("Button",{attrs:{type:"text",size:"large"},on:{click:function(t){e.copyStatus=!1}}},[e._v("取消")]),a("Button",{attrs:{type:"primary",size:"large"},on:{click:e.okCopyInteface}},[e._v("确定")])],1)],1)],1)},n=[],o=(a("ac6a"),a("7f7f"),a("ae31")),i=a("5d2d"),s=a("15af"),c={data:function(){var e=this,t=function(t,a,r){Object(o["c"])(e.interfaceItem,(function(e){"0"===e.header.code?e.body?r(new Error("接口已存在")):r():r(new Error(e.header.message))}))},a=function(t,a,r){var n={method:e.copyInterfaceItem.method,path:e.copyInterfaceItem.path,projectId:e.copyInterfaceItem.projectId};Object(o["c"])(n,(function(e){"0"===e.header.code?e.body?r(new Error("接口已存在")):r():r(new Error(e.header.message))}))};return{projectId:"",addInterfaceModal:!1,interfaceItem:{name:"",method:"get",path:"",tagIds:[],status:100,operationId:"",requestType:"",description:"",deprecated:!1,projectId:""},copyInterfaceItem:{},editStatus:!1,copyStatus:!1,status:"",loading:!1,showEditMenus:!1,singleSelect:!1,columns:[{type:"selection",width:50,align:"center"},{title:"名称",key:"name",render:function(t,a){return 500===a.row.status?t("span",{style:{"text-decoration":"line-through"}},a.row.name):t("a",{on:{click:function(){e.$router.push({path:"/project/interface/edit",query:{id:a.row.id}})}}},a.row.name)}},{title:"方法",width:80,sortable:!0,render:function(e,t){var a="green";switch(t.row.method){case"get":a="blue";break;case"put":a="yellow";break;case"post":a="green";break;case"delete":a="red";break}return 500===t.row.status&&(a="default"),e("Tag",{props:{color:a}},t.row.method)},key:"method"},{title:"路径",key:"path",sortable:!0,render:function(t,a){return 500===a.row.status?t("span",{style:{"text-decoration":"line-through"}},a.row.path):t("a",{on:{click:function(){e.$router.push({path:"/project/interface/edit",query:{id:a.row.id}})}}},a.row.path)}},{title:"标签",key:"tag",sortable:!0,render:function(e,t){var a=[];return t.row.tags.forEach((function(t){a.push(e("Tag",t.name))})),a}},{title:"状态",key:"status",sortable:!0,width:90,render:function(t,a){var r="primary",n="未开始",o=!1;switch(a.row.status){case 100:r="primary",n="未开始";break;case 200:r="warning",n="开发中";break;case 300:r="error",n="测试中";break;case 400:r="success",n="已完成";break;case 500:r="info",n="已废弃",o=!0;break}return t("Dropdown",{props:{trigger:"click"},on:{"on-click":function(t){a.row.status!==t&&e.changStatus(a.row.id,t)}}},[t("Button",{class:{isdeprecated:o},props:{type:r,size:"small",ghost:!0}},n),t("DropdownMenu",{slot:"list"},[t("DropdownItem",{props:{name:100}},"未开始"),t("DropdownItem",{props:{name:200}},"开发中"),t("DropdownItem",{props:{name:300}},"测试中"),t("DropdownItem",{props:{name:400}},"已完成"),t("DropdownItem",{props:{name:500}},"已废弃")])])}},{title:"创建人",key:"createrUserName"},{title:"创建时间",width:110,sortable:!0,render:function(e,t){return e("span",t.row.createTime.substring(0,10))}}],interfaces:[],filterInterfaces:[],searchModel:"",selection:[],tags:[],ruleValidate:{name:[{required:!0,message:"请输入接口名称",trigger:"blur"}],path:[{required:!0,message:"请输入接口路径",trigger:"blur"},{validator:t,trigger:"blur"}],method:[{required:!0,message:"请输入接口路径",trigger:"blur"},{validator:t,trigger:"change"}]},copyRuleValidate:{name:[{required:!0,message:"请输入接口名称",trigger:"blur"}],path:[{required:!0,message:"请输入接口路径",trigger:"blur"},{validator:a,trigger:"blur"}],method:[{required:!0,message:"请输入接口路径",trigger:"blur"},{validator:a,trigger:"change"}]}}},computed:{state:function(){return this.$store.state.app}},methods:{init:function(){this.projectId=this.state.projectId||Object(i["a"])("projectId"),this.getInterfaceList(),this.getTagList()},getInterfaceList:function(){var e=this;this.loading=!0;var t=this.projectId;this.interfaceItem.projectId=t,Object(o["H"])({projectId:t},(function(t){"0"===t.header.code?(e.interfaces=t.body,e.filterInterfaces=t.body):e.$Message.error(t.header.message)})),this.loading=!1},getTagList:function(){var e=this;Object(o["L"])({projectId:this.projectId},(function(t){"0"===t.header.code?e.tags=t.body:e.$Message.error(t.header.message)}))},reset:function(){this.interfaceItem={name:"",method:"get",path:"",tagIds:[],status:100,operationId:"",description:"",deprecated:!1,projectId:this.projectId}},addInterface:function(){var e=this;this.$refs["interfaceItem"].validate((function(t){t&&("get"!==e.interfaceItem.method.toLowerCase()&&(e.interfaceItem.requestType="body"),Object(o["h"])(e.interfaceItem,(function(t){"0"===t.header.code?(e.init(),e.reset()):e.$Message.error(t.header.message)})),e.addInterfaceModal=!1)}))},okCopyInteface:function(){var e=this;this.$refs["copyInterfaceItem"].validate((function(t){t&&(Object(o["f"])(e.copyInterfaceItem,(function(t){e.init()})),e.copyStatus=!1)}))},onSelectionChange:function(e){this.showEditMenus=0!==e.length,this.singleSelect=1===e.length,this.selection=e},deleteInterface:function(){var e=this;this.$Modal.confirm({title:"确认删除？",content:"<p>删除数据不可恢复！</p><p>确认要删除吗？</p>",onOk:function(){Object(o["l"])(e.selection,(function(t){"0"===t.header.code?(e.$Message.success("删除成功！"),e.showEditMenus=!1,e.init()):e.$Message.error(t.header.message)}))},onCancel:function(){e.$Message.success("取消删除！")}})},exportSelect:function(){var e=[];this.selection.forEach((function(t){return e.push(t.id)})),Object(o["u"])({projectId:this.projectId,interfaceIds:e},(function(e){Object(s["d"])(e,"swagger.json")}))},selectInterfaceDoc:function(){var e=[];this.selection.forEach((function(t){return e.push(t.id)})),this.$router.push({name:"swaggerUI",query:{projectId:this.projectId,interfaceIds:e}})},newInterface:function(){this.addInterfaceModal=!0},changStatus:function(e,t){var a=this;Object(o["a"])([{id:e,status:t}],(function(e){a.getInterfaceList()}))},okEditStatus:function(){var e=this;this.selection.forEach((function(t){t.status=e.status})),Object(o["a"])(this.selection,(function(t){"0"===t.header.code?(e.$Message.success("设置成功！"),e.showEditMenus=!1,e.init()):e.$Message.error(t.header.message)}))},searchData:function(){var e=this;this.filterInterfaces=this.interfaces.filter((function(t){return t.name.toLowerCase().indexOf(e.searchModel.toLowerCase())>-1}))},showCopyModal:function(){this.copyStatus=!0,this.copyInterfaceItem=this.selection[0];var e=[];this.copyInterfaceItem.tags.forEach((function(t){e.push(t.id)})),this.copyInterfaceItem.tagIds=e}},mounted:function(){this.init()}},l=c,d=(a("7f4c"),a("2877")),p=Object(d["a"])(l,r,n,!1,null,null,null);t["default"]=p.exports}}]);