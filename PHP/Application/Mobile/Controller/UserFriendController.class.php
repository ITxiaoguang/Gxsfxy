<?php

namespace Mobile\Controller;

use Mobile\Model\UserModel;

class UserFriendController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    public function friendList()
    {
        if (empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $user_model = new UserModel();

        $filed = 'user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_province,user_city,user_area,user_avatar,user_qq,user_sign,user_gender,user_points,user_follow,follow_mine,user_visitor,reg_time';
        $data = $user_model->field($filed)->order('user_id desc')->limit($this->number)->page(I('post.page'))->select();

        outputData($data);
    }

    public function friendSearch()
    {
        if (empty(I('post.page'))) {
            outputError('参数不正确');
        }

        $user_model = new UserModel();

        $where = array();
        $where['_string'] = '(nick_name like "%' . I('post.keyword') . '%") OR (user_college like "%' . I('post.keyword') . '%") OR (user_professional like "%' . I('post.keyword') . '%") OR (user_class like "%' . I('post.keyword') . '%")';
        $filed = 'user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_province,user_city,user_area,user_avatar,user_qq,user_sign,user_gender,user_points,user_follow,follow_mine,user_visitor,reg_time';
        $data = $user_model->where($where)->field($filed)->order('user_id desc')->limit($this->number)->page(I('post.page'))->select();
        
        outputData($data);
    }

}