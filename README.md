# 基于Springboot+Vue框架的短信调度平台的设计与实现

项目结构：
sms-parent # 短信平台父工程</br>
├── sms-entity # 短信平台实体</br>
├── sms-manage # 系统管理服务 页面管理</br>
├── sms-api # 短信接收服务，应用系统调用接口、发送短信 内部调用 内部电商平台 -->(调用) api --> redis和mysql</br>
├── sms-server # 短信发送服务，调用短信通道、发送短信 redis和mysql --> (调用)server --> 短信通道商</br>
└── sms-sdk # 短信SDK，应用系统引入、发送短信</br>
└── sms-test # 短信测试模块</br>

sdk 把软件的某种功能通过封装形成sdk 外部只需要引入maven的jar包就可以使用</br>
本质上还是调用http接口进行发送数据