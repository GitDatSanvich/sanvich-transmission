new Vue({
    el: "#app",
    data: {
        files: {
            dir: [],
            files: []
        },
        uploadFiles: '',
        mkdirName: ''
    },
    methods: {
        getFile(event) {
            this.files.files = event.target.files;
            console.log(this.files);
        },

        checkLogin: function () {
            const _this = this;
            const url = "user/checkLogin";
            axios.get(url).then(function (result) {
                if (result.data === "ok") {
                    console.log("登录成功");
                } else if (result.data === "nope") {
                    alert("登录失败请重试！");
                } else {
                    alert(result.data);
                    window.location.href = "index.html";
                }
            });
        },
        toSignIn: function () {
            window.location.href = "signIn.html";
        },
        toIndex: function () {
            window.location.href = "index.html";
        }
    },
    created: function () {
        this.checkLogin();
    }
});
