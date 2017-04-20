<?php

namespace Mobile\Controller;

use Mobile\Model\ActivityModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\NewsModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\PraiseModel;
use Mobile\Model\UserModel;

class UserPraiseController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //是否赞过了
    public function isPraise()
    {
        if (empty(I('post.table')) || empty(I('post.tid'))) {
            outputError('参数不正确');
        }

        $praiseModel = new PraiseModel();

        if (!empty($praiseModel->isPraise($this->user_info['user_id'], I('post.table'), I('post.tid')))) {
            outputData('1');
        } else {
            outputData('0');
        }
    }

    //赞过的列表
    public function praiseList()
    {
        if (empty(I('post.user_id')) || empty(I('post.page'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();

        $where = array();
        $where['praise_uid'] = I('post.user_id');
        $data = $praiseModel->order('praise_id desc')->where($where)->limit($this->number)->page(I('post.page'))->select();
        foreach ($data as $key => $val) {
            $data[$key]['user_info'] = $userModel->getBaseInfo(I('post.user_id'));
            $table = $data[$key]['praise_table'];
            $tid = $data[$key]['praise_tid'];
            $where = array();
            $where[$table . '_id'] = $tid;
            $temp = M($table)->where($where)->select();
            $data[$key]['content_info'] = $temp[0];
        }
        outputData($data);
    }

    //赞新闻
    public function praiseNews()
    {
        if (empty(I('post.news_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $newsModel = new NewsModel();
        $praiseModel = new PraiseModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($newsModel->getFiledById(I('post.news_id'), 'news_type'))) {
            $praiseModel->insert($this->user_info['user_id'], 'news', I('post.news_id'), I('post.time'));
            $newsModel->addPraiseNumber(I('post.news_id'));
            $userModel->addPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '赞新闻', I('post.time'));
            outputData('1');
        } else {
            outputError('新闻不存在');
        }
    }

    //取消赞新闻
    public function cancelNews()
    {
        if (empty(I('post.news_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $newsModel = new NewsModel();
        $praiseModel = new PraiseModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($newsModel->getFiledById(I('post.news_id'), 'news_type'))) {
            $praiseModel->cancelPraise($this->user_info['user_id'], 'news', I('post.news_id'));
            $newsModel->subPraiseNumber(I('post.news_id'));
            $userModel->subPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消赞新闻', I('post.time'));
            outputData('1');
        } else {
            outputError('新闻不存在');
        }
    }

    //赞动态
    public function praiseDynamic()
    {
        if (empty(I('post.dynamic_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();
        $dynamicModel = new DynamicModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_type'))) {
            $praiseModel->insert($this->user_info['user_id'], 'dynamic', I('post.dynamic_id'), I('post.time'));
            $dynamicModel->addPraiseNumber(I('post.dynamic_id'));
            $userModel->addPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '赞动态', I('post.time'));
            outputData('1');
        } else {
            outputError('动态不存在');
        }
    }

    //取消赞动态
    public function cancelDynamic()
    {
        if (empty(I('post.dynamic_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();
        $dynamicModel = new DynamicModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($dynamicModel->getFiledById(I('post.dynamic_id'), 'dynamic_type'))) {
            $praiseModel->cancelPraise($this->user_info['user_id'], 'dynamic', I('post.dynamic_id'));
            $dynamicModel->subPraiseNumber(I('post.dynamic_id'));
            $userModel->subPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消赞动态', I('post.time'));
            outputData('1');
        } else {
            outputError('动态不存在');
        }
    }

    //赞动态
    public function praiseActivity()
    {
        if (empty(I('post.activity_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();
        $activityModel = new ActivityModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($activityModel->getFiledById(I('post.activity_id'), 'activity_name'))) {
            $praiseModel->insert($this->user_info['user_id'], 'activity', I('post.activity_id'), I('post.time'));
            $activityModel->addPraiseNumber(I('post.activity_id'));
            $userModel->addPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '+', '5', '赞活动', I('post.time'));
            outputData('1');
        } else {
            outputError('活动不存在');
        }
    }

    //取消赞活动
    public function cancelActivity()
    {
        if (empty(I('post.activity_id'))) {
            outputError('参数不正确');
        }

        $userModel = new UserModel();
        $praiseModel = new PraiseModel();
        $activityModel = new ActivityModel();
        $pointsLogModel = new PointsLogModel();

        if (!empty($activityModel->getFiledById(I('post.activity_id'), 'activity_name'))) {
            $praiseModel->cancelPraise($this->user_info['user_id'], 'activity', I('post.activity_id'));
            $activityModel->subPraiseNumber(I('post.ac_id'));
            $userModel->subPoints($this->user_info['user_id'], '5');
            $pointsLogModel->insert($this->user_info['user_id'], '-', '5', '取消赞活动', I('post.time'));
            outputData('1');
        } else {
            outputError('动态不存在');
        }

    }

}