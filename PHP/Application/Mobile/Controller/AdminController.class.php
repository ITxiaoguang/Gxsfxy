<?php

namespace Mobile\Controller;

use Mobile\Model\CommentModel;
use Mobile\Model\ConfigModel;
use Mobile\Model\DynamicModel;
use Mobile\Model\FollowModel;
use Mobile\Model\NewsModel;
use Mobile\Model\PraiseModel;
use Mobile\Model\UploadModel;
use Mobile\Model\UserModel;

class AdminController extends UserController
{

    public function __construct()
    {
        parent::__construct();
        if ($this->user_info['user_power'] == '用户') {
            outputError('无权操作');
        }
    }

    //配置更新
    public function configUpdate()
    {
        if (empty(I('post.config_type')) || empty(I('post.config_name')) || empty(I('post.config_value'))) {
            outputError('参数错误');
        }

        $configModel = new ConfigModel();

        $configModel->updateValue(I('post.config_type'), I('post.config_name'), I('post.config_value'));
        outputData('1');
    }

    //新闻添加
    public function newsAdd()
    {
        if (empty(I('post.news_type')) || empty(I('post.news_from')) || empty(I('post.news_title')) || empty(I('post.news_image')) || empty(I('post.news_link')) || empty(I('post.news_time'))) {
            outputError('参数错误');
        }

        $newsModel = new NewsModel();

        if (!empty($newsModel->getFiledByKV('news_link', I('post.news_link'), 'news_id'))) {
            outputError('新闻已存在');
        } else {
            $newsModel->insert(I('post.news_type'), I('post.news_from'), I('post.news_title'), I('post.news_image'), I('post.news_link'), I('post.news_time'));
            outputData('1');
        }
    }

    //管理首页
    public function index()
    {
        $userModel = new UserModel();
        $newsModel = new NewsModel();
        $praiseModel = new PraiseModel();
        $uploadModel = new UploadModel();
        $followModel = new FollowModel();
        $dynamicModel = new DynamicModel();
        $commentModel = new CommentModel();

        $data = array();
        $day = date("Y-m-d");
        $data['user_count'] = $userModel->count();
        $data['user_day_reg'] = $userModel->dayRegNumber($day);
        $data['user_day_login'] = $userModel->dayLoginNumber($day);
        $data['dynamic_count'] = $dynamicModel->count();
        $data['news_count'] = $newsModel->count();
        $data['upload_count'] = $uploadModel->count();
        $data['comment_count'] = $commentModel->count();
        $data['praise_count'] = $praiseModel->count();
        $data['follow_count'] = $followModel->count();
        outputData($data);
    }

}