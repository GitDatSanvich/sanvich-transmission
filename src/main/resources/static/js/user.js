new Vue({
    el: "#app",
    data: {
        user: {
            userName: '',
            passWord: '',
            passWordRetry: '',
            email: ''
        }
    },
    methods: {
        login: function () {
            const _this = this;
            const url = "user/login";
            axios.post(url, _this.user).then(function (result) {
                console.log(result);
                if (result.data.code === "0000") {
                    _this.cookieGet();
                } else if (result.data.code === "0021") {
                    alert(result.data.process);
                } else {
                    console.log(result);
                    alert("操作失败!!快快联系管理员!");
                }
            }).catch(function (err) {
                console.log(err);
            });
        },

        cookieGet: function () {
            const _this = this;
            const url = "user/getCookie?userName=" + _this.user.userName;
            axios.get(url).then(function (result) {
                console.log(result);
                if (result.data === "ok") {
                    _this.checkLogin();
                } else {
                    alert("未知的Cookie错误!");
                }
            });
        },

        checkLogin: function () {
            const _this = this;
            const url = "user/checkLogin";
            axios.get(url).then(function (result) {
                if (result.data === "ok") {
                    alert("登陆成功！");
                    window.location.href = "fileList.html";
                } else if (result.data === "nope") {
                    alert("登录失败请重试！");
                } else {
                    alert(result.data);
                }
            });
        },

        signIn: function () {
            const _this = this;
            const url = "user/signIn";
            if (_this.user.userName === null || _this.user.userName === "") {
                alert("用户名不能为空!");
            } else if (_this.user.passWord === null || _this.user.passWord === "") {
                alert("密码不能为空！");
            } else if (_this.user.email === null || _this.user.email === "") {
                alert("邮箱不能为空!");
            } else if (_this.user.passWord !== _this.user.passWordRetry) {
                alert('两次密码不一致呢!');
            } else {
                axios.post(url, _this.user).then(function (result) {
                    console.log(result);
                    if (result.data.code === "0000") {
                        alert("创建用户成功 请检查邮箱 邮箱没有邮件的话检查下邮箱垃圾桶哦");
                        _this.toIndex();
                    } else {
                        alert(result.data.process);
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
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
