package com.fengyongge.wanandroidclient.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.fengyongge.baselib.mvp.BaseMvpActivity
import com.fengyongge.baselib.net.BaseResponse
import com.fengyongge.baselib.net.exception.ResponseException
import com.fengyongge.baselib.utils.DialogUtils
import com.fengyongge.baselib.utils.ToastUtils
import com.fengyongge.wanandroidclient.R
import com.fengyongge.wanandroidclient.mvp.contract.ShareContract
import com.fengyongge.wanandroidclient.mvp.presenterImpl.SharePresenterImpl
import kotlinx.android.synthetic.main.activity_share_project.*

/**
 * describe
 *
 * @author fengyongge(fengyongge98@gmail.com)
 * @version V1.0
 * @date 2020/09/08
 */
class ShareProjectActivity : BaseMvpActivity<SharePresenterImpl>(), ShareContract.View {
    override fun initLayout(): Int {
        return R.layout.activity_share_project
    }

    override fun initView() {
        initTitle()
        btShare.background.alpha = 100
        btShare.isEnabled = false
        btShare.setOnClickListener {
            DialogUtils.showProgress(this,"数据提交中...")
            mPresenter?.postShare(etShareTitle.text.toString(), etShareLink.text.toString())
        }
        etShareTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                legalJudge()
            }
        })

        etShareLink.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                legalJudge()
            }
        })

        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                legalJudge()
            }
        })
    }

    private fun legalJudge() {
        var legal = !TextUtils.isEmpty(etShareTitle.text.toString()) &&
                !TextUtils.isEmpty(etShareLink.text.toString()) &&
                !TextUtils.isEmpty(etContent.text.toString())
        if (legal) {
            btShare.background.alpha = 255
            btShare.isEnabled = true
        } else {
            btShare.background.alpha = 100
            btShare.isEnabled = false
        }
    }

    override fun initData() {
    }


    private fun initTitle() {
        var tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle?.text = "分享项目"
        var ivLeft = findViewById<ImageView>(R.id.ivLeft)
        ivLeft.visibility = View.VISIBLE
        ivLeft.setBackgroundResource(R.drawable.ic_back)
        ivLeft.setOnClickListener { finish() }
    }

    override fun initPresenter(): SharePresenterImpl {
        return SharePresenterImpl()
    }

    override fun postShareShow(data: BaseResponse<String>) {
        if (data.errorCode == "0") {
            DialogUtils.dismissProgressMD()
            ToastUtils.showToast(ShareProjectActivity@this, "分享成功")
            finish()
        }else{
            DialogUtils.dismissProgressMD()
            ToastUtils.showToast(ShareProjectActivity@this,data.errorMsg)
        }
    }

    override fun getShareListShow(data: BaseResponse<String>) {

    }

    override fun postDeleteMyShareShow(data: BaseResponse<String>) {

    }

    override fun onError(data: ResponseException) {
        ToastUtils.showToast(ArticleSearchActivity@this,data.getErrorMessage())
        DialogUtils.dismissProgressMD()
    }


}