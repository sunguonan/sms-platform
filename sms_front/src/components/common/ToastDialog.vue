<template>
  <el-dialog :before-close="closeDialog" :show-close="false" :title="title" :visible.sync="dialogShow" append-to-body
             top="40vh" width="315px">
    <slot></slot>
  </el-dialog>
</template>

<script lang="tsx">
import {Component, Prop, Vue} from 'vue-property-decorator'

@Component
export default class ToastDialog extends Vue {
  static componentName = 'ToastDialog'

  @Prop([String]) title !: string
  @Prop([Boolean]) dialogShow!: boolean

  closeDialog() {
    this.$emit('closeDialog')
  }
}
</script>

<style lang="scss" scoped>
/deep/ .el-dialog__header {
  padding: 0;
}

/deep/ .el-dialog {
  border-radius: 10px;
}

/deep/ .el-dialog__body {
  height: 28px;
  font-size: 18px;
  font-family: PingFangSC, PingFangSC-Regular;
  // font-weight: 500;
  text-align: center;
  color: #333333;
  line-height: 28px;
}
</style>