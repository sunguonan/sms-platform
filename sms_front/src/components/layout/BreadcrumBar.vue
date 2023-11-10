<template>
  <el-breadcrumb separator="/" style="margin-bottom:10px">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item :key="main.title" :to="main.path">
        <i :class="main.icon" class="i-icon menuicon iconfont" style="font-size:14px;"></i> {{ main.title }}
      </el-breadcrumb-item>
      <el-breadcrumb-item v-for="obj in breadcrumbList" :key="obj.id" :to="getRoute(obj)">
        <i :class="obj.icon" class='menuicon iconfont'></i> {{ obj.title }}
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
export default {
  props: {
    breadcrumbList: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      main: {
        path: '/',
        title: '首页',
        access: 1,
        icon: "icon-zhuye-copy"
      }
    }
  },
  // watch: {
  //   $roue(to, from) {
  //   }
  // },
  methods: {
    getRoute(route) {
      if (Number(route.isRoute) === 1) return route
      return null
    }
  }
}
</script>

<style lang="scss" scoped>
.i-icon {
  width: 16px !important;
  height: 16px !important;
}
</style>
