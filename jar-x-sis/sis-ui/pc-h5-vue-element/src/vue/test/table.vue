<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item>控制面板</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/table' }">data</el-breadcrumb-item>
      <el-breadcrumb-item >Table表格</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="element__body">
      <el-row :gutter="20">
        <el-col :span="2" class="form-label">产线：</el-col>
        <el-col :span="4">
          <el-select v-model="table_productLine" clearable placeholder="请选择">
            <el-option v-for="item in productLine" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-col>

        <el-col :span="2" class="form-label">应用：</el-col>
        <el-col :span="4">
          <el-select v-model="table_application" clearable  placeholder="请选择">
            <el-option v-for="item in application" :key="item.key" :label="item.value" :value="item.key"></el-option>
          </el-select>
        </el-col>
        <el-col :span="2"><el-button type="primary" @click="query">查询</el-button></el-col>
        <el-col :span="2"><el-button type="primary" @click="add">新增</el-button></el-col>
      </el-row>
      <el-table :data="tableData3" border style="width: 98%">
        <el-table-column type="index" label="#" ></el-table-column>
        <el-table-column label="配置">
          <el-table-column label="日期" prop="date"></el-table-column>
          <el-table-column label="姓名" prop="name"></el-table-column>
          <el-table-column label="地址" prop="address"></el-table-column>
        </el-table-column>
        <el-table-column prop="date" label="日期" ></el-table-column>
        <el-table-column prop="name" label="姓名" ></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column fixed="right" label="操作" width="180">
            <template slot-scope="scope">
              <el-button type="primary" @click="update(scope.row)">编辑</el-button>
              <el-button type="primary" @click="del(scope.row)">删除</el-button>
            </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChange" 
        @current-change="currentChange"
        :current-page="pager.pageNo"
        :page-sizes="[10, 20, 50]"
        :page-size="pager.pageSize"
        layout="sizes, prev, pager, next ,total,  jumper"
        :total="pager.pageTotal">
      </el-pagination>
    </div>

    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-row :gutter="20">
        <el-col :span="3" class="form-label">负责人：</el-col>
        <el-col :span="21">
          <el-input v-model="dataListItem.owner" placeholder="负责人"></el-input>
        </el-col>
      </el-row> 
      <el-row :gutter="20"> 
        <el-col :span="3" class="form-label">应用编码：</el-col>
        <el-col :span="21">
          <el-input v-model="dataListItem.applicationCode" placeholder="应用编码"></el-input>
        </el-col>
      </el-row> 
      <el-row :gutter="20">
        <el-col :span="3" class="form-label">应用名称：</el-col>
        <el-col :span="21">
          <el-input v-model="dataListItem.applicationName" placeholder="应用名称"></el-input>
        </el-col>
      </el-row> 
      <el-row :gutter="20">
        <el-col :span="3" class="form-label">描述：</el-col>
        <el-col :span="21">
          <el-input v-model="dataListItem.description" type="textarea" :rows="4" placeholder="描述"></el-input>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="!dataListItem.id" type="primary" @click="save(1)">新 增</el-button>
        <el-button v-if="dataListItem.id" type="primary"  @click="save(2)">更 新</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
export default {
  name: 'test_table',
  components : {
  },
  data () {
    return {
      dialogFormVisible : false,
      title : '',
      pager : {
        pageNo : 1,//第几页
        pageSize : 10,//每页多少条
        pageTotal : 0, // 共多少条
      },
      productLine : [
        {key:'DEV' , value : 'DEV'},
        {key:'SAT' , value : 'SAT'},
        {key:'UAT' , value : 'UAT'},
        {key:'FAT' , value : 'FAT'},
        {key:'PRD' , value : 'PRD'},
      ],
      application : [
        {key:'0' , value : '未审核'},
        {key:'1' , value : '已审核'},
        {key:'2' , value : '未通过'}
      ],
      dataListItem : {},

      table_productLine : '',//选中产线的值
      table_application : '',//选中应用的值
      tableData3: [{
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-08',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-06',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-07',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }]
    }
  },
  mounted (){
    util.isLogin();//判断是否登录
    this.query();
    
  },
  methods : {
    //分页
    sizeChange( val ){//pageSize 改变时会触发
      this.$data.pager.pageSize = val;
      this.query();
    },
    currentChange( val ){//currentPage 改变时会触发
      this.$data.pager.pageNo = val;
      this.query();
    },
    query(){
      var pager = this.$data.pager;
      pager.pageTotal = 22;

      // var self = this;
      // getAjax();
      // function getAjax(){
      //   var pager = self.$data.pager;
      //   var url = '';
      //   var param = {
      //     "pageIndex" : pager.pageNo,
      //     "pageSize"  : pager.pageSize
      //   }
      //   util.ajax(url,param,(success)=>{
      //     var dd = success.data;
      //     var d = dd.pageData;
      //     pager.pageNo = dd.pageIndex || 1;
      //     pager.pageSize = dd.pageSize;
      //     pager.pageTotal = dd.totalSize;
      //     self.$data.dataList = d;
      //   })
      // }
    },
    update( item ){
      this.$data.dialogFormVisible = true;
      this.$data.title = 'XXX管理 - 更新';
    },
    add( item ){
      this.$data.dialogFormVisible = true;
      this.$data.title = 'XXX管理 - 新增';
    },
    del( item ){
      Dialog.alert("您确定要删除吗？",{
        confirm : true,
        callback : function( txt ){
          if( txt === 'confirm'){//点击确定按钮
            var url = '';
            var param = { id : item.id};
            util.ajax(url,param,(success)=>{
              Dialog.message("删除成功!",'success');
            })
          }
        }
      })
    },
    save(){
      
    }
  }
}
</script>
<style>
  .el-table th,.el-table tr{
    text-align:center;
  }
  .el-table .cell{
    padding:0px;
  }
</style>
