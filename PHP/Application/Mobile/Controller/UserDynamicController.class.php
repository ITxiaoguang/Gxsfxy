<?php

namespace Mobile\Controller;

use Mobile\Model\CommentModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\PraiseModel;
use Mobile\Model\UserModel;

class UserDynamicController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //删除动态
    public function dynamicDel()
    {
        if (empty(I('post.dynamic_id'))) {
            outputError('参数错误');
        }

        $praiseModel = new PraiseModel();
        $dynamicModel = new DynamicModel();
        $commentModel = new CommentModel();

        $where = array();
        $where['dynamic_id'] = I('post.dynamic_id');
        $where['dynamic_uid'] = $this->user_info['user_id'];

        if ($dynamicModel->where($where)->delete()) {
            $commentModel->deleteComment('dynamic', I('post.dynamic_id'));
            $praiseModel->deletePraise('dynamic', I('post.dynamic_id'));
            outputData('1');
        } else {
            outputError('删除失败');
        }
    }

    //发表圈子动态
    public function circleCreate()
    {
        $content = $_POST['content'];
        $location = I('post.location');
        if (empty($location)) {
            $location = '不详';
        }

        $userModel = new UserModel();
        $dynamicModel = new DynamicModel();
        $pointsLogModel = new PointsLogModel();

        $dynamicModel->insert($this->user_info['user_id'], 'circle', '', $content, $location, I('post.device'), I('post.time'));
        $userModel->addPoints($this->user_info['user_id'], '10');
        $pointsLogModel->insert($this->user_info['user_id'], '+', '10', '发表圈子', I('post.time'));

        $where = array();
        $where['dynamic_uid'] = $this->user_info['user_id'];
        $where['dynamic_type'] = 'circle';
        $id = $dynamicModel->order('dynamic_id desc')->limit(1)->where($where)->getField('dynamic_id');
        outputData($id);
    }

    //分享圈子动态
    public function circleForward()
    {
        if (empty(I('post.id'))) {
            outputError('参数错误');
        }

        $dynamicModel = new DynamicModel();
        if ($dynamicModel->forward(I('post.id'), $this->user_info['user_id'])) {
            outputData('1');
        } else {
            outputError('失败了');
        }
    }

    //发表话题动态
    public function topicCreate()
    {
        if (empty(I('post.title')) || empty($_POST['content'])) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $dynamicModel = new DynamicModel();
        $pointsLogModel = new PointsLogModel();

        $dynamicModel->insert($this->user_info['user_id'], 'topic', I('post.title'), $_POST['content'], '不详', I('post.device'), I('post.time'));
        $userModel->addPoints($this->user_info['user_id'], '10');
        $pointsLogModel->insert($this->user_info['user_id'], '+', '10', '发表话题', I('post.time'));
        outputData('1');
    }

}