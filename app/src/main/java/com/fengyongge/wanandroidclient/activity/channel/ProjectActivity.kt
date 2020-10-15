package com.fengyongge.wanandroidclient.activity.channel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.fengyongge.baselib.mvp.BaseMvpActivity
import com.fengyongge.baselib.net.BaseResponse
import com.fengyongge.baselib.net.exception.ResponseException
import com.fengyongge.baselib.utils.DialogUtils
import com.fengyongge.baselib.utils.ToastUtils
import com.fengyongge.wanandroidclient.R
import com.fengyongge.wanandroidclient.bean.ProjectBean
import com.fengyongge.wanandroidclient.bean.ProjectTypeBeanItem
import com.fengyongge.wanandroidclient.fragment.ProjectItemFragment
import com.fengyongge.wanandroidclient.mvp.contract.ProjectContract
import com.fengyongge.wanandroidclient.mvp.presenterImpl.ProjectPresenterImpl
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * describe
 *
 * @author fengyongge(fengyongge98@gmail.com)
 * @version V1.0
 * @date 2020/09/08
 */
class ProjectActivity : BaseMvpActivity<ProjectPresenterImpl>(),ProjectContract.View {

    private var fragmentList = mutableListOf<Fragment>()
    private var titleList = mutableListOf<String>()
    private lateinit var myFragmentViewPager: FragmentStatePagerAdapter

    override fun initPresenter(): ProjectPresenterImpl {
        return ProjectPresenterImpl()
    }

    override fun initLayout(): Int {
        return R.layout.activity_project
    }

    override fun initView() {
        initTitle()
        mPresenter?.getProjectType()
    }
    private fun initTitle(){
        var tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle?.text = "项目"
        var ivLeft = findViewById<ImageView>(R.id.ivLeft)
        ivLeft.visibility = View.VISIBLE
        ivLeft.setBackgroundResource(R.drawable.ic_back)
        ivLeft.setOnClickListener { finish() }
    }

    override fun initData() {

    }

    private fun setupWithViewPager(){
        myFragmentViewPager =
            MyFragmentViewPager(
                supportFragmentManager,
                fragmentList,
                titleList
            )
        vpProject.adapter = myFragmentViewPager
        tlProject.setupWithViewPager(vpProject)
    }


    class MyFragmentViewPager : FragmentStatePagerAdapter {

        private var fragmentList: MutableList<Fragment>
        private var titleList: MutableList<String>

        constructor(fm: FragmentManager, fragmentList: MutableList<Fragment>,titleList: MutableList<String>) : super(fm) {
            this.fragmentList = fragmentList
            this.titleList = titleList
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }


        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }

    override fun postCollectShow(data: BaseResponse<String>) {
    }

    override fun postCancleCollectShow(data: BaseResponse<String>) {
    }


    override fun getProjectTypeShow(data: BaseResponse<List<ProjectTypeBeanItem>>) {
        if (data.errorCode == "0") {
            if (data.data.isNotEmpty()) {
                titleList.clear()
                fragmentList.clear()
                for (index in data.data.indices) {
                    titleList.add(data.data[index].name)
                    fragmentList.add(ProjectItemFragment.newInstance(data.data[index].id))
                }
                setupWithViewPager()
            }
        } else {
            ToastUtils.showToast(ProjectActivity@this, data.errorMsg)
        }
    }

    override fun getProjectByType(data: BaseResponse<ProjectBean>) {
        TODO("Not yet implemented")
    }

    override fun onError(data: ResponseException) {

        ToastUtils.showToast(ProjectActivity@this,data.getErrorMessage())
        DialogUtils.dismissProgressMD()
    }


}