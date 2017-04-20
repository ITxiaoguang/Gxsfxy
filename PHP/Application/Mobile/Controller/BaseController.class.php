<?php

namespace Mobile\Controller;

use Mobile\Model\ActivityModel;
use Mobile\Model\AreaModel;
use Mobile\Model\CommentModel;
use Mobile\Model\ConfigModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\NewsModel;
use Mobile\Model\PhoneModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\UploadModel;
use Mobile\Model\UserLogModel;
use Mobile\Model\UserModel;
use Mobile\Model\UserTokenModel;
use Think\Controller;

class BaseController extends Controller
{

    public $number = 20;

    //构析函数
    public function __construct()
    {
        parent::__construct();
    }

    //--------------------------------
    //------------- 读取 -------------
    //--------------------------------

    //获取配置
    public function getConfig()
    {
        $model = new ConfigModel();
        outputData($model->select());
    }

    //获取省份列表
    public function getProvince()
    {
        $areaModel = new AreaModel();

        $where = array();
        $where['area_parent_id'] = '0';
        outputData($areaModel->where($where)->select());
    }

    //获取城市地区
    public function getCityArea()
    {
        if (empty(I('post.parent_id'))) {
            outputError('参数错误');
        }

        $areaModel = new AreaModel();

        $where = array();
        $where['area_parent_id'] = I('post.parent_id');
        outputData($areaModel->where($where)->select());
    }

    //--------------------------------
    //------------- 登录 -------------
    //--------------------------------

