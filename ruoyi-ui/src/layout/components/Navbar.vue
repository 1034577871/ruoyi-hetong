<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <!-- <search id="header-search" class="right-menu-item" /> -->
        
        <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import request from '@/utils/request'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  data() {
    return {
      path:"ws://127.0.0.1:9001/webSocketServer/2",
      socket:"",
      queueReceiveSetting: { // 消息队列配置
        websock: null,
        client: null,
        wsuri: "ws://127.0.0.1:9001/websocket/",
      }
    }
  },
  created(){

    this.init();
  },
  methods: {
    selectMore(){
      alert(0);
    },
    init(){

      let query = this.queryParams;
      return request({
        url: '/collection/collection/getUserInfo',
        method: 'get',
        params: query
      }).then(response => {
        this.initWebSocket(response.data.user.userName)
      });
    },
    initWebSocket (name) {
      let url = window.location.hostname;
      this.queueReceiveSetting.wsuri="ws://"+url+":9001/websocket/"+name;
      // ws地址
      if (this.queueReceiveSetting.websock) {
        this.queueReceiveSetting.websock.close()
      }
      this.queueReceiveSetting.websock = new WebSocket(this.queueReceiveSetting.wsuri)
      this.queueReceiveSetting.websock.onopen = res => {
        console.log('开启连接')
      }
      this.queueReceiveSetting.websock.onmessage = res => {
        console.log("**************************");
        console.log('接收到的数据：', res.data)
        this.showMessage(res.data);
      }
      this.queueReceiveSetting.websock.onclose = res => {
        console.log('连接关闭')
      }
      this.queueReceiveSetting.websock.onerror = res => {
        console.log('连接出错')
      }
    },

    tiaozhuan(val){
      console.log(val);
      this.$notify.close();
      window.location.href=val;
    },
    //弹出待办任务的推送消息
    showMessage(data) {
      let href = "";
      let flag = false;
      if(data!=null){
        if(data.indexOf("-")!=-1){
          let datas = data.split("-");
          if(datas[2]=='待确认'){
            flag = true;
          }
        }
      }
      if(flag){
        href = " <a  href='javascript:window.location.href=\"/ng/ngList\";this.$notify.close();'  class='link-type'>您有:"+data+" </a>";
      }else{
        href = " <a  href='javascript:window.location.href=\"/task/dbTask\";this.$notify.close();'  class='link-type'>您有:"+data+" </a>";
      }
      this.$notify({
        title:'代办消息提示',
        dangerouslyUseHTMLString: true,
        message:  href,
        position: 'bottom-right',
        duration: 0,
        onClick:function(){
          this.close();
        },
      });
    },

    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      })
    }
  },
  destroyed () {
    // 销毁监听
    this.socket.onclose = this.close
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
