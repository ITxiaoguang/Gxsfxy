<?php

namespace Mobile\Controller;

use Mobile\Model\UserModel;

class AdminUserController extends UserController
{

    public function __construct()
    {
        parent::__construct();
        if ($this->user_info['user_power'] == '用户') {
            outputError('无权操作');
        }
    }

    //用户列表
    public function userList()
    {
        if (empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        outputData($userModel->order('user_id desc')->limit($this->number)->page(I('post.page'))->select());
    }

    //用户搜索
    public function userSearch()
    {
        if (empty(I('post.page')) || empty(I('post.keyword'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();

        $where = array();
        $where['_string'] = '(user_mobile like "%' . I('post.keyword') . '%") OR (nick_name like "%' . I('post.keyword') . '%") OR (user_college like "%' . I('post.keyword') . '%")';
        outputData($userModel->order('user_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select());
    }

    //用户详细
    public function userDetailed()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();

        outputData($userModel->getRowById(I('post.user_id')));
    }

    //设置权限
    public function userSetPower()
    {
        if (empty(I('post.user_id')) || empty(I('post.user_power'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();

        $userModel->updateByIdWithKV(I('post.user_id'), 'user_power', I('post.user_power'));
        outputData('1');
    }

}