    //登录
    public function login()
    {
        if (empty(I('post.type'))) {
            outputError('类型不能为空');
        }
        if (empty(I('post.username'))) {
            outputError('账号不能为空');
        }
        if (empty(I('post.password'))) {
            outputError('密码不能为空');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $userTokenModel = new UserTokenModel();

        $where = array();
        $where[I('post.type')] = I('post.username');
        $where['user_pass'] = I('post.password');
        $user_id = $userModel->where($where)->getField('user_id');

        if (!empty($user_id)) {
            //删除 & 生成 Token
            $userTokenModel->deleteByUid($user_id);
            $token = md5(I('post.username') . I('post.password') . I('post.time'));
            $userTokenModel->insert($user_id, I('post.client'), $token, I('post.time'));
            //更新数据
            $userModel->updateLoginIp($user_id);
            $userModel->updateLoginNum($user_id);
            $userModel->updateLoginTime($user_id, I('post.time'));
            $userModel->updateByIdWithKV($user_id, 'login_time', I('post.time'));
            //更新日记
            $userLogModel->insert($user_id, '登录', I('post.time'));
            //输出数据
            outputData($token);
        } else {
            outputError('账号或密码错误!');
        }
    }

    //教务系统登录
    public function loginEdu()
    {
        if (empty(I('post.stu_id'))) {
            outputError('学号不能为空');
        }
        if (empty(I('post.stu_pass'))) {
            outputError('密码不能为空');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $userTokenModel = new UserTokenModel();

        $user_id = $userModel->getFiledByKV('stu_id', I('post.stu_id'), 'user_id');

        if (!empty($user_id)) {
            //更新用户 STU_PASS
            $userModel->updateByIdWithKV($user_id, 'stu_pass', I('post.stu_pass'));
            //删除&生成 Token
            $userTokenModel->deleteByUid($user_id);
            $token = md5(I('post.stu_id') . I('post.stu_pass') . I('post.time'));
            $userTokenModel->insert($user_id, I('post.client'), $token, I('post.time'));
            //更新数据
            $userModel->updateLoginIp($user_id);
            $userModel->updateLoginNum($user_id);
            $userModel->updateLoginTime($user_id, I('post.time'));
            $userModel->updateByIdWithKV($user_id, 'login_time', I('post.time'));
            //更新日记
            $userLogModel->insert($user_id, '登录', I('post.time'));
            //输出数据
            outputData($token);
        } else {
            outputError('请先注册!');
        }
    }

    //绑定教务系统账号
    public function loginBindEdu()
    {
        if (empty(I('post.mobile'))) {
            outputError('手机号码为空');
        }
        if (empty(I('post.stu_id'))) {
            outputError('学号为空');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $userTokenModel = new UserTokenModel();

        $user_id = $userModel->getFiledByKV('user_mobile', I('post.mobile'), 'user_id');

        if (!empty($user_id)) {
            if (!empty($userModel->getFiledById($user_id, 'stu_id'))) {
                outputError('这个账号已经绑定过学号了!');
            } else {
                //保存信息
                $data = array();
                $data['stu_id'] = I('post.stu_id');
                $data['stu_pass'] = I('post.stu_pass');
                $data['user_college'] = I('post.college');
                $data['user_professional'] = I('post.professional');
                $data['user_class'] = I('post.classmate');
                $data['true_name'] = I('post.true_name');
                $data['user_gender'] = I('post.gender');
                $data['user_card'] = I('post.card');
                $data['user_birthday'] = I('post.birthday');
                $userModel->updateById($user_id, $data);
                //删除&生成 Token
                $userTokenModel->deleteByUid($user_id);
                $token = md5(I('post.stu_id') . I('post.stu_pass') . I('post.time'));
                $userTokenModel->insert($user_id, I('post.client'), $token, I('post.time'));
                //更新数据
                $userModel->updateLoginIp($user_id);
                $userModel->updateLoginNum($user_id);
                $userModel->updateLoginTime($user_id, I('post.time'));
                $userModel->updateByIdWithKV($user_id, 'login_time', I('post.time'));
                //更新日记
                $userLogModel->insert($user_id, '绑定学号 ' . I('post.stu_id'), I('post.time'));
                $userLogModel->insert($user_id, '登录', I('post.time'));
                //输出数据
                outputData($token);
            }
        } else {
            outputError('账户不存在!');
        }
    }

    //--------------------------------
    //------------- 注册 -------------
    //--------------------------------

    //教务系统注册
    public function registerEdu()
    {
        if (empty(I('post.mobile'))) {
            outputError('手机号码为空');
        }
        if (empty(I('post.password'))) {
            outputError('密码为空');
        }
        if (empty(I('post.nickname'))) {
            outputError('昵称为空');
        }
        if (I('post.password') != I('post.password_repeat')) {
            outputError('两次输入的密码不一样');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $pointsLogModel = new PointsLogModel();

        $user_id = $userModel->getFiledByKV('user_mobile', I('post.mobile'), 'user_id');

        if (empty($user_id)) {
            if ($userModel->insert(I('post.mobile'), I('post.password'), I('post.stu_id'), I('post.stu_pass'), I('post.college'), I('post.professional'), I('post.classmate'), I('post.nickname'), I('post.true_name'), I('post.card'), I('post.birthday'), I('post.gender'), I('post.time'), I('post.device'), I('post.client'), I('post.version'))) {
                $user_id = $userModel->getFiledByKV('user_mobile', I('post.mobile'), 'user_id');
                $userLogModel->insert($user_id, '注册会员', I('post.time'));
                $pointsLogModel->insert($user_id, '+', '100', '注册送积分', I('post.time'));
                sendNotifyAndroidByPushId($userModel->getFiledById('1', 'push_id'), '有新用户注册啦！快去看看吧', null);
                outputData('1');
            } else {
                outputError('注册失败');
            }
        } else {
            outputError('手机号码已存在');
        }
    }

    //手机注册
    public function registerMobile()
    {
        if (empty(I('post.mobile'))) {
            outputError('手机号码为空');
        }
        if (empty(I('post.password'))) {
            outputError('密码为空');
        }
        if (empty(I('post.nickname'))) {
            outputError('昵称为空');
        }
        if (empty(I('post.gender'))) {
            outputError('性别未选择');
        }
        if (I('post.password') != I('post.password_repeat')) {
            outputError('两次输入的密码不一样');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $pointsLogModel = new PointsLogModel();

        $user_id = $userModel->getFiledByKV('user_mobile', I('post.mobile'), 'user_id');

        if (empty($user_id)) {
            if ($userModel->insert(I('post.mobile'), I('post.password'), '', '', '', '', '', I('post.nickname'), '', '', '', I('post.gender'), I('post.time'), I('post.device'), I('post.client'), I('post.version'))) {
                $user_id = $userModel->getFiledByKV('user_mobile', I('post.mobile'), 'user_id');
                $userLogModel->insert($user_id, '注册会员', I('post.time'));
                $pointsLogModel->insert($user_id, '+', '100', '注册送积分', I('post.time'));
                sendNotifyAndroidByPushId($userModel->getFiledById('1', 'push_id'), '有新用户注册啦！快去看看吧', null);
                outputData('1');
            } else {
                outputError('注册失败');
            }
        } else {
            outputError('手机号码已存在');
        }
    }

    //--------------------------------
    //------------- 搜索 -------------
    //--------------------------------

    public function search()
    {
        if (empty(I('post.keyword'))) {
            outputError('关键字不能为空');
        }

        $userModel = new UserModel();
        $phoneModel = new PhoneModel();
        $uploadModel = new UploadModel();
        $dynamicModel = new DynamicModel();
        $activityModel = new ActivityModel();
        //用户
        $data = array();
        $where = array();
        $where['_string'] = '(user_mobile like "%' . I('post.keyword') . '%") OR (nick_name like "%' . I('post.keyword') . '%") OR (user_college like "%' . I('post.keyword') . '%") OR (user_professional like "%' . I('post.keyword') . '%") OR (user_class like "%' . I('post.keyword') . '%")';
        $user = $userModel->where($where)->field('user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_avatar,user_gender,user_sign')->select();
        $data['user_list'] = $user;
        //电话
        $where = array();
        $where['_string'] = '(phone_class like "%' . I('post.keyword') . '%") OR (phone_name like "%' . I('post.keyword') . '%") OR (phone_address like "%' . I('post.keyword') . '%")';
        $phone = $phoneModel->where($where)->select();
        $data['phone_list'] = $phone;
        //动态
        if (is_numeric(I('post.keyword'))) {
            $data['dynamic_list'] = array();
        } else {
            $where = array();
            $where['_string'] = '(dynamic_content like "%' . $_POST['uniKeyword'] . '%") OR (dynamic_location like "%' . I('post.keyword') . '%") OR (dynamic_title like "%' . I('post.keyword') . '%")';
            $dynamic = $dynamicModel->where($where)->select();
            foreach ($dynamic as $key => $val) {
                $dynamic[$key]['user_info'] = $userModel->getBaseInfo($dynamic[$key]['dynamic_uid']);
                $dynamic[$key]['image_info'] = $uploadModel->getUrlArray('image', $dynamic[$key]['dynamic_uid'], $dynamic[$key]['dynamic_type'], $dynamic[$key]['dynamic_id']);
            }
            $data['dynamic_list'] = $dynamic;
        }
        //活动
        $where = array();
        $where['activity_state'] = '1';
        $where['_string'] = '(activity_name like "%' . I('post.keyword') . '%") OR (activity_content like "%' . I('post.keyword') . '%")';
        $data['activity_list'] = $activityModel->order('activity_id desc')->where($where)->select();
        //输出
        outputData($data);
    }

    //--------------------------------
    //------------- 用户 -------------
    //--------------------------------

    //搜索用户
    public function userSearch()
    {
        if (empty(I('post.page')) || empty(I('post.keyword'))) {
            outputError('参数不正确');
        }

        $phoneModel = new PhoneModel();

        $where = array();
        $where['_string'] = '(nick_name like "%' . I('post.keyword') . '%") OR (user_college like "%' . I('post.keyword') . '%") OR (user_professional like "%' . I('post.keyword') . '%") OR (user_class like "%' . I('post.keyword') . '%")';
        $data = $phoneModel->where($where)->field('user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_avatar,user_gender,user_sign,user_follow')
            ->limit($this->number)->page(I('post.page'))->select();
        outputData($data);
    }

    //--------------------------------
    //------------- 关注 -------------
    //--------------------------------


    //关注排行榜
    public function followRank()
    {
        if (empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();

        $data = $userModel->order('CAST(`user_follow` AS DECIMAL) desc')
            ->field('user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_avatar,user_gender,user_sign,user_follow')
            ->limit($this->number)->page(I('post.page'))->select();
        outputData($data);
    }

    //--------------------------------
    //------------- 评论 -------------
    //--------------------------------

    //评论列表
    public function commentList()
    {
        if (empty(I('post.table')) || empty(I('post.tid')) || empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $commentModel = new CommentModel();

        $where = array();
        $where['comment_table'] = I('post.table');
        $where['comment_tid'] = I('post.tid');
        $data = $commentModel->order('comment_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['comment_uid']);
            $comment_rid = $data[$key]['comment_rid'];
            if ($comment_rid != '-1') {
                $where = array();
                $where['comment_id'] = $comment_rid;
                $comment_reply = $commentModel->where($where)->limit(1)->select();
                $user_info = $userModel->getBaseInfo($comment_reply[0]['comment_uid']);
                $comment_reply[0]['nick_name'] = $user_info['nick_name'];
                $data[$key]['comment_reply'] = $comment_reply[0];
            }
        }
        outputData($data);
    }

    //--------------------------------
    //------------- 动态 -------------
    //--------------------------------

    //动态列表
    public function dynamicList()
    {
        if (empty(I('post.page')) || empty(I('post.type'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $uploadModel = new UploadModel();
        $dynamicModel = new DynamicModel();

        $where = array();
        $where['dynamic_type'] = I('post.type');
        $data = $dynamicModel->order('dynamic_id desc')->limit($this->number)->page(I('post.page'))->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['dynamic_uid']);
            $data[$key]['image_info'] = $uploadModel->getUrlArray('image', $data[$key]['dynamic_uid'], I('post.type'), $data[$key]['dynamic_id']);
        }
        outputData($data);
    }

    //我的动态列表
    public function dynamicListUser()
    {
        if (empty(I('post.page')) || empty(I('post.type')) || empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $uploadModel = new UploadModel();
        $dynamicModel = new DynamicModel();

        $where = array();
        $where['dynamic_type'] = I('post.type');
        $where['dynamic_uid'] = I('post.user_id');
        $data = $dynamicModel->order('dynamic_id desc')->limit($this->number)->page(I('post.page'))->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['dynamic_uid']);
            $data[$key]['image_info'] = $uploadModel->getUrlArray('image', $data[$key]['dynamic_uid'], I('post.type'), $data[$key]['dynamic_id']);
        }
        outputData($data);
    }

    //动态详细
    public function dynamicDetailed()
    {
        if (empty(I('post.id')) || empty(I('post.type'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $uploadModel = new UploadModel();
        $dynamicModel = new DynamicModel();

        $data = $dynamicModel->getRowById(I('post.id'));
        $data['user_info'] = $userModel->getBaseInfo($data['dynamic_uid']);
        $data['image_info'] = $uploadModel->getUrlArray('image', $data['dynamic_uid'], I('post.type'), $data['dynamic_id']);
        outputData($data);
    }

    //--------------------------------
    //------------- 活动 -------------
    //--------------------------------

    //动态列表
    public function activityList()
    {
        if (empty(I('post.page'))) {
            outputError('参数错误');
        }

        $activityModel = new ActivityModel();

        $where = array();
        $where['activity_state'] = '1';
        outputData($activityModel->order('activity_id desc')->limit($this->number)->page(I('post.page'))->where($where)->select());
    }

    //动态详细
    public function activityDetailed()
    {
        if (empty(I('post.id'))) {
            outputError('参数错误');
        }

        $activityModel = new ActivityModel();

        outputData($activityModel->getRowById(I('post.id')));
    }

    //--------------------------------
    //------------- 电话 -------------
    //--------------------------------

    //获取电话列表
    public function phoneList()
    {
        if (empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $phoneModel = new PhoneModel();

        outputData($phoneModel->limit($this->number)->page(I('post.page'))->select());
    }

    //获取搜索电话列表
    public function phoneSearch()
    {
        if (empty(I('post.page')) || empty(I('post.keyword'))) {
            outputError('参数不正确');
        }

        $phoneModel = new PhoneModel();

        $where = array();
        $where['_string'] = '(phone_class like "%' . I('post.keyword') . '%") OR (phone_name like "%' . I('post.keyword') . '%") OR (phone_address like "%' . I('post.keyword') . '%")';
        outputData($phoneModel->where($where)->limit($this->number)->page(I('post.page'))->select());
    }

    //--------------------------------
    //------------- 新闻 -------------
    //--------------------------------

    //首页内容
    public function newsHome()
    {
        $newsModel = new NewsModel();

        $where = array();
        $where['news_type'] = '学校要闻';
        outputData($newsModel->order('news_id desc')->where($where)->limit(3)->select());
    }

    //读取新闻分类
    public function newsType()
    {
        $newsModel = new NewsModel();

        outputData($newsModel->distinct(true)->field('news_type')->select());
    }

    //读取新闻列表
    public function newsList()
    {
        if (empty(I('post.page')) || empty(I('post.type'))) {
            outputError('参数错误!');
        }

        $newsModel = new NewsModel();

        $where = array();
        $where['news_type'] = I('post.type');
        outputData($newsModel->order('news_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select());
    }

    //更新点击
    public function newsClick()
    {
        if (empty(I('post.news_id'))) {
            outputError('参数错误!');
        }

        $newsModel = new NewsModel();

        $newsModel->addClickNumber(I('post.news_id'));
        outputData('1');
    }

    //新闻基本信息
    public function newsInfoByLink()
    {
        if (empty(I('post.link'))) {
            outputError('参数错误!');
        }

        $newsModel = new NewsModel();
        $data = $newsModel->getInfoByLink(I('post.link'));
        if (empty($data)) {
            outputError('不存在');
        } else {
            outputData($data);
        }
    }

}