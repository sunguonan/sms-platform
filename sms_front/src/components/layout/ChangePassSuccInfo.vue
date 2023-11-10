<template>
  <div class="change">
    <el-dialog :before-close="closeChangeDialog" :modal-append-to-body="false" :show-close="false" :title="title"
               :visible.sync="dialogChangeShow" height="172px" top="40vh" width="207px">
      <img alt=""
           src="@/assets/changePass/xiugai.png" style="width: 44px;height: 44px;display: block;text-align:center;margin-left: 60px;padding-bottom: 20px;"/>
      <slot></slot>
    </el-dialog>
  </div>
</template>

<script lang="tsx">
import {Component, Prop, Vue} from 'vue-property-decorator'

@Component
export default class ChangePassSuccInfo extends Vue {
  static componentName = 'ChangePassSuccInfo'

  @Prop([String]) title !: string
  @Prop([Boolean]) dialogChangeShow !: boolean

  closeChangeDialog() {
    this.$emit('closeChangeDialog')
  }
}
</script>

<style lang="scss" scoped>

/deep/ .el-dialog {
  height: 172px;
  border-radius: 10px !important;
}

/deep/ .el-dialog__header {
  padding: 0 !important;
}

/deep/ .el-dialog__body {
  padding-top: 45px;
  // font-size: 18px;
  font-family: PingFangSC, PingFangSC-Regular;
  // font-weight: 500;
  text-align: center;
  color: #333333;
  // line-height: 28px;
}

/deep/ .el-message-box {
  width: 207px !important;
  height: 172px !important;
  height: 62px;
  background: #ffffff;
  border-radius: 10px;
}

.change {
}
</style>