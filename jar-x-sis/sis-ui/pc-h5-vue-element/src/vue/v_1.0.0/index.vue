<template>
    <div class="sgp">
        <img height="100%" width="100%" src="./resources/img/bank_service.jpg"/>
    </div>
</template>
<script>
    export default {
        name: 'index',
        data () {
            return {}
        },
        mounted (){
            Utils.isLogin();//判断是否登录
        },
        methods: {
            selectReport(){

            }
        }
    }
</script>
<style scoped>
    .sgp p {
        font-size: 17px;
        color: red;
        margin: 0 0 0 30px;
    }

</style>
