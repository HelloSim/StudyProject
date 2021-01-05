# StudyProject
所有的项目导入到这里作为模块
# baselibrary
基本模块    封装一些基本类，base类、utils工具类、网络请求等
# sqlitelibrary
数据库模块
# TravelTool
公交查询功能（珠海）、网易新闻浏览、打卡记录



# bugly
https://bugly.qq.com/v2/index

# 极光推送
https://www.jiguang.cn/accounts/login/form

# Bmob后端云
https://www.bmob.cn/login
类文档：http://docs.bmob.cn/data/Android/i_doc/doc/index.html

# 网易新闻API：https://api.apiopen.top/api.html

# SmartTable    自动生成表格框架
git地址：https://github.com/huangyanbin/smartTable
jitpack仓库：https://jitpack.io/#huangyanbin/smartTable
功能介绍：https://juejin.im/post/5a55ae6c5188257350511a8c



    /**
     * 请求验证码
     *
     * @param phone
     */
    private void requestSMSCode(String phone) {
        /**
         * TODO template 如果是自定义短信模板，此处替换为你在控制台设置的自定义短信模板名称；如果没有对应的自定义短信模板，则使用默认短信模板。
         */
        BmobSMS.requestSMSCode(phone, "SMSCode", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    LogUtil.d(this.getClass(), "发送验证码成功，短信ID：" + smsId);
                } else {
                    LogUtil.e(this.getClass(), "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage());
                }
            }
        });
    }

    /**
     * 请求验证邮箱
     *
     * @param email
     */
    private void requestEmailVerify(String email) {
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    ToastUtil.T_Success(UserUpdateActivity.this, "请登录邮箱进行验证！");
                } else {
                    ToastUtil.T_Error(UserUpdateActivity.this, "请求失败！");
                }
            }
        });
    }
