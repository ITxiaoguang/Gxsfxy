<?php

namespace Mobile\Controller;

use Mobile\Model\CommentModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\GradeModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\PraiseModel;
use Mobile\Model\UserLogModel;
use Mobile\Model\UserModel;
use Mobile\Model\UserTokenModel;
use Mobile\Model\VerifyModel;
use Think\Upload;

class UserController extends BaseController
{

    public $user_info;
    public $user_token;

    public function __construct()
    {
        parent::__construct();

        $this->user_token = I('post.token');
        if (empty($this->user_token)) {
            outputError('token不能为空!');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();
        $dynamicModel = new DynamicModel();
        $commentModel = new CommentModel();
        $userTokenModel = new UserTokenModel();

        $this->user_info = array();
        $this->user_info = $userModel->getRowById($userTokenModel->getFiledByKV('ut_token', $this->user_token, 'ut_uid'));
        if (!empty($this->user_info)) {
            $this->user_info['praise_number'] = $praiseModel->getNumberByUid($this->user_info['user_id']);
            $this->user_info['dynamic_number'] = $dynamicModel->getNumberByUid($this->user_info['user_id']);
            $this->user_info['comment_number'] = $commentModel->getNumberByUid($this->user_info['user_id']);
        } else {
            outputError('用户不存在');
        }
    }

    //注销
    public function logout()
    {
        $userModel = new UserModel();
        $userLogModel = new UserLogModel();
        $userTokenModel = new UserTokenModel();

        $userTokenModel->deleteByUid($this->user_info['user_id']);
        $userModel->updateByIdWithKV($this->user_info['user_id'], 'push_id', '');
        $userModel->updateByIdWithKV($this->user_info['user_id'], 'login_state', '0');
        $userLogModel->insert($this->user_info['user_id'], '注销登录', I('post.time'));
        outputData('1');
    }

    //读取个人信息
    public function getInfo()
    {
        $userModel = new UserModel();
        $pointLogModel = new PointsLogModel();

        $where = array();
        $where['pl_uid'] = $this->user_info['user_id'];
        $where['pl_content'] = '每日登陆加积分';
        $pl_time = $pointLogModel->order('pl_id desc')->where($where)->getField('pl_time');
        $pl_time = substr($pl_time, 0, 10);
        $now_time = substr(I('post.time'), 0, 10);
        if ($pl_time != $now_time) {
            $userModel->addPoints($this->user_info['user_id'], '20');
            $pointLogModel->insert($this->user_info['user_id'], '+', '20', '每日登陆加积分', I('post.time'));
        }
        $userModel->updateByIdWithKV($this->user_info['user_id'], 'login_state', '1');
        outputData($this->user_info);
    }

    //修改个人信息
    public function editInfo()
    {
        if (empty(I('post.user_qq')) || empty(I('post.nick_name')) || empty(I('post.user_sign')) || empty(I('post.user_city')) || empty(I('post.user_area')) || empty(I('post.user_province'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();

        $data = array();
        $data['user_qq'] = I('post.user_qq');
        $data['nick_name'] = I('post.nick_name');
        $data['user_sign'] = I('post.user_sign');
        $data['user_city'] = I('post.user_city');
        $data['user_area'] = I('post.user_area');
        $data['user_province'] = I('post.user_province');
        $userModel->updateById($this->user_info['user_id'], $data);
        outputData('1');
    }

    //修改推送ID
    public function editPush()
    {
        if (empty(I('post.push_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();

        $userModel->updateByIdWithKV($this->user_info['user_id'], 'push_id', I('post.push_id'));
        outputData('1');
    }

    //绑定教务系统账号
    public function bindEdu()
    {
        if (empty(I('post.stu_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $userLogModel = new UserLogModel();

        $user_id = $userModel->getFiledByKV('stu_id', I('post.stu_id'), 'user_id');
        if (!empty($user_id) && $user_id != $this->user_info['user_id']) {
            outputError('账号已被绑定');
        } else {
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
            $userModel->updateById($this->user_info['user_id'], $data);
            $userLogModel->insert($this->user_info['user_id'], '绑定教务系统账号：' . I('post.stu_id'), I('post.time'));
            outputData('1');
        }
    }

    //积分日记
    public function pointsLog()
    {
        $pointsLogModel = new PointsLogModel();

        $where = array();
        $where['pl_uid'] = $this->user_info['user_id'];
        outputData($pointsLogModel->order('pl_id desc')->where($where)->select());
    }

    //修改账户密码
    public function modifyPass()
    {
        if (empty(I('post.password')) || empty(I('post.password_new')) || empty(I('post.password_repeat'))) {
            outputError('参数不正确');
        }
        if ($this->user_info['user_pass'] != I('post.password')) {
            outputError('旧密码不正确');
        }
        if (I('post.password_new') != I('post.password_repeat')) {
            outputError('两次输入的新密码不一样');
        }

        $userModel = new UserModel();

        $userModel->updateByIdWithKV($this->user_info['user_id'], 'user_pass', I('post.password_new'));
        outputData('1');
    }

    //修改用户头像
    public function modifyAvatar()
    {
        $userModel = new UserModel();

        $upload = new Upload();
        $upload->hash = false;
        $upload->savePath = '';
        $upload->replace = true;
        $upload->autoSub = false;
        $upload->maxSize = 3145728;
        $upload->rootPath = './Public/upload/avatar/';
        $upload->saveName = $this->user_info['user_id'];
        $upload->exts = array('jpg', 'gif', 'png', 'jpeg');
        $info = $upload->uploadOne($_FILES['file']);
        if ($info) {
            $url = 'http://gxsfxy.yokey.top/Public/upload/avatar/' . $info['savename'];
            $userModel->updateByIdWithKV($this->user_info['user_id'], 'user_avatar', $url);
            outputData($url);
        } else {
            outputError($upload->getError());
        }
    }

    //更新成功
    public function updateGrade()
    {
        $gradeModel = new GradeModel();

        $where = array();
        $where['grade_uid'] = $this->user_info['user_id'];
        $where['grade_kkxq'] = I('post.kkxq');
        $where['grade_kcmc'] = I('post.kcmc');
        $where['grade_cj'] = I('post.cj');
        $data = array();
        $data['grade_uid'] = $this->user_info['user_id'];
        $data['grade_xh'] = I('post.xh');
        $data['grade_kkxq'] = I('post.kkxq');
        $data['grade_kcbh'] = I('post.kcbh');
        $data['grade_kcmc'] = I('post.kcmc');
        $data['grade_cj'] = I('post.cj');
        $data['grade_xf'] = I('post.xf');
        $data['grade_zxs'] = I('post.zxs');
        $data['grade_khfs'] = I('post.khfs');
        $data['grade_kcsx'] = I('post.kcsx');
        $data['grade_kcxz'] = I('post.kcxz');
        $grade_id = $gradeModel->where($where)->getField('grade_id');
        if (!empty($grade_id)) {
            $gradeModel->updateById($grade_id, $data);
        } else {
            $gradeModel->add($data);
        }
    }

    //发送邮箱验证码
    public function sendEMailCode()
    {
        if (empty(I('post.email'))) {
            outputError('参数错误');
        }

        $verifyModel = new VerifyModel();

        $code = rand(100000, 999999);
        $verifyModel->insert($this->user_info['user_id'], 'email', $code);
        $content = '尊敬的用户：' . $this->user_info['user_mobile'] . '，您于' . I('post.time')
            . '申请绑定邮箱，验证码为：' . $code . '，验证一次失效，请尽快完成验证。谢谢您对广西师范学院APP的支持，祝您身体健康，万事如意';
        sendMail(I('post.email'), '广西师范学院移动APP邮箱绑定验证', $content);
        outputData('1');
    }

    //验证邮箱验证码
    public function verifyEMailCode()
    {
        if (empty(I('post.email')) || empty(I('post.code'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $verifyModel = new VerifyModel();
        $userLogModel = new UserLogModel();

        if ($verifyModel->verifyCode($this->user_info['user_id'], 'email', I('post.code'))) {
            $data = array();
            $data['user_email'] = I('post.email');
            $data['email_bind'] = '1';
            $userModel->updateById($this->user_info['user_id'], $data);
            $userLogModel->insert($this->user_info['user_id'], '绑定邮箱:' . I('post.email'), I('post.time'));
            outputData('1');
        } else {
            outputError('验证失败');
        }
    }

    //修改登录状态
    public function changeLoginState()
    {
        $userModel = new UserModel();
        $userModel->updateByIdWithKV($this->user_info['user_id'], 'login_state', I('post.state'));
        outputData('1');
    }

}