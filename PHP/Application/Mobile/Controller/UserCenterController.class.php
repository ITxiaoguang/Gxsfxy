<?php

namespace Mobile\Controller;

use Mobile\Model\CommentModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\FollowModel;
use Mobile\Model\PraiseModel;
use Mobile\Model\UserModel;
use Mobile\Model\VisitorModel;

class UserCenterController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //获取用户信息 - 聊天时
    public function userInfo()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();

        $data = $userModel->getBaseInfo(I('post.user_id'));
        $data['is_follow'] = $followModel->isFollow($this->user_info['user_id'], I('post.user_id'));
        $data['follow_mine'] = $followModel->isFollow(I('post.user_id'), $this->user_info['user_id']);
        outputData($data);
    }

    //获取用户信息 - 个人中心时
    public function userCenter()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();
        $praiseModel = new PraiseModel();
        $commentModel = new CommentModel();
        $dynamicModel = new DynamicModel();
        $visitorModel = new VisitorModel();

        $visitorModel->insert($this->user_info['user_id'], I('post.user_id'), I('post.time'));
        $userModel->addVisitorNumber(I('post.user_id'));

        $data = $userModel->getBaseInfo(I('post.user_id'));
        $data['praise_number'] = $praiseModel->getNumberByUid(I('post.user_id'));
        $data['dynamic_number'] = $dynamicModel->getNumberByUid(I('post.user_id'));
        $data['comment_number'] = $commentModel->getNumberByUid(I('post.user_id'));
        $data['is_follow'] = $followModel->isFollow($this->user_info['user_id'], I('post.user_id'));
        $data['is_follow_mine'] = $followModel->isFollow(I('post.user_id'), $this->user_info['user_id']);
        outputData($data);
    }

    //获取用户访问记录
    public function userVisitor()
    {
        if (empty(I('post.user_id')) || empty(I('post.page'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $visitorModel = new VisitorModel();

        $where = array();
        $where['visitor_tid'] = I('post.user_id');
        $data = $visitorModel->order('visitor_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['visitor_uid']);
        }
        outputData($data);
    }

}