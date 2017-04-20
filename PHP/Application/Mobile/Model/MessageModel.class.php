<?php

namespace Mobile\Model;

use Think\Model;

class MessageModel extends Model
{

    protected $tableName = 'message';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['message_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['message_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['message_id'] = $id;
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

    public function insert($uid, $tid, $type, $content, $time)
    {
        $data = array();
        $data['message_uid'] = $uid;
        $data['message_tid'] = $tid;
        $data['message_type'] = $type;
        $data['message_content'] = $content;
        $data['message_del'] = '0';
        $data['message_time'] = $time;
        return $this->add($data);
    }

}