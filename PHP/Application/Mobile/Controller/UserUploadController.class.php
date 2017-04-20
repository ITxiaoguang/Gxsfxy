<?php

namespace Mobile\Controller;

use Think\Upload;

class UserUploadController extends UserController
{

    public function __construct()
    {
        parent::__construct();
    }

    //上传图片
    public function uploadImage()
    {
        $type = I('post.type');
        $tid = I('post.tid');
        if (empty($tid) || empty($tid)) {
            outputError('参数错误');
        }
        $upload = new Upload();
        $upload->hash = false;
        $upload->savePath = '';
        $upload->replace = true;
        $upload->autoSub = false;
        $upload->maxSize = 3145728;
        $upload->rootPath = './Public/upload/' . $type . '/';
        $upload->saveName = $this->user_info['user_id'] . strtotime(I('post.time'));
        $upload->exts = array('jpg', 'gif', 'png', 'jpeg');
        $info = $upload->uploadOne($_FILES['file']);
        if ($info) {
            $data = array();
            $data['upload_class'] = 'image';
            $data['upload_uid'] = $this->user_info['user_id'];
            $data['upload_type'] = $type;
            $data['upload_tid'] = $tid;
            $data['upload_url'] = 'http://gxsfxy.yokey.top/Public/upload/' . $type . '/' . $info['savename'];
            $data['upload_time'] = I('post.time');
            M('upload')->add($data);
            outputData('1');
        } else {
            outputError($upload->getError());
        }
    }

    //上传图片
    public function uploadImageChat()
    {
        $upload = new Upload();
        $upload->hash = false;
        $upload->savePath = '';
        $upload->replace = true;
        $upload->autoSub = false;
        $upload->maxSize = 3145728;
        $upload->rootPath = './Public/upload/chat/';
        $upload->saveName = $this->user_info['user_id'] . strtotime(I('post.time'));
        $upload->exts = array('jpg', 'gif', 'png', 'jpeg');
        $info = $upload->uploadOne($_FILES['file']);
        if ($info) {
            outputData('http://gxsfxy.yokey.top/Public/upload/chat/' . $info['savename']);
        } else {
            outputError($upload->getError());
        }
    }

}