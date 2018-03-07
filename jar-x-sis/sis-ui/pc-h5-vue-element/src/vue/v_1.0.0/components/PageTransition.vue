<template>
  <transition :name="transitionName">
    <router-view class="child-view"></router-view>
  </transition>
</template>

<script>
  export default {
    data () {
      return {
        transitionName: 'slide-left'
      }
    }
  }
</script>

<style scoped>
  .child-view {
    position: absolute;
    left: 0;
    top: 0;
    width:100%;
    transition: all .8s cubic-bezier(0.51, -0.5, 0.54, 1.73);
  }
  .slide-left-enter, .slide-right-leave-active {
    opacity: 0;
    -webkit-transform: translate(50px, 0);
    transform: translate(50px, 0);
  }
  .slide-left-leave-active, .slide-right-enter {
    opacity: 0;
    -webkit-transform: translate(-50px, 0);
    transform: translate(-50px, 0);
  }
</style>
