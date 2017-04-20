<?php

namespace Mobile\Controller;

use Mobile\Model\MessageModel;
use Mobile\Model\UserModel;

class AdminMessageController extends UserController
{

    public function __construct()
    {
        parent::__construct();
        if ($this->user_info['user_power'] == '用户') {
            outputError('无权操作');
        }
    }

    //反馈记录
    public function feedbackRecord()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $where = array();
        $where['_string'] = '(message_uid=' . I('post.user_id') . ' and message_tid=-1 ) or (message_uid=-1 and message_tid=' . I('post.user_id') . ')';
        $data = $messageModel->where($where)->select();
        foreach ($data as $key => $val) {
            if ($data[$key]['message_uid'] == '-1') {
                $user_info = $userModel->getBaseInfo($data[$key]['message_tid']);
            } else {
                $user_info = $userModel->getBaseInfo($data[$key]['message_uid']);
            }
            $data[$key]['uid_info'] = $user_info;
        }
        outputData($data);
    }

    //反馈列表
    public function feedbackList()
    {
        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $out_data = array();
        $sql = "SELECT DISTINCT message_uid from message WHERE message_tid='-1'";
        $data = M()->query($sql);
        foreach ($data as $key => $val) {
            $where = array();
            $where['message_del'] = '0';
            $where['_string'] = '(message_uid=-1 and message_tid=' . $data[$key]['message_uid'] . ') or (message_uid=' . $data[$key]['message_uid'] . ' and message_tid=-1)';
            $sql_data = $messageModel->order('message_id desc')->where($where)->limit(1)->select();
            if ($sql_data[0]) {
                $out_data[] = array_merge($sql_data[0], $userModel->getBaseInfo($data[$key]['message_uid']));
            }
        }
        foreach ($out_data as $key => $value) {
            $id[$key] = $value['message_id'];
        }
        array_multisort($id, SORT_NUMERIC, SORT_DESC, $out_data);
        outputData($out_data);
    }

    //反馈增加
    public function feedbackAdd()
    {
        if (empty(I('post.type')) || empty(I('post.user_id')) || empty($_POST['content'])) {
            outputError('参数错误');
        }

        $messageModel = new MessageModel();

        $messageModel->insert('-1', I('post.user_id'), I('post.type'), $_POST['content'], I('post.time'));
        outputData('1');
    }

    //管理留言
    public function messageRecord()
    {
        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $where = array();
        $where['message_tid'] = '0';
        $data = $messageModel->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]["uid_info"] = $userModel->getBaseInfo($data[$key]['message_uid']);
        }
        outputData($data);
    }

    //管理留言首页
    public function messageIndex()
    {
        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $where = array();
        $where['message_tid'] = '0';
        $data = $messageModel->order('message_id desc')->limit(3)->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['uid_info'] = $userModel->getBaseInfo($data[$key]['message_uid']);
        }
        outputData($data);
    }

    //管理团队消息增加
    public function messageAdd()
    {
        if (empty(I('post.type')) || empty($_POST['content'])) {
            outputError('参数错误');
        }

        $messageModel = new MessageModel();

        $messageModel->insert($this->user_info['user_id'], '0', I('post.type'), $_POST['content'], I('post.time'));
        outputData('1');
    }

}