package com.fengyongge.login.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.fengyongge.androidcommonutils.ktutils.DialogUtils
import com.fengyongge.androidcommonutils.ktutils.SharedPreferencesUtils
import com.fengyongge.androidcommonutils.ktutils.ToastUtils
import com.fengyongge.androidcommonutils.ktutils.ToolsUtils
import com.fengyongge.basecomponent.app.BaseApplication
import com.fengyongge.basecomponent.bean.LogoutUpdateBean
import com.fengyongge.baseframework.mvp.BaseMvpActivity
import com.fengyongge.login.bean.LoginBean
import com.fengyongge.login.bean.RegisterBean
import com.fengyongge.login.mvp.contract.LoginContact

import com.fengyongge.rxhttp.bean.BaseResponse
import com.fengyongge.rxhttp.exception.ResponseException
import com.fengyongge.basecomponent.constant.Const
import com.fengyongge.basecomponent.constant.RouterManageConst
import com.fengyongge.basecomponent.utils.RxNotify
import com.fengyongge.login.MainActivity
import com.fengyongge.login.R
import com.fengyongge.login.mvp.presenterImpl.LoginPresenterImpl
import kotlinx.android.synthetic.main.activity_login.*


/**
 * describe
 * 登录
 * @author fengyongge(fengyongge98@gmail.com)
 * @version V1.0
 * @date 2020/09/08
 */
@Route(path = RouterManageConst.LOGIN_LOGIN)
class LoginActivity : BaseMvpActivity<LoginPresenterImpl>(),LoginContact.View,View.OnClickListener{

    private var flag = false

    override fun initPresenter(): LoginPresenterImpl {
        return LoginPresenterImpl()
    }

    override fun initLayout(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        tvVersionName.text = "V${ToolsUtils.getVersionName(this)}"
        btLogin.background.alpha = 100
        btLogin.isEnabled = false

        etUserName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                justContent()
            }
        })


        etPassword.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etPassword.text.toString().isNotEmpty()) {
                    ivPasswordShow.visibility = View.VISIBLE
                    ivClearPassword.visibility = View.VISIBLE
                } else {
                    ivPasswordShow.visibility = View.INVISIBLE
                    ivClearPassword.visibility = View.INVISIBLE
                }
                justContent()
            }
        })

        btLogin.setOnClickListener(this)
        tvRigister.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
        ivPasswordShow.setOnClickListener(this)
        ivClearPassword.setOnClickListener(this)
    }

    private fun justContent(){
        if(TextUtils.isEmpty(etUserName.text.toString().trim())||TextUtils.isEmpty(etPassword.text.toString().trim())){
            btLogin.background.alpha = 100
            btLogin.isEnabled = false
        }else{
            btLogin.background.alpha = 255
            btLogin.isEnabled = true
        }
    }

    override fun initData() {

    }

    override fun postLoginShow(data: BaseResponse<LoginBean>) {

        if (data.errorCode == "0") {
            with(SharedPreferencesUtils(BaseApplication.getAppContext())){
                put(Const.IS_LOGIN,true)
                put(Const.NICKNAME,data.data.nickname)
                put(Const.ICON,data.data.icon)
                put(Const.USER_ID,data.data.id)
            }
            ARouter.getInstance().build(RouterManageConst.APP_HOMEPAGE).navigation()
            DialogUtils.dismissProgressMD()
            var logoutUpdateBean = LogoutUpdateBean()
            logoutUpdateBean.isUpdate = true
            RxNotify.instance?.post(logoutUpdateBean)
            finish()
        }else{
            DialogUtils.dismissProgressMD()
            ToastUtils.showToast(LoginActivity@this,data.errorMsg)
        }

    }


    override fun postRegisterShow(data: BaseResponse<RegisterBean>) {

    }



    override fun onError(data: ResponseException) {
        ToastUtils.showToast(ArticleSearchActivity@this,data.getErrorMessage())
        DialogUtils.dismissProgressMD()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivPasswordShow ->{
                if(flag){
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    ivPasswordShow.setImageResource(R.drawable.icon_pwd_hide)
                    etPassword.setSelection(etPassword.text.length)
                    flag = false
                }else{
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    ivPasswordShow.setImageResource(R.drawable.icon_pwd_show)
                    etPassword.setSelection(etPassword.text.length)
                    flag = true
                }
            }
            R.id.ivClearPassword ->{
                etPassword.text = Editable.Factory.getInstance().newEditable("")
            }
            R.id.tvRigister ->{
                ARouter.getInstance().build(RouterManageConst.LOGIN_REGISTER).navigation()
            }
            R.id.tvForgetPassword ->{
                ARouter.getInstance().build(RouterManageConst.LOGIN_REGISTER)
                    .withBoolean("isReset",true)
                    .navigation()
            }
            R.id.btLogin ->{
                DialogUtils.showProgress(this,getString(R.string.login_hint))
                mPresenter?.postLogin(etUserName.text.toString().trim(),etPassword.text.toString().trim())
            }
        }
    }


}