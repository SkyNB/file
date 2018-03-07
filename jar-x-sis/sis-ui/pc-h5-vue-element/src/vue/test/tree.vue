<template>
  <div class="element__body">
    <el-col :span="6">
      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    </el-col>
    <el-col :span="2"><div ></div></el-col>
    <el-col :span="6">
      节点过滤
      <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
      <el-tree class="filter-tree" :data="data" :props="defaultProps" default-expand-all :filter-node-method="filterNode" ref="tree2"></el-tree>
    </el-col>
    <el-col :span="6"><div ></div></el-col>
  </div>
</template>
<script>
export default {
  name: 'test_tree',
  components : {
    
  },
  mounted (){
    util.isLogin();//判断是否登录
  },
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      filterText: '',
      data: [
        {
          label: '一级 1',
          children: [{
            label: '二级 1-1',
            children: [{
              label: '三级 1-1-1'
            }]
          }]
        }, 
        {
          label: '一级 2',
          children: [
            {
              label: '二级 2-1',
              children: [{
                label: '三级 2-1-1'
              }]
            }, 
            {
              label: '二级 2-2',
              children: [{
                label: '三级 2-2-1'
              }]
            }
          ]
        }, 
        {
          label: '一级 3',
          children: [
          {
            label: '二级 3-1',
            children: [{
              label: '三级 3-1-1'
            }]
          }, 
          {
            label: '二级 3-2',
            children: [{
              label: '三级 3-2-1'
            }]
          }]
        } 
      ] 
    }  
  },
  methods : {
    handleNodeClick(data) {
      console.log(data);
    },
    filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      
    }
  }
}
</script>
<style scoped>
  
</style>
