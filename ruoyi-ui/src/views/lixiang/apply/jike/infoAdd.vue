<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="基本信息" name="first">
      <basicInfo></basicInfo>
    </el-tab-pane>
    <el-tab-pane label="材料信息" name="second">
      <materialsInfo></materialsInfo>
    </el-tab-pane>
    <el-tab-pane label="施工费信息" name="third">
      <construcostInfo></construcostInfo>
    </el-tab-pane>
    <el-tab-pane label="事前审批" name="fourth">
      <approvalInfo></approvalInfo>
    </el-tab-pane>
  </el-tabs>
</template>

<script>

  import basicInfo from './basicInfo.vue'
  import materialsInfo from './materialsInfo.vue'
  import construcostInfo from './construcostInfo.vue'
  import approvalInfo from './approvalInfo.vue'

  export default {
    components: {
    	basicInfo,
      materialsInfo,
      construcostInfo,
      approvalInfo
    },

    data() {
      return {
        activeName: 'first',

      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
</script>

