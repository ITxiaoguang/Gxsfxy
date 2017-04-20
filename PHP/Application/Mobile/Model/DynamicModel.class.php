<?php

namespace Mobile\Model;

use Think\Model;

class DynamicModel extends Model
{

    protected $tableName = 'dynamic';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['dynamic_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['dynamic_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['dynamic_id'] = $id;
        return $this->where($where)->getField($filed);
    }

    public function getFiledByKV($key, $value, $filed)
    {
        $where = array();
        $where[$key] = $value;
        return $this->where($where)->getField($filed);
    }

    public function updateByIdWithKV($id, $key, $value)
    {
        $data = array();
        $data[$key] = $value;
        return $this->updateById($id, $data);
    }

    public function insert($uid, $type, $title, $content, $location, $device, $time)
    {
        $data = array();
        $data['dynamic_uid'] = $uid;
        $data['dynamic_type'] = $type;
        $data['dynamic_praise'] = '0';
        $data['dynamic_comment'] = '0';
        $data['dynamic_title'] = $title;
        $data['dynamic_content'] = $content;
        $data['dynamic_location'] = $location;
        $data['dynamic_device'] = $device;
        $data['dynamic_time'] = $time;
        return $this->add($data);
    }

    //自定义

    public function forward($id, $uid)
    {
        $data = $this->getRowById($id);
        if ($data['dynamic_uid'] == $uid) {
            return false;
        } else {
            $this->insert($uid, $data['dynamic_type'], $data['dynamic_title'], $data['dynamic_content'], $data['dynamic_location'], $data['dynamic_device'], $data['dynamic_time']);
            return true;
        }
    }

    public function addPraiseNumber($id)
    {
        $praise = $this->getFiledById($id, 'dynamic_praise');
        $praise = intval($praise) + 1;
        return $this->updateByIdWithKV($id, 'dynamic_praise', $praise);
    }

    public function subPraiseNumber($id)
    {
        $praise = $this->getFiledById($id, 'dynamic_praise');
        $praise = intval($praise) - 1;
        return $this->updateByIdWithKV($id, 'dynamic_praise', $praise);
    }

    public function getNumberByUid($uid)
    {
        $where = array();
        $where['dynamic_uid'] = $uid;
        $data = $this->where($where)->select();
        return sizeof($data);
    }

    public function addCommentNumber($id)
    {
        $comment = $this->getFiledById($id, 'dynamic_comment');
        $comment = intval($comment) + 1;
        return $this->updateByIdWithKV($id, 'dynamic_comment', $comment);
    }

    public function subCommentNumber($id)
    {
        $comment = $this->getFiledById($id, 'dynamic_comment');
        $comment = intval($comment) - 1;
        return $this->updateByIdWithKV($id, 'dynamic_comment', $comment);
    }

}