<?php

namespace Mobile\Controller;

use Mobile\Model\MessageModel;
use Mobile\Model\PointsLogModel;
use Mobile\Model\UserModel;

class UserMessageController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //聊天记录
    public function messageRecord()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $where = array();
        $where['message_del'] = '0';
        $where['_string'] = '(message_uid=' . $this->user_info['user_id'] . ' and message_tid=' . I('post.user_id') . ') or (message_uid=' . I('post.user_id') . ' and message_tid=' . $this->user_info['user_id'] . ')';
        $data = $messageModel->where($where)->select();
        foreach ($data as $key => $val) {
            $data[$key]['uid_info'] = $userModel->getBaseInfo($data[$key]['message_uid']);
        }
        outputData($data);
    }

    //聊天清空
    public function messageClear()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $messageModel = new MessageModel();

        $where = array();
        $where['_string'] = '(message_uid=' . $this->user_info['user_id'] . ' and message_tid=' . I('post.user_id') . ') or (message_uid=' . I('post.user_id') . ' and message_tid=' . $this->user_info['user_id'] . ')';
        $data = array();
        $data['message_del'] = '1';
        $messageModel->where($where)->save($data);
        outputData('1');
    }

    //聊天列表
    public function messageList()
    {
        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $out_data = array();
        $sql = "SELECT * from (select * from follow where follow_tid=" . $this->user_info['user_id'] . ") as tb1 INNER JOIN (select * from follow where follow_uid=" . $this->user_info['user_id'] . ") as tb2 where tb1.follow_uid=tb2.follow_tid";
        $data = M()->query($sql);

        foreach ($data as $key => $val) {
            $user_id = $this->user_info['user_id'];
            if ($data[$key]['follow_uid'] == $this->user_info['user_id']) {
                $user_id = $data[$key]['follow_tid'];
            }
            $where = array();
            $where['message_del'] = '0';
            $where['_string'] = '(message_uid=' . $this->user_info['user_id'] . ' and message_tid=' . $user_id . ') or (message_uid=' . $user_id . ' and message_tid=' . $this->user_info['user_id'] . ')';
            $sql_data = $messageModel->order('message_id desc')->where($where)->limit(1)->select();
            if ($sql_data[0]) {
                $out_data[] = array_merge($sql_data[0], $userModel->getBaseInfo($user_id));
            }
        }
        foreach ($out_data as $key => $value) {
            $id[$key] = $value['message_id'];
        }
        array_multisort($id, SORT_NUMERIC, SORT_DESC, $out_data);
        outputData($out_data);
    }

    //消息增加
    public function messageAdd()
    {
        if (empty(I('post.tid')) || empty(I('post.type')) || empty($_POST['content'])) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $messageModel = new MessageModel();

        $push_id = $userModel->getFiledById(I('post.tid'), 'push_id');
        $system = $userModel->getFiledById(I('post.tid'), 'device_system');
        $messageModel->insert($this->user_info['user_id'], I('post.tid'), I('post.type'), $_POST['content'], I('post.time'));
        if ($userModel->getFiledById(I('post.tid'), 'login_state') == '1') {
            $data = array();
            $data['message_id'] = '-1';
            $data['message_uid'] = $this->user_info['user_id'];
            $data["message_type"] = I('post.type');
            $data["message_content"] = $_POST['content'];
            $data["uid_info"] = $userModel->getBaseInfo($this->user_info['user_id']);
            sendMessageByPushId($push_id, $data);
        } else {
            if (strpos($system, 'Android') !== false) {
                sendNotifyAndroidByPushId($push_id, '您有新的聊天消息', null);
            }
            if (strpos($system, 'Ios') !== false) {
                sendNotifyIosByPushId($push_id, '您有新的聊天消息', null);
            }
        }
        outputData('1');
    }

    //消息删除
    public function messageDel()
    {
        if (empty(I('post.message_id'))) {
            outputError('参数错误');
        }

        $messageModel = new MessageModel();

        $messageModel->updateByIdWithKV(I('post.message_id'), 'message_del', '1');
        outputData('1');
    }

    //反馈记录
    public function feedbackRecord()
    {
        $messageModel = new MessageModel();

        $where = array();
        $where['_string'] = '(message_uid=' . $this->user_info['user_id'] . ' and message_tid=-1 ) or (message_uid=-1 and message_tid=' . $this->user_info['user_id'] . ')';
        outputData($messageModel->where($where)->select());
    }

    //反馈增加
    public function feedbackAdd()
    {
        if (empty(I('post.type')) || empty($_POST['content'])) {
            outputError('参数错误');
        }

        $userModel = new UserModel();
        $messageModel = new MessageModel();
        $pointsLogModel = new PointsLogModel();

        $messageModel->insert($this->user_info['user_id'], '-1', I('post.type'), $_POST['content'], I('post.time'));
        $userModel->addPoints($this->user_info['user_id'], '10');
        $pointsLogModel->insert($this->user_info['user_id'], '+', '10', '提交意见建议', I('post.time'));


        outputData('1');
    }

    //通知我
    public function notifyMine()
    {
        if (empty(I('post.user_id'))) {
            outputError('参数错误');
        }

        $userModel = new UserModel();

        $data = array();
        $data['user_id'] = I('post.user_id');

        $push_id = $userModel->getFiledById($this->user_info['user_id'], 'push_id');
        $system = $userModel->getFiledById($this->user_info['user_id'], 'device_system');

        if (strpos($system, 'Android') !== false) {
            sendNotifyAndroidByPushId($push_id, '您有新的聊天消息', $data);
        }
        if (strpos($system, 'Android') !== false) {
            sendNotifyIosByPushId($push_id, '您有新的聊天消息', $data);
        }
    }

}