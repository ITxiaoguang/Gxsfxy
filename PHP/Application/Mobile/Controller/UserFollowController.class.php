<?php

namespace Mobile\Controller;

use Mobile\Model\FollowModel;
use Mobile\Model\UserModel;

class UserFollowController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //关注
    public function follow()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }
        if (I('post.user_id') == $this->user_info['user_id']) {
            outputError('您不能关注自己');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();

        if (!empty($followModel->isFollow($this->user_info['user_id'], I('post.user_id')))) {
            outputError('你已经关注过他了');
        } else {
            $followModel->insert($this->user_info['user_id'], I('post.user_id'), I('post.time'));
            $userModel->addFollowNumber($this->user_info['user_id']);
            $userModel->addFollowMineNumber(I('post.user_id'));
            $push_id = $userModel->getFiledById(I('post.user_id'), 'push_id');
            $system = $userModel->getFiledById(I('post.user_id'), 'device_system');
            if (strpos($system, 'Android') !== false) {
                sendNotifyAndroidByPushId($push_id, '有人关注你了，快去看看吧！', null);
            }
            if (strpos($system, 'Ios') !== false) {
                sendNotifyIosByPushId($push_id, '有人关注你了，快去看看吧！', null);
            }
            outputData('1');
        }
    }

    //关注取消
    public function followCancel()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }
        if (I('post.user_id') == $this->user_info['user_id']) {
            outputError('您不能取消关注自己');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();

        if ($followModel->isFollow($this->user_info['user_id'], I('post.user_id'))) {
            $followModel->cancelFollow($this->user_info['user_id'], I('post.user_id'));
            $userModel->subFollowNumber($this->user_info['user_id']);
            $userModel->subFollowMineNumber(I('post.user_id'));
            outputData('1');
        } else {
            outputError('你尚未关注对方');
        }
    }

    //我关注的
    public function mineFollow()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();

        $where = array();
        $where['follow_uid'] = I('post.user_id');
        $data = $followModel->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['follow_tid']);
        }
        outputData($data);
    }

    //我关注的
    public function followMine()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $followModel = new FollowModel();

        $where = array();
        $where['follow_tid'] = I('post.user_id');
        $data = $followModel->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo($data[$key]['follow_uid']);
        }
        outputData($data);
    }

}