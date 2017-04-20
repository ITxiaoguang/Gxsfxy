<?php

namespace Mobile\Controller;

use Mobile\Model\ActivityModel;
use Mobile\Model\CommentModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\NewsModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\UserModel;

class UserCommentController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //是否评论
    public function isComment()
    {
        if (empty(I('post.table')) || empty(I('post.tid'))) {
            outputError('参数不正确');
        }

        $commentModel = new CommentModel();

        $where = array();
        $where['comment_uid'] = $this->user_info['user_id'];
        $where['comment_table'] = I('post.table');
        $where['comment_tid'] = I('post.tid');
        if (!empty($commentModel->where($where)->getField('comment_id'))) {
            outputData('1');
        } else {
            outputData('0');
        }
    }

    //评论过的列表
    public function commentList()
    {
        if (empty(I('post.user_id')) || empty(I('post.page'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $commentModel = new CommentModel();

        $where = array();
        $where['comment_uid'] = I('post.user_id');
        $data = $commentModel->order('comment_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo(I('post.user_id'));
            $table = $data[$key]['comment_table'];
            $tid = $data[$key]['comment_tid'];
            $where = array();
            $where[$table . '_id'] = $tid;
            $temp = M($table)->where($where)->select();
            $data[$key]['content_info'] = $temp[0];
        }
        outputData($data);
    }

    //评论新闻
    public function commentNews()
    {
        if (empty(I('post.rid')) || empty(I('post.news_id')) || empty($_POST['content'])) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $newsModel = new NewsModel();
        $commentModel = new CommentModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($newsModel->getFiledById(I('post.news_id'), 'news_type'))) {
            //增加评论
            $commentModel->insert($this->user_info['user_id'], 'news', I('post.news_id'), $_POST['content'], I('post.rid'), I('post.time'));
            //更新评论数
            $newsModel->addCommentNumber(I('post.news_id'));
            //增加积分
            $userModel->addPoints($this->user_info['user_id'], '5');
            //积分日记
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '评论新闻', I('post.time'));
            //回复的时候通知对方
            if (I('post.rid') != '-1') {
                $user_id = $commentModel->getFiledById(I('post.rid'), 'comment_uid');
                if ($user_id != $this->user_info['user_id']) {
                    $data = array();
                    $data['news_id'] = I('post.news_id');
                    $push_id = $userModel->getFiledById($user_id, 'push_id');
                    $message = '有人回复了你评论的新闻,快去看看吧';
                    if (strpos($this->user_info['device_system'], 'Android') !== false) {
                        sendNotifyAndroidByPushId($push_id, $message, $data);
                    }
                    if (strpos($this->user_info['device_system'], 'Ios') !== false) {
                        sendNotifyIosByPushId($push_id, $message, $data);
                    }
                }
            }
            //输出数据
            outputData('1');
        } else {
            outputError('新闻不存在');
        }
    }

    //取消评论新闻
    public function cancelNews()
    {
        if (empty(I('post.news_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $newsModel = new NewsModel();
        $commentModel = new CommentModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($newsModel->getFiledById(I('post.news_id'), 'news_type'))) {
            //取消评论
            $commentModel->cancelComment($this->user_info['user_id'], 'news', I('post.news_id'));
            //更新评论数
            $newsModel->subCommentNumber(I('post.news_id'));
            //删除积分
            $userModel->subPoints($this->user_info['user_id'], '5');
            //积分日记
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消评论新闻', I('post.time'));
            //输出数据
            outputData('1');
        } else {
            outputError('新闻不存在');
        }
    }

    //评论动态
    public function commentDynamic()
    {
        if (empty(I('post.rid')) || empty(I('post.dynamic_id')) || empty($_POST['content'])) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $dynamicModel = new DynamicModel();
        $commentModel = new CommentModel();
        $pointsLogModel = new PointsLogModel();

        $dynamic_uid = $dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_uid');

        if (!empty($dynamic_uid)) {
            //增加评论
            $commentModel->insert($this->user_info['user_id'], 'dynamic', I('post.dynamic_id'), $_POST['content'], I('post.rid'), I('post.time'));
            //更新评论数
            $dynamicModel->addCommentNumber(I('post.dynamic_id'));
            //增加积分
            $userModel->addPoints($this->user_info['user_id'], '5');
            //积分日记
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '评论动态', I('post.time'));
            //通知对方
            if ($dynamic_uid != $this->user_info['user_id']) {
                $message = '';
                $data = array();
                $data['dynamic_id'] = I('post.dynamic_id');
                $push_id = $userModel->getFiledById($dynamic_uid, 'push_id');
                $dynamic_type = $dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_type');
                if ($dynamic_type == 'circle') {
                    $message = '有人评论了你发表的圈子,快去看看吧';
                }
                if ($dynamic_type == 'topic') {
                    $message = '有人评论了你发表的话题,快去看看吧';
                }
                if (strpos($this->user_info['device_system'], 'Android') !== false) {
                    sendNotifyAndroidByPushId($push_id, $message, $data);
                }
                if (strpos($this->user_info['device_system'], 'Ios') !== false) {
                    sendNotifyIosByPushId($push_id, $message, $data);
                }
            }
            //回复的时候通知对方
            if (I('post.rid') != '-1') {
                $user_id = $commentModel->getFiledById(I('post.rid'), 'comment_uid');
                if ($user_id != $this->user_info['user_id']) {
                    $message = '';
                    $data = array();
                    $data['dynamic_id'] = I('post.dynamic_id');
                    $push_id = $userModel->getFiledById($user_id, 'push_id');
                    $dynamic_type = $dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_type');
                    if ($dynamic_type == 'circle') {
                        $message = '有人回复了你评论的圈子,快去看看吧';
                    }
                    if ($dynamic_type == 'topic') {
                        $message = '有人回复了你评论的话题,快去看看吧';
                    }
                    if (strpos($this->user_info['device_system'], 'Android') !== false) {
                        sendNotifyAndroidByPushId($push_id, $message, $data);
                    }
                    if (strpos($this->user_info['device_system'], 'Ios') !== false) {
                        sendNotifyIosByPushId($push_id, $message, $data);
                    }
                }
            }
            //输出数据
            outputData('1');
        } else {
            outputError('动态不存在');
        }
    }

    //取消评论动态
    public function cancelDynamic()
    {
        if (empty(I('post.dynamic_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $commentModel = new CommentModel();
        $dynamicModel = new DynamicModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_type'))) {
            //取消评论
            $commentModel->cancelComment($this->user_info['user_id'], 'dynamic', I('post.dynamic_id'));
            //更新评论数
            $dynamicModel->subCommentNumber(I('post.dynamic_id'));
            //删除积分
            $userModel->subPoints($this->user_info['user_id'], '5');
            //积分日记
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消评论新闻', I('post.time'));
            //输出数据
            outputData('1');
        } else {
            outputError('动态不存在');
        }
    }

    //评论活动
    public function commentActivity()
    {
        if (empty(I('post.rid')) || empty(I('post.activity_id')) || empty($_POST['content'])) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $commentModel = new CommentModel();
        $activityModel = new ActivityModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($activityModel->getFiledById(I('post.activity_id'), 'activity_name'))) {
            //评论
            $commentModel->insert($this->user_info['user_id'], 'activity', I('post.activity_id'), $_POST['content'], I('post.rid'), I('post.time'));
            //更新评论数
            $activityModel->addCommentNumber(I('post.activity_id'));
            //增加积分
            $userModel->addPoints($this->user_info['user_id'], '5');
            //更新日记
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '评论活动', I('post.time'));
            //回复的时候通知对方
            if (I('post.rid') != '-1') {
                $user_id = $commentModel->getFiledById(I('post.rid'), 'comment_uid');
                if ($user_id != $this->user_info['user_id']) {
                    $data = array();
                    $data['activity_id'] = I('post.activity_id');
                    $push_id = $userModel->getFiledById($user_id, 'push_id');
                    $message = '有人回复了你评论的活动,快去看看吧';
                    if (strpos($this->user_info['device_system'], 'Android') !== false) {
                        sendNotifyAndroidByPushId($push_id, $message, $data);
                    }
                    if (strpos($this->user_info['device_system'], 'Ios') !== false) {
                        sendNotifyIosByPushId($push_id, $message, $data);
                    }
                }
            }
            //输出数据
            outputData('1');
        } else {
            outputError('活动不存在');
        }
    }

    //取消评论活动
    public function cancelActivity()
    {
        if (empty(I('post.activity_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $commentModel = new CommentModel();
        $activityModel = new ActivityModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($activityModel->getFiledById(I('post.activity_id'), 'activity_name'))) {
            //取消评论
            $commentModel->cancelComment($this->user_info['user_id'], 'activity', I('post.activity_id'));
            //更新评论数
            $activityModel->subCommentNumber(I('post.activity_id'));
            //删除积分
            $userModel->subPoints($this->user_info['user_id'], '5');
            //积分日记
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消评论活动', I('post.time'));
            //输出数据
            outputData('1');
        } else {
            outputError('活动不存在');
        }
    }

}